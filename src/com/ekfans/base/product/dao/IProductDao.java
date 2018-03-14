package com.ekfans.base.product.dao;

import java.util.List;

import com.ekfans.base.product.model.Product;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;

/**
 * 商品DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductDao extends IGenericDao {

	/**
	 * 获取web（前台）显示数据
	 * @param pager
	 * @param categoryId
	 * @param categoryRootId
	 * @param name
	 * @param minPrice
	 * @param maxPrice
	 * @param cjl 成交量
	 * @return
	 */
	public List<Product> getListWebShow(Pager pager, String categoryId, String categoryRootId, String name, String minPrice, String maxPrice ,boolean cjl);
}