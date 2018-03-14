package com.ekfans.base.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 
 * @ClassName: Refund
 * @Description: TODO询价表
 * @author 成都易科远见科技有限公司
 * @date 2014-5-12 下午2:47:53
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class OrderPhoto extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 订单ID
	private String orderId = "";
	// 订单明细ID
	private String orderDetailId = "";
	// 商品id
	private String productId = "";

	// 商品名称
	private String productName = "";
	// 店铺ID -- 对应店铺表(Store)主键
	private String storeId = "";
	// 商城价
	private BigDecimal unitPrice = new BigDecimal("0.00");
	// 市场价
	private BigDecimal listPrice = new BigDecimal("0.00");
	// 成交价格
	private BigDecimal lastPrice = new BigDecimal("0.00");
	// 数量单位
	private String unit = "";
	// 所属品牌
	private String brand = "";
	// 商品原图
	private String picture = "";
	// 商品大图
	private String bigPicture = "";
	// 商品中图
	private String centerPicture = "";
	// 商品小图
	private String smallPicture = "";
	// 商品描述
	private String description = "";
	// 商品备注
	private String note = "";
	// 商品类型
	private String type = "";
	// 商品模版ID -- 对应商品模版表(PRODUCT_TEMPLATE)主键
	private String templateId = "";
	// 商品主分类
	private String mainCategory = "";
	// 商品产地
	private String habitat = "";
	// 商品产地详细地址
	private String habitatAddress = "";
	// 商品编号
	private String productNumber = "";
	// 所属仓库ID
	private String wareHouseId = "";
	// 商品邮费
	private BigDecimal fare = new BigDecimal("0.00");

	private List<OrderPhotoInfo> infoList = new ArrayList<OrderPhotoInfo>();

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

	public String getCenterPicture() {
		return centerPicture;
	}

	public void setCenterPicture(String centerPicture) {
		this.centerPicture = centerPicture;
	}

	public String getSmallPicture() {
		return smallPicture;
	}

	public void setSmallPicture(String smallPicture) {
		this.smallPicture = smallPicture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public List<OrderPhotoInfo> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<OrderPhotoInfo> infoList) {
		this.infoList = infoList;
	}
}
