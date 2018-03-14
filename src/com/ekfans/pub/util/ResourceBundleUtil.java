package com.ekfans.pub.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Java获取资源束的工具类
 * 
 * @Title: ResourceBundleUtil.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-17
 * @version 1.0
 */
public class ResourceBundleUtil {
	// 定义资源束文件名
	private static String fileName = "/resources/configuration.properties";

	// 定义Properties对象
	private static Properties properties = new Properties();

//	public ResourceBundleUtil(String fileNames) {
//		// 将传进来的资源束名赋给fileName
//		fileName = fileNames;
//	}

	static {
		// 加载属性文件
		try {
			// 获取输入流
			InputStream in = ResourceBundleUtil.class.getResourceAsStream(fileName);

			if (in == null) {
				throw new RuntimeException(fileName + " not found");
			} else {
				if (!(in instanceof BufferedInputStream))
					in = new BufferedInputStream(in);

				try {
					// 加载资源束文件
					properties.load(in);
					in.close();
				} catch (Exception e) {
					throw new RuntimeException("Error while processing " + fileName, e);
				}
			}

		} catch (Exception e) {
			// logger.error(e.getMessage());
		}
	}

	// 根据资源束Key获取对应的值
	public  String getProperty(String key) {
		return properties.getProperty(key);

		// return ResourceBundle.getBundle("resources.configuration", new
		// Locale("", "")).getString(key);
	}

	public static void main(String[] args) {
		// System.out.println(ResourceBundle.getBundle("resources.configuration",
		// new Locale("", "")).getString("system.cache.memcache"));
		ResourceBundleUtil rbu = new ResourceBundleUtil();
		System.out.println(rbu.getProperty("area.parent.code"));
	}
}
