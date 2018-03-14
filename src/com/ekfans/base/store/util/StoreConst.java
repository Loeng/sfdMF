package com.ekfans.base.store.util;

import java.util.HashMap;
import java.util.Map;

public class StoreConst {
	// 认证状态：未认证
	public static final String RZ_TYPE_NONE = "0";
	// 认证状态：待审核
	public static final String RZ_TYPE_CHECK = "1";
	// 认证状态：审核失败
	public static final String RZ_TYPE_FAIL = "2";
	// 认证状态：审核通过
	public static final String RZ_TYPE_SUC = "3";

	// 认证状态名称MAP
	public static Map<String, String> rzStatusMap = new HashMap<String, String>();

	static {
		rzStatusMap.put(RZ_TYPE_NONE, "未认证");
		rzStatusMap.put(RZ_TYPE_CHECK, "认证审核中");
		rzStatusMap.put(RZ_TYPE_FAIL, "认证审核失败");
		rzStatusMap.put(RZ_TYPE_SUC, "已认证");
	}
}
