package com.ekfans.plugin.cache.service;

import java.util.List;

import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.utils.WebCacheKeyUtil;

@SuppressWarnings("unchecked")
public class productCategoryCachService implements IProductCategoryCacheService {

	@Override
	public List<ProductCategory> getCategories() {

		String key = WebCacheKeyUtil.WEB_PRODUCTCATEGORY;

		// 调用方法从缓存中获取
		List<ProductCategory> configList = (List<ProductCategory>) Cache.engine.get(key);
		// 如果从缓存中获取的数据为空，则从数据库中查询，并放入缓存
		if (configList == null || configList.size() <= 0) {
			// 调用Service方法获取集合
			IProductCategoryService productCategoryService = SpringContextHolder.getBean(IProductCategoryService.class);
			configList = productCategoryService.getMallCatg();
			// 将查询出来的集合放入缓存中
			Cache.engine.add(key, configList);
		}

		return configList;
	}

	@Override
	public ProductCategory getCategoryById(String categoryId) {
		String cacheKey = WebCacheKeyUtil.getProductCategoryCacheKey(categoryId);
		ProductCategory category = (ProductCategory) Cache.engine.get(cacheKey);
		if (category == null) {
			IProductCategoryService categoryService = SpringContextHolder.getBean(IProductCategoryService.class);
			category = categoryService.getCategoryById(categoryId);
			Cache.engine.add(cacheKey, category);
		}
		return category;
	}

	@Override
	public List<String> getChildCategoryIds(String parentId) {
		// TODO Auto-generated method stub
		String cacheKey = WebCacheKeyUtil.getProductCategoryChildIdsCacheKey(parentId);
		List<String> categoryIds = (List<String>) Cache.engine.get(cacheKey);
		if (categoryIds == null || categoryIds.size() <= 0) {
			IProductCategoryService categoryService = SpringContextHolder.getBean(IProductCategoryService.class);
			// category = categoryService.getCategoryById(categoryId);
			categoryIds = categoryService.getChildCategorieIds(parentId);
			Cache.engine.add(cacheKey, categoryIds);
		}
		return categoryIds;
	}

}
