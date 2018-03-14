package com.ekfans.base.user.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 会员权限实体类
 * 
 * @Title: User.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-13
 * @version 1.0
 */
@Entity
public class UserPurview extends BasicBean {
	// 序列化
	private static final long serialVersionUID = 1L;

	// 权限名称
	private String className = "";

	// 权限路径
	private String fullPath = "";

	// 权限排序位置
	private int position = 0;

	// 父权限ID
	private String parentId = "";

	// 权限状态
	private boolean status = false;

	// 权限等级
	private int level = 0;

	// 权限图标
	private String icon = "";

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
