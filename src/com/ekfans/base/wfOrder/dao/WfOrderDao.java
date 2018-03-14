package com.ekfans.base.wfOrder.dao;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.pub.util.StringUtil;

/**
 * 店铺自定义页面DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Repository
public class WfOrderDao extends GenericDao implements IWfOrderDao {
	public WfOrderDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.wfOrder.model.WfOrder";
	}
	

	// ============================================================
	// 同步的保存和更新在dao统一处理，删除方法还在在service方法里面执行。
	// 有两个原因：
	//	1.重载的delete系方法过多，太复杂
	//	2.删除使用的其实是逻辑删除（具体看service业务）
	// ============================================================
	@Override
	public void updateBean(Object po) throws Exception {
		super.updateBean(po);
		sycnThis((WfOrder) po);
	}

	@Override
	public void updateBean(Object po, Session session) throws Exception {
		super.updateBean(po, session);
		sycnThis((WfOrder) po);
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
		sycnThis((WfOrder) po);
	}

	@Override
	public void addBean(Object po, Session session) throws Exception {

		super.addBean(po, session);
		sycnThis((WfOrder) po);
	}

	@Override
	public void saveOrUpdateBean(Object po) throws Exception {

		super.saveOrUpdateBean(po);
		sycnThis((WfOrder) po);
	}

	@Override
	public void saveOrUpdateBean(Object po, Session session) throws Exception {

		super.saveOrUpdateBean(po, session);
		sycnThis((WfOrder) po);
	}

	/**
	 * 通过wfOrder对象执行同步
	 */
	private void sycnThis(WfOrder wfOrder) {
		try {
			// 同步
			new MonitorSyncMain(MonitorDataConvert.initTask(wfOrder), "301").start();
		} catch (Exception e) {
			System.out.println("Sync To SfdMonitorSys Error " + this.getClass());
		}
	}

	/**
	 * 从参数中得到wfOrderId值，并执行同步
	 */
	private void sycnThis(Map<String, Object> map) {
		try {
			String wfOrderId = (String) map.get("id");
			if (!StringUtil.isEmpty(wfOrderId)) {
				WfOrder wfOrder = (WfOrder) get(wfOrderId);
				sycnThis(wfOrder);
			}
		} catch (Exception e) {
			System.out.println("Sync To SfdMonitorSys Error " + this.getClass());
		}
	}

}