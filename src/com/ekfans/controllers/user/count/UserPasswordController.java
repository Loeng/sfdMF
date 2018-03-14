package com.ekfans.controllers.user.count;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 密码修改Controller
 * 
 * @Title: UserPasswordController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liulin
 * @date 2014-4-1 上午17:23:47 
 * @version V1.0
 */
@Controller
public class UserPasswordController{
    @Autowired
	IUserService userService;
    @Autowired
    IUserDao userDao;
/**
 * 
* @Title: toUpdateUserPwd
* @Description: TODO(跳轉到修改页面)
* 详细业务流程:
* (跳转到修改页面，并把user对象绑定到页面)
* @param @param request
* @param @param session
* @param @return    设定文件
* @return String    返回类型
* @throws
 */
@RequestMapping(value = "/user/pwd/toUpdate")
public String toUpdateUserPwd(HttpServletRequest request, HttpSession session){

	try{
	    	// 从Session中获取user对象
	    User u = (User) session.getAttribute(SystemConst.USER);
		//如果没有得到user对象，则返回登陆页面
		if(u == null){
		   return "/user";
		}
		//由session中user的id得到user对象
		User user = userService.getUser(u.getId());
		//将user对象绑定到user里，传送到页面
		request.setAttribute("user",user);
		request.setAttribute("parentPurviewId", "account");
		request.setAttribute("purviewId","toPwdUpdate");
		return "/userCenter/customer/count/passwordUpdate";
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	return "error";
}

/**
 * 
* @Title: updateUserPassword
* @Description: TODO(修改完成保存)
* 详细业务流程:
* (修改完成后保存到数据库)
* @param @param request
* @param @param session
* @param @return    设定文件
* @return String    返回类型
* @throws
 */
@RequestMapping(value = "/user/password/update")
public String updateUserPassword(HttpServletRequest request,
                                 HttpSession session){
	try {//页面得到的新密码
	    User u = (User) session.getAttribute(SystemConst.USER);
	    String password = request.getParameter("password");
	    if(StringUtil.isEmpty(password)){
	           request.setAttribute("updateOk",false);
	           request.setAttribute("user",u);
	           return "/userCenter/customer/count/passwordUpdate";
	    }
	    
	    //页面上获得的密码强度
	    String passwordStrong = request.getParameter("passwordStrong");
	    //从session中得到user对象
	  
	    if(u == null){
		return "/user";
	    }
	    //得到user对象
	    User user = userService.getUser(u.getId()); 
	    if(user == null){
		return "false";
	    }
	    MD5Util m = new MD5Util();
	    user.setPassword( m.getMD5ofStr(password));
	    user.setPasswordStrong(passwordStrong);
	    user.setUpdateTime(DateUtil.getSysDateTimeString());
	    //调用Service更新数据库
	   if( userService.updateUser(user)){
	       request.setAttribute("updateOk",true);
	       request.setAttribute("user",user);
	       request.setAttribute("parentPurviewId", "account");
	       request.setAttribute("purviewId","toPwdUpdate");
	       return "/userCenter/customer/count/passwordUpdate";
	    };
	}catch(Exception e){
	    	e.printStackTrace();
	}
	 request.setAttribute("updateOk",false);
	return "error";
}
  
    
    
    @RequestMapping (value="/user/security/password" ,method=RequestMethod.POST)
    @ResponseBody
    public String systemPassword(HttpServletRequest request,HttpSession session){
            User user = (User) session.getAttribute(SystemConst.USER);
            //根据userid更改数据库密码
            try {
                user = (User) userDao.get(user.getId());
                String pass  =  request.getParameter("password");
                String newPass = request.getParameter("newPassword");
                newPass = new MD5Util().getMD5ofStr(newPass);
                pass = new MD5Util().getMD5ofStr(pass);
                if(pass.equals(user.getPassword())){
                    user.setPassword(newPass);
                    userDao.updateBean(user);
                }else{
                    return "2";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "3";
            }
            return "1";
    }
/**
 * 
* @Title: checkUserPassword
* @Description: TODO(现在原密码输入是否正确)
* 详细业务流程:
* (从页面得到输入密码，在从数据库得到原密码，MD5加密后进行比较)
* @param @param lastpwd
* @param @param request
* @param @param session
* @param @return    设定文件
* @return boolean    返回类型
* @throws
 */
@RequestMapping(value = "/user/pwd/check/{lastpwd}")
@ResponseBody
public boolean checkUserPassword(@PathVariable String lastpwd,HttpServletRequest request,
                                HttpSession session){
	try{
	    //通过session得到的user对象
	    User u= (User)session.getAttribute(SystemConst.USER);
	    //如果没有得到user对象，则返回登陆页面
		if(u == null){
		   return false;
		}
	    //得到user对象
	    User user = userService.getUser(u.getId()); 
	    String password = user.getPassword();
	    //比较判断用户输入的密码和数据库里的密码是否一致
	    MD5Util m = new MD5Util();
	    String newMd5Password = m.getMD5ofStr(lastpwd);
	    if(StringUtil.isEmpty(password) || !password.equals(newMd5Password)){
		// 将用户名错误放入request中
		request.setAttribute("pswError", true);
		return false;
	    }
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	return true;
}
}
