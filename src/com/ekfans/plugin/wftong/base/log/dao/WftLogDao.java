package com.ekfans.plugin.wftong.base.log.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * Wft信息Dao接口实现
 */
@Repository
public class WftLogDao extends GenericDao implements IWftLogDao {

	public WftLogDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.plugin.wftong.base.log.model.WftLog";
	}
}
