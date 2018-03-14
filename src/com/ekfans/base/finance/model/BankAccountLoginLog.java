package com.ekfans.base.finance.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行账户登陆日志实体类
 * 
 * @Title: StoreRole.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author lgy
 * @date 2016-04-21
 * @version 1.0
 */
@Entity
public class BankAccountLoginLog extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 银行ID
	private String bankId = "";
	// 银行用户ID
	private String accountId = "";
	// 日志类型：0登陆，1退出
	private String loginType = "0";
	// 创建时间
	private String createTime = "";
	// 备注
	private String note = "";
	// IP地址
	private String ipAddress = "";

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}