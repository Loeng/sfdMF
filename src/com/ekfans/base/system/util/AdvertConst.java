package com.ekfans.base.system.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 广告工具类
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-2
 * @version 1.0
 */
public class AdvertConst {

	// 竖排广告
	public static String SHOW_TYPE_UPRIGHT = "shupai";

	// 横排广告
	public static String SHOW_TYPE_SIDEWAY = "hengpai";

	// 单图广告
	public static String SHOW_TYPE_PIC = "dantu";
	
	// 纯结构广告
	public static String SHOW_TYPE_CHUN = "chun";

	// 图文切换1
	public static String SHOW_TYPE_PICTXT1 = "tuwen1";

	// 图文切换2
	public static String SHOW_TYPE_PICTXT2 = "tuwen2";

	// 数字切换
	public static String SHOW_TYPE_NUM = "shuzi";

	// 大小图切换
	public static String SHOW_TYPE_BIGSMALL = "daxiao";

	// 自定义广告
	public static String SHOW_TYPE_DIY = "zidingyi";

	// 文字广告
	public static String SHOW_TYPE_TXT = "wenzi";

	public static Map<String, String> typeNameMap = new HashMap<String, String>();
	static {
		typeNameMap.put(SHOW_TYPE_UPRIGHT, "竖排广告");
		typeNameMap.put(SHOW_TYPE_SIDEWAY, "横排广告");
		typeNameMap.put(SHOW_TYPE_PIC, "单图广告");
		typeNameMap.put(SHOW_TYPE_PICTXT1, "图文切换1");
		typeNameMap.put(SHOW_TYPE_PICTXT2, "图文切换2");
		typeNameMap.put(SHOW_TYPE_NUM, "数字切换");
		typeNameMap.put(SHOW_TYPE_BIGSMALL, "大小图切换");
		typeNameMap.put(SHOW_TYPE_DIY, "自定义广告");
		typeNameMap.put(SHOW_TYPE_TXT, "文字广告");
		typeNameMap.put(SHOW_TYPE_CHUN, "纯结构广告");
	}

	// 广告在XML中的占位符 - 广告宽度
	public static String STR_PIC_WIDTH = "${width}";

	// 广告在XML中的占位符 - 广告宽度 - 替换文本字符
	public static String STR_PIC_WIDTH_REPLACE = "\\$\\{width\\}";

	// 广告在XML中的占位符 - 广告高度
	public static String STR_PIC_HEIGHT = "${height}";

	// 广告在XML中的占位符 - 广告高度 - 替换文本字符
	public static String STR_PIC_HEIGHT_REPLACE = "\\$\\{height\\}";

	// 广告在XML中的占位符 - 项目外网路径
	public static String STR_CONTEXT_PATH = "${fullPath}";

	// 广告在XML中的占位符 - 项目外网路径- 替换文本字符
	public static String STR_CONTEXT_PATH_REPLACE = "\\$\\{fullPath\\}";

	// 广告在XML中的占位符 - 广告链接路径
	public static String STR_LINK_URL = "${linkUrl}";

	// 广告在XML中的占位符 - 广告链接路径- 替换文本字符
	public static String STR_LINK_URL_REPLACE = "\\$\\{linkUrl\\}";

	// 广告在XML中的占位符 - 广告图片外网路径
	public static String STR_IMG_URL = "${adImg}";

	// 广告在XML中的占位符 - 广告图片外网路径- 替换文本字符
	public static String STR_IMG_URL_REPLACE = "\\$\\{adImg\\}";

	// 广告在XML中的占位符 - 广告标题
	public static String STR_AD_NAME = "${adName}";
	
	// 广告在XML中的占位符 - 广告标题- 替换文本字符
	public static String STR_AD_NAME_REPLACE = "\\$\\{adName\\}";
	
	// 广告在XML中的占位符 - 广告文字
	public static String STR_AD_CONTENT = "${adContent}";
	
	// 广告在XML中的占位符 - 广告文字- 替换文本字符
	public static String STR_AD_CONTENT_REPLACE = "\\$\\{adContent\\}";

	// 广告在XML中的占位符 - 广告文字颜色
	public static String STR_AD_COLOR = "${contentColor}";

	// 广告在XML中的占位符 - 广告文字颜色- 替换文本字符
	public static String STR_AD_COLOR_REPLACE = "\\$\\{contentColor\\}";

	// 广告在XML中的占位符 - 判断是否第一个元素开始标识符
	public static String STR_IF_START = "${ifFirst}";

	// 广告在XML中的占位符 - 判断是否第一个元素开始标识符- 替换文本字符
	public static String STR_IF_START_REPLACE = "\\$\\{ifFirst\\}";

	// 广告在XML中的占位符 - 判断是否第一个元素结束标识符
	public static String STR_IF_END = "${endIf}";

	// 广告在XML中的占位符 - 判断是否第一个元素结束标识符- 替换文本字符
	public static String STR_IF_END_REPLACE = "\\$\\{endIf\\}";

	// 广告在XML中的占位符 - 循环数
	public static String STR_FOR_INDEX = "${forIndex}";

	// 广告在XML中的占位符 - 循环数- 替换文本字符
	public static String STR_FOR_INDEX_REPLACE = "\\$\\{forIndex\\}";

}
