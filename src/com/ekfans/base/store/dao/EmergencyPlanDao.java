package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class EmergencyPlanDao extends GenericDao implements IEmergencyPlanDao{
	public EmergencyPlanDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.store.model.EmergencyPlan";
	}
}
