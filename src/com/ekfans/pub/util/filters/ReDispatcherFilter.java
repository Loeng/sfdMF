package com.ekfans.pub.util.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该类是定义了一个过滤器，当既用到struts2有用到servlet时， 必须用该过滤器对所有的servlet做一次过滤，否则servlet将无法正常运行。
 * 
 * @Title: ReDispatcherFilter.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-31
 * @version 1.0
 */
public class ReDispatcherFilter implements Filter {
	public void destroy() {

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		String uri = request.getRequestURI();
		String actionName = uri.substring(uri.lastIndexOf("/") + 1);
		if (allServlet.contains(actionName)) {
			RequestDispatcher dis = request.getRequestDispatcher(actionName);
			dis.forward(request, response);
		} else {
			arg2.doFilter(request, response);
		}
	}

	private List<String> allServlet = new ArrayList<String>();

	public void init(FilterConfig arg0) throws ServletException {

		String[] str = arg0.getInitParameter("includeServlets").split(",");
		this.allServlet.addAll(Arrays.asList(str));

	}
}