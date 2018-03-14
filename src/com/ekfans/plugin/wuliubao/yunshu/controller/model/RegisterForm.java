package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;
/**
 * 用户注册请求参数接收类
 * @author Administrator
 *
 */
public class RegisterForm extends JsonToObject{

	//注册类型(1:产生企业,4:运输企业)
	public  String type="";
	//密码
	public  String password="";
	//验证码
	public  String SecurityCode="";
	//手机号
	public  String mobile="";
}
