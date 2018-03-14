package com.ekfans.plugin.wuliubao.yunshu.controller.model;
import org.json.JSONArray;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;
/**
 * 物流宝议价请求参数接收类
 * @author pp
 * @Date 2017年7月18日14:44:07
 */
public class BargainingForm extends JsonToObject {
    //议价的车货源ID
	public String sourceId = "";
	// 议价人ID
	public String buyerId = "";
	// 被议价人ID
	public String sellerId = "";
	//货物名称
	public String productName="";
	// 价格
	public String price  ="";
	//价格单位
	public String priceUnit="";
	// 联系人
	public String contactMan = "";
	// 联系电话
	public String contactPhone = "";
	//起始地
	public JSONArray startFullPaths;
	//目的地
	public JSONArray endFullPaths;
}
