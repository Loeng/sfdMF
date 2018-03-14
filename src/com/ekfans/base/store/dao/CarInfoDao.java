package com.ekfans.base.store.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ekfans.base.store.model.CarInfo;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 运输企业车辆信息DAO接口实现
 * 
 * @ClassName: CreditEstimatesDao
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Repository
public class CarInfoDao extends GenericDao implements ICarInfoDao {

	public CarInfoDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.store.model.CarInfo";
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
		sycnThis((CarInfo) po);
	}

	@Override
	public void updateBean(Object po, Session session) throws Exception {
		super.updateBean(po, session);
		sycnThis((CarInfo) po);
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
		sycnThis((CarInfo) po);
	}

	@Override
	public void addBean(Object po, Session session) throws Exception {

		super.addBean(po, session);
		sycnThis((CarInfo) po);
	}

	@Override
	public void saveOrUpdateBean(Object po) throws Exception {

		super.saveOrUpdateBean(po);
		sycnThis((CarInfo) po);
	}

	@Override
	public void saveOrUpdateBean(Object po, Session session) throws Exception {

		super.saveOrUpdateBean(po, session);
		sycnThis((CarInfo) po);
	}

	/**
	 * 通过carInfo对象执行同步
	 */
	private void sycnThis(CarInfo carInfo) {
		try {
			// 同步
			new MonitorSyncMain(MonitorDataConvert.initCarInfo(carInfo), "101").start();
		} catch (Exception e) {
			System.out.println("Sync To SfdMonitorSys Error " + this.getClass());
		}
	}

	/**
	 * 从参数中得到carInfoId值，并执行同步
	 */
	private void sycnThis(Map<String, Object> map) {
		try {
			if (map != null && map.size() > 0) {
				String carInfoId = (String) map.get("id");
				if (!StringUtil.isEmpty(carInfoId)) {
					CarInfo carInfo = (CarInfo) get(carInfoId);
					sycnThis(carInfo);
				}
			}
		} catch (Exception e) {
			System.out.println("Sync To SfdMonitorSys Error " + this.getClass());
		}
	}

	@SuppressWarnings("unchecked")
	public List<CarInfo> getUserCarInfo(String userid,Pager page) throws Exception {
		StringBuffer sql = new StringBuffer("select w from CarInfo as w where 1=1");
		List<CarInfo> car=null;
		Map<String,Object> map = new HashMap<String, Object>();
		// 用户id
		if(!StringUtil.isEmpty(userid)){
			sql.append(" and w.storeId =:storeId");
			map.put("storeId",userid);
			// 根据需求排序
			sql.append(" order by updateTime desc");
			car= super.list(page,sql.toString(), map);
		}
		return car;
	}

}