package com.ekfans.plugin.cache.utils;

/**
 * 
 * 店铺缓存工具类
 * 
 * @Title: StoreCacheKeyUtil.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-21 上午11:28:53 
 * @version V1.0
 */
public class StoreCacheKeyUtil {

	// 店铺功能菜单缓存Key
	public static final String PURVIEW_ROLE_KEY = "/store/purview/role/";
	

	// 店铺功能菜单缓存Key
	public static final String PURVIEW_KEY = "/store/purview/";

	public static final String CHILD_PURVIEW_KEY = "/store/childs/";
	

	/**
	 * 获取店铺功能菜单在缓存中的Key
	 * 
	 * @return
	 */
	public static String getStorePurviewByRoleKey(String key) {
		return PURVIEW_ROLE_KEY + key;
	}
	
	/**
	 * 获取店铺功能菜单在缓存中的Key
	 * 
	 * @return
	 */
	public static String getStorePurviewKey(String key) {
		return PURVIEW_KEY + key;
	}

	/**
	 * 获取店铺功能菜单在缓存中的子Key
	 * 
	 * @return
	 */
	public static String getStoreChildPurviewKey(String roleId, String purviewId) {
		return CHILD_PURVIEW_KEY + roleId + "/" + purviewId;
	}
	
}	