package com.ekfans.plugin.wuliubao.yunshu.controller.model;


import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;
/**
 * 手机号修改密码请求参数接收类
 * @author pp
 *
 */
public class ModifyPWForm extends JsonToObject{
 
  //手机号
  public String mobile="";
  //新密码
  public String newpassword="";
  //验证码
  public String SecurityCode="";
}
