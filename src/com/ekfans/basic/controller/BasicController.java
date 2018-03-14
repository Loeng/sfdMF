package com.ekfans.basic.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Action(Controller)基类
 * 
 * @ClassName: SystemController
 * @author 成都易科远见科技有限公司 
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class BasicController {
	
	private Logger log = LoggerFactory.getLogger(BasicController.class);
	
	/**
	 * 获取 HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * 获取URl   <br /><br />
	 * 
	 * 列如：                                   <br />
	 * http://localhost:8080/lq    <br />
	 * http://localhost:8080       <br />
	 * http://localhost/lq         <br />
	 * http://localhost            <br />
	 */
	public String getBasePath(){
		String str = getRequest().getScheme() + "://" + getRequest().getServerName();
		int port = getRequest().getServerPort();
		if(port != 80) {
			str += ":" + port;
		}
		String path = getRequest().getContextPath().trim();
		if(!"".equals(path) && !"/".equals(path)) {
			str += path;
		}
		
		return str;
	}
	
	/**
	 * 获取 HttpSession   <br /><br />
	 * 
	 * 需要注意的地方是request.getSession() 等同于 request.getSession(true)，      <br />
	 * 除非我们确认session一定存在或者sesson不存在时明确有创建session的需要，      <br />
	 * 否则尽量使用request.getSession(false)。在使用request.getSession()函数，    <br />
	 * 通常在action中检查是否有某个变量/标记存放在session中。                               <br />
	 */
	public HttpSession getSession() {
		/*
		 * HttpSession session = getRequest().getSession(boolean create);
		 * 
		 * 当前reqeust中的HttpSession，如果当前reqeust中的HttpSession 为null，
		 * 当create为true，就创建一个新的Session，否则返回null
		 */
		HttpSession session = getRequest().getSession(false);
		
		return session != null ? session : null;
	}
	
	/**
	 * ajax请求完成后回写数据
	 * 
	 * @param resp  HttpServletResponse
	 * @param str  回写字符串
	 */
	public void backWriteStr(HttpServletResponse resp, String str) {
		resp.reset();
		resp.setContentType("text/html; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		ServletOutputStream out = null;
		try {
			out = resp.getOutputStream();
			out.write(str.getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e1) {
					log.error(e1.getMessage());
				}
			}
		}
	}
}
