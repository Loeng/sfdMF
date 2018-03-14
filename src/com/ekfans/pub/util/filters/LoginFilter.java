/*
 * EAAS
 * Copyright (C) 2003 eplugger.com
 * See the www.aebiz.com and www.wmmw.org for more details.
 */
package com.ekfans.pub.util.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppAptitude;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppUserDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.WlbUser;

/**
 * 
 * 登录过滤器
 * 
 * @Title: LoginFilter.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author yikelizi 
 * @date 2014-3-10 下午5:09:26 
 * @version V1.0
 */
public class LoginFilter extends OncePerRequestFilter {

	@Override
	/**
	 * 登陆过滤器具体实现
	 */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// 获取Session
		HttpSession session = request.getSession();
		String servletPath = request.getRequestURI();
		System.out.println("访问路径:"+servletPath);

		// 如果获取的Session为空，则返回，不走过滤器
		if (session == null) {
			return;
		}

		
		//进入物流宝登录过滤
		if (servletPath.indexOf("wlbApp") != -1) {
			//不需要登录验证  直接跳转
			if(servletPath.indexOf("login") != -1||servletPath.indexOf("share") != -1||servletPath.indexOf("modifyPW") != -1||servletPath.indexOf("getCode") != -1||
					servletPath.indexOf("register") != -1|| servletPath.indexOf("getHuoList") != -1 ||servletPath.indexOf("getCheList") != -1 ||servletPath.indexOf("getHuoDetail") != -1
					||servletPath.indexOf("getVehicleType") != -1 ||servletPath.indexOf("getTankMaterial") != -1 ||servletPath.indexOf("getCarCKG") != -1 ||servletPath.indexOf("getArea") != -1
					||servletPath.indexOf("getGoodCategory") != -1||servletPath.indexOf("getGoodWfpy") != -1||servletPath.indexOf("getCapacity") != -1||servletPath.indexOf("getAd") != -1||servletPath.indexOf("addMessageBack") != -1||servletPath.indexOf("checkupdate") != -1){
				filterChain.doFilter(request, response);
				return;
			}
			// 从请求头中获取物流宝用户token
			String token = request.getHeader("WLB-Token");
			if(token==null){
				response.sendError(401,"未授权[无token、token错误、token过期]");
				return;
			}
			if(Cache.engine.get(token)==null){
				IWlbAppUserDao userDao = SpringContextHolder.getBean(IWlbAppUserDao.class);
				try {
					//通过token获取用户id
					String userid = token.split("_")[0];
					User user = (User) userDao.get(userid);
					if(user==null){
						response.sendError(401,"未授权[无token、token错误、token过期]");
						return;
					}
					if(user.getWlbToken().equals(token)){
						IWlbAppAptitude apDao = SpringContextHolder.getBean(IWlbAppAptitude.class);
						//通过用户id获取用户资质认证
						Aptitude ap =  apDao.getAptitude(userid);
						//包装用户信息
						WlbUser wlbUser = new WlbUser(user,ap,token);
						//放入缓存
						Cache.engine.add(token, wlbUser);
						filterChain.doFilter(request, response);
						return;
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				response.sendError(401,"未授权[无token、token错误、token过期]");
				return;
			}
			filterChain.doFilter(request, response);
			return;
		}

		// 如果路径中存在 .jetx 或者 存在 login 字符，则直接跳转，不需要登陆验证
		if (servletPath.indexOf(".jetx") != -1 || servletPath.indexOf("login") != -1 || servletPath.indexOf("push") != -1 ) {
			filterChain.doFilter(request, response);
			return;
		}

		// 如果路径是以"/user"开头并且不存在 login字符串，则使用会员的过滤
		if (servletPath.startsWith(request.getContextPath() + SystemConst.USER_PATH) && servletPath.indexOf("login") == -1) {
			// 从Session中获取会员对象
			User user = null;
			if (session != null) {
				user = (User) request.getSession().getAttribute(SystemConst.USER);
			}

			if (request.getRequestURI().contains("/user/center/getSessionUser")) {
				filterChain.doFilter(request, response);
				return;
			}

			if ((user == null || !UserConst.USER_TYPE_CUSTOMER.equals(user.getType())) && request.getRequestURI().contains("/user/center/top")) {
				filterChain.doFilter(request, response);
				return;
			}
			// 如果会员对象为空，则重定向到会员登陆页面
			if ((user == null || !UserConst.USER_TYPE_CUSTOMER.equals(user.getType())) || !UserConst.USER_TYPE_CUSTOMER.equals(user.getType())) {
				String toUri = request.getContextPath() + "/web/login/0";
				if (response != null) {
					response.sendRedirect(toUri);
					return;
				}
			}

			// 如果路径是 ../user 或者 ../user/则跳转到会员中心首页
			if (servletPath.equals(request.getContextPath() + SystemConst.USER_PATH) || servletPath.equals(request.getContextPath() + SystemConst.USER_PATH + "/")) {
				String toUri = request.getContextPath() + "/user/center/index";
				if (response != null) {
					response.sendRedirect(toUri);
					return;
				}
			}
		}

		// 如果路径是以 "/store" 开头 并且不存在 login字符串，则进入店铺的过滤
		if (servletPath.startsWith(request.getContextPath() + SystemConst.STORE_PATH) && servletPath.indexOf("login") == -1) {
			// 从Session中获取店铺对象
			Store store = null;
			if (session != null) {
				store = (Store) request.getSession().getAttribute(SystemConst.STORE);
			}

			Account account = (Account) request.getSession().getAttribute(SystemConst.ACCOUNT);

			// 如果获取的店铺对象为空，则重定向到店铺登陆页面
			if (store == null) {
				String toUri = request.getContextPath() + "/web/login";
				if (response != null) {
					response.sendRedirect(toUri);
					return;
				}
			}

			// 调用缓存获取子权限集合,并将角色放入SESSION
			if (session.getAttribute("purviewList") == null) {
				List<StorePurview> purviewList = null;
				if (account != null) {
					purviewList = Cache.getStorePurviewByRoleId(account.getId());
				} else {
					purviewList = Cache.getStorePurviewByRoleId(store.getRoleId());
				}
				session.setAttribute("purviewList", purviewList);
			}

			// 如果路径是 ../store 或者 ../store/则跳转到店铺中心首页
			if (servletPath.equals(request.getContextPath() + SystemConst.STORE_PATH) || servletPath.equals(request.getContextPath() + SystemConst.STORE_PATH + "/")) {
				String toUri = request.getContextPath() + "/store/index";
				if (response != null) {
					response.sendRedirect(toUri);
					return;
				}
			}

		}

		// 如果路径是以 "/system" 开头 并且不存在 login字符串，则进入后台系统登陆过滤
		if (servletPath.startsWith(request.getContextPath() + SystemConst.ROOT_PATH) && servletPath.indexOf("login") == -1) {
			// 从Session获取管理员对象
			Manager manager = null;
			if (session != null) {
				manager = (Manager) request.getSession().getAttribute(SystemConst.MANAGER);
			}

			if (request.getRequestURI().contains("/system/channel/config/adChose")) {
				filterChain.doFilter(request, response);
				return;
			}

			// 如果管理员为空，则重定向到管理员登陆页面
			if (manager == null) {
				String toUri = request.getContextPath() + "/system/login";
				if (session != null) {
					response.sendRedirect(toUri);
					return;
				}
			}

			// 如果路径是 ../system 或者 ../system/则跳转到管理员管理中心首页
			if (servletPath.equals(request.getContextPath() + SystemConst.MANAGER) || servletPath.equals(request.getContextPath() + SystemConst.MANAGER + "/")) {
				String toUri = request.getContextPath() + "/system/index";
				if (response != null) {
					response.sendRedirect(toUri);
					return;
				}
			}
		}

		// 如果uri中不包含background，则继续
		filterChain.doFilter(request, response);
	}
}
