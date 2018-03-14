package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 订单快照DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class OrderPhotoInfoDao extends GenericDao implements IOrderPhotoInfoDao {
	public OrderPhotoInfoDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.order.model.OrderPhotoInfo";
	}
}
