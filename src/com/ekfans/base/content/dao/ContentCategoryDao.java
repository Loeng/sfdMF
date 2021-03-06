package com.ekfans.base.content.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 内容分类DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class ContentCategoryDao extends GenericDao implements IContentCategoryDao {
	public ContentCategoryDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.content.model.ContentCategory";
	}
}