package com.ekfans.controllers.store.count;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 店铺商品Controller
 * 
 * @Title: StorePasswordController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liulin
 * @date 2014-3-25 上午09:23:47 
 * @version V1.0
 */
@Controller
public class StorePasswordController extends BasicController {
    @Autowired
	IUserService userService;
    
    /**
     * 跳转带修改页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/store/pwd/toUpdate")
    public String toUpdateUserPwd(HttpServletRequest request,
                                  HttpServletResponse response, HttpSession session){
	// 从Session中获取user对象
    	try{
	    	// 从Session中获取store对象
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		//如果没有得到store对象，则返回登陆页面
		if(store == null){
		   return "/store/login";
		}
		//由store的id得到user对象
		User user = userService.getUser(store.getId());
		//将user对象绑定到user里，传送到页面
		request.setAttribute("user",user);
		return "/store/count/passwordUpdate";
	}
	catch(Exception e){
	}
	return "error";

    }
  
    /**
     * 修改完成，保存操作
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "/store/password/update")
    public String updateUserPassword(HttpServletRequest request,
                                     HttpServletResponse response,HttpSession session){
	try
	{
         //      从session中得到stroe对象
	    Store store = (Store)session.getAttribute(SystemConst.STORE);
	    
	    if(store == null){
            return "/store/login";
        }
	    //得到user对象
        User user = userService.getUser(store.getId());
        
	    String password = request.getParameter("password");
	    if(StringUtil.isEmpty(password)){
	        request.setAttribute("returnType","failure");
	        request.setAttribute("user",user);
	        return "/store/count/passwordUpdate";
	    }
	    
	    String passwordStrong = request.getParameter("passwordStrong");

	   
	    
	    if(user == null){
		return "false";
	    }
	    MD5Util m = new MD5Util();
	    user.setPassword( m.getMD5ofStr(password));
	    user.setPasswordStrong(passwordStrong);
	    user.setUpdateTime(DateUtil.getSysDateTimeString());
	    //调用Service更新数据库
	   if( userService.updateUser(user)){
	       request.setAttribute("returnType","success");
	       request.setAttribute("user",user);
	       return "/store/count/passwordUpdate";
	    };
	}catch(Exception e){
	    	e.printStackTrace();
	}
	 request.setAttribute("returnType","failure");
	return "error";
    }
    
    /**
     * 验证原始密码是否正确
     * @param request
     * @param lastPwd 原密码
     * @return
     */
    @RequestMapping(value = "/store/pwd/check/{lastpwd}")
    @ResponseBody
    public boolean checkUserPassword(HttpServletRequest request,@PathVariable String lastpwd,
                                    HttpSession session){
	try{
	    //通过session得到的用户密码
	    Store store = (Store)session.getAttribute(SystemConst.STORE);
	    //得到user对象
	    User user = userService.getUser(store.getId()); 
	    String password = user.getPassword();
	    //比较判断用户输入的密码和数据库里的密码是否一致
	    MD5Util m = new MD5Util();
	    if(StringUtil.isEmpty(password) || !m.getMD5ofStr(lastpwd).equals(password)){
		// 将用户名错误放入request中
		request.setAttribute("pswError", true);
		return false;
	    }
	}
	catch(Exception e){
	}
	return true;
    }
}
