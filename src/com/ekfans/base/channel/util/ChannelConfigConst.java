package com.ekfans.base.channel.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 频道配置常量类
 * 
 * @ClassName: ChannelConfigConst
 * @author 成都易科远见科技有限公司
 * @date 2014-6-22 下午01:14:13
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ChannelConfigConst {
	// 频道配置类型 - 商品 （配置明细中保存的还是商品分类ID）
	public static final String CHANNEL_CONFIG_TYPE_PRODUCT = "p";

	// 频道配置类型 - 商品分类 (配置明细中保存的类型还是商品分类ID)
	public static final String CHANNEL_CONFIG_TYPE_PRODUCT_CATEGORY = "pc";

	// 频道配置类型 - 商品分类商品列表 (配置明细中保存的类型还是商品分类ID)
    public static final String CHANNEL_CONFIG_TYPE_PRODUCT_CATEGORY_PRODUCT_LIST = "pcp";
	
	// 频道配置类型 - 资讯 (配置明细中保存的类型还是资讯分类ID)
	public static final String CHANNEL_CONFIG_TYPE_CONTENT = "c";

	// 频道配置类型 - 资讯 分类(配置明细中保存的类型还是资讯分类ID)
	public static final String CHANNEL_CONFIG_TYPE_CONTENT_CATEGORY = "cc";

	// 频道配置类型 - 广告 (配置明细中保存的是广告ID)
	public static final String CHANNEL_CONFIG_TYPE_AD = "ad";

	// 频道配置类型 -热门供应  (配置明细中保存的是供求ID)
	public static final String CHANNEL_CONFIG_TYPE_GY = "gy";
		
	// 频道配置类型 -求购  (配置明细中保存的是供求ID)
	public static final String CHANNEL_CONFIG_TYPE_QG = "qg";
	
	// 频道配置类型 -车源  (配置明细中保存的是供求ID)
	public static final String CHANNEL_CONFIG_TYPE_CY = "cy";
	
	// 频道配置类型 -货源  (配置明细中保存的是供求ID)
	public static final String CHANNEL_CONFIG_TYPE_HY = "hy";
	
	
	public static Map<String, String> configNamesmap = new HashMap<String, String>();
	static {
		configNamesmap.put(CHANNEL_CONFIG_TYPE_PRODUCT, "商品");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_PRODUCT_CATEGORY, "商品分类");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_PRODUCT_CATEGORY_PRODUCT_LIST, "商品分类商品列表");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_CONTENT, "资讯");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_CONTENT_CATEGORY, "资讯分类");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_AD, "广告");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_GY, "热门供应");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_QG, "热门求购");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_CY, "危废车源");
		configNamesmap.put(CHANNEL_CONFIG_TYPE_HY, "危废货源");
	}
}
