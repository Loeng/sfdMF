package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 订单DAO接口实现
 * 
 * @ClassName: OrderDao
 * @Description:
 * @author lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class OrderLogDao extends GenericDao implements IOrderLogDao {

	public OrderLogDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.order.model.OrderLog";
	}
}
