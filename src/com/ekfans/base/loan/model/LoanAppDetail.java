package com.ekfans.base.loan.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 贷款申请字段明细
 * 
 * @Title: LoanTypeDetail.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author lgy
 * @date 2016-4-25
 * @version 1.0
 */
@Entity
public class LoanAppDetail extends BasicBean {

	private static final long serialVersionUID = 1L;

	// 银行ID
	private String bankId = "";

	// 贷款类型表ID
	private String typeId = "";

	// 贷款类型明细表ID
	private String typeDetailId = "";

	// 贷款申请表ID
	private String appId = "";

	// 字段名称
	private String name = "";

	// 字段标识号
	private String nameCode = "";

	// 字段类型
	private String type = "";

	// 值
	private String value = "";

	/*
	 *  数据类型
	 *  当nameCode=contract时：valueType为9表示合同文件路径，4，表示合同id
	 */
	private String valueType = "0";

	// 详细说明
	private String note = "";
	// 排序
	private int position = 0;

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameCode() {
		return nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTypeDetailId() {
		return typeDetailId;
	}

	public void setTypeDetailId(String typeDetailId) {
		this.typeDetailId = typeDetailId;
	}

}
