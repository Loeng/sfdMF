package com.ekfans.base.channel.model;

import com.ekfans.basic.hibernate.model.BasicBean;
import com.ekfans.pub.util.Pager;

/**
 * 频道配置--实体类
 * 
 * @ClassName: BuyerCompany
 * @Description: 
 * @author lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ChannelConfig extends BasicBean {
	
	private static final long serialVersionUID = 1L;
	// 频道ID -- 对应频道表(Channel)主键
	private String channelId = "";
	// 配置类型
	private String configType = "";
	// 配置位置
	private Integer configPosition = 0;
	// 配置明细ID
	private String objectId = "";
	// 配置内容类型
	private String objectType = "";
	// 展示数量
	private Integer showNumber = 0;
	// 配置名称
	private String configName = "";
	// 配置名称图标
	private String configIcon = "";

	private Object details = null;
	
	private	Pager pager=null;
	
	//选中分类名称
	private String catgName;

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	// ====================  get set  ====================
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public Integer getConfigPosition() {
		return configPosition;
	}

	public void setConfigPosition(Integer configPosition) {
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

	public Integer getShowNumber() {
		return showNumber;
	}

	public void setShowNumber(Integer showNumber) {
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

	public String getCatgName() {
		return catgName;
	}

	public void setCatgName(String catgName) {
		this.catgName = catgName;
	}

}