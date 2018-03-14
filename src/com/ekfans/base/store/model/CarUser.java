package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 运输企业运输车辆人员信息实体类
 * 
 * @ClassName: CarInfo
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class CarUser extends BasicBean {
	// ID头：避免ID与第三方冲突;
	public static final String SINGLE_MARK = "MC";

	private static final long serialVersionUID = 1L;
	// 运输企业ID
	private String storeId = "";
	// 人员姓名
	private String name = "";
	// 性别
	private String sex = "";
	// 手机号
	private String mobile = "";
	// 身份证号
	private String cardNo = "";
	// 类型 -- 0：驾驶员 1：押运员
	private String type = "";
	// 驾驶证附件
	private String driverFile = "";
	// 从业资格证附件
	private String licenseFile = "";
	// 行驶证有效期开始时间
	private String startTime = "";
	// 行驶证有效期结束时间
	private String endTime = "";
	// 从业资格证号
	private String licenseNum = "";
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
	// 登陆用户名
	private String loginName = "";
	// 密码
	private String password = "";
	// 头像
	private String headPortrait = "";
	// 是否被第三方使用
	private boolean useData = false;

	// --- 临时字段 ---
	private String storeName;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public boolean isUseData() {
		return useData;
	}

	public void setUseData(boolean useData) {
		this.useData = useData;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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

	public String getLicenseNum() {
		return licenseNum;
	}

	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
}