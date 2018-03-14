package com.ekfans.base.loan.dao;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 贷款申请DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2016-1-3
 * @version 1.0
 */
@Repository
public class LoanAppDao extends GenericDao implements ILoanAppDao {
	public LoanAppDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.loan.model.LoanApp";
	}

	public static Logger log = LoggerFactory.getLogger(LoanAppDao.class);

}
