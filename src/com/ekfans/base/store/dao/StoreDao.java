package com.ekfans.base.store.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.base.store.model.Store;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.pub.util.StringUtil;

/**
 * 企业基础信息DAO接口实现
 * 
 * @ClassName: StoreDao
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class StoreDao extends GenericDao implements IStoreDao {

	@Resource
	private SessionFactory sessionFactory;

	public StoreDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.store.model.Store";
	}

	/**
	 * 根据主键获取持久化对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz, Serializable id) throws Exception {
		Object tempBean = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tempBean = session.get(clazz, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return (T) tempBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getStoresInterface() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("select s,u from Store s,User u where 1=1");
		// 关联
		sql.append(" and s.id = u.id");
		// 启用
		sql.append(" and u.status = 1");
		// 激活
		sql.append(" and u.verificationStatus = 1");
		// 未同步
		sql.append(" and s.useData = 0");
		// 限制企业类型
		sql.append(" and u.type in (1,3,4)");
		// 根据时间排序
		sql.append(" order by s.createTime desc");
		try {
			return this.list(sql.toString(), paramMap);
		} catch (Exception e) {
		}
		return null;
	}

	// ============================================================
	// 同步的保存和更新在dao统一处理，删除方法还在在service方法里面执行。
	// 有两个原因：
	// 1.重载的delete系方法过多，太复杂
	// 2.删除使用的其实是逻辑删除（具体看service业务）
	// ============================================================
	@Override
	public void updateBean(Object po) throws Exception {
		super.updateBean(po);
		sycnThis((Store) po);
	}

	@Override
	public void updateBean(Object po, Session session) throws Exception {
		super.updateBean(po, session);
		sycnThis((Store) po);
	}

	@Override
	public void updateBean(String hql, Map<String, Object> map) throws Exception {
		super.updateBean(hql, map);
		sycnThis(map);
	}

	@Override
	public void updateBean(String hql, Map<String, Object> map, Session session) throws Exception {
		super.updateBean(hql, map, session);
		sycnThis(map);
	}

	@Override
	public void addBean(Object po) throws Exception {
		super.addBean(po);
		sycnThis((Store) po);
	}

	@Override
	public void addBean(Object po, Session session) throws Exception {
		super.addBean(po, session);
		sycnThis((Store) po);
	}

	@Override
	public void saveOrUpdateBean(Object po) throws Exception {
		super.saveOrUpdateBean(po);
		sycnThis((Store) po);
	}

	@Override
	public void saveOrUpdateBean(Object po, Session session) throws Exception {
		super.saveOrUpdateBean(po, session);
		sycnThis((Store) po);
	}

	/**
	 * 通过store对象执行同步
	 */
	private void sycnThis(Store store) {
		try {
			// 同步监控系统
			new MonitorSyncMain(MonitorDataConvert.initStore(store), "001").start();
			// 同步到仓库系统
			// new WareHouseSyncMain(WareHouseDataConvert.initStore(store),
			// "001").start();
		} catch (Exception e) {
			System.out.println("Sync To SfdMonitorSys Error " + this.getClass());
		}
	}

	/**
	 * 从参数中得到storeId值，并执行同步
	 */
	private void sycnThis(Map<String, Object> map) {
		try {
			String storeId = (String) map.get("id");
			if (!StringUtil.isEmpty(storeId)) {
				Store store = (Store) get(storeId);
				sycnThis(store);
			}
		} catch (Exception e) {
			System.out.println("Sync To SfdMonitorSys Error " + this.getClass());
		}
	}

}