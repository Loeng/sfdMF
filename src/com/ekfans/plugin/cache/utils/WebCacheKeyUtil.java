package com.ekfans.plugin.cache.utils;

public class WebCacheKeyUtil {

	public static final String WEB_KEY = "/web/";

	public static final String WEB_PRODUCTCATEGORY = "/web/index/productCategory";

	public static final String PRODUCT_CATEGORY_KEY = "/productCategory/";

	public static final String PRODUCT_CATEGORY_CHILD_KEY = "/productCategory/childs/";

	public static String getStorePurviewKey(String key) {
		return WEB_KEY + key;
	}

	public static String getWebProductcategory() {
		return WEB_PRODUCTCATEGORY;
	}

	public static String getProductCategoryCacheKey(String categoryId) {
		return PRODUCT_CATEGORY_KEY + categoryId;
	}

	public static String getProductCategoryChildIdsCacheKey(String categoryId) {
		return PRODUCT_CATEGORY_CHILD_KEY + categoryId;
	}

}
