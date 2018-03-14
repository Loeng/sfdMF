package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 合作机构Dao接口实现类
 * @ClassName CooperationDao
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月15日
 */
@Repository
public class CooperationDao extends GenericDao implements ICooperationDao{

	public CooperationDao() throws HibernateException{
		this.beanClass = "com.ekfans.base.store.model.Cooperation";
	}
	
}
