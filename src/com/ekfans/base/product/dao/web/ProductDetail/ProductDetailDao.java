package com.ekfans.base.product.dao.web.ProductDetail;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class ProductDetailDao extends GenericDao implements IProductDetailDao {
    public ProductDetailDao() throws HibernateException{
	super();
	this.beanClass = "com.ekfans.base.product.model.Product";
    }
}
