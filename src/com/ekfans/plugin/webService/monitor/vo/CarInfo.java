package com.ekfans.plugin.webService.monitor.vo;

import java.util.Map;

/**
 * 车辆信息
 * 
 * @ClassName CarInfo
 * @Description TODO
 * @author ekfans
 * @date 2016-2-17
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class CarInfo extends VoBasicBean {

	private static final long serialVersionUID = -6069712271073457420L;
	// 所属企业ID
	private String companyId;
	// 车牌号
	private String carNo;
	// 车辆类型
	private String carType;
	// 核定载质量
	private String fullWeight;
	// 行驶证有效期开始时间
	private String startTime;
	// 行驶证有效期结束时间
	private String endTime;
	// 道路运输证号
	private String ysNo;
	// 核定载人数
	private String fullNum;
	// 行驶证附件
	private String xszFile;
	// 运输证附件
	private String yszFile;
	// 车辆状态 (0:空闲，1：在运)
	private String status;
	// 同步时间
	private String createTime;
	// 更新时间
	private String updateTime;
	// 监控设备ID
	private String deviceId;

	private Map<String, Object> xszFileIn;

	private Map<String, Object> yszFileIn;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	public String getFullWeight() {
		return fullWeight;
	}

	public void setFullWeight(String fullWeight) {
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

	public String getFullNum() {
		return fullNum;
	}

	public void setFullNum(String fullNum) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Map<String, Object> getXszFileIn() {
		return xszFileIn;
	}

	public void setXszFileIn(Map<String, Object> xszFileIn) {
		this.xszFileIn = xszFileIn;
	}

	public Map<String, Object> getYszFileIn() {
		return yszFileIn;
	}

	public void setYszFileIn(Map<String, Object> yszFileIn) {
		this.yszFileIn = yszFileIn;
	}

}
