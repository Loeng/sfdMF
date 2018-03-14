package com.ekfans.base.product.dao.web.productList;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;
/**
 * 
* @ClassName: ProductListDao
* @Description: TODO(前台商品列表Dao)
* @author 成都易科远见可以有限公司
* @date 2014-5-19 上午10:28:24
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class ProductListDao extends GenericDao implements IProductListDao {
    public ProductListDao() throws HibernateException{
	super();
	this.beanClass = "com.ekfans.base.product.model.Product";
    }
}
