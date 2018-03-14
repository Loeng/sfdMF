package com.ekfans.base.user.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 合作银行DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Repository
public class BankDao extends GenericDao implements IBankDao {
	public BankDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.user.model.Bank";
	}
}
