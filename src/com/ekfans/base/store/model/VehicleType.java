package com.ekfans.base.store.model;

import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 车辆类型表实体类: 铝合金罐车、不锈钢罐车、压力罐车、铁罐车、压力罐车、仓栏危化品车、危化品车冷藏车、箱式板车
 * @ClassName VehicleType
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月29日
 */
public class VehicleType extends BasicBean{

	private static final long serialVersionUID = 1L;
	
	// 车辆类型名
	private String name;
	private List<TankMaterial> childList=null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<TankMaterial> getChildList() {
		return childList;
	}

	public void setChildList(List<TankMaterial> childList) {
		this.childList = childList;
	}

}
