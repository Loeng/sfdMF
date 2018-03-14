package com.ekfans.base.order.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 订单快照商品扩展信息实体类
 * 
 * @Title: ProductInfo.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class OrderPhotoInfo extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 快照ID
	private String photoId = "";

	// 模板明细ID
	private String templateFieldId = "";

	// 扩展信息名
	private String infoName = "";

	// 扩展信息值
	private String infoValue = "";

	// 扩展信息类型 0,不可选;1,可选
	private String infoType = "";

	// 模板分类id
	private String categoryId = "";

	// 模板分类名
	private String categoryName = "";

	// 显示位置 (目前显示位置1、2、3、4为可选项;不可选项的显示位置还未管)
	private int position = 0;

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public String getTemplateFieldId() {
		return templateFieldId;
	}

	public void setTemplateFieldId(String templateFieldId) {
		this.templateFieldId = templateFieldId;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getInfoValue() {
		return infoValue;
	}

	public void setInfoValue(String infoValue) {
		this.infoValue = infoValue;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}