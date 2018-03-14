package com.ekfans.base.order.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 订单常量工具类
 * 
 * @ClassName: OrderConst
 * @Description: 用来存放订单的一些常量
 * @author 成都易科远见科技有限公司
 * @date 2014-5-8
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class OrderConst {
//	订单状态(0：取消，1：关闭，2：待付款，3：已付款，4：确认收货(确认收货后,买家进行评价)；5：完成(卖家对买家评价完毕,算完成))
	// 下单(待确认)
	public final static String ORDER_STATUS_NEW_ORDER = "0";

	// 已确认(待付款)
	public final static String ORDER_STATUS_WAIT_PAY = "1";

	// 已付款待确认收款
	public final static String ORDER_STATUS_WAIT_PAY_SURE = "2";

	// 已付款(待发货)
	public final static String ORDER_STATUS_WAIT_SEND = "3";

	// 已发货(待收货)
	public final static String ORDER_STATUS_WAIT_REC = "4";
	
	// 已收货(待评价)
	public final static String ORDER_STATUS_WAIT_APP = "5";

	// 订单完成(订单已完成)
	public final static String ORDER_STATUS_FINISH = "6";

	// 订单关闭
	public final static String ORDER_STATUS_CLOSE = "7";
	
	// 订单取消
	public final static String ORDER_STATUS_CANCEL = "8";

	// 会员类型Key对应的中文名 （直付订单）order.type = "1"
	public static Map<String, String> orderStatusNameMap = new LinkedHashMap<String, String>();
	static {
		orderStatusNameMap.put(ORDER_STATUS_NEW_ORDER, "待确认");
		orderStatusNameMap.put(ORDER_STATUS_WAIT_PAY, "已确认待付款");
		orderStatusNameMap.put(ORDER_STATUS_WAIT_PAY_SURE, "已付款待确认收款");
		orderStatusNameMap.put(ORDER_STATUS_WAIT_SEND, "待发货");
		orderStatusNameMap.put(ORDER_STATUS_WAIT_REC, "待确认收货");
		orderStatusNameMap.put(ORDER_STATUS_WAIT_APP, "完成");
		orderStatusNameMap.put(ORDER_STATUS_FINISH, "完成");
		orderStatusNameMap.put(ORDER_STATUS_CLOSE, "关闭");
		orderStatusNameMap.put(ORDER_STATUS_CANCEL, "取消订单");
	}

	// 下单
	public final static String ORDER_TREAT_NEW_ORDER = "0";

	//确定订单
	public final static String ORDER_TREAT_ORDER_SURE = "21";

	// 修改金额
	public final static String ORDER_TREAT_MODIFY = "1";

	// 修改运费
	public final static String ORDER_TREAT_FREIGHT = "12";

	// 付款
	public final static String ORDER_TREAT_PAY = "2";

	// 付款
	public final static String ORDER_TREAT_PAY_SURE = "22";

	// 发货
	public final static String ORDER_TREAT_SEND = "3";

	// 确认收货
	public final static String ORDER_TREAT_RECEIPT = "4";

	// 买家评价
	public final static String ORDER_TREAT_USER_APP = "5";

	// 卖家评价
	public final static String ORDER_TREAT_STORE_APP = "6";

	// 申请退款
	public final static String ORDER_TREAT_APPLY = "7";

	// 同意退款
	public final static String ORDER_TREAT_ACCEPT = "8";

	// 取消订单
	public final static String ORDER_TREAT_CANCEL = "9";

	// 关闭订单
	public final static String ORDER_TREAT_CLOSE_ORDER = "10";

	// 订单操作完成
	public final static String ORDER_TREAT_OK = "11";

	// 订单类型 - 普通订单
	public final static int ORDER_TYPE_COMMON = 0;
	// 订单类型 - 直付订单
	public final static int ORDER_TYPE_ZF = 1;

	// 订单支付方式：余额支付
	public final static String ORDER_PAY_TYPE_YUE = "0";
	// 订单支付方式：线下转账
	public final static String ORDER_PAY_TYPE_UNLINE = "1";
	// 订单支付方式：在线支付
	public final static String ORDER_PAY_TYPE_ONLINE = "2";
	// 会员类型Key对应的中文名
	public static Map<String, String> orderPayTypeNamsMap = new LinkedHashMap<String, String>();
	static {
		orderPayTypeNamsMap.put(ORDER_PAY_TYPE_YUE, "余额支付");
		orderPayTypeNamsMap.put(ORDER_PAY_TYPE_UNLINE, "线下转账");
		orderPayTypeNamsMap.put(ORDER_PAY_TYPE_ONLINE, "在线支付");
	}

}
