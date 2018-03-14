package com.ekfans.controllers.user.count;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;

/**
 * 
 * 跳转到安全页面Controller
 * 
 * @Title: UserSafeController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liulin
 * @date 2014-4-1 下午15:57:47 
 * @version V1.0
 */
@Controller
public class UserSafeControllers extends BasicController {
    @Autowired
    IUserService userService;
    /**
     * 跳转到安全设置页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/safe/list")
    public String toUpdateUserPwd(HttpServletRequest request,
                                   HttpSession session){
	try {
	    	// 从Session中获取user对象
		User u = (User) session.getAttribute(SystemConst.USER);
		//如果没有得到user对象，则返回登陆页面
		if(u == null){
		   return "/user/login";
		}
		//由store中的id得到user对象
		User user = userService.getUser(u.getId());
		//将user对象绑定到user中传送到页面
		request.setAttribute("user",user);
		request.setAttribute("purviewId","safeList");
		request.setAttribute("parentPurviewId", "account");
		return "/userCenter/customer/count/safeList";
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
	return "error";
    }
}
