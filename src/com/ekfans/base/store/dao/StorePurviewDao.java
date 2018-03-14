package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 企业权限功能菜单DAO接口实现
 * 
 * @ClassName: StorePurviewDao
 * @author Lgy
 * @date 2014-01-06 上午11:35:38 
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class StorePurviewDao extends GenericDao implements IStorePurviewDao {
	
	public StorePurviewDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.store.model.StorePurview";
	}
}