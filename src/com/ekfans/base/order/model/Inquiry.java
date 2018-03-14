package com.ekfans.base.order.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 询价--实体类
 * 
 * @ClassName: Inquiry
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Inquiry extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 买家ID
	private String buyersId = "";
	// 卖家ID
	private String sellersId = "";
	// 买家备注
	private String buyerRemarks = "";
	// 卖家备注
	private String sellerRemarks = "";
	// 卖家是否已联系 (0:未联系,1:已联系) 默认 0
	private String sellerContactState = "0";
	// 资源ID
	private String productId = "";
	// 购买数量(买家提交)
	private Integer number = 0;
	// 回复数量
	private Integer sellersNumber = 0;
	// 商城价
	private BigDecimal price = new BigDecimal(0.00);
	// 报价
	private BigDecimal FinalPrice = new BigDecimal(0.00);
	// 创建时间
	private String createTime = "";
	// 询价单状态(0:询价中 1:关闭2：已经下单)
	private String status ;
	//联系人
    private String linkPerson ="";
    //联系方式
    private String linkTel = "";
    //核定回复时间
    private String updateTime ="";
    //询价单状态(是否已取消或者下单 1：下单，2，取消，3，关闭)
    private String inquiryStatus ="0";
    //订单是否已超时
    private String endTime;
    //是否过期时间
    private String checkTime;
    //伪造数据
	private String productName = "";
	//图片
	private String pic = "";
	//单位
	private String unit = "";
	//备注
	private String note = "";
	//类型(0询价1报价)
	private int type;
	//货物来源(1供求2货源)
	private int sourceType;
	//企业名(针对游客询价)
	private String storeName;
	//是否接受询价(0:未接受,1:已接受,2:已拒绝)
	private String ifAccept = "0";
	//临时数据
	String Title ="";
	
   
    public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getInquiryStatus() {
        return inquiryStatus;
    }
    public void setInquiryStatus(String inquiryStatus) {
        this.inquiryStatus = inquiryStatus;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Integer getSellersNumber() {
        return sellersNumber;
    }
    public void setSellersNumber(Integer sellersNumber) {
        this.sellersNumber = sellersNumber;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getBuyersId() {
        return buyersId;
    }
    public void setBuyersId(String buyersId) {
        this.buyersId = buyersId;
    }
    public String getSellersId() {
        return sellersId;
    }
    public void setSellersId(String sellersId) {
        this.sellersId = sellersId;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public BigDecimal getFinalPrice() {
        return FinalPrice;
    }
    public void setFinalPrice(BigDecimal finalPrice) {
        FinalPrice = finalPrice;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
   
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
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
