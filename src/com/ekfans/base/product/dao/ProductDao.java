package com.ekfans.base.product.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.base.product.model.Product;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class ProductDao extends GenericDao implements IProductDao {
	private Logger log = LoggerFactory.getLogger(ProductDao.class);

	public ProductDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.product.model.Product";
	}

	@Override
	public List<Product> getListWebShow(Pager pager, String categoryId, String categoryRootId, String name, String minPrice, String maxPrice, boolean cjl) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select p.id,p.name,p.unitPrice,p.pfPrice,p.quantity,p.status,p.checkStatus,p.visitCount,p.buyCount,s.storeName ,p.productNumber,p.recommendPicture4,p.habitatAddress,p.productModel,p.note,p.unit,p.isAdvance,p.sortName,p.centerPicture from Product as p , Store as s,ProductCategory as pc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.storeId = s.id");
		sql.append(" and p.mainCategory = pc.id");
		sql.append(" and p.checkStatus = 1");
		sql.append(" and p.status = 1");
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and p.name like :name");
			paramMap.put("name", "%" + name + "%");
		}
		if (!StringUtil.isEmpty(minPrice) && !StringUtil.isEmpty(maxPrice)) {
			sql.append(" and p.unitPrice  BETWEEN :minUnitPrice AND :maxUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(minPrice));
			paramMap.put("maxUnitPrice", new BigDecimal(maxPrice));
		} else if (!StringUtil.isEmpty(minPrice) && StringUtil.isEmpty(maxPrice)) {
			sql.append(" and p.unitPrice >= :minUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(minPrice));
		} else if (StringUtil.isEmpty(minPrice) && !StringUtil.isEmpty(maxPrice)) {
			sql.append(" and p.unitPrice <= :maxUnitPrice");
			paramMap.put("maxUnitPrice", new BigDecimal(maxPrice));
		}
		if (!StringUtil.isEmpty(categoryId)) {
			// 关联商品分类
			sql.append(" and pc.fullPath like :categoryId");
			paramMap.put("categoryId", "%" + categoryId + "%");
		} else if (!StringUtil.isEmpty(categoryRootId)) {
			sql.append(" and pc.fullPath like :categoryId");
			paramMap.put("categoryId", "%" + categoryRootId + "%");
		} else {
			return null;
		}
		sql.append(" order by p.id desc");
		// 是否根据成交量排序
		if (cjl) {
			sql.append(",p.cjl desc");
		}
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = list(pager, sql.toString(), paramMap);
			List<Product> products = new ArrayList<Product>();
			for (Object[] object : list) {
				Product product = new Product();
				product.setId((String) object[0]);
				product.setName((String) object[1]);
				product.setUnitPrice((BigDecimal) object[2]);
				product.setListPrice((BigDecimal) object[3]);
				product.setQuantity((Integer) object[4]);
				product.setStatus((Boolean) object[5]);
				product.setCheckStatus((Integer) object[6]);
				product.setVisitCount((Integer) object[7]);
				product.setBuyCount((Integer) object[8]);
				product.setStoreId((String) object[9]);
				product.setProductNumber((String) object[10]);
				product.setSmallPicture((String) object[11]);
				product.setHabitatAddress((String) object[12]);
				product.setProductModel((String) object[13]);
				product.setNote((String) object[14]);
				product.setUnit((String) object[15]);
				product.setIsAdvance((String) object[16]);
				product.setSortName(null != object[17] && !("").equals(object[17]) ? (String) object[17] : "");
				product.setCenterPicture((String) object[18]);
				products.add(product);
			}
			return products;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
}