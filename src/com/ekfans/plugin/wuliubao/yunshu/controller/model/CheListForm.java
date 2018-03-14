package com.ekfans.plugin.wuliubao.yunshu.controller.model;


import org.json.JSONArray;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

/**
 * 接收获取车源参数的实体
 * @author Administrator
 *
 */
public class CheListForm extends JsonToObject {

	//分页的当前页
	public String currentpageStr="";
   // 出发地名称
	public String startPlac="";
	// 目的地名称
	public String endPlace="";
	// 出发地省级id
	public String startProvinceId="";
	// 目的地省级id
	public String endProvinceId="";
	// 出发地市级id
	public String startCityId="";
	// 目的地市级id
	public String endCityId="";
	// 出发地区级id
	public String startAreaId="";
	// 目的地区级id
	public String endAreaId="";
	//车源地
	public String starPlace="";
	//车辆类型
	public JSONArray carName;
	//经营范围
	public JSONArray wfpysParentId;
	//车辆载重
	public JSONArray wfpTotal;
	//用户当前维度
	public String latitude="";
	//用户当前经度
	public String longitude="";
	//用户id
	public String userId="";
	
}
