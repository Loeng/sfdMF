package com.ekfans.base.product.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

public class SupplyProduct extends BasicBean {
	// 序列化
	private static final long serialVersionUID = 1L;
	// 商品名称
	private String supplyProductName = "";
	// 商品编号
	private String productNum = "";
	// 商品数量
	private int quantity = 0;
	// 商品单位
	private String unit = "";
	// 批发价
	private BigDecimal pfPrice = new BigDecimal("0.00");
	// 商品主分类
	private String mainCategory = "";
	// 商品产地
	private String habitat = "";
	// 商品产地详细地址
	private String habitatAddress = "";
	// 发布人
	private String userId = "";
	// 联系人
	private String linkPerson = "";
	// 联系电话
	private String linkTel = "";
	// 商品状态
	private int status = 0;
	// 发布时间
	private String createTime = "";
	// 备注
	private String note = "";

	public String getProductNum() {
		return productNum;
	}

	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSupplyProductName() {
		return supplyProductName;
	}

	public void setSupplyProductName(String supplyProductName) {
		this.supplyProductName = supplyProductName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getPfPrice() {
		return pfPrice;
	}

	public void setPfPrice(BigDecimal pfPrice) {
		this.pfPrice = pfPrice;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getHabitat() {
		return habitat;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	public String getHabitatAddress() {
		return habitatAddress;
	}

	public void setHabitatAddress(String habitatAddress) {
		this.habitatAddress = habitatAddress;
	}

	public String getLinkPerson() {
		return linkPerson;
	}

	public void setLinkPerson(String linkPerson) {
		this.linkPerson = linkPerson;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
