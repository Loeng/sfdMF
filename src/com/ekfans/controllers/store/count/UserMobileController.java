package com.ekfans.controllers.store.count;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.message.util.MessageUtil;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: UserMobileController
* @Description: TODO(这里用一句话描述这个类的作用)更新用户手机号码
* @author Jimbo
* @date 2014-3-29 下午08:25:23
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class UserMobileController extends BasicController
{
    @Autowired
    private IUserService userService;

    /**
     * 跳转带修改页面
    * @Title: toupdate
    * @Description: TODO(跳转带修改页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/store/count/mobile/toUpdateMobile")
    public String toupdate(HttpServletRequest request,HttpSession session) {
	    Store store = (Store)session.getAttribute(SystemConst.STORE);
	    User user = userService.getUser(store.getId());
	    request.setAttribute("mobile",user.getMobile());
	    return "/store/count/toUpdateMobile";
    }
    
    
    /**
     * 修改完成，保存操作
    * @Title: update
    * @Description: TODO(修改完成，保存操作)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/store/count/mobile/updateMobile")
    public String update(HttpServletRequest request , HttpSession session) {
	try{
	    Store store = (Store)session.getAttribute(SystemConst.STORE);
	    User user = userService.getUser(store.getId());
	    String mobile = request.getParameter("mobile");
	    if(StringUtil.isEmpty(mobile)){
            request.setAttribute("updateOk",false);
            request.setAttribute("mobile",user.getMobile());
            return "/store/count/toUpdateMobile";
        }
	  
	    user.setMobile(mobile);
	    if(userService.updateMobile(user)){
	        request.setAttribute("updateOk",true);
	        request.setAttribute("mobile",mobile);
	        return "/store/count/toUpdateMobile";
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
	 return "error";
    }
    
    /**
     * 
    * @Title: checkCade
    * @Description: TODO(验证验证码是否输入正确)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param cade
    * @param request
    * @param session
    * @return Object 返回类型
    * @throws
     */
    @RequestMapping(value="/store/mobile/code")
    @ResponseBody
    public Object checkCade(HttpServletRequest request , HttpSession session) {
        MessageUtil util = new MessageUtil();
        int random = util.randomNo();
        String sysRandom = util.getContent(random);
        System.out.println(sysRandom);
      return random+"";
    
    }
}


