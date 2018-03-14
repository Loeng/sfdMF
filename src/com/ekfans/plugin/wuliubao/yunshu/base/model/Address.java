package com.ekfans.plugin.wuliubao.yunshu.base.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 用户地址信息实体
 * @author Administrator
 *
 */
@Entity
public class Address  extends BasicBean {

	private static final long serialVersionUID = 1L;
	//所属用户id
	private String userId;
	//省ID
	private String provinceId;
	//市id
	private String cityId;
	//区id 
	private String areaId;
	//省名称
	private String provinceName ;
	//市名称
	private String cityName ;
	//区名称
	private String areaName;
	//详细地址
	private String habitatAddress ;
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getHabitatAddress() {
		return habitatAddress;
	}
	public void setHabitatAddress(String habitatAddress) {
		this.habitatAddress = habitatAddress;
	}

	
}
