package com.ekfans.base.store.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 
* @ClassName: StoreAppraiseDao
* @Description: TODO(商户 评价管理)
* @author 成都易科远见科技有限公司
* @date 2014-5-9 下午5:28:03
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class StoreAppraiseDao extends GenericDao implements IStoreAppraiseDao{
    
    public StoreAppraiseDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.order.model.Appraise";
    }

}
