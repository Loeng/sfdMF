package com.ekfans.base.finance.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行角色权限实体类
 * 
 * @Title: StorePurviewRel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author lgy
 * @date 2016-04-20
 * @version 1.0
 */
@Entity
public class BankPurviewRel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 角色ID
	private String roleId = "";

	// 权限ID
	private String purviewId = "";

	// 银行ID
	private String bankId = "";

	// 权限的类型 false 银行账户，true 子部门账户
	private boolean type = false;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPurviewId() {
		return purviewId;
	}

	public void setPurviewId(String purviewId) {
		this.purviewId = purviewId;
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

}