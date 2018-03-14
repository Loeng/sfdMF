package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 店铺频道实体类
 * 
 * @Title: Channel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class StoreChannel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 店铺id
    private String storeId = "";
    
	// 频道名称
	private String name = "";

	// 频道位置
	private int position = 0;

	// 频道说明
	private String note = "";

	// 频道状态
	private boolean status = false;

	// 模板路径
	private String viewPath = "";

	// 创建时间
	private String createTime = "";

	// 修改时间
	private String updateTime = "";

	// 链接路径 - 临时字段，不需要保存到数据库
	private String linkUrl = "";

	public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getViewPath() {
		return viewPath;
	}

	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
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

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
}