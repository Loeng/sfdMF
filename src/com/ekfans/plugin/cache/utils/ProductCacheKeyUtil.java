package com.ekfans.plugin.cache.utils;

/**
 * 
 * 商品缓存工具类
 * 
 * @Title: StoreCacheKeyUtil.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-21 上午11:28:53 
 * @version V1.0
 */
public class ProductCacheKeyUtil {

	public static final String PRODUCT_CATEGORY_TYPE_KEY = "/products/";
	public static final String PRODUCT_KEY = "/product/";
	public static final String PRODUCT_WEB_INDEX_KEY = "/product/web/indexpage";


	/**
	 * 根據商品分類以及商品類型從緩存中獲取商品集合的KEY
	 * 
	 * @Title: getProductCacheKey
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 * @param type
	 * @return String 返回类型
	 * @throws
	 */
	public static String getCategoryProductsCacheKey(String categoryId, String type) {
		return PRODUCT_CATEGORY_TYPE_KEY + categoryId + "/" + type + "/";
	}

	/**
	 * 根據商品分類以及商品類型從緩存中獲取商品集合的KEY
	 * 
	 * @Title: getProductCacheKey
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 * @param type
	 * @return String 返回类型
	 * @throws
	 */
	public static String getProductCacheKey(String productId) {
		return PRODUCT_KEY + productId;
	}

	public static String getProductWebIndexKey() {
		return PRODUCT_WEB_INDEX_KEY;
	}
}