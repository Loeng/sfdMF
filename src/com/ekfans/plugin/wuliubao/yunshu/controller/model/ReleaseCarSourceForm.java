package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import org.json.JSONArray;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;
/**
 * 车源发布请求参数接收类
 * @author pp
 * @Date 2017-7-17 09:08:52
 *
 */
public class ReleaseCarSourceForm extends JsonToObject{
		//车源id
	    public String id;
	    //1:上架   0:下架
		public String status="0";
		//经营范围
	    public JSONArray wfpysName;
		// 经营范围危废品运输界定ID 
	    public JSONArray wfpysParentId;
	    //起始地全路径
	    public JSONArray startFullPath;
	 	//目的地全路径
	    public JSONArray endFullPath;
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
	    //详细地址
	    public String habitatAddress="";
	    //车牌号码
	    public String carNumber ="";
	    //车辆类型
	    public String carName ="";
	    //有效期限
	    public String endTime ="";
	    //罐体材质
	    public String tankMaterialName ="";
	    //车辆载重
	    public String wfpTotal ="";
	    //载重单位 
	    public String unit ="";
	    //车辆尺寸单位 
	    public String sizeUnit ="米";
	    //车长
	    public String carLength ="";
	    //车宽
	    public String carWidth ="";
	    //车高
	    public String carHeight ="";
	    //主车司机姓名
	    public String linkMan ="";
	    //主车司机电话
	    public String phone ="";
}
