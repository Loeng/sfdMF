package com.ekfans.plugin.cache.service;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.plugin.wftong.controller.model.AppUser;

/**
 * AppUser用户对象缓存服务
 * @author 成都易科远见有限公司
 *
 */
public interface IAppUserCacheService {
	
	/**
	 * 获取对象
	 * @param token
	 * @return
	 */
	public AppUser getAppUserByToken(String token,HttpServletRequest request);

	/**
	 * 添加或更新对象
	 * @param token
	 * @param user
	 * @return
	 */
	public boolean addOrUpdAppUserByToken(String token, AppUser user);

}
