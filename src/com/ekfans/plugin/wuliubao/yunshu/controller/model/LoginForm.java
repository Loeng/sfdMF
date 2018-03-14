package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;
/**
 * 用户登录请求参数接收类
 * @author pp
 * @Date 2017年7月14日09:16:10
 */
public class LoginForm extends JsonToObject{

	//登录类型(0:账号密码登录,1:手机号验证码登录)
	public String type="";
	//密码
	public String password="";
	//验证码
	public String SecurityCode="";
	//用户类型
	public String userType="";
	//手机号
	public String mobile="";
	//极光推送的设备唯一性标识
	public String registrationID="";
	
}
