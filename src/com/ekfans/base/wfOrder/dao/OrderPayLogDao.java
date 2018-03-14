package com.ekfans.base.wfOrder.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 店铺自定义页面DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class OrderPayLogDao extends GenericDao implements IOrderPayLogDao {
	public OrderPayLogDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.wfOrder.model.OrderPayLog";
	}
}