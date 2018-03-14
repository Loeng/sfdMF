package com.ekfans.base.finance.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行客户实体
 * @ClassName BankClient
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年5月26日
 */
@Entity
public class BankClient extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 银行id
	private String bankId = "";
	
	// 企业id
	private String storeId = "";
	
	// 客户类型(0普通 1黑名单)
	private String type = "0";
	
	// 企业名
	private String storeName = "";
	
	// 信用评级
	private String creditRate = "";
	
	// 创建时间
	private String createTime = "";
	
	// -----get/set方法 -----
	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getCreditRate() {
		return creditRate;
	}

	public void setCreditRate(String creditRate) {
		this.creditRate = creditRate;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
