package com.ekfans.base.wfOrder.util;

import java.util.HashMap;
import java.util.Map;

public class ChildOrderConst {
	// 子订单状态 - 待处置企业确认
	public static final String CHILD_ORDER_STATUS_DQR = "0";
	// 子订单状态 - 待物流企业确认
	public static final String CHILD_ORDER_STATUS_DQR2 = "1";
	// 子订单状态 - 待发货
	public static final String CHILD_ORDER_STATUS_DFH = "2";
	// 子订单状态 - 待收货(已发货)
	public static final String CHILD_ORDER_STATUS_YFH = "3";
	// 子订单状态 - 已完成(已收货)
	public static final String CHILD_ORDER_STATUS_FINISH = "4";

	public static Map<String, String> orderStatusMap = new HashMap<String, String>();
	static {
		orderStatusMap.put(CHILD_ORDER_STATUS_DQR, "待处置企业确认");
		orderStatusMap.put(CHILD_ORDER_STATUS_DQR2, "待物流企业确认");
		orderStatusMap.put(CHILD_ORDER_STATUS_DFH, "待发货");
		orderStatusMap.put(CHILD_ORDER_STATUS_YFH, "待收货");
		orderStatusMap.put(CHILD_ORDER_STATUS_FINISH, "已完成");

	}

}
