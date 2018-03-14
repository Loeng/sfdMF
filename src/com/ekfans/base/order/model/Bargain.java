package com.ekfans.base.order.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 议价-实体类
 * @ClassName Bargain
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月16日
 */
public class Bargain extends BasicBean{
	// 序列化
	private static final long serialVersionUID = 1L;
	// 买家ID
	private String buyerId = "";
	// 卖家ID
	private String sellerId = "";
	// 买家备注
	private String buyerRemarks = "";
	// 卖家备注
	private String sellerRemarks = "";
	// 卖家是否已联系 (0:未联系,1:已联系) 默认 0
	private String sellerContactState = "0";
	//起始地
	private String startFullPath = "";
	//目的地
	private String endFullPath = "";
	// 货源Id(商品id等)
	private String sourceId = "";
	// 议价数量
	private BigDecimal quantity = new BigDecimal(0.00);
	// 价格
	private BigDecimal price = new BigDecimal(0.00);
	// 最终数量
	private BigDecimal finalQuantity = new BigDecimal(0.00);
	// 最终价格
	private BigDecimal finalPrice = new BigDecimal(0.00);
	// 议价状态（0：议价中，1：已议价，2：取消）
	private String status = "";
	//是否接受议价(0:未接受,1:已接受,2:已拒绝)
	private String ifAccept = "0";
	// 联系人
	private String contactMan = "";
	// 联系电话
	private String contactPhone = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";
	// 截止时间
	private String endTime = "";
	// 下单状态（0未下单，1已下单）
	private String orderStatus = "";
	// 来源类型（0商品1车源）
	private String type = "";
	// 商品名
	private String productName = "";
	
	// --------虚拟字段------------
	// 买家名称
	private String buyerName = "";
	// 卖家名称
	private String sellerName = "";
	// 商品小圖
	private String smallPicture = "";
	// 商品價格
	private BigDecimal unitPrice = new BigDecimal(0.00);
	// 商品规格
	private String productModel = "";
	// 商品单位
	private String unit = "";
	// 商品单位
	private String start = "";
	// 商品单位
	private String end = "";
	// 车载/货源总量
	private BigDecimal wfpTotal;
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getProductModel() {
		return productModel;
	}
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}
	public String getSmallPicture() {
		return smallPicture;
	}
	public void setSmallPicture(String smallPicture) {
		this.smallPicture = smallPicture;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getFinalQuantity() {
		return finalQuantity;
	}
	public void setFinalQuantity(BigDecimal finalQuantity) {
		this.finalQuantity = finalQuantity;
	}
	public BigDecimal getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getContactMan() {
		return contactMan;
	}
	public void setContactMan(String contactMan) {
		this.contactMan = contactMan;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public BigDecimal getWfpTotal() {
		return wfpTotal;
	}
	public void setWfpTotal(BigDecimal wfpTotal) {
		this.wfpTotal = wfpTotal;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getBuyerRemarks() {
		return buyerRemarks;
	}
	public void setBuyerRemarks(String buyerRemarks) {
		this.buyerRemarks = buyerRemarks;
	}
	public String getSellerRemarks() {
		return sellerRemarks;
	}
	public void setSellerRemarks(String sellerRemarks) {
		this.sellerRemarks = sellerRemarks;
	}
	public String getStartFullPath() {
		return startFullPath;
	}
	public void setStartFullPath(String startFullPath) {
		this.startFullPath = startFullPath;
	}
	public String getEndFullPath() {
		return endFullPath;
	}
	public void setEndFullPath(String endFullPath) {
		this.endFullPath = endFullPath;
	}
	public String getSellerContactState() {
		return sellerContactState;
	}
	public void setSellerContactState(String sellerContactState) {
		this.sellerContactState = sellerContactState;
	}
	public String getIfAccept() {
		return ifAccept;
	}
	public void setIfAccept(String ifAccept) {
		this.ifAccept = ifAccept;
	}
	
}
