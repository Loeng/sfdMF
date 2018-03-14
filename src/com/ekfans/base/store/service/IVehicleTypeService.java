package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.VehicleType;



/**
 * 车辆类型Service接口
 * @ClassName IVehicleTypeService
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月29日
 */
public interface IVehicleTypeService {

	/**
	 * 得到所有的车辆类型
	 * @return
	 */
	public List<VehicleType> getAllVehicleType();
}