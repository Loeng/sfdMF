package com.ekfans.plugin.cache.service;

import java.util.List;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.base.store.model.StorePurview;

/**
 * 
 * 店铺相关缓存接口
 * 
 * @Title: IStoreCacheService.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-21 上午10:13:20 
 * @version V1.0
 */
public interface IStoreCacheService {
	/**
	 * 根据店铺角色ID获取店铺权限集合
	 * 
	 * @param roleId
	 * @return
	 */
	public List<StorePurview> getStorePurviewsByRoleId(String roleId);
	
	
	/**
	 * 根据店铺权限ID获取店铺子权限集合
	 * 
	 * @param roleId
	 * @param purviewId
	 * @return
	 */
	public StorePurview getStorePurview(String roleId,String purviewId, Boolean upStatus);
	

	/**
	 * 根据店铺权限ID获取店铺权限
	 * 
	 * @param roleId
	 * @param purviewId
	 * @return
	 */
	public StorePurview getStorePurview(String purviewId);
	
	public void refreshStorePurview(String roleId);
	
	/**
	 * 获取底部导航的数据
	 */
	public ContentCategory getBottomContentByCategoryName(String categoryName);
	
	/**
	 * 获取底部导航的数据
	 */
	public ContentCategory getContentByCategoryName(String categoryName);
	
	
	public ContentCategory getContentByConditions(String categoryName,String parentId);
	
	public ContentCategoryRel getCategoryByCatgId(String contentId);
}
