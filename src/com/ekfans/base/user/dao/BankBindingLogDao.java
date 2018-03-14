package com.ekfans.base.user.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
@Repository
public class BankBindingLogDao extends GenericDao implements IBankBindingLogDao {
    public BankBindingLogDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.user.model.BankBindingLog";
    }
}
