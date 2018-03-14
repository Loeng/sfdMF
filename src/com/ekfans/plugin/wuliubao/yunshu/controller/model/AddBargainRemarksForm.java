package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

/**
 * 添加 更新议价备注请求参数接收类
 * @author pp
 * @Date 2017年7月23日18:33:22
 *
 */
public class AddBargainRemarksForm extends JsonToObject {
    //议价类型(1:我的议价,2:对我议价)
	public String bargainType="";
	//议价id
	public String bargainId="";
	//用户备注内容
	public String userRemarks="";
}
