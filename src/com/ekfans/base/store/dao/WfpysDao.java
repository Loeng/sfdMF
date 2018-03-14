package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class WfpysDao extends GenericDao implements IWfpysDao{
    public WfpysDao() throws HibernateException {
        this.beanClass = "com.ekfans.base.store.model.Wfpys";
    }
}
