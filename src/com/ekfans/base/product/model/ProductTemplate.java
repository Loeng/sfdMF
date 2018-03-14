package com.ekfans.base.product.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品模版实体类
 * 
 * @Title: ProductTemplate.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ProductTemplate extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 名称
	private String name = "";
	
	// 模板是否为系统默认模板
	private boolean type = false;
	
	// 创建时间
	private String createTime = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}