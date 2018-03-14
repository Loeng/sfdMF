package com.ekfans.base.product.model;

import com.ekfans.basic.hibernate.model.BasicBean;

public class ProductPicture extends BasicBean {
	
	// 序列化
	private static final long serialVersionUID = 1L;
	// 商品Id
	private String productId = "";
	// 商品原图
	private String picture = "";
	// 商品大图
	private String bigPicture = "";
	// 商品中图
	private String midPicture = "";
	// 商品小图
	private String smallPicture = "";
	// 显示位置
	private String position = "";
	
	// ================  临时数据  ================
	private Product product;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getBigPicture() {
		return bigPicture;
	}

	public void setBigPicture(String bigPicture) {
		this.bigPicture = bigPicture;
	}

	public String getMidPicture() {
		return midPicture;
	}

	public void setMidPicture(String midPicture) {
		this.midPicture = midPicture;
	}

	public String getSmallPicture() {
		return smallPicture;
	}

	public void setSmallPicture(String smallPicture) {
		this.smallPicture = smallPicture;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
