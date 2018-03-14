package com.ekfans.base.system.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统后台工具類
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-2
 * @version 1.0
 */
public class SystemConst {

	public static String ROLE_SELECT = "select id, name from shop_role";

	// 管理员
	public static String MANAGER = "Manager";

	// 用户
	public static String USER = "User";

	// 店铺
	public static String STORE = "Store";

	// 店铺子账号
	public static String ACCOUNT = "Account";

	// 缓存的待完善资料用户
	public static String USER_NEED_SUP = "UserNeedPer";

	public static String PROVIDER = "Provider";

	// 商品管理模块
	public static String ROLE_PRODUCT = "PRODUCT";

	// 订单管理模块
	public static String ROLE_ORDER = "ORDER";

	// 系统管理模块
	public static String ROLE_SYSTEM = "SYSTEM";

	// 频道管理模块
	// public static String ROLE_CHANNEL = "CHANNEL";

	// 内容管理模块
	public static String ROLE_CONTENT = "CONTENT";

	// 权限管理模块
	// public static String ROLE_RIGHT = "RIGHT";

	// 统计管理模块
	public static String ROLE_STATISTICS = "REPORT";

	// 系统管理员权限
	public static String SYSTEM_ROLE = "SYSTEM";

	// 系统管理员权限
	public static String SYSTEM_OFFERS = "OFFERS";

	public static String ROOT_PATH = "/system";

	public static String STORE_PATH = "/store";

	public static String USER_PATH = "/user";

	// 添加统计信息
	public static Map<String, String> PAGETYPE_MAP = new HashMap<String, String>();

	public static String PRODUCTLIST = "1";

	public static String PRODUCT = "2";

	public static String OTHER = "3";

	public final static byte[] DESKEY = "desc_MariaISG".getBytes();

	public final static String PHONESESSIONKEY = "phonekey";

	public static ConcurrentHashMap<String, Object> MAPUTIL = new ConcurrentHashMap<String, Object>();

	static {
		PAGETYPE_MAP.put(PRODUCTLIST, "商品搜索");
		PAGETYPE_MAP.put(PRODUCT, "商品信息");
		PAGETYPE_MAP.put(OTHER, "其他");
	}

	public SystemConst() {

	}

}
