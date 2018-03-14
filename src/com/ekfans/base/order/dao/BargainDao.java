package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 议价dao接口实现类
 * @ClassName BargainDao
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月16日
 */
@Repository
public class BargainDao extends GenericDao implements IBargainDao{

	public BargainDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.order.model.Bargain";
	}
}
