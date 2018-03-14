package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import org.json.JSONArray;

import com.ekfans.plugin.wuliubao.yunshu.controller.util.JsonToObject;

/**
 * 添加车源请求参数接收类
 * @author pp
 *
 */
public class AddVehicleForm extends JsonToObject {
	//车辆id
	public String id="";
	//车牌号码
    public String carNumber ="";
    //经营范围
    public JSONArray wfpysName;
	// 经营范围危废品运输界定ID 
    public JSONArray wfpysParentId;
    //车辆类型
    public String carName ="";
    //有效期限
    public String endTime ="";
    //罐体材质
    public String tankMaterialName ="";
    //车辆载重
    public String wfpTotal ="";
    //车辆尺寸单位 
    public String sizeUnit ="米";
    //载重单位 
    public String unit ="";
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
    //驾驶证二进制数据
    public String dIBD ="";
    //运输证二进制数据
    public String rTQBD ="";
}
