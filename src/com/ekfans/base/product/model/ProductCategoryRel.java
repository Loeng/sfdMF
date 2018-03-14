package com.ekfans.base.product.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品与分类关系实体类
 * 
 * @Title:ProductCategoryRel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ProductCategoryRel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 分类ID -- 对应商品分类表(ProductCategory)主键
	private String categoryId = "";
	
	// 商品ID -- 对应商品表(Product)主键
	private String productId = "";
	
	// 是否主分类
	private boolean main = false;
	
	// 排序
	private int position = 0;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}