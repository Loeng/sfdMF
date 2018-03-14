package com.ekfans.base.content.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 内容和分类关系实体类
 * 
 * @Title: ContentCategoryRel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ContentCategoryRel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 内容ID -- 对应资讯表(Content)主键
	private String contentId = "";
	
	// 分类ID
	private String categoryId = "";
	
	// 排序
	private int position = 0;

	// 修改咨询显示用
	private String contentCategoryName = "";
	
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getContentCategoryName() {
	    return contentCategoryName;
	}

	public void setContentCategoryName(String contentCategoryName) {
	    this.contentCategoryName = contentCategoryName;
	}
	
}