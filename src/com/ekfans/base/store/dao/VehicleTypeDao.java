package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;


/**
 * 车辆类型DAO接口实现
 * @ClassName VehicleTypeDao
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月29日
 */
@Repository
public class VehicleTypeDao extends GenericDao implements IVehicleTypeDao {

	public VehicleTypeDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.store.model.VehicleType";
	}
}