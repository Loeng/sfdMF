package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;
/**
 * 
* @ClassName: StoreConsultDao
* @Description: TODO(店铺咨询DAO的实现)
* @author ekfans
* @date 2014-5-4 上午11:02:14
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class StoreConsultDao extends GenericDao implements IStoreConsultDao {
    public StoreConsultDao() throws HibernateException{
	super();
	this.beanClass = "com.ekfans.base.store.model.Consult";
    }
}
