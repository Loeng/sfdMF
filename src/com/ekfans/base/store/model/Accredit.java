package com.ekfans.base.store.model;

import java.util.ArrayList;
import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 企业资质认证资料实体类
 * 
 * @ClassName: CarInfo
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Accredit extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 企业ID
	private String storeId = "";
	// 认证类型 0:危险废物经营许可证 1:排放污染物许可证 2:道路运输经营许可证
	private String rzType = "";
	// 文件类型 ------暂时预留
	private String licenseType = "";
	// 业户名称
	private String storeName = "";
	// 业户地区
	private String areaId = "";
	// 业户详细地址
	private String address = "";
	// 许可证号
	private String licenseNo = "";
	// 证件扫描件
	private String licenseFile = "";
	// 证件副本扫描件
	private String licenseTwoFile = "";
	// 发证机关
	private String linceseAuthor = "";
	// 有效期开始时间
	private String startTime = "";
	// 有效期结束时间
	private String endTime = "";
	// 核准经营规模
	private String scale = "";
	// 资质明细MODEL
	private String detailModel = "";
	// 审核状态
	private boolean checkStatus = false;
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

	private List<Object> childList = new ArrayList<Object>();
	// 明细id集合，以分号隔开
	private String childIds = "";
	private String areaFullName = "";

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getRzType() {
		return rzType;
	}

	public void setRzType(String rzType) {
		this.rzType = rzType;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseFile() {
		return licenseFile;
	}

	public void setLicenseFile(String licenseFile) {
		this.licenseFile = licenseFile;
	}

	public String getLicenseTwoFile() {
		return licenseTwoFile;
	}

	public void setLicenseTwoFile(String licenseTwoFile) {
		this.licenseTwoFile = licenseTwoFile;
	}

	public String getLinceseAuthor() {
		return linceseAuthor;
	}

	public void setLinceseAuthor(String linceseAuthor) {
		this.linceseAuthor = linceseAuthor;
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

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public boolean isCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(boolean checkStatus) {
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

	public List<Object> getChildList() {
		return childList;
	}

	public void setChildList(List<Object> childList) {
		this.childList = childList;
	}

	public String getDetailModel() {
		return detailModel;
	}

	public void setDetailModel(String detailModel) {
		this.detailModel = detailModel;
	}

	public String getChildIds() {
		return childIds;
	}

	public void setChildIds(String childIds) {
		this.childIds = childIds;
	}

	public String getAreaFullName() {
		return areaFullName;
	}

	public void setAreaFullName(String areaFullName) {
		this.areaFullName = areaFullName;
	}
}