package com.ekfans.base.store.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 运输企业运输车辆信息实体类
 * 
 * @ClassName: CarInfo
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class CarInfo extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 运输企业ID
	private String storeId = "";
	// 车牌号
	private String carNo = "";
	// 车辆类型
	private String carType = "";
	//经营范围
    private String wfpysName ="";
	//经营范围危废品运输界定父类ID
    private String wfpysParentId="";
    //罐体材质
    private String tankMaterialName ="";
    //主车司机姓名
    private String linkMan ="";
    //主车司机电话
    private String phone ="";
    //载重单位 
    private String unit ="吨";
    //车辆尺寸单位 
    private String sizeUnit ="米";
    //车长
    private BigDecimal carLength;
    //车宽
    private BigDecimal carWidth;
    //车高
    private BigDecimal carHeight;
	// 核定载质量
	private Double fullWeight = 0.00;
	// 行驶证有效期开始时间
	private String startTime = "";
	// 行驶证有效期结束时间
	private String endTime = "";
	// 道路运输证号
	private String ysNo = "";
	// 核定载人数
	private int fullNum = 0;
	// 行驶证附件
	private String xszFile = "";
	// 道路运输证附件
	private String yszFile = "";
	// 审核状态 0--未审核 1--已审核 2--审核失败
	private String checkStatus = "0";
	// 审核时间
	private String checkTime = "";
	// 审核人
	private String checkMan = "";
	// 审核备注
	private String checkNote = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";
	// 监控设备ID
	private String deviceId = "";
	// 是否被第三方使用
	private boolean useData = false;
	// 监控信息
	private String monitorUrl;
	// 车辆状态 0空闲 1使用中
	private String transportationStatus;

	// --- 临时字段 ---
	private String storeName;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getTransportationStatus() {
		return transportationStatus;
	}

	public void setTransportationStatus(String transportationStatus) {
		this.transportationStatus = transportationStatus;
	}

	public String getMonitorUrl() {
		return monitorUrl;
	}

	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public Double getFullWeight() {
		return fullWeight;
	}

	public void setFullWeight(Double fullWeight) {
		this.fullWeight = fullWeight;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getYsNo() {
		return ysNo;
	}

	public void setYsNo(String ysNo) {
		this.ysNo = ysNo;
	}

	public int getFullNum() {
		return fullNum;
	}

	public void setFullNum(int fullNum) {
		this.fullNum = fullNum;
	}

	public String getXszFile() {
		return xszFile;
	}

	public void setXszFile(String xszFile) {
		this.xszFile = xszFile;
	}

	public String getYszFile() {
		return yszFile;
	}

	public void setYszFile(String yszFile) {
		this.yszFile = yszFile;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
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

	public boolean isUseData() {
		return useData;
	}

	public void setUseData(boolean useData) {
		this.useData = useData;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getWfpysName() {
		return wfpysName;
	}

	public void setWfpysName(String wfpysName) {
		this.wfpysName = wfpysName;
	}

	public String getWfpysParentId() {
		return wfpysParentId;
	}

	public void setWfpysParentId(String wfpysParentId) {
		this.wfpysParentId = wfpysParentId;
	}

	public String getTankMaterialName() {
		return tankMaterialName;
	}

	public void setTankMaterialName(String tankMaterialName) {
		this.tankMaterialName = tankMaterialName;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
	
}