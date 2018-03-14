/*
 * EAAS
 * Copyright (C) 2003 eplugger.com
 * See the www.aebiz.com and www.wmmw.org for more details.
 */
package com.ekfans.pub.util.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.sitemesh.config.ConfigurableSiteMeshFilter;

import com.ekfans.pub.util.StringUtil;

/**
 * 
 * SiteMesh Filter 重写
 * 
 * @Title: ConfigurableSiteMeshFilter.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-22 下午01:53:18 
 * @version V1.0
 */
public class SiteMeshFilter extends ConfigurableSiteMeshFilter {
	private static final String IE_CONTENT_TYPE = "application/x-www-form-urlencoded";

	private static final String FF_AJAX_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";

	private static final String XMLHTTP_REQUEST = "XMLHttpRequest";

	private static final String AJAX_CHARACTER_ENCODING_UTF8 = "UTF-8";

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * Select and set (if specified) the character encoding to be used to
	 * interpret request parameters for this request.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 * @param response
	 *            The servlet response we are creating
	 * @param chain
	 *            The filter chain we are processing
	 * 
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestedWith = httpRequest.getHeader("x-requested-with");
		String type = request.getContentType();
		if (XMLHTTP_REQUEST.equals(requestedWith) && (FF_AJAX_CONTENT_TYPE.equals(type) || IE_CONTENT_TYPE.equals(type))) {
			request.setCharacterEncoding(AJAX_CHARACTER_ENCODING_UTF8);
			response.setCharacterEncoding(AJAX_CHARACTER_ENCODING_UTF8);
			request.getParameter("");
		}

		String requestUri = httpRequest.getRequestURI();
		if (!StringUtil.isEmpty(requestUri) && requestUri.contains("/system/")) {
			chain.doFilter(request, response);
		} else {
			super.doFilter(request, response, chain);
		}
	}

	/**
	 * Place this filter into service.
	 * 
	 * @param filterConfig
	 *            The filter configuration object
	 * @exception ServletException
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}
}
