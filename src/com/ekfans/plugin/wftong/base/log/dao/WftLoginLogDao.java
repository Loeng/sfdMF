package com.ekfans.plugin.wftong.base.log.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 用户登录信息Dao接口实现
 */
@Repository
public class WftLoginLogDao extends GenericDao implements IWftLoginLogDao {

	public WftLoginLogDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.plugin.wftong.base.log.model.WftLoginLog";
	}
}
