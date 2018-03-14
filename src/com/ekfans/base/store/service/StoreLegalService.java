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

import com.ekfans.base.store.dao.IStoreLegalDao;
import com.ekfans.base.store.model.StoreLegal;
import com.ekfans.base.store.model.StoreLegalResume;
import com.ekfans.pub.util.StringUtil;

/**
 * 企业法人信息Service接口实现
 * 
 * @ClassName: StoreLegalService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class StoreLegalService implements IStoreLegalService {
	
	private Logger log = LoggerFactory.getLogger(StoreLegalService.class);
	@Resource
	private IStoreLegalDao storeLegalDao;

	@Override
	public StoreLegal getStoreLegalById(String id) {
		if(StringUtil.isEmpty(id)){
			return null;
		}
		
		try {
			return (StoreLegal)storeLegalDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean updateOrSaveStoreLegal(StoreLegal sl, List<StoreLegalResume> slrlist) {
		Session session = null;
		Transaction tran = null;
		
		try {
			session = storeLegalDao.createSession();
			tran = session.beginTransaction();
			
			Map<String, Object> map = new HashMap<String, Object>();
			String hql2 = "delete from StoreLegalResume where storeId=:id";
			String hql3 = "delete from StoreLegal where id=:id";
			String hql4 = "update Store set bankStatus='1' where id=:id";
			map.put("id", sl.getId());
			
			storeLegalDao.delete(hql2, map, session); // 删除法人简历信息
			storeLegalDao.delete(hql3, map, session); // 删除法人基础信息
			storeLegalDao.updateBean(hql4, map, session); // 更新企业银行认证状态
			storeLegalDao.addBean(sl, session); // 新增法人基础信息
			// 更新法人简历信息
			if(slrlist != null && slrlist.size() > 0){
				for (StoreLegalResume slr : slrlist) {
					slr.setStoreId(sl.getId());
					storeLegalDao.addBean(slr, session);
				}
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
