package com.ekfans.base.system.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 电子邮箱设置的工具类
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-29
 * @version 1.0
 */
public class MailConst {
	// 会员注册的邮件设置
	public static String MAIL_CONFIG_USER_CREATE = "user_create";

	// 会员修改密码的邮件设置
	public static String MAIL_CONFIG_USER_MODIFY = "user_modify";

	// 会员修改注册邮箱的邮件设置
	public static String MAIL_CONFIG_USER_UPDATE_EMAIL = "user_email";

	// 下单通知的邮件设置
	public static String MAIL_CONFIG_ORDER_ADD = "order_add";

	// 发货
	public static String MAIL_CONFIG_ORDER_FH = "order_fh";

	// 订单成功的
	public static String MAIL_CONFIG_ORDER_SUCCESS = "order_success";

	// 订单失败(关闭)
	public static String MAIL_CONFIG_ORDER_CLOSE = "order_close";

	public static Map<String, String> MAIL_CONFIGS = new LinkedHashMap<String, String>();
	static {
		MAIL_CONFIGS.put(MAIL_CONFIG_USER_CREATE, "user_create");
		MAIL_CONFIGS.put(MAIL_CONFIG_USER_MODIFY, "user_modify");
		MAIL_CONFIGS.put(MAIL_CONFIG_USER_UPDATE_EMAIL, "user_email");
		MAIL_CONFIGS.put(MAIL_CONFIG_ORDER_ADD, "order_add");
		MAIL_CONFIGS.put(MAIL_CONFIG_ORDER_FH, "order_fh");
		MAIL_CONFIGS.put(MAIL_CONFIG_ORDER_SUCCESS, "order_success");
		MAIL_CONFIGS.put(MAIL_CONFIG_ORDER_CLOSE, "order_close");
	}

	public static Map<String, String> MAIL_CONFIG_NAMES = new LinkedHashMap<String, String>();
	static {
		MAIL_CONFIG_NAMES.put(MAIL_CONFIG_USER_CREATE, "会员注册通知");
		MAIL_CONFIG_NAMES.put(MAIL_CONFIG_USER_MODIFY, "会员修改密码通知");
		MAIL_CONFIG_NAMES.put(MAIL_CONFIG_USER_UPDATE_EMAIL, "会员修改邮箱通知");
		MAIL_CONFIG_NAMES.put(MAIL_CONFIG_ORDER_ADD, "下单通知");
		MAIL_CONFIG_NAMES.put(MAIL_CONFIG_ORDER_FH, "订单发货通知");
		MAIL_CONFIG_NAMES.put(MAIL_CONFIG_ORDER_SUCCESS, "订单成功通知");
		MAIL_CONFIG_NAMES.put(MAIL_CONFIG_ORDER_CLOSE, "订单失败(关闭)通知");
	}
}
