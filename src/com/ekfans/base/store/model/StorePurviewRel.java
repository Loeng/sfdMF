package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 店铺角色权限实体类
 * 
 * @Title: StorePurviewRel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class StorePurviewRel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 角色ID
	private String roleId = "";

	// 权限ID
	private String purviewId = "";

	// 权限的类型 false 为普通用户，true 为子账户
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

}