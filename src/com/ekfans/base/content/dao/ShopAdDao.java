package com.ekfans.base.content.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.base.content.model.ShopAd;
import com.ekfans.base.order.model.Bargain;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.StringUtil;

/**
 * 广告DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Repository
public class ShopAdDao extends GenericDao implements IShopAdDao {
	public ShopAdDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.content.model.ShopAd";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShopAd> getConfiguredList() throws Exception {
		StringBuffer sql = new StringBuffer(" from ShopAd where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		sql.append(" and isConfigure = :isConfigure");
		paramMap.put("isConfigure", "1");
		List<ShopAd> bargain=null;
		bargain = (List<ShopAd>) super.list(sql.toString(), paramMap);
		return bargain;
	}
}
