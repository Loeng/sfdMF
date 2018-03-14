package com.ekfans.base.finance.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行部门管理
 * 
 * @Title: StoreRole.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author lgy
 * @date 2016-04-20
 * @version 1.0
 */
@Entity
public class BankDepartment extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 银行ID
	private String bankId = "";
	// 部门名称
	private String name = "";
	// 上级部门ID
	private String parentId = "";
	// 状态0不启用，1启用
	private Boolean status = false;
	// 级别
	private int level = 0;
	// 排序
	private int position = 0;
	// 创建时间
	private String createTime = "";
	// 修改时间
	private String updateTime = "";
	// 备注
	private String note = "";

	// ------临时数据
	private List<BankDepartment> childList = new LinkedList<BankDepartment>();

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<BankDepartment> getChildList() {
		return childList;
	}

	public void setChildList(List<BankDepartment> childList) {
		this.childList = childList;
	}
}