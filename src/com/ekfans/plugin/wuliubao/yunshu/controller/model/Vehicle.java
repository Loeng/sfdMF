package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import java.math.BigDecimal;

import com.ekfans.base.store.model.CarInfo;

/**
 * 车辆响应数据包装类
 * @author pp
 * @Date 2017年7月14日18:11:15
 */
public class Vehicle {
	//车辆id
	private String id;
	// 企业ID
	private String storeId;
	// 车载
	private String wfpTotal;
	// 创建时间
	private String createTime;
	// 更新时间
	private String updateTime;
	// 有效时间
	private String endTime;
	// 联系人
	private String linkMan;
	// 联系电话
	private String phone;
	// 企业名称
	private String storeName;
	// 单位 默认为吨
	private String unit = "吨";
    // 车辆类型
	private String carName;
	// 罐体材质
	private String tankMaterialName;
	//车长
    private BigDecimal carLength;
    //车宽
    private BigDecimal carWidth;
    //车高
    private BigDecimal carHeight;
    //车辆尺寸单位 
    private String sizeUnit ="米";
	//车牌号
    private String carNumber;
    // 危废品运输界定名称
 	private String[] wfpysName;
    // 危废品运输界定父类ID
 	private String[] wfpysParentId;
    //行驶证图片保存路径
    private String dIPath="";
    //道路运输资质图片保存路径
    private String rTQPath="";
    public Vehicle(CarInfo w) {
		this.id=w.getId();
		this.storeId=w.getStoreId();
		this.wfpTotal=w.getFullWeight().toString();
		this.createTime=w.getCreateTime();
		this.updateTime=w.getUpdateTime();
		this.endTime=w.getEndTime();
		this.linkMan=w.getLinkMan();
		this.phone=w.getPhone();
		this.storeName=w.getStoreName();
		this.unit=w.getUnit();
		this.carName=w.getCarType();
		this.tankMaterialName=w.getTankMaterialName();
		this.carLength=w.getCarLength();
		this.carHeight=w.getCarHeight();
		this.carWidth=w.getCarWidth();
		this.sizeUnit=w.getSizeUnit();
		this.carLength=w.getCarLength();
		this.carNumber=w.getCarNo();
		this.dIPath=w.getXszFile();
		this.rTQPath=w.getYszFile();
		this.wfpysParentId=w.getWfpysParentId().split(",");
		this.wfpysName=w.getWfpysName().split("\\|");
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getWfpTotal() {
		return wfpTotal;
	}
	public void setWfpTotal(String wfpTotal) {
		this.wfpTotal = wfpTotal;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
	public String getTankMaterialName() {
		return tankMaterialName;
	}
	public void setTankMaterialName(String tankMaterialName) {
		this.tankMaterialName = tankMaterialName;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getdIPath() {
		return dIPath;
	}
	public void setdIPath(String dIPath) {
		this.dIPath = dIPath;
	}
	public String getrTQPath() {
		return rTQPath;
	}
	public void setrTQPath(String rTQPath) {
		this.rTQPath = rTQPath;
	}
	public BigDecimal getCarLength() {
		return carLength;
	}
	public void setCarLength(BigDecimal carLength) {
		this.carLength = carLength;
	}
	public BigDecimal getCarWidth() {
		return carWidth;
	}
	public void setCarWidth(BigDecimal carWidth) {
		this.carWidth = carWidth;
	}
	public BigDecimal getCarHeight() {
		return carHeight;
	}
	public void setCarHeight(BigDecimal carHeight) {
		this.carHeight = carHeight;
	}
	public String getSizeUnit() {
		return sizeUnit;
	}
	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}
	public String[] getWfpysName() {
		return wfpysName;
	}
	public void setWfpysName(String[] wfpysName) {
		this.wfpysName = wfpysName;
	}
	public String[] getWfpysParentId() {
		return wfpysParentId;
	}
	public void setWfpysParentId(String[] wfpysParentId) {
		this.wfpysParentId = wfpysParentId;
	}
	
}
