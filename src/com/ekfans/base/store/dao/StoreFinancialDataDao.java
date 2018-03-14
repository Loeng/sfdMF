package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 企业近期财务数据DAO接口实现
 * 
 * @ClassName: StoreFinancialDataDao
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class StoreFinancialDataDao extends GenericDao implements IStoreFinancialDataDao {
	
	public StoreFinancialDataDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.store.model.StoreFinancialData";
	}
}
