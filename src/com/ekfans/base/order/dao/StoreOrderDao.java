package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 
* @ClassName: StoreOrderDao
* @Description: TODO(商户中心交易管理(订单)DAO实现)
* @author ekfans
* @date 2014-5-7 上午11:33:10
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class StoreOrderDao extends GenericDao implements IStoreOrderDao {
    public StoreOrderDao() throws HibernateException{
	super();
	this.beanClass = "com.ekfans.base.order.model.Order";
    }
}
