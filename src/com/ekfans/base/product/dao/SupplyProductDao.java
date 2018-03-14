package com.ekfans.base.product.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class SupplyProductDao  extends GenericDao implements ISupplyProductDao{
    
    public SupplyProductDao() throws HibernateException {
        this.beanClass = "com.ekfans.base.product.model.SupplyProduct";
    }
}
