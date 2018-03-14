package com.ekfans.plugin.cache.service;

import java.util.List;

import org.antlr.v4.runtime.atn.LL1Analyzer;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.base.content.service.IContentCategoryRelService;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.store.service.IStorePurviewService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.utils.StoreCacheKeyUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 店铺缓存相关Service实现
 * 
 * @Title: StoreCacheService.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author liuguoyu 
 * @date 2014-3-21 上午10:15:01 
 * @version V1.0
 */
@SuppressWarnings("unchecked")
public class StoreCacheService implements IStoreCacheService {

	/**
	 * 根据店铺角色ID获取店铺所对应的权限集合
	 */
	@Override
	public List<StorePurview> getStorePurviewsByRoleId(String roleId) {
		// 如果传进来的角色ID为空，则返回null
		if (StringUtil.isEmpty(roleId)) {
			return null;
		}

		// 调用工具类获取店铺权限在缓存中的Key
		String cacheKey = StoreCacheKeyUtil.getStorePurviewByRoleKey(roleId);

		// 调用方法从缓存中获取
		List<StorePurview> purviewList = (List<StorePurview>) Cache.engine.get(cacheKey);
		// 如果从缓存中获取的数据为空，则从数据库中查询，并放入缓存
		if (purviewList == null || purviewList.size() <= 0) {
			// 调用Service方法获取集合
			IStorePurviewService storePurviewService = SpringContextHolder.getBean(IStorePurviewService.class);
			purviewList = storePurviewService.getStorePurviewByRoleId(roleId);
			// 将查询出来的集合放入缓存中
			Cache.engine.add(cacheKey, purviewList);
		}

		return purviewList;
	}

	/**
	 * 根据店铺权限ID获取店铺子权限集合
	 * 
	 * @param roleId
	 * @param purviewId
	 * @return
	 */
	public StorePurview getStorePurview(String roleId, String purviewId, Boolean upStatus) {
		// 如果传进来的角色ID为空或者传进来的权限ID为空，则返回null
		if (StringUtil.isEmpty(roleId) || StringUtil.isEmpty(purviewId)) {
			return null;
		}

		String cacheKey = StoreCacheKeyUtil.getStoreChildPurviewKey(roleId, purviewId);

		// 调用方法从缓存中获取
		StorePurview purview = (StorePurview) Cache.engine.get(cacheKey);
		// 如果从缓存中获取的数据为空，则从数据库中查询，并放入缓存
		if (purview == null) {
			// 调用方法获取角色ID所对应店铺集合权限
			List<StorePurview> totalList = getStorePurviewsByRoleId(roleId);
			if (totalList != null && totalList.size() > 0) {
				for (int i = 0; i < totalList.size(); i++) {
					StorePurview storePurview = totalList.get(i);
					if (storePurview != null && purviewId.equals(storePurview.getId())) {
						purview = storePurview;
						break;
					}
				}
			}
			if (purview == null) {
				IStorePurviewService service = SpringContextHolder.getBean(IStorePurviewService.class);
				purview = service.getPurviewById(roleId, purviewId);
				if (!upStatus) {
					return purview;
				}
			}

			if (purview == null) {
				StorePurview thisPurview = getStorePurview(purviewId);

				if (totalList != null && totalList.size() > 0) {
					for (int i = 0; i < totalList.size(); i++) {
						StorePurview storePurview = totalList.get(i);
						if (storePurview != null) {
							if (thisPurview == null || thisPurview.getTopId().equals(storePurview.getId())) {
								purview = getLastPurview(storePurview);
								break;
							}
						}
					}
				}
			}

			// 将查询出来的集合放入缓存中
			Cache.engine.add(cacheKey, purview);
		}
		return purview;
	}

	/**
	 * 根据店铺权限ID获取店铺子权限集合
	 * 
	 * @param roleId
	 * @param purviewId
	 * @return
	 */
	public StorePurview getStorePurview(String purviewId) {
		// 如果传进来的角色ID为空或者传进来的权限ID为空，则返回null
		if (StringUtil.isEmpty(purviewId)) {
			return null;
		}

		String cacheKey = StoreCacheKeyUtil.getStorePurviewKey(purviewId);

		// 调用方法从缓存中获取
		StorePurview purview = (StorePurview) Cache.engine.get(cacheKey);
		// 如果从缓存中获取的数据为空，则从数据库中查询，并放入缓存
		if (purview == null) {
			IStorePurviewService service = SpringContextHolder.getBean(IStorePurviewService.class);
			purview = service.getPurviewById(purviewId);
			// 将查询出来的集合放入缓存中
			Cache.engine.add(cacheKey, purview);
		}
		return purview;
	}

	/**
	 * 获取底部导航的数据
	 */
	@Override
	public ContentCategory getBottomContentByCategoryName(String categoryName) {
		if (StringUtil.isEmpty(categoryName)) {
			return null;
		}

		// 获取分类
		ContentCategory category = (ContentCategory) Cache.engine.get(categoryName);

		// 为空则从数据库查询
		if (category == null || category.getChildList().size() <= 0) {
			IContentCategoryService categoryService = SpringContextHolder.getBean(IContentCategoryService.class);
			// 获取分类
			category = categoryService.getContentCategoryByName(categoryName,null);
			// 获取子分类
			if (category != null) {
				List<ContentCategory> list = categoryService.getChildCategories(category.getId());
				// 查询子分类下的资讯列表
				if (list != null && list.size() > 0) {
					for (ContentCategory c : list) {
						List<Content> contents = categoryService.getContentsByCategoryId(c.getId());
						c.setContents(contents);
					}
				}
				// 设置子分类
				category.setChildList(list);
				// 将列表放入缓存
				Cache.engine.add(categoryName, category);
			}
		}

		return category;
	}

	public void refreshStorePurview(String roleId) {
		// 如果传进来的角色ID为空或者传进来的权限ID为空，则返回null
		if (StringUtil.isEmpty(roleId)) {
			return;
		}
		// 调用工具类获取店铺权限在缓存中的Key
		String cacheKey = StoreCacheKeyUtil.getStorePurviewByRoleKey(roleId);
		Cache.engine.remove(cacheKey);
		List<StorePurview> purviewList = this.getStorePurviewsByRoleId(roleId);
		if (purviewList != null && purviewList.size() > 0) {
			for (int i = 0; i < purviewList.size(); i++) {
				StorePurview purview = (StorePurview) purviewList.get(i);
				if (purview != null) {
					String cacheKey2 = StoreCacheKeyUtil.getStoreChildPurviewKey(roleId, purview.getId());
					Cache.engine.remove(cacheKey2);
					this.getStorePurview(roleId, purview.getId(), true);
				}
			}
		}
	}

	public static void main(String[] args) {
		StoreCacheService ca = new StoreCacheService();
		ca.getBottomContentByCategoryName("优选帮助中心");
	}

	public static StorePurview getLastPurview(StorePurview purview) {
		if (purview == null) {
			return null;
		}

		if (purview.getChildList() != null && purview.getChildList().size() > 0) {
			StorePurview childPurview = purview.getChildList().get(0);
			return getLastPurview(childPurview);
		} else {
			return purview;
		}
	}

	@Override
	public ContentCategory getContentByCategoryName(String categoryName) {
		// TODO Auto-generated method stub
		IContentCategoryService categoryService = SpringContextHolder.getBean(IContentCategoryService.class);
		ContentCategory category=categoryService.getContentCategoryByName(categoryName,null);
		return category;
	}
	
	@Override
	public ContentCategory getContentByConditions(String categoryName,String parentId) {
		// TODO Auto-generated method stub
		IContentCategoryService categoryService = SpringContextHolder.getBean(IContentCategoryService.class);
		ContentCategory category=categoryService.getContentCategoryByName(categoryName,parentId);
		return category;
	}

	@Override
	public ContentCategoryRel getCategoryByCatgId(String contentId) {
		// TODO Auto-generated method stub
		IContentCategoryRelService categoryService = SpringContextHolder.getBean(IContentCategoryRelService.class);
		List<ContentCategoryRel> rels=categoryService.getContentCategoryRelByContentId(contentId);
		ContentCategoryRel rel=null;
		if(null!=rels&&rels.size()>0){
			rel=rels.get(0);
		}
		return rel;
	}
}
