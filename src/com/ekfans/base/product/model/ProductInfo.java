package com.ekfans.base.product.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品扩展信息实体类
 * 
 * @Title: ProductInfo.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ProductInfo extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 商品ID
	private String productId = "";

	// 模板明细ID
	private String templateFieldId = "";

	// 扩展信息名
	private String infoName = "";

	// 扩展信息值
	private String infoValue = "";

	// 扩展信息类型 0,不可选;1,可选
	private String infoType = "";

	// 模板分类id
	private String categoryId = "";

	// 模板分类名
	private String categoryName = "";

	// 显示位置 (目前显示位置1、2、3、4为可选项;不可选项的显示位置还未管)
	private int position = 0;

	// 显示用(当扩展字段类型为可选时、将扩展字段值查分并存储在这个集合)
	private List<String> infoValueList = null;
	
	// 显示用(当扩展字段类型为可选时、将扩展字段对应的图片存储在这个集合)
    private List<String> picList = null;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTemplateFieldId() {
		return templateFieldId;
	}

	public void setTemplateFieldId(String templateFieldId) {
		this.templateFieldId = templateFieldId;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getInfoValue() {
		return infoValue;
	}

	public void setInfoValue(String infoValue) {
		this.infoValue = infoValue;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<String> getInfoValueList() {
		if ((infoValueList == null || infoValueList.size() <= 0) && !StringUtil.isEmpty(this.infoValue)) {
			List<String> returnList = new ArrayList<String>();
			String[] values = this.infoValue.split(";");
			for (int i = 0; i < values.length; i++) {
				returnList.add(values[i]);
			}
			return returnList;
		}
		return infoValueList;
	}

	public void setInfoValueList(List<String> infoValueList) {
		this.infoValueList = infoValueList;
	}

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}