package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreIntelDao;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreIntel;
import com.ekfans.base.store.model.StoreIntelAppendix;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 资质认证Service接口实现
 * 
 * @ClassName: StoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class StoreIntelService implements IStoreIntelService {
	
	private Logger log = LoggerFactory.getLogger(StoreIntelService.class);
	@Resource
	private IStoreIntelDao storeIntelDao;
	
	@Override
	public int updateOrSaveIntel(List<StoreIntel> silist, List<String> files) {
		// hibernate session
		Session session = null;
		// hibernate Transaction
		Transaction tran = null;
		
		try {
			// 获取session
			session = storeIntelDao.createSession();
			// 开启事务
			tran = session.beginTransaction();
			
			// 判断是否选择资质
			if(silist != null && silist.size() > 0){
				// 判断资质是否有资质证明文件
				if(files == null || files.size() <= 0){
					return 1;
				}
				
				String hql2 = "delete from StoreIntelAppendix where storeIntelId in (select si.id from StoreIntel si where si.storeId='" + silist.get(0).getStoreId() + "'"
						    + "and si.type='" + silist.get(0).getType() + "' and si.checkStatus<>'1')";
				storeIntelDao.delete(hql2, null, session);
				
				String hql1 = "delete from StoreIntel where storeId='" + silist.get(0).getStoreId() + "' and type='" + silist.get(0).getType() + "' and checkStatus<>'1'";
				storeIntelDao.delete(hql1, null, session);
				
				for (StoreIntel si : silist) {
					storeIntelDao.addBean(si, session);
					for (String str : files) {
						StoreIntelAppendix sia = new StoreIntelAppendix();
						sia.setStoreIntelId(si.getId());
						sia.setFile(str);
						storeIntelDao.addBean(sia, session);
					}
				}
			}
			
			tran.commit();
			session.flush();
			
			return 2;
		} catch (Exception e) {
			log.error(e.getMessage());
			if(tran != null){
				tran.rollback();
			}
			return 3;
		} finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> getUnauthenStoreList(Pager pager, String type, String storeName) {
		// 定义参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// hql语句
		StringBuffer hql = new StringBuffer("select s,u from Store s,User u,StoreIntel si where s.id=si.storeId and si.storeId=u.id and si.checkStatus<>:checkStatus");
		
		map.put("checkStatus", "1");
		if(!StringUtil.isEmpty(type)){
			hql.append(" and si.type=:type");
			map.put("type", type);
		}
		if(!StringUtil.isEmpty(storeName)){
			hql.append(" and s.storeName like :storeName");
			map.put("storeName", storeName);
		}
		
		try {
			// 查询
			List<Object[]> list = storeIntelDao.list(pager, hql.toString(), map);
			if(list != null && list.size() > 0){
				List<Store> slist = new ArrayList<Store>();
				
				// 去掉重复企业
				Map<String, Store> tempMap = new HashMap<String, Store>();
				for (Object[] obj : list) {
					Store store = (Store)obj[0];
					store.setUserEntity((User)obj[1]);
					tempMap.put(store.getId(), store);
				}
				for (String str : tempMap.keySet()) {
					slist.add(tempMap.get(str));
				}
				
				return slist;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreIntel> getUnZiZhi(String storeId, String type) {
		// 定义参数Map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		StringBuffer hql = new StringBuffer("select si,i.name from StoreIntel si,Intel i where si.intelId=i.id and si.checkStatus<>:checkStatus and si.type=:type and si.storeId=:storeId");
		
		params.put("checkStatus", "1");
		params.put("type", type);
		params.put("storeId", storeId);
		
		try {
			List<Object[]> list = storeIntelDao.list(hql.toString(), params);
			if(list != null && list.size() > 0){
				List<StoreIntel> silist = new ArrayList<StoreIntel>();
				for (Object[] obj : list) {
					StoreIntel si = (StoreIntel)obj[0];
					si.setIntelName(obj[1].toString());
					// 获取资质对应的证明文件
					String hql1 = "from StoreIntelAppendix where storeIntelId='" + si.getId() + "'";
					si.setSialist(storeIntelDao.list(hql1, null));
					
					silist.add(si);
				}
				
				return silist;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean checkIntel(String caoid, String ids) {
		if(StringUtil.isEmpty(ids)){
			return false;
		}
		
		Session session = null;
		Transaction tran = null;
		try {
			session = storeIntelDao.createSession();
			tran = session.beginTransaction();
			
			if(ids.indexOf(",") < 0){
				checkIntelUtil(caoid, ids, session);
			}else{
				String[] idsa = ids.split(",");
				for (String str : idsa) {
					checkIntelUtil(caoid, str, session);
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

	private void checkIntelUtil(String caoid, String ids, Session session) throws Exception {
		String[] st = ids.split("-");
		String hql = "update from StoreIntel set checkStatus='" + st[1] + "',checkTime='" + DateUtil.getSysDateTimeString() + "',checkMan='" + caoid + "' where id='" + st[0] + "'";
		storeIntelDao.updateBean(hql, null, session);
	}
}