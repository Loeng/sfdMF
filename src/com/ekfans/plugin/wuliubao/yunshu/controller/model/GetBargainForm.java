package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

/**
 * 获取议价丶询价信息请求参数接收类
 * @author pp
 * @date 2017年8月25日10:22:35
 *
 */
public class GetBargainForm extends JsonToObject{
    //用户类型(1:运输端,2:产生端)
    public String userType="";
    //议价类型 (1:我的议价,2:对我议价)
	public String bargainType="";
	//联系状态(0:未联系 1:已联系 其他业务不想要联系状态可填非0非1任何值)
	public String sellerContactState="";
	//分页的当前页
	public String currentPage="";
	//商品id
	public String wftransportIds="";
	//用户当前维度
	public String latitude="";
	//用户当前经度
	public String longitude="";
}
