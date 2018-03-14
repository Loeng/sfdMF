package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 店铺装修实体类
 * 
 * @Title: StoreDecoration.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class StoreDecoration extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 店铺ID -- 对应店铺表(Store)主键
	private String storeId = "";
	
	// 店铺背景图
	private String bgPhoto = "";
	
	// 店铺背景图效果
	private String bgPhotoEffect = "";
	
	// 店铺背景色
	private String bgColor = "";
	
	// 店铺背景图横向位置
	private String ptohoX = "";
	
	// 店铺背景图纵向位置
	private String ptohoY = "";

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getBgPhoto() {
		return bgPhoto;
	}

	public void setBgPhoto(String bgPhoto) {
		this.bgPhoto = bgPhoto;
	}

	public String getBgPhotoEffect() {
		return bgPhotoEffect;
	}

	public void setBgPhotoEffect(String bgPhotoEffect) {
		this.bgPhotoEffect = bgPhotoEffect;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getPtohoX() {
		return ptohoX;
	}

	public void setPtohoX(String ptohoX) {
		this.ptohoX = ptohoX;
	}

	public String getPtohoY() {
		return ptohoY;
	}

	public void setPtohoY(String ptohoY) {
		this.ptohoY = ptohoY;
	}
}