package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

public class FindHuoForm extends JsonToObject {
	//分页参数
	public String currentpageStr="";
	//状态
	//全部  已上架 已下架 正在审核 审核未通过 已完成
	//0,1,2,3,4,5
	public String status="";
	
}
