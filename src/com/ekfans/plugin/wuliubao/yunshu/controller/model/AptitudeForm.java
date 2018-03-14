package com.ekfans.plugin.wuliubao.yunshu.controller.model;


import org.json.JSONArray;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

/**
 * 资质认证 请求参数接收类
 * @author pp
 *
 */
public class AptitudeForm extends JsonToObject {
   
	//用户类型(1:司机,2:运输企业,3:产生企业)
	public String type="";
	//经营范围
	public JSONArray scope;
	//营业执照号
	public String bINumber="";
	//营业执照图片二进制数据
	public String bIBD="";
	//行驶证号 
	public String dINumber="";
	//行驶证图片二进制数据
	public String dIBD="";
	//道路运输资质号
	public String rTQNumber="";
	//道路运输资质图片二进制数据
	public String rTQBD="";
	//驾驶证号 
	public String dsINumber="";
	//驾驶证图片二进制数据
	public String dsIBD="";
	//危险品道路运输经营许可证号
	public String dTMNumber="";
	//危险品道路运输经营许可证图片二进制数据
	public String dTMBD="";
	//危险品运输资格证号
	public String dTQNumber="";
	//危险品运输资格证二进制数据
	public String dTQBD="";
	//排污许可证号
	public String sPNumber=""; 
	//排污许可证图片二进制数据
	public String sPBD=""; 
	//排污许可证附件图片二进制数据
	public String sPEBD=""; 
}
