package com.ekfans.base.order.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 供需关系DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: ekfans
 * @author lh
 * @date 2016年3月3日17:26:07
 * @version 1.0
 */
@Repository
public class SupplyBuyDao extends GenericDao implements ISupplyBuyDao {

	private Logger log = LoggerFactory.getLogger(SupplyBuy.class);

	public SupplyBuyDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.order.model.SupplyBuy";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SupplyBuy> listCommon(Pager pager, String title, String type, String status, String productType, Integer checkStatus) {
		// 定义参数Map
		Map<String, Object> params = new HashMap<String, Object>();
		// 定义hql
		StringBuffer hql = new StringBuffer("select s from SupplyBuy s where 1 = 1");
		// 条件
		if (!StringUtil.isEmpty(title)) {
			hql.append(" and s.title like :title");
			params.put("title", "%" + title + "%");
		}
		if (!StringUtil.isEmpty(type)) {
			hql.append(" and s.type = :type");
			params.put("type", type);
		}
		if (!StringUtil.isEmpty(status)) {
			hql.append(" and s.status = :status");
			params.put("status", status);
		}
		if (!StringUtil.isEmpty(productType)) {
			hql.append(" and s.productType = :productType");
			params.put("productType", productType);
		}
		if (checkStatus != null) {
			hql.append(" and s.checkStatus = :checkStatus");
			params.put("checkStatus", checkStatus);
		}
		hql.append(" order by s.createTime desc");

		try {
			List<SupplyBuy> supplys =  list(pager, hql.toString(), params);
			return supplys;
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return null;
	}

}
