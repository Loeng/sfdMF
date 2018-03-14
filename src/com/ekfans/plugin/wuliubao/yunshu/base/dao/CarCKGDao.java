package com.ekfans.plugin.wuliubao.yunshu.base.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class CarCKGDao extends GenericDao implements ICarCKGDao {

	public CarCKGDao() throws HibernateException {
		this.beanClass = "com.ekfans.plugin.wuliubao.yunshu.base.model.CarCKG";
	}
}
