package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 店铺频道配置实体类
 * 
 * @Title: ChannelConfig.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author 成都易科远见科技有限公司
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class StoreChannelConfig extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 频道ID -- 对应频道表(StoreChannel)主键
	private String channelId = "";
	
	// 店铺Id
	private String storeId = "";

	// 配置类型
	private String configType = "";

	// 配置位置
	private int configPosition = 0;

	// 配置明细ID
	private String objectId = "";

	// 配置内容类型
	private String objectType = "";

	// 展示数量
	private int showNumber = 0;

	// 配置名称
	private String configName = "";

	// 配置名称图标
	private String configIcon = "";

	private Object details = null;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public int getConfigPosition() {
		return configPosition;
	}

	public void setConfigPosition(int configPosition) {
		this.configPosition = configPosition;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public int getShowNumber() {
		return showNumber;
	}

	public void setShowNumber(int showNumber) {
		this.showNumber = showNumber;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigIcon() {
		return configIcon;
	}

	public void setConfigIcon(String configIcon) {
		this.configIcon = configIcon;
	}

	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}

}