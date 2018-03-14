package com.ekfans.base.loan.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 贷款申请操作日志
 * 
 * @Title: LoanType.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author lgy
 * @date 2016-4-25
 * @version 1.0
 */
@Entity
public class LoanAppLog extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 申请ID
	private String appId = "";
	// 申请银行ID
	private String bankId = "";
	// 申请企业ID
	private String companyId = "";
	// 操作人ID
	private String creatorId = "";
	// 操作人类型 0：企业，1：银行
	private String creatorType = "0";
	// 操作人
	private String creator = "";
	// 操作说明
	private String note = "";
	// 操作时间
	private String createTime = "";

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
