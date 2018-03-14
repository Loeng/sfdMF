package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

/**
 * 更新报价信息参数接收类
 * @author pp
 *
 */
public class UpdateBargainForm extends JsonToObject{
	//议价表id
	public String bargainId="";
	//议价类型(1:我的议价,2:对我议价)
	public String bargainType="";
	//备注
	public String userRemarks="";
}
