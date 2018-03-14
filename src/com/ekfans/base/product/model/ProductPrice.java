package com.ekfans.base.product.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品价格区间实体类
 * 
 * @Title: ProductBrand.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ProductPrice extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 品牌名称
	private String productId = "";

	// 价格
	private BigDecimal price = new BigDecimal("0.00");

	// 价格区间起始数
	private int startNum = 0;

	// 价格区间结束数
	private int endNum = 0;

	// 排序
	private int position = 0;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getEndNum() {
		return endNum;
	}

	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
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

	// 状态
	private boolean status = false;

	// 描述
	private String note = "";

}