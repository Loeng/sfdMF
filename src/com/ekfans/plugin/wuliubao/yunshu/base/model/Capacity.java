package com.ekfans.plugin.wuliubao.yunshu.base.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 车辆载重量实体类
 * @author Administrator
 *
 */
@Entity
public class Capacity extends BasicBean {

	private static final long serialVersionUID = 1L;
	
	private String  wfpTotal;

	public String getWfpTotal() {
		return wfpTotal;
	}

	public void setWfpTotal(String wfpTotal) {
		this.wfpTotal = wfpTotal;
	}
	
	
}
