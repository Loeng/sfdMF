package com.ekfans.plugin.wuliubao.yunshu.controller.model;


import org.json.JSONArray;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

/**
 * 添加货源时的参数实体
 * @author Administrator
 *
 */
public class AddHuoForm extends JsonToObject {
        //货源id(在修改时用到)
	    public String id="";  
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
	    //最次发货时间
		public String endTime="";
		//装货地全称
		public JSONArray startFullPath;
	    //装货地详细地址
		public String startHabitatAddress="";
		//卸货地全称
		public JSONArray endFullPath;
		//卸货地详细地址
		public String endHabitatAddress="";
		//货物名称
		public String supplyName="";
		//所属经营范围id
		public String wfpysParentId="";
		//经营范围名称
		public String wfpysName="";
		//货物类型
		public String categoryName="";
		//货物重量
		public String wfpTotal="";
		//货物体积
		public String cargoVolume="";
		//有效时限
		public String validTime="";
		//联系电话
		public String phone="";
		//联系人
		public String linkMan="";
		//备注
		public String content="";
		//车长
		public String carLength="";
		//车辆尺寸单位 
		public String sizeUnit ="米";
        //车型		
		public String carName="";
		//罐体材质
		public String tankMaterialName="";
		//是否上架
		public String status="";
		//货物体积单位
		public String volumeUnit="";
		//货物重量单位
		public String unit="";
	
}







































