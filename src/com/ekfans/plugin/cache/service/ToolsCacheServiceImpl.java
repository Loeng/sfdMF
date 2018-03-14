package com.ekfans.plugin.cache.service;

import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.utils.ToolsCacheKeyUtil;
import com.ekfans.pub.util.AppResourceBundleUtil;
import com.ekfans.pub.util.BCSResourceBundleUtil;
import com.ekfans.pub.util.ResourceBundleUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 工具缓存的接口实现
 * 
 * @author liuguoyu
 * 
 */
public class ToolsCacheServiceImpl implements IToolsCacheService {
	/**
	 * 根据Key值获取资源束的值
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String getResourceByKey(String key) {
		// 如果主键为空，则返回空
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		// 调用工具类获取资源束在缓存中的key
		String cacheKey = ToolsCacheKeyUtil.getResourceBundlesKey(key);
		// 从缓存中获取资源束的值
		String resourceValue = (String) Cache.engine.get(cacheKey);
		// 如果获取的值为空，则调用方法从资源束获取
		if (StringUtil.isEmpty(resourceValue)) {
			ResourceBundleUtil rbu = new ResourceBundleUtil();
			// 调用方法从资源束中获取Key对应的值
			resourceValue = rbu.getProperty(key);
			// 如果取出来的值不为空，则放入缓存
			if (!StringUtil.isEmpty(resourceValue)) {
				Cache.engine.add(cacheKey, resourceValue);
			}
		}

		return resourceValue;
	}

	/**
	 * 根据Key值获取资源束的值
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String getBCSResourceByKey(String key) {
		// 如果主键为空，则返回空
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		// 调用工具类获取资源束在缓存中的key
		String cacheKey = ToolsCacheKeyUtil.getBCSResourceBundlesKey(key);
		// 从缓存中获取资源束的值
		String resourceValue = (String) Cache.engine.get(cacheKey);
		// 如果获取的值为空，则调用方法从资源束获取
		if (StringUtil.isEmpty(resourceValue)) {
			BCSResourceBundleUtil rbu = new BCSResourceBundleUtil();
			// 调用方法从资源束中获取Key对应的值
			resourceValue = rbu.getProperty(key);
			// 如果取出来的值不为空，则放入缓存
			if (!StringUtil.isEmpty(resourceValue)) {
				Cache.engine.add(cacheKey, resourceValue);
			}
		}

		return resourceValue;
	}

	/**
	 * 根据Key值获取资源束的值
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String getAppResourceByKey(String key) {
		// 如果主键为空，则返回空
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		// 调用工具类获取资源束在缓存中的key
		String cacheKey = ToolsCacheKeyUtil.getResourceBundlesKey(key);
		// 从缓存中获取资源束的值
		String resourceValue = (String) Cache.engine.get(cacheKey);
		// 如果获取的值为空，则调用方法从资源束获取
		if (StringUtil.isEmpty(resourceValue)) {
			AppResourceBundleUtil rbu = new AppResourceBundleUtil();
			// 调用方法从资源束中获取Key对应的值
			resourceValue = rbu.getProperty(key);
			// 如果取出来的值不为空，则放入缓存
			if (!StringUtil.isEmpty(resourceValue)) {
				Cache.engine.add(cacheKey, resourceValue);
			}
		}

		return resourceValue;
	}
}
