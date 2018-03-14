package com.ekfans.base.system.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 
* @ClassName: ShopCartDao
* @Description: TODO(购物车的实现)
* @author ekfans
* @date 2014-5-19 上午11:32:24
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class ShopCartDao extends GenericDao implements IShopCartDao{
    public ShopCartDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.system.model.ShopCart";
    }
}
