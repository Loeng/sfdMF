package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import java.math.BigDecimal;

import com.ekfans.base.order.model.Bargain;
import com.ekfans.base.order.model.Inquiry;

/**
 * 获取用户议价返回数据
 * @author pp
 * @Date 2017年7月21日14:12:34
 */
public class UserBargain {
	 //议价id
	 private String bargainId="";
	 // 议价人ID
     private String buyerId = "";
	 // 被议价人ID
	 private String sellerId = "";
	 // 货源Id(商品id等)
	 private String sourceId = "";
	 //起始地
	 private String[] startFullPaths;
	 //目的地
	 private String[] endFullPaths;
	 // 价格
	 private BigDecimal price = new BigDecimal(0.00);
	 //价格单位
	 private String priceUnit ="";
	 // 联系人
	 private String contactMan = "";
	 // 联系电话
	 private String contactPhone = "";
	 // 创建时间
	 private String createTime = "";
	 // 更新时间
	 private String updateTime = "";
	 // 来源类型（0:车源 1:货源）
	 private String type = "";
	 // 商品名
	 private String productName = "";
	 // 卖家是否已联系 (0:未联系,1:已联系)
	 private String sellerContactState = "0";
	 //商品距离
	 private String commodityDistance="";
	 //离我距离
	 private String distanceMe="";
	 //用户备注信息
	 private String userRemarks="";
	 //是否接受询价议价(0:未接受,1:已接受,2:已拒绝)
	 private String ifAccept;
	 //议价货源信息
	 private HuoResponse Goods;
	 //议价车源信息
	 private CarSource carSource;
	 
	 public UserBargain(Bargain b,CarSource carSource,String type ) {
		this.ifAccept=b.getIfAccept();
		this.bargainId=b.getId();
		this.buyerId=b.getBuyerId();
		this.sellerId=b.getSellerId();
		this.sourceId=b.getSourceId();
		this.contactMan=b.getContactMan();
		this.contactPhone=b.getContactPhone();
		this.createTime=b.getCreateTime();
		this.updateTime=b.getUpdateTime();
		this.type="0";
		this.productName=b.getProductName();
		this.startFullPaths=
		this.endFullPaths=b.getEndFullPath().split(",");
		this.sellerContactState=b.getSellerContactState();
		if(type.equals("1")){
			this.userRemarks=b.getBuyerRemarks();
		}
        if(type.equals("2")){
        	this.userRemarks=b.getSellerRemarks();
		}
		this.carSource=carSource;
	 }
	 public UserBargain(Inquiry i,HuoResponse Goods,String type ) {
		this.ifAccept=i.getIfAccept();
		this.bargainId=i.getId();
		this.buyerId=i.getBuyersId();
		this.sellerId=i.getSellersId();
		this.sourceId=i.getProductId();
		this.price=i.getFinalPrice();
		this.priceUnit=i.getUnit();
		this.contactMan=i.getLinkPerson();
		this.contactPhone=i.getLinkTel();
		this.createTime=i.getCreateTime();
		this.updateTime=i.getUpdateTime();
		this.type="1";
		this.sellerContactState=i.getSellerContactState();
		if(type.equals("1")){
			this.userRemarks=i.getBuyerRemarks();
		}
        if(type.equals("2")){
        	this.userRemarks=i.getSellerRemarks();
		}
		this.Goods=Goods;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
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
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public HuoResponse getGoods() {
		return Goods;
	}
	public void setGoods(HuoResponse goods) {
		Goods = goods;
	}
	public CarSource getCarSource() {
		return carSource;
	}
	public void setCarSource(CarSource carSource) {
		this.carSource = carSource;
	}
	public String getUserRemarks() {
		return userRemarks;
	}
	public void setUserRemarks(String userRemarks) {
		this.userRemarks = userRemarks;
	}
	
	public String[] getStartFullPaths() {
		return startFullPaths;
	}
	public void setStartFullPaths(String[] startFullPaths) {
		this.startFullPaths = startFullPaths;
	}
	public String[] getEndFullPaths() {
		return endFullPaths;
	}
	public void setEndFullPaths(String[] endFullPaths) {
		this.endFullPaths = endFullPaths;
	}
	public String getBargainId() {
		return bargainId;
	}
	public void setBargainId(String bargainId) {
		this.bargainId = bargainId;
	}
	public String getSellerContactState() {
		return sellerContactState;
	}
	public void setSellerContactState(String sellerContactState) {
		this.sellerContactState = sellerContactState;
	}
	public String getCommodityDistance() {
		return commodityDistance;
	}
	public void setCommodityDistance(String commodityDistance) {
		this.commodityDistance = commodityDistance;
	}
	public String getDistanceMe() {
		return distanceMe;
	}
	public void setDistanceMe(String distanceMe) {
		this.distanceMe = distanceMe;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getIfAccept() {
		return ifAccept;
	}
	public void setIfAccept(String ifAccept) {
		this.ifAccept = ifAccept;
	}
	
}
