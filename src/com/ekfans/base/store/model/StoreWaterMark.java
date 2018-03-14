package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 店铺水印管理实体类
 * 
 * @Title: StoreWaterMark.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class StoreWaterMark extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 店铺ID -- 对应店铺表(Store)主键
	private String storeId = "";
	
	// 中图水印
	private String centerWater = "";
	
	// 大图水印
	private String bigWater = "";
	
	// 水印水平位置
	private String XPosition = "";
	
	// 水印垂直位置
	private String YPosition = "";

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCenterWater() {
		return centerWater;
	}

	public void setCenterWater(String centerWater) {
		this.centerWater = centerWater;
	}

	public String getBigWater() {
		return bigWater;
	}

	public void setBigWater(String bigWater) {
		this.bigWater = bigWater;
	}

	public String getXPosition() {
		return XPosition;
	}

	public void setXPosition(String xPosition) {
		XPosition = xPosition;
	}

	public String getYPosition() {
		return YPosition;
	}

	public void setYPosition(String yPosition) {
		YPosition = yPosition;
	}
}