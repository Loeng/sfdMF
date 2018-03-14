package com.ekfans.plugin.message.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息工具类
 * 
 * @ClassName: MessageConst
 * @author 成都易科远见科技有限公司
 * @date 2014-5-30 上午10:35:26
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class MessageConst {

	// 跳转标识
	public static final String MESSAGE_JUDGMENT = "$JUDGMENT$";
	public static final String MESSAGE_JUDGMENT_REP = "\\$JUDGMENT\\$";
	// 消息正文 - 会员用户名
	public static final String MESSAGE_USER_NAME = "$USER_NAME$";
	public static final String MESSAGE_USER_NAME_REP = "\\$USER_NAME\\$";

	// 消息正文 - 会员昵称
	public static final String MESSAGE_USER_NICKNAME = "$USER_NICKNAME$";
	public static final String MESSAGE_USER_NICKNAME_REP = "\\$USER_NICKNAME\\$";

	// 消息正文 - 会员手机号
	public static final String MESSAGE_USER_MOBILE = "$USER_MOBILE$";
	public static final String MESSAGE_USER_MOBILE_REP = "\\$USER_MOBILE\\$";

	// 消息正文 - 会员邮箱
	public static final String MESSAGE_USER_EMAIL = "$USER_EMAIL$";
	public static final String MESSAGE_USER_EMAIL_REP = "\\$USER_EMAIL\\$";

	// 消息正文 - 系统时间
	public static final String MESSAGE_DATE_TIME = "$DATE_TIME$";
	public static final String MESSAGE_DATE_TIME_REP = "\\$DATE_TIME\\$";

	// 消息正文 - 验证码
	public static final String MESSAGE_VERIFY_CODE = "$VERIFY_CODE$";
	public static final String MESSAGE_VERIFY_CODE_REP = "\\$VERIFY_CODE\\$";

	// 消息正文 - 商品编号
	public static final String MESSAGE_PRODUCT_NO = "$PRODUCT_NO$";
	public static final String MESSAGE_PRODUCT_NO_REP = "\\$PRODUCT_NO\\$";

	// 消息正文 - 商品名称
	public static final String MESSAGE_PRODUCT_NAME = "$PRODUCT_NAME$";
	public static final String MESSAGE_PRODUCT_NAME_REP = "\\$PRODUCT_NAME\\$";

	// 消息正文 - 商品剩余库存
	public static final String MESSAGE_PRODUCT_STOCK = "$PRODUCT_STOCK$";
	public static final String MESSAGE_PRODUCT_STOCK_REP = "\\$PRODUCT_STOCK\\$";

	// 消息正文 - 商品库存预警数量
	public static final String MESSAGE_PRODUCT_STOCK_WARNING = "$PRODUCT_STOCK_WARNING$";
	public static final String MESSAGE_PRODUCT_STOCK_WARNING_REP = "\\$PRODUCT_STOCK_WARNING\\$";

	// 消息正文 - 订单编号
	public static final String MESSAGE_ORDER_NO = "$PRODUCT_NO$";
	public static final String MESSAGE_ORDER_NO_REP = "\\$PRODUCT_NO\\$";

	// 消息正文 - 订单金额
	public static final String MESSAGE_ORDER_TOTAL_PRICE = "$ORDER_TOTAL_PRICE$";
	public static final String MESSAGE_ORDER_TOTAL_PRICE_REP = "\\$ORDER_TOTAL_PRICE\\$";

	// 消息正文 - 商城访问路径
	public static final String MESSAGE_URL = "$SHOP_DOMAIN$";
	public static final String MESSAGE_URL_REP = "\\$SHOP_DOMAIN\\$";

	// 消息正文 - 会员注册验证邮箱路径
	public static final String MESSAGE_USER_REG_MAIL_ADDR = "$USER_REG_MAIL_ADDR$";
	public static final String MESSAGE_USER_REG_MAIL_ADDR_REP = "\\$USER_REG_MAIL_ADDR\\$";

	// 消息正文 - 会员修改邮箱验证路径
	public static final String MESSAGE_USER_MODIFY_MAIL_ADDR = "$USER_MODIFY_MAIL_ADDR$";
	public static final String MESSAGE_USER_MODIFY_MAIL_ADDR_REP = "\\$USER_MODIFY_MAIL_ADDR\\$";

	// 消息正文 - 会员找回密码邮箱验证路径
	public static final String MESSAGE_USER_FORGET_MAIL_ADDR = "$USER_FORJET_MAIL_ADDR$";
	public static final String MESSAGE_USER_FORGET_MAIL_ADDR_REP = "\\$USER_FORJET_MAIL_ADDR\\$";

	public static Map<String, String> KEYS_MAP = new HashMap<String, String>();
	static {
		KEYS_MAP.put(MESSAGE_JUDGMENT, "跳转标识");
		KEYS_MAP.put(MESSAGE_USER_NAME, "会员用户名");
		KEYS_MAP.put(MESSAGE_USER_NICKNAME, "会员昵称");
		KEYS_MAP.put(MESSAGE_USER_MOBILE, "会员手机号");
		KEYS_MAP.put(MESSAGE_USER_EMAIL, "会员邮箱");
		KEYS_MAP.put(MESSAGE_DATE_TIME, "系统时间");
		KEYS_MAP.put(MESSAGE_VERIFY_CODE, "验证码");
		KEYS_MAP.put(MESSAGE_PRODUCT_NO, "商品编号");
		KEYS_MAP.put(MESSAGE_PRODUCT_NAME, "商品名称");
		KEYS_MAP.put(MESSAGE_PRODUCT_STOCK, "商品剩余库存");
		KEYS_MAP.put(MESSAGE_PRODUCT_STOCK_WARNING, "商品库存预警数量");
		KEYS_MAP.put(MESSAGE_ORDER_NO, "订单编号");
		KEYS_MAP.put(MESSAGE_ORDER_TOTAL_PRICE, "订单金额");
		KEYS_MAP.put(MESSAGE_URL, "商城访问路径");
		KEYS_MAP.put(MESSAGE_USER_REG_MAIL_ADDR, "会员注册验证邮箱路径");
		KEYS_MAP.put(MESSAGE_USER_MODIFY_MAIL_ADDR, "会员修改邮箱验证路径");
		KEYS_MAP.put(MESSAGE_USER_FORGET_MAIL_ADDR, "会员找回密码邮箱路径");
	}
	// -----------------------------------------------------------------------------------------------------

	// 会员注册成功通知模板ID
	public static final String MESSAGE_DETAIL_USER_REGISTE = "user_registe";

	// 会员注册验证模板ID
	public static final String MESSAGE_DETAIL_USER_REGISTE_VERIFY = "user_registe_verify";

	// 会员修改手机/邮箱成功通知模板ID
	public static final String MESSAGE_DETAIL_USER_MODIFY = "user_modify";

	// 会员修改手机/邮箱验证模板ID
	public static final String MESSAGE_DETAIL_USER_MODIFY_VERIFY = "user_modify_verify";

	// 会员找回密码ID
	public static final String MESSAGE_DETAIL_USER_PWD_RESET = "user_pwd_reset";

	// 商品库存预警通知模板ID
	public static final String MESSAGE_DETAIL_PRODUCT_INVENTORY_WARNING = "product_inventory_warning";

	// 订单生成通知模板ID
	public static final String MESSAGE_DETAIL_ORDER_CREATE = "order_create";

	// 订单支付成功通知模板ID
	public static final String MESSAGE_DETAIL_ORDER_PAY = "order_pay";

	// 订单发货通知模板ID
	public static final String MESSAGE_DETAIL_ORDER_SHIP = "order_ship";

	// 订单成功通知模板ID
	public static final String MESSAGE_DETAIL_ORDER_SUCCESS = "order_success";

	// 系统运营推广模板ID
	public static final String MESSAGE_DETAIL_SYSTEM_SPREAD = "system_spread";

}
