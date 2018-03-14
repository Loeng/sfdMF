package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductInfoDao;
import com.ekfans.base.product.model.ProductInfo;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品扩展信息业务实现Service
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
public class ProductInfoService implements IProductInfoService {
	// 定义DAO
	@Autowired
	private IProductInfoDao productInfoDao;

	@Override
	public boolean addProductInfo(ProductInfo productInfo) {
		if (productInfo == null) {
			return false;
		}
		try {
			productInfoDao.addBean(productInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据productId取得productInfo集合
	 */
	@Override
	public List<ProductInfo> getProductInfoByProductId(String productId) {
		StringBuffer hql = new StringBuffer("from ProductInfo as pi where 1=1");

		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(productId)) {
			hql.append(" and pi.productId = :productId");
			map.put("productId", productId);
		}
		try {
			List<ProductInfo> list = productInfoDao.list(hql.toString(), map);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据productId取得productInfo集合
	 */
	@Override
	public List<ProductInfo> getProductInfoByProductId(String productId, Session session) {
		StringBuffer hql = new StringBuffer("from ProductInfo as pi where 1=1");

		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(productId)) {
			hql.append(" and pi.productId = :productId");
			map.put("productId", productId);
		}
		try {
			List<ProductInfo> list = productInfoDao.list(hql.toString(), map, session);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改操作
	 */
	@Override
	public boolean modifyProductInfo(ProductInfo productInfo) {
		if (productInfo == null) {
			return false;
		}
		try {
			productInfoDao.updateBean(productInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 通过传来的扩展字段类型查询出集合
	 */
	public List<ProductInfo> getProductInfoType(String productId, String infoType) {
		StringBuffer hql = new StringBuffer("from ProductInfo as pi where 1=1");

		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(productId)) {
			hql.append(" and pi.productId = :productId");
			map.put("productId", productId);
		}
		if (!StringUtil.isEmpty(infoType)) {
			hql.append(" and pi.infoType = :infoType");
			map.put("infoType", infoType);
		}
		try {
			List<ProductInfo> list = productInfoDao.list(hql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据商品id删除productInfo表数据
	 */
	public boolean getProductIdDelete(String productId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" delete pi from ProductInfo as pi where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 如果roleId不为空
		if (!StringUtil.isEmpty(productId)) {
			// 添加查询条件
			sql.append(" and pi.productId = :productId");
			paramMap.put("productId", productId);
		}
		try {
			productInfoDao.delete(sql.toString(), paramMap);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}