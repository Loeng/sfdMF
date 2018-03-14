package com.ekfans.plugin.wftong.base.easemob.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 
 * ClassName: GroupsDao 
 * @Description: TODO
 * @author chengm
 * @date 2015年7月28日
 */
@Repository
public class GroupsDao extends GenericDao implements IGroupsDao{
	public GroupsDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.plugin.wftong.base.easemob.model.Groups";
	}

}
