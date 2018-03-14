package com.ekfans.base.store.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IVehicleTypeDao;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.VehicleType;


/**
 * 车辆类型Service接口实现类
 * @ClassName VehicleTypeService
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月29日
 */
@SuppressWarnings("unchecked")
@Service
public class VehicleTypeService implements IVehicleTypeService {

	private Logger log = LoggerFactory.getLogger(VehicleTypeService.class);
	@Resource
	private IVehicleTypeDao vehicleTypeDao;
	
	@Override
	public List<VehicleType> getAllVehicleType() {
		
		StringBuffer hql = new StringBuffer(" from VehicleType as vt");
		try {
			List<VehicleType> list = vehicleTypeDao.list(hql.toString(), null);
			if (list != null&&list.size() > 0 ) {
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	  
	  

}