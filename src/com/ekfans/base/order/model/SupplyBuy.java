package com.ekfans.base.order.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 供需关系--实体类
 * 
 * @ClassName: SupplyBuy
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class SupplyBuy extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 企业ID
	private String storeId = "";
	// 供求的标题
	private String title = "";
	// 供求的描述
	private String content = "";
	// 联系人
	private String contactName = "";
	// 联系电话
	private String contactPhone = "";
	// 类型0供应，1求购
	private String type = "";
	// 状态0关闭，1正常，2已完成
	private String status = "";
	// 商品类型 0现货 1原料 2危废品
	private String productType = "";
	// 有效期
	private String endTime = "";
	// 创建时间
	private String createTime = "";
	// 修改时间
	private String updateTime = "";
	// 需要的资质
	private String intelligenceIds = "";
	// 备注
	private String note = "";
	// 组织部门id
	private String orgId = "";
	// 审核状态(0:未审核，1：通过，-1不通过)
	private int checkStatus = 0;
	// 审核人
	private String checkMan = "";
	// 审核时间
	private String checkTime = "";
	// 审核说明
	private String checkNote = "";
	// 预估价
	private BigDecimal futurePrices = new BigDecimal(0.00);

	// 分类id
	private String categoryId = "";
	// 区县id
	private String areaId = "";

	// 详细地址
	private String address = "";
	// 交货地址
	private String destination = "";
	// 交货方式(0自提;1送货上门)
	private int deliveryType = 0;
	// 数量
	private BigDecimal number = new BigDecimal(0.00);
	// 计价单位
	private String unit = "";
	// 访问量
	private int viewCount = 0;
	// 质量等级
	private String qualityLevel = "";
	// 企业名称
	private String storeName = "";

	// ***app field***
	// 环信用户名
	private String hxUserName = "";
	// 好友关系 0不是好友 1好友 2好友的好友
	private String friendStatus = "0";
	// 分类名称
	private String categoryName;

	//------临时
	private String shareUrl;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(String friendStatus) {
		this.friendStatus = friendStatus;
	}

	public String getHxUserName() {
		return hxUserName;
	}

	public void setHxUserName(String hxUserName) {
		this.hxUserName = hxUserName;
	}

	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	public BigDecimal getFuturePrices() {
		return futurePrices;
	}

	public void setFuturePrices(BigDecimal futurePrices) {
		this.futurePrices = futurePrices;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getIntelligenceIds() {
		return intelligenceIds;
	}

	public void setIntelligenceIds(String intelligenceIds) {
		this.intelligenceIds = intelligenceIds;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getAreaId() {
		return areaId;
	}

	public String getAddress() {
		return address;
	}

	public String getDestination() {
		return destination;
	}

	public int getViewCount() {
		return viewCount;
	}

	public int getDeliveryType() {
		return deliveryType;
	}

	public String getQualityLevel() {
		return qualityLevel;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public void setDeliveryType(int deliveryType) {
		this.deliveryType = deliveryType;
	}

	public void setQualityLevel(String qualityLevel) {
		this.qualityLevel = qualityLevel;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
}
