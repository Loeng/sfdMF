package com.ekfans.base.finance.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行客户日志表
 * @ClassName BankClientLog
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年5月26日
 */
@Entity
public class BankClientLog extends BasicBean{

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 银行用户id
	private String accountId = "";
	
	// 企业id
	private String storeId = "";
	
	// 银行id
	private String bankId = "";
	
	// 操作人
	private String creator = "";
	
	// 操作人id
	private String creatorId = "";
	
	// 操作内容
	private String note = "";
	
	// 操作时间
	private String createTime = "";

	//-----get/set方法----
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

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

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
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
