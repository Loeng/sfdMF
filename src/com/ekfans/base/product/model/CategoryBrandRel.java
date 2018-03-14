package com.ekfans.base.product.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品分类与品牌关系实体类
 * 
 * @Title: CategoryBrandRel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class CategoryBrandRel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 商品分类ID -- 对应商品分类表(PRODUCT_CATEGORY)主键
	private String categoryId = "";
	
	// 品牌ID -- 对应品牌表(PRODUCT_BRAND)主键
	private String brandId = "";
	
	// 排序
	private int position = 0;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getBrandId() {
	    return brandId;
	}

	public void setBrandId(String brandId) {
	    this.brandId = brandId;
	}

	public static long getSerialversionuid() {
	    return serialVersionUID;
	}
}