package com.ekfans.base.product.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品收藏管理实体类
 * 
 * @Title: ProductCollect.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ProductCollect extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 商品ID -- 对应商品表(Product)主键
	private String productId = "";
	
	// 会员ID -- 对应会员表(Customer)主键
	private String userId = "";
	
	// 收藏时间
	private String collectTime = "";
	
	// 收藏备注名
	private String collectNoteName = "";
	
	// 收藏说明
	private String collectDirections = "";
	
	//显示的图片
	private String picture = "";
	//显示的商品描述
	private String description = "";
	//显示的商场价格
	private BigDecimal unitPrice = new BigDecimal("0.00");
	//显示的店铺名字
	private String name = "";
	//显示的商品名字
	private String productName = "";

	public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public String getCollectNoteName() {
		return collectNoteName;
	}

	public void setCollectNoteName(String collectNoteName) {
		this.collectNoteName = collectNoteName;
	}

	public String getCollectDirections() {
		return collectDirections;
	}

	public void setCollectDirections(String collectDirections) {
		this.collectDirections = collectDirections;
	}
}