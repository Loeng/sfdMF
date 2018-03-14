package com.ekfans.controllers.user.count;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.StringUtil;

@Controller
public class UsersMobileController extends BasicController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserDao userDao;
    /**
     * 跳转带修改页面
    * @Title: toupdate
    * @Description: TODO(跳转带修改页面)
    * 详细业务流程:
    * (跳转带修改页面)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/user/count/mobile/toUpdateUserMobile")
    public String toUpdate(HttpServletRequest request ,HttpSession session) {
	// 从Session中获取用户对象 
	User u = (User)session.getAttribute(SystemConst.USER);
	User user = userService.getUser(u.getId());
	//给页面中的mobile赋值
	request.setAttribute("mobile",user.getMobile());
	//跳转到修改手机号码的页面
	request.setAttribute("parentPurviewId", "account");
	request.setAttribute("purviewId","toUpdateUserMobile");
	return "/userCenter/customer/count/toUpdateUserMobile";
    }
    
    /**
     * 修改完成，保存操作
    * @Title: update
    * @Description: TODO(修改完成，保存操作)
    * 详细业务流程:
    * (修改完成，保存操作)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/user/count/mobile/updateUserMobile")
    public String update(HttpServletRequest request ,HttpSession session) {
	try
	{
	 // 从Session中获取用户对象 
	    User u = (User)session.getAttribute(SystemConst.USER);
	    User user = userService.getUser(u.getId());
	    //从页面上获取mobile的值
	    String mobile = request.getParameter("mobile");
	   
	    //防止提交后  再次直接刷新浏览器
	    if(StringUtil.isEmpty(mobile)){
	        request.setAttribute("updateOk",false);
	        request.setAttribute("mobile",user.getMobile());
	        request.setAttribute("parentPurviewId", "account");
	        request.setAttribute("purviewId","toUpdateUserMobile");
	        return "/userCenter/customer/count/toUpdateUserMobile";
	    }
	    //把从页面上获得的值存入数据库之中
	    user.setMobile(mobile);
	    //调用Service更新数据库
	    if(userService.updateMobile(user)){
		request.setAttribute("updateOk",true);
		request.setAttribute("mobile",mobile);
		request.setAttribute("parentPurviewId", "account");
		request.setAttribute("purviewId","toUpdateUserMobile");
		return "/userCenter/customer/count/toUpdateUserMobile";
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
	return "error";
	
    }
    /**
     * @Title: updatePhone
     * @Description: TODO(更改手机号码)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param request
     * @param session
     * @return String 返回类型
     * @throws
      */
     @RequestMapping(value="/user/security/phone")
     @ResponseBody
     public String updatePhone(HttpServletRequest request,HttpSession session){
         try {
             //从session获取登录用户
             User user = (User)session.getAttribute(SystemConst.USER);
             //获取原手机号码
             String oldPhoen = request.getParameter("oldphone");
             //获取新手机号码
             String newPhoen = request.getParameter("newphone");
             if(!StringUtil.isEmpty(oldPhoen)){
                 //判断原手机号码是否相同
                 if(user.getMobile().equals(oldPhoen)){
                     user.setMobile(newPhoen);
                     userDao.updateBean(user);
                 }else{
                     //原手机号码错误
                     return "1";
                 }
             }else{
                 user.setMobile(newPhoen);
                 userDao.updateBean(user);
             }
         } catch (Exception e) {
             e.printStackTrace();
             return "0";
         }
         return "0";
     }
}





