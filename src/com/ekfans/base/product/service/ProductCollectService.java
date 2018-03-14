package com.ekfans.base.product.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductCollectDao;
import com.ekfans.base.product.model.ProductCollect;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品收藏管理业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProductCollectService implements IProductCollectService {
	// 定义DAO
	@Autowired
	private IProductCollectDao productCollectDao;

	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(ProductService.class);

	/**
	 * 根据id删除
	 */
	public boolean delete(String id) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			// 调用SERVICE的方法删除店铺
			productCollectDao.deleteById(id);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 得到对应的商品收藏集合
	 */
	public List<ProductCollect> getList(Pager pager, String userId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select pc.id,p.id,p.picture,p.description,p.unitPrice,s.storeName,p.name,p.sortName from Product as p ,ProductCollect as pc,Store as s where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.id = pc.productId");
		// 当品牌的对应店铺id和店铺id对应
		sql.append(" and p.storeId = s.id");
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and pc.userId = :userId");
			paramMap.put("userId", userId);
		}
		sql.append(" order by s.id desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = productCollectDao.list(pager, sql.toString(), paramMap);
			List<ProductCollect> productCollects = new ArrayList<ProductCollect>();
			for (Object[] object : list) {

				ProductCollect productCollect = new ProductCollect();
				productCollect.setId((String) object[0]);
				productCollect.setProductId((String) object[1]);
				productCollect.setPicture((String) object[2]);
				productCollect.setDescription((String) object[3]);
				productCollect.setUnitPrice((BigDecimal) object[4]);
				productCollect.setName((String) object[7]);
				productCollect.setProductName((String) object[6]);
				productCollects.add(productCollect);
			}
			return productCollects;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 添加
	 */
	public boolean add(ProductCollect pc) {
		try {
			pc.setCollectTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法添加商品
			productCollectDao.addBean(pc);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据productId查询
	 */
	public ProductCollect getProductCollectByProductId(String productId, String userId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select pc from ProductCollect as pc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and pc.userId = :userId");
			paramMap.put("userId", userId);
		}
		// 如果为空直接返回空
		if (!StringUtil.isEmpty(productId)) {
			sql.append(" and pc.productId = :productId");
			paramMap.put("productId", productId);
		}
		try {
			List<ProductCollect> list = productCollectDao.list(sql.toString(), paramMap);
			if (list.size() >= 0 && list != null) {
				return list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据id得到对象
	 */
	public ProductCollect getProductCollectById(String id) {
		try {
			return (ProductCollect) productCollectDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 得到购物车总的商品数量
	 */
	public String getProductCollectSum(String userId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("select count(pc.id) from ProductCollect as pc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and pc.userId = :userId");
			paramMap.put("userId", userId);
		}
		try {
			List list = productCollectDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				String object = list.get(0) + "";
				return object;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

}