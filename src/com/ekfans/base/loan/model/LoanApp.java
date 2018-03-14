package com.ekfans.base.loan.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 贷款申请
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
public class LoanApp extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 贷款类型表ID
	private String loanTypeId = "";
	// 申请贷款人ID
	private String companyId = "";
	// 银行ID
	private String bankId = "";
	// 申请的状态:00：待处理，01：融资立项,02:实地考察，03:信审会审批，04:落实担保条件，05：贷款发放，06：贷后资料收集，07：还款，08：完成
	private String appStatus = "00";
	// 企业是否符合申请筛选条件 false:不满足，true:满足
	private Boolean companyStatus = false;
	// 申请时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";
	// 详细说明
	private String note = "";
	// 贷款金额
	private BigDecimal price = new BigDecimal(0.00);

	// -----临时数据
	private List<LoanAppDetail> details = null;
	private List<LoanAppLog> logs = null;
	// 银行名称
	private String bankName = "";
	// 贷款类型名称
	private String loanTypeName = "";

	public String getLoanTypeId() {
		return loanTypeId;
	}

	public void setLoanTypeId(String loanTypeId) {
		this.loanTypeId = loanTypeId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public Boolean getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(Boolean companyStatus) {
		this.companyStatus = companyStatus;
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

	public List<LoanAppDetail> getDetails() {
		return details;
	}

	public void setDetails(List<LoanAppDetail> details) {
		this.details = details;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<LoanAppLog> getLogs() {
		return logs;
	}

	public void setLogs(List<LoanAppLog> logs) {
		this.logs = logs;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getLoanTypeName() {
		return loanTypeName;
	}

	public void setLoanTypeName(String loanTypeName) {
		this.loanTypeName = loanTypeName;
	}

}
