package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 运输企业运输车辆罐体材质
 * 
 * @ClassName: TankMaterial
 * @author ekfans
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class TankMaterial extends BasicBean {

	private static final long serialVersionUID = 1L;
	//罐体材质名称
	private String name;
	//车辆类型id
	private String vehicleTypeId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVehicleTypeId() {
		return vehicleTypeId;
	}
	public void setVehicleTypeId(String vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}

	
}