package com.ekfans.base.loan.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 贷款类型入门规则
 * 
 * @Title: LoanTypeRole.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author lgy
 * @date 2016-4-25
 * @version 1.0
 */
@Entity
public class LoanTypeRole extends BasicBean {

	private static final long serialVersionUID = 1L;

	// 银行ID
	private String bankId = "";

	// 贷款类型表ID
	private String typeId = "";

	// 相关规则ID(适用于多个规则必须同时满足的情况)
	private String parentRoleId = "";

	// 规则名称
	private String roleName = "";

	// 规格类型(大于，小于等于等)
	private String roleType = "";

	// 规则界限值
	private String roleValue = "";

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

	public String getParentRoleId() {
		return parentRoleId;
	}

	public void setParentRoleId(String parentRoleId) {
		this.parentRoleId = parentRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleValue() {
		return roleValue;
	}

	public void setRoleValue(String roleValue) {
		this.roleValue = roleValue;
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

}
