package com.ekfans.base.user.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 会员积分 DAO接口 的实现
 * 
 * @ClassName: IUserIntegralDao
 * @author qxh
 * @date 2014-4-24 上午11:21:45
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class ViratualLogDao extends GenericDao implements IViratualLogDao{
	public ViratualLogDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.user.model.ViratualLog";
	}
}
