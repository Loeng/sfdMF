package com.ekfans.base.product.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 商品价格修改DAO接口的实现
 * @author Administrator
 *
 */
@Repository
public class ProductPriceChangeDao extends GenericDao implements IProductPriceChangeDao {
	public ProductPriceChangeDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.product.model.ProductPriceChange";
	}
}