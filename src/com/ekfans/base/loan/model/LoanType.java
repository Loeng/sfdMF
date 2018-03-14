package com.ekfans.base.loan.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 贷款类型
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
public class LoanType extends BasicBean {

	private static final long serialVersionUID = 1L;

	// 银行ID
	private String bankId = "";

	// 创建者ID
	private String creator = "";

	// 创建者真实姓名
	private String creatorRealName = "";

	// 状态: true启用，false不启用
	private Boolean status = false;

	// 贷款类型
	private String loanName = "";

	// 操作时间
	private String createTime = "";

	// 更新时间
	private String updateTime = "";

	// 详细说明
	private String note = "";

	// -----临时数据
	private List<LoanTypeDetail> details = new LinkedList<LoanTypeDetail>();
	private List<LoanTypeRole> roles = new LinkedList<LoanTypeRole>();

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorRealName() {
		return creatorRealName;
	}

	public void setCreatorRealName(String creatorRealName) {
		this.creatorRealName = creatorRealName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<LoanTypeDetail> getDetails() {
		return details;
	}

	public void setDetails(List<LoanTypeDetail> details) {
		this.details = details;
	}

	public List<LoanTypeRole> getRoles() {
		return roles;
	}

	public void setRoles(List<LoanTypeRole> roles) {
		this.roles = roles;
	}

}
