package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class OrderValuationDao extends GenericDao implements IOrderValuationDao{
    public OrderValuationDao() throws HibernateException {
        this.beanClass = "com.ekfans.base.order.model.OrderValuation";
    }
}
