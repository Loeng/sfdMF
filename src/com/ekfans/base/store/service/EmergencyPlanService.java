package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IEmergencyPlanDao;
import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.EmergencyPlan;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
public class EmergencyPlanService implements IEmergencyPlanService{
	private Logger log = LoggerFactory.getLogger(EmergencyPlanService.class);
	@Resource
	private IEmergencyPlanDao quaDao;
	@Resource
	private IStoreDao storeDao;
	
	
	@Override
	public boolean save(EmergencyPlan qua,HttpServletRequest request, 
			HttpServletResponse response) {
		Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
		if(qua == null || store == null){
			return false;
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = quaDao.createSession();
			transaction = session.beginTransaction();

			// 设置附件保存路径
			String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
			// 调用方法获取分类图标，返回图标路径
			String file = FileUploadHelper.uploadFile("file", currentPath, request, response);
			qua.setFile(file);
			qua.setStoreId(store.getId());
			qua.setCreateTime(DateUtil.getSysDateString());
			qua.setUpdateTime(DateUtil.getSysDateString());
			//如果类型为有效的话
			if(qua.getType().equals("1")){
				//查询企业id为那个的qua
				List<EmergencyPlan> list=getQuaByStoreId(store.getId());
				
				if(list.size()>0 && list!=null){
					//遍历集合
					for(int i=0;i<list.size();i++){
						EmergencyPlan q=list.get(i);
						//因为一个企业里当前有效的道路紧急预案只能有一个，
						//所以如果集合里面有有效的预案的话就把之前的那些有效的预案改成无效
						if(q.getType().equals("1")){
							q.setType("0");
							q.setUpdateTime(DateUtil.getSysDateString());
							quaDao.updateBean(q, session);
						}
					}
				}
				quaDao.saveOrUpdateBean(qua, session);
				
				//修改企业预案
				store.setEmergencyPlanId(qua.getId());
				store.setEmergencyPlanFile(qua.getFile());
				store.setUpdateTime(DateUtil.getSysDateString());
				
				storeDao.updateBean(store, session);
				
				session.flush();
				transaction.commit();
				session.close();
				return true;
			}
			quaDao.saveOrUpdateBean(qua, session);
			
			session.flush();
			transaction.commit();
			session.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (transaction != null) {
				transaction.rollback();
			}
		}finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}
	
	@Override
	public List<EmergencyPlan> getQuaByStoreId(Pager page, String startTime, String endTime, 
			HttpServletRequest request, HttpServletResponse response) {
		Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
		if(store == null){
			return null;
		}
		StringBuffer hql=new StringBuffer(" from EmergencyPlan c where c.storeId=:storeId ");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("storeId", store.getId());
		
		//动态查询  开始时间
		if(!StringUtil.isEmpty(startTime)){
			hql.append("and c.startTime >= :startTime");
			map.put("startTime", startTime);
		}
		//动态查询  结束时间
		if(!StringUtil.isEmpty(endTime)){
			hql.append("and c.endTime <= :endTime");
			map.put("endTime", endTime);
		}
		//根据创建时间排序
		hql.append(" order by c.createTime desc");
		try {
			return quaDao.list(page, hql.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean delete(String[] quaIds) {
		// 如果传过来的数组为空或者长度小于等于0，则返回失败
		if (quaIds == null || quaIds.length <= 0) {
			return false;
		}
		try {
			// 如果长度为1，则调用单个删除方法，并返回
			if (quaIds.length == 1) {
				return deleteById(quaIds[0]);
			}
			// 调用DAO方法批量删除
			quaDao.deleteByIds(quaIds);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean deleteById(String quaId) {
		// 如果传过来的ID为空，则返回失败
		if (StringUtil.isEmpty(quaId)) {
			return false;
		}
		try {
			// 调用DAO删除对象
			quaDao.deleteById(quaId);
			// 返回删除成功状态
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public EmergencyPlan getQuaById(String quaId) {
		try {
			if(!StringUtil.isEmpty(quaId)){
				return (EmergencyPlan) quaDao.get(quaId);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean updateBean(EmergencyPlan qua) {
		try {
			if(qua!=null){
				quaDao.updateBean(qua);
				
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<EmergencyPlan> getQuaByStoreId(String storeId) {
		if(StringUtil.isEmpty(storeId)){
			return null;
		}
		StringBuffer sb=new StringBuffer(" from EmergencyPlan q where q.storeId=:storeId");
		Map<String , Object> map=new HashMap<String, Object>();
		map.put("storeId",storeId);
		try {
			List<EmergencyPlan> list=quaDao.list(sb.toString(), map);
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean startUse(String quaId, HttpServletRequest request, 
			HttpServletResponse response) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = quaDao.createSession();
			transaction = session.beginTransaction();
			
			//获取登录企业
			Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
			//获取id为这个的qua
			EmergencyPlan qua=getQuaById(quaId);
			//获取企业id为这个的qua集合
			List<EmergencyPlan> list = getQuaByStoreId(store.getId());
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					EmergencyPlan qua1=list.get(i);
					//如果在企业id为这个企业id的qua集合里有状态为有效的qua，就把其状态设为无效
					if(qua1.getType().equals("1")){
						qua1.setType("0");
						qua1.setUpdateTime(DateUtil.getSysDateString());
						quaDao.updateBean(qua1, session);
					}
				}
			}
			//修改企业预案
			store.setEmergencyPlanId(quaId);
			store.setEmergencyPlanFile(qua.getFile());
			store.setUpdateTime(DateUtil.getSysDateString());
			
			storeDao.updateBean(store, session);
			
			//设置当前qua的状态为有效
			qua.setType("1");
			qua.setUpdateTime(DateUtil.getSysDateString());
			
			quaDao.updateBean(qua, session);
			
			session.flush();
			transaction.commit();
			session.close();
			
			return true;
		
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (transaction != null) {
				transaction.rollback();
			}
		}finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		return false;
	}
}
