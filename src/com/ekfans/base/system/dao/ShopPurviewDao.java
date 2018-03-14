package com.ekfans.base.system.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 系统权限菜单DAO接口实现
 * 
 * @ClassName: ShopPurviewDao
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class ShopPurviewDao extends GenericDao implements IShopPurviewDao {
	
	public ShopPurviewDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.system.model.ShopPurview";
	}
}
