package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class StoreApplyDao extends GenericDao implements IStoreApplyDao{
    public StoreApplyDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.store.model.StoreApply";
    }
}
  
