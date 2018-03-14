package com.ekfans.base.order.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 订单发货地址实体类
 * 
 * @Title: OrderAddress.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class OrderAddress extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 订单ID -- 对应订单表(Order)主键
	private String orderId = "";
	
	// 收货人姓名
	private String name = "";
	
	// 收货人所在省
	private String provincial = "";
	
	// 收货人坐在市
	private String city = "";
	
	// 收货人所在区
	private String area = "";
	
	// 收货人详细地址
	private String address = "";
	
	// 收货人收货人电话
	private String phoneNum = "";
	
	// 收货人手机
	private String mobile = "";
	
	// 收货人Email
	private String email = "";
	
	// 收货人邮编
	private String zipCode = "";
	
	// 收货时间区间ID -- 对应收货时间区间表(RECEIPT_TIME)主键
	private String receiptTimeId = "";
	
	// 收货时间区间(配送时间区间)
	private String receiptTime = "";

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvincial() {
		return provincial;
	}

	public void setProvincial(String provincial) {
		this.provincial = provincial;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getReceiptTimeId() {
		return receiptTimeId;
	}

	public void setReceiptTimeId(String receiptTimeId) {
		this.receiptTimeId = receiptTimeId;
	}

	public String getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}
}