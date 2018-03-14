package com.ekfans.controllers.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.CookieUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 会员注册或登陆的Controller
 * 
 * @Title: UserLoginAndRegistController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-25 上午11:01:20 
 * @version V1.0
 */
@Controller
@RequestMapping("/user")
public class UserLoginAndRegistController extends BasicController {
	@Autowired
	IUserService userService;

	/**
	 * 会员登陆跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login")
	public ModelAndView goLogin(HttpServletRequest request, HttpServletResponse response) {
		// 定义Cookie工具类
		CookieUtil cookieUtil = new CookieUtil(request, response);

		// 定义返回参数的Map
		Map<String, Object> map = new HashMap<String, Object>();

		// 调用工具类获取管理员的登录Cookies
		String cookieName = cookieUtil.getUserLoginNameCookie();
		String isCookie = cookieUtil.getValueByKey("userRemeber");
		map.put("cookieName", !StringUtil.isEmpty(cookieName) ? cookieName : "");
		map.put("isCookie", !StringUtil.isEmpty(isCookie) ? isCookie : "");
		return new ModelAndView("login", map);

	}

	/**
	 * 会员登录信息校验
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
	public String logCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		// 获取页面输入的用户名
		String name = request.getParameter("name");
		// 获取页面输入的密码
		String pwd = request.getParameter("password");
		// 获取页面获取的是否保存用户名
		String isCookie = request.getParameter("isCookie");

		// 调用Service根据用户名获取用户
		User user = userService.getUserByName(name);
		// 如果获取的用户对象为空，则证明用户名不存在
		if (user == null) {
			// 将验证信息放入request，返回视图页面
			request.setAttribute("nameError", true);
			request.setAttribute("cookieName", name);
			return "login";
		}

		// 定义Md5工具类
		MD5Util m = new MD5Util();

		// 如果获取的页面输入密码为空，或者Md5加密后和从数据库取出的密码不一致，则返回密码错误
		if (StringUtil.isEmpty(pwd) || !m.getMD5ofStr(pwd).equals(user.getPassword())) {
			// 将验证信息放入request，返回视图页面
			request.setAttribute("pwdError", true);
			request.setAttribute("cookieName", name);
			return "login";
		}

		// 定义Cookie工具类
		CookieUtil cookieUtil = new CookieUtil(request, response);

		// 将用户名密码记入Cookie
		if ("true".equals(isCookie)) {
			cookieUtil.addStoreCookie(user.getId(), name);
			cookieUtil.addCookie("storeRemeber", "true");
		} else {
			cookieUtil.removeManagerCookie(user.getId());
			cookieUtil.removeCookieByKey("storeRemeber");
		}

		// 更改上次登陆时间
		user.setLastLoginTime(DateUtil.getSysDateTimeString());
		// 更改登陆次数
		user.setLoginNum(user.getLoginNum() + 1);
		// 调用Service更新会员
		try {
		    userService.updateUser(user);
		}
		catch(Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		// 将User对象放入Session中
		session.setAttribute(SystemConst.USER, user);

		return "redirect:/user/center/index";
	}

	/**
	 * 会员退出登陆
	 * 
	 * @Title: logout
	 * @Description: 会员退出登陆的Controller方法 1.从Session中移除会员对象
	 *               2.跳转到首页(测试时暂时跳转到会员登陆页)
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 从Session中移除会员对象
		session.removeAttribute(SystemConst.USER);
		session.invalidate();
		return "redirect:/user/login";
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	public String register() {
		return "register";
	}
}
