package com.ekfans.base.product.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品的常用工具类
 * 
 * @ClassName: ProductConst
 * @author 成都易科远见科技有限公司
 * @date 2014-6-19 上午11:28:57
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ProductConst {
	// 模板字段类型 - 文本框
	public static final String TEMP_FIELD_TYPE_TEXT = "text";
	// 模板字段类型 - 文本域
	public static final String TEMP_FIELD_TYPE_TEXT_AREA = "textArea";
	// 模板字段类型 - 多选框
	public static final String TEMP_FIELD_TYPE_CHECK_BOX = "checkBox";
	// 模板字段类型 - 单选框
	public static final String TEMP_FIELD_TYPE_RADIO = "radio";
	// 模板字段类型 - 下拉框
	public static final String TEMP_FIELD_TYPE_SELECT = "select";

	public static Map<String, String> tempFieldTypeMap = new HashMap<String, String>();
	static {
		tempFieldTypeMap.put(TEMP_FIELD_TYPE_TEXT, "字符串文本");
		tempFieldTypeMap.put(TEMP_FIELD_TYPE_TEXT_AREA, "文本域");
		tempFieldTypeMap.put(TEMP_FIELD_TYPE_CHECK_BOX, "多选框");
		tempFieldTypeMap.put(TEMP_FIELD_TYPE_RADIO, "单选框");
		tempFieldTypeMap.put(TEMP_FIELD_TYPE_SELECT, "下拉框");
	}

	// 商品类型 - 优选商城商品
	public static final String PRODUCT_TYPE_XIAOZONG = "1";
	// 商品类型 - 大宗采购商品
	public static final String PRODUCT_TYPE_DAZONG = "2";
	// 商品类型 - 采购商品
	public static final String PRODUCT_TYPE_CAIGOU = "3";
}
