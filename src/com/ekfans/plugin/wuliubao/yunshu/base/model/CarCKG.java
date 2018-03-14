package com.ekfans.plugin.wuliubao.yunshu.base.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 车辆的长宽高信息实体类
 * @author Administrator
 *
 */
@Entity
public class CarCKG extends BasicBean {

	private static final long serialVersionUID = 1L;
	
	private String carLength;
	private String carWidth;
	private String carHight;
	
	public String getCarLength() {
		return carLength;
	}
	public void setCarLength(String carLength) {
		this.carLength = carLength;
	}
	public String getCarWidth() {
		return carWidth;
	}
	public void setCarWidth(String carWidth) {
		this.carWidth = carWidth;
	}
	public String getCarHight() {
		return carHight;
	}
	public void setCarHight(String carHight) {
		this.carHight = carHight;
	}
	
	

}
