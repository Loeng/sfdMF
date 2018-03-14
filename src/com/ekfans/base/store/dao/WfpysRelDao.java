package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 危废品运输标准与企业运输认证关系dao实现
 * @author Administrator
 *
 */
@Repository
public class WfpysRelDao extends GenericDao implements IWfpysRelDao {

	public WfpysRelDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.store.model.WfpysRel";
	}
}