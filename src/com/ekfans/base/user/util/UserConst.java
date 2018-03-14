package com.ekfans.base.user.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 会员常量工具类
 * 
 * @ClassName: UserConst
 * @Description: 用来存放会员的一些常量
 * @author liuguoyu
 * @date 2014-3-31 下午05:32:42
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class UserConst {

	// 会员积分类型 - 增加
	public final static String USER_INTEGRAL_TYPE_ADD = "1";

	// 会员积分类型 - 减少
	public final static String USER_INTEGRAL_TYPE_REDUCE = "0";

	// 会员积分来源 - 添加 （管理员手动添加）
	public final static String USER_INTEGRAL_SOURCE_ADD = "1";

	// 会员积分来源 - 订单赠送（购买商品赠送积分）
	public final static String USER_INTEGRAL_SOURCE_ORDER = "2";

	// 会员积分来源 - 积分返还（特殊情况下返还积分）
	public final static String USER_INTEGRAL_SOURCE_RETURN = "3";

	// 会员积分来源 - 减少（管理员手动减少）
	public final static String USER_INTEGRAL_SOURCE_REDUCE = "4";

	// 会员积分来源 - 兑换（使用积分兑换商品/服务等减少积分）
	public final static String USER_INTEGRAL_SOURCE_EXCHANGE = "5";

	// 会员积分来源 - 冲正(积分回退，特殊情况下减少积分)
	public final static String USER_INTEGRAL_SOURCE_CORRECTION = "6";

	// 会员积分来源 - 管理员修改等级
	public final static String USER_INTEGRAL_SOURCE_LEVEL = "7";

	// 会员类型 - 个人会员
	public static final String USER_TYPE_CUSTOMER = "0";
	// 会员类型 - 产生企业
	public static final String USER_TYPE_STORE = "1";
	// 会员类型 - 采购商
	public static final String USER_TYPE_BUYER = "2";
	// 会员类型 - 核心企业
	public static final String USER_TYPE_CODE = "3";
	// 会员类型 - 运输企业
	public static final String USER_TYPE_YUNSHU = "4";
	// 会员类型 - 供应商
	public static final String USER_TYPE_GONGYING = "5";
	// 会员类型Key对应的中文名
	public static Map<String, String> userTypeNamesMap = new LinkedHashMap<String, String>();
	static {
		// userTypeNamesMap.put(USER_TYPE_CUSTOMER, "个人会员");
		userTypeNamesMap.put(USER_TYPE_BUYER, "采购商");
		userTypeNamesMap.put(USER_TYPE_GONGYING, "供应商");
		userTypeNamesMap.put(USER_TYPE_STORE, "产生企业");
		userTypeNamesMap.put(USER_TYPE_CODE, "处置企业");
		userTypeNamesMap.put(USER_TYPE_YUNSHU, "运输企业");
	}

}
