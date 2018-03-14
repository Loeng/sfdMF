package com.ekfans.base.content.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 资讯详细内容实体类
 * 
 * @Title: ContentModel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ContentModel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 资讯ID -- 对应资讯表(Content)主键
	private String contentId = "";
	
	// 页号
	private int pageNo = 0;
	
	// 详细内容
	private String content = "";
	
	
	// 创建时间
	private String createTime = "";

	//虚拟的content实体类的name属性
	// 资讯名称
	private String contentName = "";
	

	public String getContentName()
	{
	    return contentName;
	}


	public void setContentName(String contentName)
	{
	    this.contentName = contentName;
	}


	public String getContentId() {
		return contentId;
	}


	public void setContentId(String contentId) {
		this.contentId = contentId;
	}


	public int getPageNo() {
		return pageNo;
	}


	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}