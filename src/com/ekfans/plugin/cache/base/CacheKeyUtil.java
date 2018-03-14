package com.ekfans.plugin.cache.base;

/**
 * 缓存Key工具类
 * 
 * @author huanxu
 * 
 */
public class CacheKeyUtil {
	// 频道Key
	public static final String CHANNEL_KEY = "/channel/";

	/**
	 * 获取频道缓存的Key
	 * 
	 * @return
	 */
	public static String getChannelKey(String orgId) {
		return CHANNEL_KEY + orgId;
	}

}
