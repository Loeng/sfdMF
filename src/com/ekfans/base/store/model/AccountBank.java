package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 虚拟账户绑定表
 * 
 * @ClassName: Store
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
public class AccountBank extends BasicBean {
	// 用户ID
	private String userId = "";
	// 客户名称
	private String companyName = "";
	// 客户账户名
	private String accountName = "";
	// 客户结算账户
	private String accountNo = "";
	// 客户结算账户行号
	private String bankNo = "";
	// 客户结算账户行名
	private String bankName = "";
	// 客户手机号码
	private String mobile = "";
	// 客户电话号码
	private String telPhone = "";
	// 客户地址
	private String address = "";
	// 备注
	private String note = "";
	// 用户类型：false个人用户，true：企业用户
	private Boolean customerType = false;
	// 是否开通电票 false:不开通 true:开通
	private Boolean enableEcds = false;
	// 状态：0:未绑定 1:已绑定 2:待审核 3:审核未通过 4:失效
	private String status = "";
	// 创建时间
	private String createTime = "";
	//客户身份证号码
   private String indentity;
   
	public String getIndentity() {
	return indentity;
}

public void setIndentity(String indentity) {
	this.indentity = indentity;
}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getEnableEcds() {
		return enableEcds;
	}

	public void setEnableEcds(Boolean enableEcds) {
		this.enableEcds = enableEcds;
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

	public Boolean getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Boolean customerType) {
		this.customerType = customerType;
	}

}