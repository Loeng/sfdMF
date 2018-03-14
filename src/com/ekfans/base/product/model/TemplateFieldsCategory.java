package com.ekfans.base.product.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品模板扩展字段所属分类实体类
 * 
 * @Title: TemplateFieldsCategory.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class TemplateFieldsCategory extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 分类名称
	private String categoryName = "";
	
	// 分类说明
	private String categoryNote = "";
	
	//上级ID
	private String parentId = "";
	
	//状态
	private Boolean status = false;
	
	// 下级菜单
	private List<TemplateFieldsCategory> childList = new ArrayList<TemplateFieldsCategory>();
	
	// 模板扩展字段
	private List<TemplateFields> fields = new ArrayList<TemplateFields>();

	public List<TemplateFields> getFields() {
        return fields;
    }

    public void setFields(List<TemplateFields> fields) {
        this.fields = fields;
    }

    public List<TemplateFieldsCategory> getChildList() {
		return childList;
	}

	public void setChildList(List<TemplateFieldsCategory> childList) {
		this.childList = childList;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryNote() {
		return categoryNote;
	}

	public void setCategoryNote(String categoryNote) {
		this.categoryNote = categoryNote;
	}

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}