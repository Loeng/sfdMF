package com.ekfans.base.content.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * @ClassName: ContentRelDao  
 * @Description: TODO(实现) 
 * @Copyright: Copyright (c) 2016年7月28日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年7月28日上午11:23:52
 * @version 1.0
 */
@Repository
public class ContentRelDao extends GenericDao implements IContentRelDao{
	public ContentRelDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.content.model.ContentRel";
	}
}
