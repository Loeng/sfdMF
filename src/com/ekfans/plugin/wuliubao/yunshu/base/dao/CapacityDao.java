package com.ekfans.plugin.wuliubao.yunshu.base.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class CapacityDao extends GenericDao implements ICapacityDao {

	public CapacityDao() throws HibernateException {
		this.beanClass = "com.ekfans.plugin.wuliubao.yunshu.base.model.Capacity";
	}
}
