package com.ekfans.controllers.store;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.user.service.UserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.CookieUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 店铺登录Controller
 * 
 * @Title: StoreLoginController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author lgy 
 * @date 2014-3-17 下午9:18:54 
 * @version V1.0
 */
@Controller
public class StoreLoginController extends BasicController {
	@Autowired
	private IStoreService storeService;

	/**
	 * 店铺登录跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/login")
	public ModelAndView goLogin(HttpServletRequest request, HttpServletResponse response) {
		// 定义Cookie工具类
		CookieUtil cookieUtil = new CookieUtil(request, response);

		// 定义返回参数的Map
		Map<String, Object> map = new HashMap<String, Object>();

		// 调用工具类获取管理员的登录Cookies
		String cookieName = cookieUtil.getStoreLoginNameCookie();
		String isCookie = cookieUtil.getValueByKey("storeRemeber");
		map.put("cookieName", !StringUtil.isEmpty(cookieName) ? cookieName : "");
		map.put("isCookie", !StringUtil.isEmpty(isCookie) ? isCookie : "");
		return new ModelAndView("storeLogin", map);

	}

	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/loginCheck")
	public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 从页面获取店铺登录名
		String name = request.getParameter("name");
		// 从页面获取店铺登录密码
		String password = request.getParameter("password");
		// 从页面获取是否保存用户名
		String isCookie = request.getParameter("isCookie");

		// 调用Service方法从数据库根据用户名查询Store对象
		Object[] obj = storeService.storeLoginByName(name);
		// 如果获取的Object数组对象为空，则返回用户名不存在的错误
		if (obj == null || obj.length <= 0) {
			// 将用户名错误放入request中
			request.setAttribute("nameError", true);
			request.setAttribute("cookieName", name);
			return "storeLogin";
		}

		String storePassword = (String) obj[0];
		Store store = (Store) obj[1];
		MD5Util m = new MD5Util();
		// 将密码使用MD5加密与数据库查询出来的进行比较，如果不正确，则返回密码错误的提示
		if (StringUtil.isEmpty(password) || !m.getMD5ofStr(password).equals(storePassword)) {
			// 将用户名错误放入request中
			request.setAttribute("pswError", true);
			request.setAttribute("cookieName", name);
			return "storeLogin";
		}
		// 定义Cookie工具类
		CookieUtil cookieUtil = new CookieUtil(request, response);
		if ("true".equals(isCookie)) {
			// TODO 将用户名密码记入Cookie
			cookieUtil.addStoreCookie(store.getId(), name);
			cookieUtil.addCookie("storeRemeber", "true");
		} else {
			cookieUtil.removeManagerCookie(store.getId());
			cookieUtil.removeCookieByKey("storeRemeber");
		}
		// 驻入的形式获取会员对象
		IUserService userService = SpringContextHolder.getBean(UserService.class);
		// 根据会员主键获取会员对象
		User user = userService.getUser(store.getId());
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
		// 将Store对象放入Session中
		session.setAttribute(SystemConst.STORE, store);

		// 返回到店铺会员中心首页
		return "redirect:/store/index";
	}
}
