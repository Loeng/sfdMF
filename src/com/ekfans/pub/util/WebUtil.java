/**
 * Web相关处理工具类
 */
package com.ekfans.pub.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Web相关处理工具类
 * 
 * @author chenzhiheng
 */
public class WebUtil {

	/**
	 *  
	 */
	public WebUtil() {

	}

	/**
	 * 获取Web上下文的真实地址
	 * 
	 * @param request
	 *            HttpServletRequest请求对象
	 * @return Web上下文真实地址
	 */
	public static String getContextAddress(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	}

	/**
	 * 根据制定路径上下文路径获取真实路径
	 * 
	 * @param request
	 *            HttpServletRequest请求对象
	 * @param contextPath
	 *            指定的上下文路径
	 * @return 根据制定的上下文路径获取到的真实路径
	 */
	public static String getRealPath(HttpServletRequest request, String contextPath) {
		ServletContext servletContext = request.getSession().getServletContext();
		return servletContext.getRealPath(contextPath);
	}

}
