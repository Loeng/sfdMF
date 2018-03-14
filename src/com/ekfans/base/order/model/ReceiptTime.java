package com.ekfans.base.order.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 收货时间区间类
 * 
 * @Title: ReceiptTime.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ReceiptTime extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 名称
	private String name = "";
	
	// 具体说明
	private String note = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}