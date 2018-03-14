package com.ekfans.pub.util.filters;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetbrick.template.web.springmvc.JetTemplateViewResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewRendererServlet;

import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 集成SiteMash与jetbirck的Filter
 * 
 * @Title: JetBirckFilter.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-22 下午03:51:03 
 * @version V1.0
 */
public class JetBirckFilter implements Filter {

	public static Logger log = LoggerFactory.getLogger(JetBirckFilter.class);
	private Locale locale;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String localeStr = filterConfig.getInitParameter("locale");
		if (!StringUtil.isEmpty(localeStr)) {
			locale = new Locale(localeStr);
		} else {
			locale = Locale.getDefault();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			// 将ServletRequest转换成HttpServletRequest
			HttpServletRequest req = (HttpServletRequest) request;
			// 将ServletResponse转换成HttpServletResponse
			HttpServletResponse res = (HttpServletResponse) response;
			// 从Request中获取访问URI
			String name = req.getRequestURI();
			// 如果URI不含有 .jetx，则返回
			if (name.indexOf(".jetx") == -1) {
				return;
			}
			// 获取模板的路径+名称 如： /WEB-INF/pages/store/index
			name = name.substring(0, name.lastIndexOf(".jetx"));
			// 如果获取的路径含有项目上下文，则将上下文去掉
			if (!StringUtil.isEmpty(req.getContextPath()) && !StringUtil.isEmpty(name) && name.indexOf(req.getContextPath()) != -1) {
				name = name.substring(name.indexOf(req.getContextPath()) + req.getContextPath().length(), name.length());
			}
			// 如果获取的路径以"/"开头，则将"/"去掉
			if (!StringUtil.isEmpty(name) && name.startsWith("/")) {
				name = name.substring(1, name.length());
			}

			// 通过Spring的方式获取JetTemplate的视图
			JetTemplateViewResolver viewResolver = null;
			View view = null;
			viewResolver = (JetTemplateViewResolver) SpringContextHolder.getBean(JetTemplateViewResolver.class);
			if (viewResolver != null) {
				view = viewResolver.resolveViewName(name, locale);
			} else {
				log.error("JetBirckFilter Error --- viewResolver is null!");
			}
			@SuppressWarnings({ "unchecked", "unused" })
			Map<String, Object> model = (Map<String, Object>) request.getAttribute(ViewRendererServlet.MODEL_ATTRIBUTE);
			if (view != null) {
				view.render(null, req, res);
			} else {
				log.error("JetBirckFilter Error --- viewResolver is null!");
			}
		} catch (Exception e) {
			log.error("JetBirckFilter Error --" + e.getMessage());
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
