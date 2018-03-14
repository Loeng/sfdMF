package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 转发Dao接口实现
 * 
 * @Title: SupplyDao
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author yx
 * @date 2015年7月6日
 * @version 1.0
 */
@Repository
public class AppForwardingDao extends GenericDao implements IAppForwardingDao {

	public AppForwardingDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.supply.model.AppForwarding";
	}
}
