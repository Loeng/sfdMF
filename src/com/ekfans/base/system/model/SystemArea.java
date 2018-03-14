package com.ekfans.base.system.model;

import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 地区实体类
 * 
 * @Title: SystemArea.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class SystemArea extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 地区名称
	private String areaName = "";

	// 地区英文名
	private String areaEnName = "";

	// 地区简称
	private String areaAbbreviation = "";

	// 邮政编码
	private String zipCode = "";

	// 电话前缀
	private String areaCode = "";

	// 上级地区ID
	private String parentCode = "";

	private String pingYin = "";

	// 下级地区合集
	private List<SystemArea> childList = null;

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaEnName() {
		return areaEnName;
	}

	public void setAreaEnName(String areaEnName) {
		this.areaEnName = areaEnName;
	}

	public String getAreaAbbreviation() {
		return areaAbbreviation;
	}

	public void setAreaAbbreviation(String areaAbbreviation) {
		this.areaAbbreviation = areaAbbreviation;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public List<SystemArea> getChildList() {
		return childList;
	}

	public void setChildList(List<SystemArea> childList) {
		this.childList = childList;
	}

	public String getPingYin() {
		return pingYin;
	}

	public void setPingYin(String pingYin) {
		this.pingYin = pingYin;
	}

}