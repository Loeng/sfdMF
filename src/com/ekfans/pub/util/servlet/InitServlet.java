package com.ekfans.pub.util.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.ResourceBundleUtil;

@SuppressWarnings("serial")
public class InitServlet extends DispatcherServlet {

	private Logger log = LoggerFactory.getLogger(InitServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		log.info("初始化系统配置开始");

		// 初始化系统全局变量
		// 定义资源束
		log.info("初始化缓存开始");
		ResourceBundleUtil rbu = new ResourceBundleUtil();
		// 初始化缓存
		Cache.isMemcached = Boolean.parseBoolean(rbu.getProperty("system.cache.memcache"));
		Cache.init();
		// 加载自定义配置文件
		// ...

		// 从缓存中获取网站名
		config.getServletContext().setAttribute("webName", Cache.getSystemContentConfig("网站名称"));
		// 从缓存中获取网站热线
		config.getServletContext().setAttribute("webPhone", Cache.getSystemContentConfig("热线电话"));
		// 从缓存中获取网站logo
		config.getServletContext().setAttribute("webLogo", Cache.getSystemContentConfig("网站Logo"));
		// 从缓存中获取网站logo
		// config.getServletContext().setAttribute("yxLogo",
		// Cache.getSystemContentConfig("优选商城LOGO"));
		// // 从缓存中获取网站logo
		// config.getServletContext().setAttribute("companyLogo",
		// Cache.getSystemContentConfig("企业中心LOGO"));
		// 从缓存中获取网站的底部版权信息
		config.getServletContext().setAttribute("webCopyright", Cache.getSystemContentConfig("底部版权信息"));
		// 从缓存获取网站底部联系信息
		config.getServletContext().setAttribute("webContactInformation", Cache.getSystemContentConfig("底部联系信息"));
		// 从缓存获取网站底部联系信息
		config.getServletContext().setAttribute("servieRule", Cache.getSystemContentConfig("服务条款"));

		// 从缓存获取大宗帮助中心
		config.getServletContext().setAttribute("dzHelp", Cache.getContentsByCategoryName("大宗帮助中心"));
		// 从缓存获取优选商城帮助中心
		config.getServletContext().setAttribute("yxHelp", Cache.getContentsByCategoryName("优选帮助中心"));

		// 启动定时任务
		// ...
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
	}

}
