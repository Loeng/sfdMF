package com.ekfans.base.finance.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 银行客户日志DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Repository
public class BankClientLogDao extends GenericDao implements IBankClientLogDao {
	public BankClientLogDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.finance.model.BankClientLog";
	}
}
