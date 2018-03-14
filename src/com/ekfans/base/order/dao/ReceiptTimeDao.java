package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 收货时间区间DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class ReceiptTimeDao extends GenericDao implements IReceiptTimeDao {
	public ReceiptTimeDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.order.model.ReceiptTime";
	}
}