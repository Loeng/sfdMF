package com.ekfans.plugin.cache.service;

/**
 * 工具缓存的接口
 * 
 * @author liuguoyu
 * 
 */
public interface IToolsCacheService {

	/**
	 * 根据Key值获取资源束的值
	 * 
	 * @param key
	 * @return
	 */
	public String getResourceByKey(String key);
	
	/**
	 * 根据Key值获取资源束的值
	 * 
	 * @param key
	 * @return
	 */
	public String getBCSResourceByKey(String key);

	/**
	 * 根据Key值获取资源束的值
	 * 
	 * @param key
	 * @return
	 */
	public String getAppResourceByKey(String key);
}
