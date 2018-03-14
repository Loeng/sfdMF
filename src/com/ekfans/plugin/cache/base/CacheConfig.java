package com.ekfans.plugin.cache.base;

/**
 * 缓存配置
 * 
 * @Title: CacheConfig.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-16
 * @version 1.0
 */
public class CacheConfig {
	public CacheConfig() {
	}

	// 配置文件路径
	public static String SYSTEMCONFIG_CACHECONFIG = "/WEB-INF/classes/xmlconfig/CacheConfig.xml";

	// 判断是否运行
	private boolean isRun = true;

	// 初始化更新时间
	private int refreshTime = 60;

	public int getRefreshTime() {
		return refreshTime;
	}

	public boolean isIsRun() {
		return isRun;
	}

	public void setIsRun(boolean isRun) {
		this.isRun = isRun;
	}

	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}
}