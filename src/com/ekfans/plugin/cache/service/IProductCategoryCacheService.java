package com.ekfans.plugin.cache.service;

import java.util.List;

import com.ekfans.base.product.model.ProductCategory;

public interface IProductCategoryCacheService {
	/**
	 * 
	 * @Title: getCategories
	 * @Description: TODO(从缓存获取商品分类列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<ProductCategory> 返回类型
	 * @throws
	 */
	public List<ProductCategory> getCategories();

	/**
	 * 根据商品分类ID获取商品分类对象
	 * 
	 * @param categoryId
	 * @return
	 */
	public ProductCategory getCategoryById(String categoryId);

	/**
	 * 根据商品分类ID获取子商品分类ID集合
	 * 
	 * @param parentId
	 * @return
	 */
	public List<String> getChildCategoryIds(String parentId);
}
