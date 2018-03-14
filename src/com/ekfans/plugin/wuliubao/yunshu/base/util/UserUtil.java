package com.ekfans.plugin.wuliubao.yunshu.base.util;

public class UserUtil {
   /**
    * 获取密码强度
    * @param password
    * @return
    */
	public static String getpasswordStrength(String password){
		if(password.length()>=6&&password.length()<=8){
			return "1";
		}
		if(password.length()>8&&password.length()<=10){
			return "2";
		}
		return "3";
	}
	
}
