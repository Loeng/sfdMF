package com.ekfans.plugin.cache.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.product.model.Product;

public interface IProductCacheService {
	/**
	 * 根据商品分类ID和商品类型获取商品集合
	 * 
	 * @Title: getProductsByCategory
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 * @param productType
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<String> getProductsByCategory(String categoryId, String productType);

	/**
	 * 根据商品主键获取商品对象
	 * 
	 * @param productId
	 * @return
	 */
	public Product getProductById(String productId);

	/**
	 * 刷新缓存中的商品对象
	 * 
	 * @param productId
	 */
	public void refreshProduct(String productId);

	/**
	 * 刷新缓存中的商品分类所对应的商品id集合
	 * 
	 * @param categoryId
	 * @param productType
	 */
	public void refrefshProductsByCategory(String categoryId, String productType);

	/**
	 * 得到首页销售挂牌产品最新数据缓存
	 * @return 首页产品展示数据
	 */
	public List<List<Product>> getWebIndexProductXsgp();


	/**
	 * 刷新首页销售挂牌产品缓存
	 */
	public void refrefshWebIndexProductXsgp();

}
