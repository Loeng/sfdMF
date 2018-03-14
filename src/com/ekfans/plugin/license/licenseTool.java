package com.ekfans.plugin.license;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 用来做License解析校验的核心类
 * 
 * @Title: licenseTool.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-23
 * @version 1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class licenseTool {
	/**
	 * 校验License
	 * 
	 * @return
	 */
	public static boolean checkLicenseStr(HttpServletRequest request, String domainName) {
		// 获取license的详细目录
		// String fileName =
		// Class.class.getClassLoader().getResource("").getPath() +
		// "license.lic";
		// String fileName = licenseTool.class.getClassLoader().getResource("")
		// + "license.lic";
		String fileName = request.getSession().getServletContext().getRealPath("/") + "WEB-INF/classes/license.lic";
		System.out.println(fileName);
		// 调用File工具类获取license的详细内容
		String tempString = FileUtil.getFileContent(fileName);
		String encodeStr = "";
		// 定义解析License的类
		KeyRSA keyRSA = new KeyRSA();
		try {
			// 调用方法解析license中的字符串
			encodeStr = keyRSA.Decoder(tempString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}

		// 如果解析出来的License字符串为空，则返回false
		if (StringUtil.isEmpty(encodeStr)) {
			return false;
		}

		try {
			// 获取本机hostName
			InetAddress address = InetAddress.getLocalHost();
			// 解析license获取域名和有效期
			String[] licenses = encodeStr.split("==");
			// 获取license设置的有效结束日期
			String endTime = licenses[1];

			// 比较该license的有效期是否已过期，如果过期，则返回false；
			if (DateUtil.compareTimestamp(DateUtil.getSysDateTimeString(), endTime) <= 0) {
				return false;
			}

			// 创建一个Map。将license里的域名、localhost、127.0.0.1等等放到map中
			Map<String, String> domainMap = new HashMap();
			domainMap.put(licenses[0], "true");
			domainMap.put("localhost", "true");
			domainMap.put("127.0.0.1", "true");
			domainMap.put(address.getHostAddress(), "true");
			domainMap.put(address.getHostName(), "true");

			// 如果map中存在传过来的域名，则表示license校验有效
			if ("true".equals(domainMap.get(domainName))) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	public static void main(String[] args) {
		String fileName = ClassLoader.getSystemResource("").getPath() + "license.lic";
		System.out.println(fileName);
	}
}
