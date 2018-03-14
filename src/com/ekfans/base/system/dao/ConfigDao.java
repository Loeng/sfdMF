package com.ekfans.base.system.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 系统配置DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Repository
public class ConfigDao extends GenericDao implements IConfigDao {
	public ConfigDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.system.model.Config";
	}
}
