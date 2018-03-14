package com.ekfans.base.content.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 广告明细实体类
 * 
 * @Title: AdDetail.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class AdDetail extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 广告ID -- 对应广告表(SHOP_AD)主键
	private String adId = "";
	
	// 广告文字
	private String adContent = "";
	
	// 文字颜色
	private String contentColor = "";
	
	// 广告图片
	private String adPicture = "";
	
	// 链接广告
	private String linkAddress = "";
	
	// 排序位置
	private int position = 0;

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getAdContent() {
		return adContent;
	}

	public void setAdContent(String adContent) {
		this.adContent = adContent;
	}

	public String getContentColor() {
        return contentColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }

    public String getAdPicture() {
        return adPicture;
    }

    public void setAdPicture(String adPicture) {
        this.adPicture = adPicture;
    }

    public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}