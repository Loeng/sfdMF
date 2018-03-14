package com.ekfans.base.product.model;

import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品模板扩展字段实体类
 * 
 * @Title: TemplateFields.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class TemplateFields extends BasicBean {
	// 序列化
	private static final long serialVersionUID = 1L;

	// 模版ID
	private String tempId = "";

	// 扩展字段名称
	private String fieldName = "";

	// 扩展字段值
	private String fieldValue = "";

	// 扩展字段类型
	private String fieldType = "";

	// 前台是否可搜索
	private boolean search = false;

	// 扩展字段所属分类
	private String fieldCategory = "";

	// 是否为可选字段
	private boolean commons = false;

	// 排序位置
	private int position = 0;
	// 虚拟数据。放的是扩张字段值拆分以后的第一个String数组--------wsj-------
	private List<String> fieldValueList;
	// 定义选中的权限
	private boolean checked = false;
	private int iftype;

	public int getIftype() {
		return iftype;
	}

	public void setIftype(int iftype) {
		this.iftype = iftype;
	}

	public List<String> getFieldValueList() {
		return fieldValueList;
	}

	public void setFieldValueList(List<String> fieldValueList) {
		this.fieldValueList = fieldValueList;
	}

	//
	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

	public String getFieldCategory() {
		return fieldCategory;
	}

	public void setFieldCategory(String fieldCategory) {
		this.fieldCategory = fieldCategory;
	}

	public boolean isCommons() {
		return commons;
	}

	public void setCommons(boolean commons) {
		this.commons = commons;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}