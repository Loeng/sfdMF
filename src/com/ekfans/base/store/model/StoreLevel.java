package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 店铺等级实体类
 * 
 * @Title: StoreLevel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class StoreLevel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 等级名称
	private String name = "";
	
	// 等级标志
	private String icon = "";
	
	// 升级最低条件
	private int lowestConditions = 0;
	
	// 降级最高条件
	private int highestConditions = 0;
	
	// 等级说明
	private String note = "";
	
	// 创建时间
	private String createTime = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getLowestConditions() {
		return lowestConditions;
	}

	public void setLowestConditions(int lowestConditions) {
		this.lowestConditions = lowestConditions;
	}

	public int getHighestConditions() {
		return highestConditions;
	}

	public void setHighestConditions(int highestConditions) {
		this.highestConditions = highestConditions;
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