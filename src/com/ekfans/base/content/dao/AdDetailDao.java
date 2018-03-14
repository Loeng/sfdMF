package com.ekfans.base.content.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import com.ekfans.base.content.model.AdDetail;
import com.ekfans.basic.hibernate.dao.GenericDao;

/**
 * 广告明细DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class AdDetailDao extends GenericDao implements IAdDetailDao {
	public AdDetailDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.content.model.AdDetail";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdDetail> getListAdDetail(String adId) throws Exception {
		StringBuffer sql = new StringBuffer(" from AdDetail where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		sql.append(" and adId = :adId");
		paramMap.put("adId", adId);
		List<AdDetail> bargain=null;
		bargain = (List<AdDetail>) super.list(sql.toString(), paramMap);
		return bargain;
	}
	
	
}
