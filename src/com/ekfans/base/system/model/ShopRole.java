package com.ekfans.base.system.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 后台角色--实体类
 * 
 * @ClassName: ShopRole
 * @author zgm
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ShopRole extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 父级ID
	private String parentId = "";
	// 角色名称
	private String name = "";
	// 创建时间
	private String createTime = "";
	// 最近一次更新时间
	private String updateTime = "";
	// 描述
	private String note = "";

	// ================== get set ==================
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