package com.ekfans.base.user.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 会员等级DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class UserLevelDao extends GenericDao implements IUserLevelDao {
	public UserLevelDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.user.model.UserLevel";
	}
}