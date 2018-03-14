package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 仓储表
 * 
 * @Title: Store.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
public class Warehouse extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 仓库名称
	private String name = "";
	// 地区
	private String areaId = "";
	// 详细地址
	private String address = "";
	// 操作人
	private String creator = "";
	// 创建时间
	private String createTime = "";
	// 状态（禁用，启用）
	private boolean status = false;
	// 备注
	private String note = "";
	
	
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAreaId() {
        return areaId;
    }
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
	
	
	
}