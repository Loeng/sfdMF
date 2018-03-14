package com.ekfans.pub.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证
 * 
 * @Title: MatcherUtil.java
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: ekfans
 * @date 2016年3月14日
 * @version 1.0
 */
public class MatcherUtil {
	
	/**
	 * 是否是手机
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles == null) {
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 是否是邮箱
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null) {
			return false;
		}
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
}