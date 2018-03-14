package com.ekfans.base.order.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 
* @ClassName: RefundDao
* @Description: TODO(退货管理的dao的实现)
* @author Eugene
* @date 2014-5-12 下午4:49:05
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class RefundDao extends GenericDao implements IRefundDao{

    public RefundDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.order.model.Refund";
    }
    
}
