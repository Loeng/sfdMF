package com.ekfans.base.content.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 相关内容关系实体类
 * 
 * @Title: ContentRelation.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-23
 * @version 1.0
 */
@Entity
public class ContentRelation extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	//  资讯ID -- 对应资讯表(Content)主键
	private String contentId = "";
	
	// 相关资讯ID
	private String relatedId = "";

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(String relatedId) {
		this.relatedId = relatedId;
	}
}