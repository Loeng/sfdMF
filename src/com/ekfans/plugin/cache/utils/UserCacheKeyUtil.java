package com.ekfans.plugin.cache.utils;

public class UserCacheKeyUtil {
	// 用户TOKEN缓存KEY
	public static final String USER_TOKEN_KEY = "token/";

	/**
	 * 获取用户TOKEN在缓存中的Key
	 * 
	 * @return
	 */
	public static String getUserTokenCacheKey(String key) {
		return USER_TOKEN_KEY + key;
	}
}
