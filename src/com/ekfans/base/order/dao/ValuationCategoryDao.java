package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class ValuationCategoryDao extends GenericDao implements IValuationCategoryDao{
    public ValuationCategoryDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.order.model.ValuationCategory";
    }
}
