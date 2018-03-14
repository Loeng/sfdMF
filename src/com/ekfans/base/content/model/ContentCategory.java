package com.ekfans.base.content.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 内容分类实体类
 * 
 * @Title: ContentCategory.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-23
 * @version 1.0
 */
@Entity
public class ContentCategory extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 分类名称
	private String name = "";

	// 父分类ID
	private String parentId = "";
	// 关键字
	private String keyword = "";

	// 分类描述
	private String description = "";

	// 分类类型
	private String type = "";

	// 分类位置
	private int position = 0;

	// 分类图片
	private String icon = "";

	// 全路径
	private String fullPath = "";

	// 状态
	private boolean status = false;

	// 创建时间
	private String createTime = "";
	// 虚拟伪造的数据
	// 下级菜单
	private List<ContentCategory> childList = new ArrayList<ContentCategory>();
	// 分类下的资讯
	private List<Content> contents = new ArrayList<Content>();
	// 是否选中
	private Boolean checked = false;
	
	// --------ContentCategoryRel------------
	// 内容ID -- 对应资讯表(Content)主键
	private String contentId = "";

	// 分类ID
	private String categoryId = "";
	// -----content-----------
	// 资讯的ID
	private String cId = "";

	// 资讯名称
	private String contentName = "";

	// 创建时间
	private String creatTime = "";

	// 导航文字
	private String navigationText = "";

	// 导航图片
	private String navigationImage = "";

	// 简介
	private String introduction = "";

	private String linkUrl = "";

	
	public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getNavigationText() {
		return navigationText;
	}

	public void setNavigationText(String navigationText) {
		this.navigationText = navigationText;
	}

	public String getNavigationImage() {
		return navigationImage;
	}

	public void setNavigationImage(String navigationImage) {
		this.navigationImage = navigationImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<ContentCategory> getChildList() {
		return childList;
	}

	public void setChildList(List<ContentCategory> childList) {
		this.childList = childList;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getCId() {
		return cId;
	}

	public void setCId(String id) {
		cId = id;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}