package com.ekfans.plugin.cache.utils;

public class SystemConfigCacheKeyUtil {
	// 系统参数配置缓存key
	public static final String PARAM_CONFIG_KEY = "paramConfig/";

	// 系统信息配置缓存key
	public static final String CONTENT_CONFIG_KEY = "contentConfig";

	// 消息发送配置KEY
	public static final String MESSAGE_CONFIG_KEY = "messageConfig";

	// 地区的配置KEY
	public static final String SYSTEM_AREA_CACHE_KEY = "systemareas";

	// 频道的KEY
	public static final String CHANNEL_KEY = "channels/";

	// MessageConfigDetail在缓存中的Key
	public static final String MESSAGE_DETAIL_KEY = "messageDetail/";

	public static final String SYSTEM_AREA_ALONE = "systemarea/";

	/**
	 * 
	 * @Title: getParamConfigKey
	 * @Description: TODO(获取系统参数配置在缓存中的key) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public static String getParamConfigKey(String key) {
		return PARAM_CONFIG_KEY + key;
	}

	/**
	 * 
	 * @Title: getContentConfigKey
	 * @Description: TODO(获取系统信息配置在缓存中的key) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public static String getContentConfigKey() {
		return CONTENT_CONFIG_KEY;
	}

	/**
	 * 获取消息发送配置KEY
	 * 
	 * @Title: getMessageConfigKey
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getMessageConfigKey() {
		return MESSAGE_CONFIG_KEY;
	}

	/**
	 * 获取系统地址的配置KEY
	 * 
	 * @return
	 */
	public static String getSystemAreaCacheKey() {
		return SYSTEM_AREA_CACHE_KEY;
	}

	public static String getSystemAreaProvinceCacheKey() {
		return SYSTEM_AREA_CACHE_KEY + "/provinces";
	}

	public static String getChannelCacheKey(String type) {
		return CHANNEL_KEY + type;
	}

	/**
	 * 获取消息发送正文配置Key
	 * 
	 * @param id
	 * @return
	 */
	public static String getMessageConfigDetailKey(String id) {
		return MESSAGE_DETAIL_KEY + id;
	}

	public static String getSystemAreaByIdKey(String areaId) {
		return SYSTEM_AREA_ALONE + areaId;
	}
}
