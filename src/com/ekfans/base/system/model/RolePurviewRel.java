package com.ekfans.base.system.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 管理员角色和权限关系实体类
 * 
 * @Title: RolePurviewRel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class RolePurviewRel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 角色ID -- 对应管理员角色表(SHOP_ROLE)主键
	private String roleId = "";
	
	// 权限ID -- 对应后台功能菜单表表(SHOP_PURVIEW)主键
	private String purviewId = "";

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
}