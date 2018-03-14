package com.ekfans.base.store.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ekfans.base.store.model.CarUser;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.pub.util.StringUtil;

/**
 * 运输企业车辆人员信息DAO接口实现
 * 
 * @ClassName: CreditEstimatesDao
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class CarUserDao extends GenericDao implements ICarUserDao {

	public CarUserDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.store.model.CarUser";
	}

	@Override
	public List<CarUser> getCarUsersInterface() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("select cu from CarUser cu where 1=1");
		// 条件：未被同步过
		sql.append(" and cu.useData = 0 ");
		// 条件：状态正常
		sql.append(" and cu.checkStatus = 1 ");
		// 根据权限的级别倒序，位置升序排序
		sql.append(" order by cu.createTime desc");
		try {
			return this.list(sql.toString(), paramMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
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
		sycnThis((CarUser) po);
	}

	@Override
	public void updateBean(Object po, Session session) throws Exception {
		super.updateBean(po, session);
		sycnThis((CarUser) po);
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
		sycnThis((CarUser) po);
	}

	@Override
	public void addBean(Object po, Session session) throws Exception {

		super.addBean(po, session);
		sycnThis((CarUser) po);
	}

	@Override
	public void saveOrUpdateBean(Object po) throws Exception {

		super.saveOrUpdateBean(po);
		sycnThis((CarUser) po);
	}

	@Override
	public void saveOrUpdateBean(Object po, Session session) throws Exception {

		super.saveOrUpdateBean(po, session);
		sycnThis((CarUser) po);
	}

	/**
	 * 通过user对象执行同步
	 */
	private void sycnThis(CarUser user) {
		try {
			// 审核通过将车辆信息同步到监控平台
			new MonitorSyncMain(MonitorDataConvert.initCarUser(user), "201").start();
		} catch (Exception e) {
			System.out.println("Sync To SfdMonitorSys Error " + this.getClass());
		}
	}

	/**
	 * 从参数中得到userId值，并执行同步
	 */
	private void sycnThis(Map<String, Object> map) {
		try {
			String userId = (String) map.get("id");
			if (!StringUtil.isEmpty(userId)) {
				CarUser user = (CarUser) get(userId);
				sycnThis(user);
			}
		} catch (Exception e) {
			System.out.println("Sync To SfdMonitorSys Error " + this.getClass());
		}
	}

}