package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商户收藏管理实体类
 * 
 * @Title: StoreCollect.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class StoreCollect extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 会员ID -- 对应会员表(User)主键
	private String userId = "";
	
	// 店铺ID -- 对应店铺表(Store)主键
	private String storeId = "";
	
	// 收藏时间
	private String collectTime = "";
	
	// 收藏备注名
	private String collectNoteName = "";
	
	// 收藏说明
	private String collectDirections = "";
	
	//收藏页面的店铺logo
	private String logo = "";
	//收藏页面的店铺名字
	private String name = "";
	
	public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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