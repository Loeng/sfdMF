package com.ekfans.plugin.cache.service;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.utils.UserCacheKeyUtil;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.pub.util.StringUtil;

public class AppUserCacheService implements IAppUserCacheService {

	@Override
	public AppUser getAppUserByToken(String token,HttpServletRequest request) {
		// 如果主键为空，则返回空
		if (StringUtil.isEmpty(token)) {
			return null;
		}
		// 调用工具类获取在缓存中的key
		String cacheKey = UserCacheKeyUtil.getUserTokenCacheKey(token);
		// 从缓存中获取值
		AppUser appUser = (AppUser) Cache.engine.get(cacheKey);
		// 不从数据库获取了
		// 如果获取的值为空，则调用方法从数据库束获取
		if (appUser == null) {
			String id = token.substring(0, token.indexOf("_"));

			IUserService userService = SpringContextHolder.getBean(IUserService.class);
			// 调用方法从数据库中获取Key对应的值
			appUser = userService.getAppUserById(id);
			// 如果取出来的值不为空，则放入缓存
			if (appUser != null) {
				Cache.engine.add(cacheKey, appUser);
			}
		}
		return appUser;
	}

	public boolean addOrUpdAppUserByToken(String token, AppUser appUser) {
		if (StringUtil.isEmpty(token) || appUser == null) {
			return false;
		}
		// 调用工具类获取在缓存中的key
		String cacheKey = UserCacheKeyUtil.getUserTokenCacheKey(token);
		Cache.engine.remove(cacheKey);
		Cache.engine.add(cacheKey, appUser);
		return true;
	}

}
