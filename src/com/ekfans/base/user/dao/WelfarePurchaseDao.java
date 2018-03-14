package com.ekfans.base.user.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 
* @ClassName: WelfarePurchaseDao
* @Description: TODO 福利采购dao实现
* @author ekfans
* @date 2014-11-21 上午9:32:43
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class WelfarePurchaseDao extends GenericDao implements IWelfarePurchaseDao {
	public WelfarePurchaseDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.user.model.WelfarePurchase";
	}
}
