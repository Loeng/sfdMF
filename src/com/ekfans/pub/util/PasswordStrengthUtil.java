package com.ekfans.pub.util;

import java.util.regex.Pattern;

public class PasswordStrengthUtil {

	private PasswordStrengthUtil(){}
	
	private static class PasswordStrengthUtilHolder{
		private static final PasswordStrengthUtil INSTANCE = new PasswordStrengthUtil();
	}
	
	public static final PasswordStrengthUtil getInstance() {  
		return PasswordStrengthUtilHolder.INSTANCE;  
	}
	
	/**
	 * 查找字符串是否有匹配的字符
	 * 
	 * @return 有:true，没有:false
	 */
	private boolean regexJ(String regex, String password){
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(password).find();
	}
	
	/**
	 * 验证密码强度
	 * 
	 * @return 1:弱，2:中，3:高，4:高
	 */
	public int strength(String password){
		int i = 0;
		
		String regex1 = "\\d+?";
		if(regexJ(regex1, password)){
			// 清楚密码中的数字
			password = password.replaceAll(regex1, "");
			// 加权
			++i;
		}
		String regex2 = "[a-z]+?";
		if(regexJ(regex2, password)){
			// 清楚密码中的小写字母
			password = password.replaceAll(regex2, "");
			// 加权
			++i;
		}
		String regex3 = "[A-Z]+?";
		if(regexJ(regex3, password)){
			// 清楚密码中的大写字母
			password = password.replaceAll(regex3, "");
			// 加权
			++i;
		}
		if(password.length() > 0){
			++i;
		}
		
		return i;
	}
}
