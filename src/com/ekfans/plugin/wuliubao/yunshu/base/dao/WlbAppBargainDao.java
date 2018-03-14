package com.ekfans.plugin.wuliubao.yunshu.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.base.order.model.Bargain;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
@Repository
public class WlbAppBargainDao extends GenericDao implements IWlbAppBargainDao {
	public WlbAppBargainDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.order.model.Bargain";
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Bargain> getBargain(String buyerId,String sellerId,String sourceId,Pager page) throws Exception {
		StringBuffer sql = new StringBuffer(" from Bargain  where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(buyerId)){
			sql.append(" and buyerId = :buyerId");
			paramMap.put("buyerId", buyerId);
		}
		if(!StringUtil.isEmpty(sourceId)){
		sql.append(" and sourceId = :sourceId");
		paramMap.put("sourceId", sourceId);
		}
		if(!StringUtil.isEmpty(sellerId)){
			sql.append(" and sellerId = :sellerId");
			paramMap.put("sellerId", sellerId);
		}
		// 根据需求排序
		sql.append(" order by updateTime desc");
		List<Bargain> bargain=null;
		if(page!=null){
			bargain = (List<Bargain>) super.list(page,sql.toString(), paramMap);
			return bargain;
		}
		bargain = (List<Bargain>) super.list(sql.toString(), paramMap);
		return bargain;
	}

}
