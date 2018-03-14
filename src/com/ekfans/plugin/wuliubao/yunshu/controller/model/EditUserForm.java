package com.ekfans.plugin.wuliubao.yunshu.controller.model;


import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

/**
 * 用来处理编辑用户信息的数据
 * @author Administrator
 *
 */
public class EditUserForm extends JsonToObject {

	//安全手机号
	public String safeMobile="";
	//昵称
	public String nickName="";
}
