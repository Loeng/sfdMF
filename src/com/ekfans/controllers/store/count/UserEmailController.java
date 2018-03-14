package com.ekfans.controllers.store.count;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.user.service.UserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.message.util.MessageUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: UserEmailController
* @Description: TODO更新用户Email
* @author Jimbo
* @date 2014-3-29 下午08:24:49
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class UserEmailController extends BasicController {
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IStoreService storeService;
    
 // 邮件注册用户，转户激活加密字段
    private final String FIELDPWD = "ekfans.com";
    /**
     * 
    * @Title: 跳转带修改页面
    * @Description: TODO(跳转带修改页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/store/count/email/toUpdateEmail")
    public String toupdate(HttpServletRequest request, HttpSession session) {
	// 获取登陆成功的店铺对象
	Store store = (Store)session.getAttribute(SystemConst.STORE);
	//通过Store对象的ID获取user对象
	User user = userService.getUser(store.getId());
	// 给前端视图传递设置成功的参数
	request.setAttribute("email",user.getEmail());
	//去往更新Email的页面
	return "/store/count/toUpdateEmail";
    }
    
    
    /**
     * 
    * @Title: 修改完成，保存操作
    * @Description: TODO(修改完成，保存操作)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/store/count/email/updateEmail")
    public String update(HttpServletRequest request ,HttpSession session) {
	try
	{
	    Store store = (Store)session.getAttribute(SystemConst.STORE);
	    User user = userService.getUser(store.getId());
	    String email = request.getParameter("email");
	    if(StringUtil.isEmpty(email)){
            request.setAttribute("updateOk",false);
            request.setAttribute("email",user.getEmail());
            return "/store/count/toUpdateEmail";
        }
	    
	    user.setEmail(email);
	    if(userService.updateEmail(user)) {
	     
	        request.setAttribute("updateOk",true);
	        request.setAttribute("email",email);
	        // 邮件标题
            String emailTitle = "账户激活";
            // 邮件内容
            String emailContent = "点击：" + getBasePath() + "/web/updateEmail/accountcheck?userName=" + user.getName()+ "&code=" + new MD5Util().getMD5ofStr(user.getName() + FIELDPWD);
            // 发送邮件（这是注册后激活账户的邮件）
            if (!MessageUtil.sendMail(user.getEmail(), emailTitle, emailContent)) {
                request.setAttribute("judgmentMail", "邮件发送失败,请点击下方重新发送按钮");
            } else {
                request.setAttribute("judgmentMail", "还差一步，请激活您的帐户");
            }
	        return "/store/count/validateUserEmail";
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    
	}
	return "error";
	
    }
    
    
    /**
     * 修改邮件，完成验证的方法
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/web/updateEmail/accountcheck")
    public String accountActivation(HttpServletRequest request,HttpSession session ) {
        // 获取用户名
        String userName = request.getParameter("userName");
        // 获取特定识别字符
        String code = request.getParameter("code");
        // 获取客户信息
        Object[] obj = storeService.storeLoginByName(userName);
        if (obj.length > 0 && (new MD5Util().getMD5ofStr(userName + FIELDPWD)).equals(code)) {
            Store store = (Store) obj[1];
         // 驻入的形式获取会员对象
            IUserService userService = SpringContextHolder.getBean(UserService.class);
         // 根据会员主键获取会员对象
            User user = userService.getUser(store.getId());
            user.setVerificationStatus(true);
            session.setAttribute(SystemConst.STORE, store);
            // 调用缓存获取子权限集合
            List<StorePurview> purviewList = Cache.getStorePurviewByRoleId(store.getRoleId());
            session.setAttribute("purviewList", purviewList);
            session.setAttribute("parentChose", "ACCOUNT");
            session.setAttribute("storeChose", "MAIL_BIDING");
                // 设置用户账户状态为：激活
                try {
                    userService.updateUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                request.setAttribute("updateOk", "ture");
                request.setAttribute("name", user.getName());
                request.setAttribute("email", user.getEmail());
                return "redirect:/store/count/email/toUpdateEmail";
        }
        return "error";
        
    }
}
