package com.ekfans.base.product.dao.web.storeList;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;
@Repository
public class StoreListDao extends GenericDao implements IStoreListDao {
    public StoreListDao() throws HibernateException{
	super();
	this.beanClass = "com.ekfans.base.store.model.Store";
    }
}
