package com.ekfans.base.wfOrder.dao;

import com.ekfans.basic.hibernate.dao.GenericDao;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @version 1.0
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 */
@Repository
public class WfOrderJiudingPayRelDao extends GenericDao implements IWfOrderJiudingPayRelDao {
    public WfOrderJiudingPayRelDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.wfOrder.model.WfOrderJiudingPayRel";
    }
}