package com.ekfans.plugin.cache.utils;

/**
 * 缓存Key工具类
 * 
 * @author huanxu
 * 
 */
public class ToolsCacheKeyUtil {
	// 频道Key
	public static final String RESOURCE_KEY = "/resource/";

	// 长沙银行监管系统资源束Key
	public static final String BCS_RESOURCE_KEY = "/bcs/resource/";

	/**
	 * 获取资源束在缓存中的Key
	 * 
	 * @return
	 */
	public static String getResourceBundlesKey(String key) {
		return RESOURCE_KEY + key;
	}

	/**
	 * 获取长沙银行监管系统资源束在缓存中的Key
	 * 
	 * @return
	 */
	public static String getBCSResourceBundlesKey(String key) {
		return BCS_RESOURCE_KEY + key;
	}
}
