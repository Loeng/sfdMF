package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 店铺权限实体类
 * 
 * @Title: StoreRole.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author lgy
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class StoreRole extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 角色名称
	public String name = "";

	// 创建时间
	public String createTime = "";

	// 更新时间
	public String updateTime = "";

	// 备注
	public String note = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}