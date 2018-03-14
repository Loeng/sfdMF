package com.ekfans.plugin.webService.monitor.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 车辆人员
 * 
 * @ClassName CarUser
 * @Description TODO
 * @author ekfans
 * @date 2016-2-17
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class CarUser extends VoBasicBean {

	private static final long serialVersionUID = 5126225496661985425L;

	// 所属企业ID
	private String companyId;
	// 姓名
	private String name;
	// 手机号
	private String mobile;
	// 紧急联系人
	private String urgentContactMan;
	// 紧急联系人手机号
	private String urgentContactMobile;
	// 身份证号
	private String cardNo;
	// 人员类别 (0:司机，1：押运员)
	private String type;
	// 驾驶证扫描件
	private String driverFile;
	// 从业资格证扫描件
	private String licenseFile;
	// 从业资格证有效期
	private String startTime;
	// 从业资格证有效期
	private String endTime;
	// 从业资格证号
	private String linceseNum;
	// 人员状态 (0:空闲，1：在运)
	private String status;
	// 同步时间
	private String createTime;
	// 更新时间
	private String updateTime;

	// 驾驶证扫描件文件输入Map
	private Map<String, Object> driverFileIn = new HashMap<String, Object>();
	// 从业资格证扫描件文件输入Map
	private Map<String, Object> licenseFileIn = new HashMap<String, Object>();

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUrgentContactMan() {
		return urgentContactMan;
	}

	public void setUrgentContactMan(String urgentContactMan) {
		this.urgentContactMan = urgentContactMan;
	}

	public String getUrgentContactMobile() {
		return urgentContactMobile;
	}

	public void setUrgentContactMobile(String urgentContactMobile) {
		this.urgentContactMobile = urgentContactMobile;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDriverFile() {
		return driverFile;
	}

	public void setDriverFile(String driverFile) {
		this.driverFile = driverFile;
	}

	public String getLicenseFile() {
		return licenseFile;
	}

	public void setLicenseFile(String licenseFile) {
		this.licenseFile = licenseFile;
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

	public String getLinceseNum() {
		return linceseNum;
	}

	public void setLinceseNum(String linceseNum) {
		this.linceseNum = linceseNum;
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

	public Map<String, Object> getDriverFileIn() {
		return driverFileIn;
	}

	public Map<String, Object> getLicenseFileIn() {
		return licenseFileIn;
	}

	public void setDriverFileIn(Map<String, Object> driverFileIn) {
		this.driverFileIn = driverFileIn;
	}

	public void setLicenseFileIn(Map<String, Object> licenseFileIn) {
		this.licenseFileIn = licenseFileIn;
	}

}
