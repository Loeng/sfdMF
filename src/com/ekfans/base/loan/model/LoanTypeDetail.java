package com.ekfans.base.loan.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 贷款类型字段明细
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
public class LoanTypeDetail extends BasicBean {

	private static final long serialVersionUID = 1L;

	// 银行ID
	private String bankId = "";

	// 贷款类型表ID
	private String typeId = "";

	// 字段名称
	private String name = "";

	// 字段标识号
	private String nameCode = "";

	// 字段类型
	private String type = "";

	// 字段值(用于选择时)
	private String typeValue = "";

	// 该字段是否必须提交 true:必须提交，false:非必需提交
	private Boolean commentType = false;

	// 详细说明
	private String note = "";
	// 排序
	private int position = 0;
	
	//----临时数据
	private Boolean checked = false;

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

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
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

	public Boolean getCommentType() {
		return commentType;
	}

	public void setCommentType(Boolean commentType) {
		this.commentType = commentType;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
