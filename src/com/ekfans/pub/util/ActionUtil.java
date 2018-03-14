package com.ekfans.pub.util;

import javax.servlet.http.HttpServletRequest;

public class ActionUtil {

	private ActionUtil(){}
	
	/**
	 * 获取站点url根路径                                           <br /><br />
	 * 
	 * 呈现格式一：http://localhost:8080/fscm      <br />
	 * 呈现格式二：http://localhost/fscm           <br />
	 * 呈现格式三：http://localhost:8080           <br />
	 * 呈现格式四：http://localhost                <br />
	 * @param request
	 * @return
	 */
	public static String getCtx(HttpServletRequest request) {
		String str = request.getScheme() + "://" + request.getServerName();
		int port = request.getServerPort();
		if (port != 80) {
			str += ":" + port;
		}
		String path = request.getContextPath().trim();
		if (!"".equals(path) && !"/".equals(path)) {
			str += path;
		}

		return str;
	}
}
