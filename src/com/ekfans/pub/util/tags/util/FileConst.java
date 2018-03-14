package com.ekfans.pub.util.tags.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ekfans.pub.util.StringUtil;

/**
 * 文件上传控件的常用工具类
 * 
 * @ClassName: FileConst
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author liuguoyu
 * @date 2014-5-6 下午05:57:00
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class FileConst {
	// 上传文件类型 - 图片S
	// public static String FILE_TYPE_PICTURE = "pic";
	// 上传文件类型 - 压缩包
	public static String FILE_TYPE_ZIP = "zip";
	// 上传文件类型 - EXCEL
	public static String FILE_TYPE_EXCEL = "excel";
	// 上传文件类型 - WORD
	public static String FILE_TYPE_WORD = "word";
	// 上传文件类型 - PPT
	public static String FILE_TYPE_PPT = "ppt";
	// 上传文件类型 - PDF
	public static String FILE_TYPE_PDF = "pdf";
	// 上传文件类型 - APK
	public static String FILE_TYPE_APK = "apk";
	
	public static Map<String,String> fileTypeNames = new HashMap<String,String>();
	static{
		fileTypeNames.put(FILE_TYPE_ZIP, "压缩包");
		fileTypeNames.put(FILE_TYPE_EXCEL, "EXCEL文档");
		fileTypeNames.put(FILE_TYPE_WORD, "WORD文档");
		fileTypeNames.put(FILE_TYPE_PPT, "PPT文档");
		fileTypeNames.put(FILE_TYPE_PDF, "PDF文档");
		fileTypeNames.put(FILE_TYPE_APK, "APK文件");
		
	}

	// 截取数字
	public static String getNumbers(String content) {
		if (StringUtil.isEmpty(content)) {
			return null;
		}
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}
}
