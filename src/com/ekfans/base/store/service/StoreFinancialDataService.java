package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreFinancialDataDao;
import com.ekfans.base.store.model.StoreFinancialData;

/**
 * 企业近期财务数据Service接口实现
 * 
 * @ClassName: StoreFinancialDataService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class StoreFinancialDataService implements IStoreFinancialDataService {
	
	private Logger log = LoggerFactory.getLogger(StoreFinancialDataService.class);
	@Resource
	private IStoreFinancialDataDao storeFinancialDataDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreFinancialData> getStoreFinancialDataByStoreId(String storeId) {
		// param data
		Map<String, Object> param = new HashMap<String, Object>();
		// hql
		String hql = "from StoreFinancialData where storeId=:storeId";
		// setting data
		param.put("storeId", storeId);
		
		try {
			return storeFinancialDataDao.list(hql, param);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}

	@Override
	public boolean updateOrSaveStoreFinancialData(List<StoreFinancialData> sfdlist) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql1 = "delete from StoreFinancialData where storeId=:storeId";
		String hql2 = "update Store set bankStatus='1' where id=:id";
		if(sfdlist != null && sfdlist.size() > 0){
			map.put("storeId", sfdlist.get(0).getStoreId());
			map.put("id", sfdlist.get(0).getStoreId());
		}
		
		Session session = null;
		Transaction tran = null;
		
		try {
			session = storeFinancialDataDao.createSession();
			tran = session.beginTransaction();
			
			storeFinancialDataDao.updateBean(hql2, map);
			storeFinancialDataDao.delete(hql1, map, session);
			for (StoreFinancialData sfd : sfdlist) {
				storeFinancialDataDao.addBean(sfd, session);
			}
			tran.commit();
			session.flush();
			
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			if(tran != null){
				tran.rollback();
			}
		} finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		return false;
	}
}