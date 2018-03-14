package com.ekfans.base.product.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.basic.hibernate.dao.GenericDao;

@Repository
public class productPictureDao extends GenericDao implements IProductPictureDao{
    public productPictureDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.product.model.ProductPicture";
    }
}
