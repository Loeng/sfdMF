package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductPriceDao;
import com.ekfans.base.product.model.ProductPrice;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品价格区间业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class ProductPriceService implements IProductPriceService {
	// 定义一个错误日志文件
	public static Logger log = LoggerFactory.getLogger(ProductPriceService.class);
	// 定义DAO
	@Autowired
	private IProductPriceDao productPriceDao;

	/**
	 * 根据商品价格区间ID获取对象
	 * 
	 * @param priceId
	 * @return
	 */
	public ProductPrice getProductPrice(String priceId) {
		// 如果传进来的ID为空，则反悔NULL
		if (StringUtil.isEmpty(priceId)) {
			return null;
		}
		// 调用DAO方法查询并返回
		try {
			return (ProductPrice) productPriceDao.get(priceId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据商品ID获取该商品的价格区间集合
	 * 
	 * @param ProductId
	 * @return
	 */
	public List<ProductPrice> getProductPriceByProductId(String productId) {
		// 如果传进来的商品ID为空，则返回NULL
		if (StringUtil.isEmpty(productId)) {
			return null;
		}

		// 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL，按照排序的升序排列
		StringBuffer sql = new StringBuffer("select price from ProductPrice as price where price.productId = :productId order by price.position asc");
		// 将商品ID放入参数MAP
		paramMap.put("productId", productId);
		try {
			// 调用DAO查询sql并返回数据
			List<ProductPrice> returnList = productPriceDao.list(sql.toString(), paramMap);
			return returnList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据商品ID删除该商品所有价格区间
	 * 
	 * @param ProductId
	 * @return
	 */
	public boolean removeAllPriceByProductId(String productId) {
		// 如果传进来的商品ID为空，则返回FALSE
		if (StringUtil.isEmpty(productId)) {
			return false;
		}

		// 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer("from ProductPrice as price where price.productId = :productId ");
		// 将商品ID放入参数MAP
		paramMap.put("productId", productId);

		try {
			// 调用DAO查询sql并返回数据
			productPriceDao.delete(sql.toString(), paramMap);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 根据商品ID删除该商品所有价格区间(带事物处理)
	 * 
	 * @param ProductId
	 * @return
	 */
	public boolean removeAllPriceByProductId(String productId, Session session) {
		// 如果传进来的商品ID为空，则返回FALSE
		if (StringUtil.isEmpty(productId)) {
			return false;
		}

		// 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer("from ProductPrice as price where price.productId = :productId ");
		// 将商品ID放入参数MAP
		paramMap.put("productId", productId);

		try {
			// 调用DAO查询sql并返回数据
			productPriceDao.delete(sql.toString(), paramMap, session);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
}