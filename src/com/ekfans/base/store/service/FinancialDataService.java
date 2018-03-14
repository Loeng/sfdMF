package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IFinancialDataDao;
import com.ekfans.base.store.model.FinancialData;

/**
 * 企业财务信息Service接口实现
 * 
 * @ClassName: FinancialDataService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class FinancialDataService implements IFinancialDataService {
	
	private Logger log = LoggerFactory.getLogger(FinancialDataService.class);
	@Resource
	private IFinancialDataDao financialDataDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<FinancialData> getFinancialDataByStoreId(String storeId) {
		// param data
		Map<String, Object> param = new HashMap<String, Object>();
		// hql
		String hql = "from FinancialData where companyId=:storeId";
		// setting data
		param.put("storeId", storeId);
		
		try {
			return this.financialDataDao.list(hql, param);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
}