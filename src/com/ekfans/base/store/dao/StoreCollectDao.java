package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 商户收藏管理DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class StoreCollectDao extends GenericDao implements IStoreCollectDao {
	public StoreCollectDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.store.model.StoreCollect";
	}
}