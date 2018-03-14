package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.ICreditEstimatesDao;
import com.ekfans.base.store.model.CreditEstimates;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 信用测算信息Service接口实现
 * 
 * @ClassName: CreditEstimatesService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class CreditEstimatesService implements ICreditEstimatesService {
	
	private Logger log = LoggerFactory.getLogger(CreditEstimatesService.class);
	@Resource
	private ICreditEstimatesDao creditEstimatesDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<CreditEstimates> getCeList(Pager pager, Integer status, String storeName, String type) {
		// param_data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		StringBuilder hql = new StringBuilder("select ce,s.storeName from CreditEstimates ce,Store s,User u where ce.companyId=s.id and ce.companyId=u.id and u.type=:type");
		params.put("type", type);
		
		if(status != -1){
			hql.append(" and ce.creditStatus=:creditStatus");
			params.put("creditStatus", status);
		}
		if(!StringUtil.isEmpty(storeName)){
			hql.append(" and s.storeName like :storeName");
			params.put("storeName", "%" + storeName + "%");
		}
		hql.append(" order by ce.creditStatus asc");
		
		try {
			List<Object[]> list = this.creditEstimatesDao.list(pager, hql.toString(), params);
			if(list == null || list.size() < 0){
				return null;
			}
			
			List<CreditEstimates> celist = new ArrayList<CreditEstimates>();
			for (Object[] obj : list) {
				CreditEstimates ce = (CreditEstimates)obj[0];
				ce.setStoreName(obj[1].toString());
				
				celist.add(ce);
			}
			
			return celist;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CreditEstimates getCreditEstimates(String id) {
		if(StringUtil.isEmpty(id)){
			return null;
		}
		
		// param_data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		String hql = "select ce,s.storeName from CreditEstimates ce,Store s where ce.companyId=s.id and ce.id=:id";
		// setting data
		params.put("id", id);
		
		try {
			List<Object[]> list = this.creditEstimatesDao.list(hql, params);
			if(list == null || list.size() <= 0){
				return null;
			}
			
			Object[] obj = list.get(0);
			CreditEstimates ce = (CreditEstimates)obj[0];
			ce.setStoreName(obj[1].toString());
			
			return ce;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean auCreditInfo(String id, String note) {
		try {
			CreditEstimates ce = (CreditEstimates)this.creditEstimatesDao.get(id);
			ce.setCreditStatus(2);
			ce.setCreditResult(note);
			this.creditEstimatesDao.updateBean(ce);
			
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}
}