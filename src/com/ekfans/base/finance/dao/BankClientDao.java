package com.ekfans.base.finance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.base.finance.model.BankClient;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 银行客户DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Repository
@SuppressWarnings("unchecked")
public class BankClientDao extends GenericDao implements IBankClientDao {
	public BankClientDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.finance.model.BankClient";
	}

	@Override
	public List<BankClient> getBankClientsByParams(String storeId,
			String bankId, String type, String storeName,Pager pager) {
		// 定义参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// 定义hql语句
		StringBuffer hql = new StringBuffer(" from BankClient as bc where 1=1");
		
		// 判断并传入参数
		if(!StringUtil.isEmpty(storeId)){
			hql.append(" and bc.storeId = :storeId");
			params.put("storeId", storeId);
		}
		if(!StringUtil.isEmpty(bankId)){
			hql.append(" and bc.bankId = :bankId");
			params.put("bankId", bankId);
		}
		if(!StringUtil.isEmpty(type)){
			hql.append(" and bc.type = :type");
			params.put("type", type);
		}
		if(!StringUtil.isEmpty(storeName)){
			hql.append(" and bc.storeName like :storeName");
			params.put("storeName", "%" + storeName + "%");
		}
		hql.append(" order by bc.createTime desc");
		
		try {
			List<BankClient> bcs = this.list(pager, hql.toString(), params);
			if(bcs.size()>0){
				return bcs;
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public BankClient getByStoreIdAndBankId(String storeId, String bankId) {
		if (StringUtil.isEmpty(storeId) || StringUtil.isEmpty(bankId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from BankClient as bc where 1=1");
		sql.append(" and bc.bankId = :bankId");
		paramMap.put("bankId", bankId);
		sql.append(" and bc.storeId = :storeId");
		paramMap.put("storeId", storeId);

		try {
			List<BankClient> list = list(sql.toString(), paramMap);
			return list.get(0);
		} catch (Exception e) {
		}
		return null;
	}

}
