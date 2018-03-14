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
public class SupplyBuyCacheKeyUtil {

	public static final String SUPPLYBUY_CATEGORY_TYPE_KEY = "/SupplyBuys/";
	public static final String SUPPLYBUY_KEY = "/SupplyBuy/";
	public static final String SUPPLYBUY_WEB_INDEX_KEY_0 = "/SupplyBuy0/web/indexpage";
	public static final String SUPPLYBUY_WEB_INDEX_KEY_1 = "/SupplyBuy1/web/indexpage";


	/**
	 * 根據商品分類以及商品類型從緩存中獲取商品集合的KEY
	 * 
	 * @Title: getSupplyBuyCacheKey
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 * @param type
	 * @return String 返回类型
	 * @throws
	 */
	public static String getCategorySupplyBuysCacheKey(String categoryId, String type) {
		return SUPPLYBUY_CATEGORY_TYPE_KEY + categoryId + "/" + type + "/";
	}

	/**
	 * 根據供求分類從緩存中獲取供求的KEY
	 * 
	 * @Title: getSupplyBuyCacheKey
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 * @param type
	 * @return String 返回类型
	 * @throws
	 */
	public static String getSupplyBuyCacheKey(String SupplyBuyId) {
		return SUPPLYBUY_KEY + SupplyBuyId;
	}

	public static String getSupplyBuyWebIndexKey(String type) {
		if (type.equals("0")){
			return SUPPLYBUY_WEB_INDEX_KEY_0;
		}else{
			return SUPPLYBUY_WEB_INDEX_KEY_1;
		}
	}
}