package com.ekfans.base.channel.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 频道配置DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository 
public class ChannelConfigDao extends GenericDao implements IChannelConfigDao {
	public ChannelConfigDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.channel.model.ChannelConfig";
	}
}
