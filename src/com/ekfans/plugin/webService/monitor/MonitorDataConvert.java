package com.ekfans.plugin.webService.monitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderTrans;
import com.ekfans.base.wfOrder.model.WfOrderTransDriver;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.base.wfOrder.util.WfOrderTransStore;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.webService.monitor.util.MonitorSyncConst;
import com.ekfans.plugin.webService.monitor.vo.CarInfo;
import com.ekfans.plugin.webService.monitor.vo.CarUser;
import com.ekfans.plugin.webService.monitor.vo.Company;
import com.ekfans.plugin.webService.monitor.vo.Task;
import com.ekfans.plugin.webService.monitor.vo.TaskCars;
import com.ekfans.plugin.webService.monitor.vo.TaskDrivers;
import com.ekfans.plugin.webService.monitor.vo.containar.MonitorSyncReqBizData;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 数据转换类，用于三分地项目的业务数据，包装成三分地监控系统需要的业务数据
 * @author 成都易科远见有限公司
 *
 */
public class MonitorDataConvert {

	/**
	 * 初始化通用删除
	 * @param store
	 * @param user
	 * @return
	 */
	public static MonitorSyncReqBizData initDelCommon(String ids) {
		MonitorSyncReqBizData data = new MonitorSyncReqBizData();

		data.setIds(ids);
		return data;
	}

	/**
	 * 初始化企业
	 * @param store
	 * @param user
	 * @return
	 */
	public static MonitorSyncReqBizData initStore(Store store) {
		MonitorSyncReqBizData data = new MonitorSyncReqBizData();

		User user = null;
		if (store.getUserEntity() == null || StringUtil.isEmpty(store.getUserEntity().getType())) {
			IUserDao userDao = SpringContextHolder.getBean(IUserDao.class);
			try {
				user = (User) userDao.get(store.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			user = store.getUserEntity();
		}

		Company company = new Company();
		company.setId(store.getId());
		company.setOrgId(MonitorSyncConst.ORG_ID);
		company.setName(store.getStoreName());
		company.setContactMen(store.getContactName());
		company.setContactTel(store.getContactPhone());
		company.setContactFax(store.getContactFax());
		company.setContactAddress(store.getMailingAddress());
		company.setCreateTime(DateUtil.getSysDateTimeString());
		company.setUpdateTime(DateUtil.getSysDateTimeString());
		if (user != null) {
			company.setType(user.getType());
			company.setContactMobile(user.getMobile());
			company.setContactMail(user.getEmail());
		}

		List<Company> companies = new ArrayList<>();
		companies.add(company);
		data.setCompanies(companies);
		return data;
	}

	/**
	 * 初始化车辆
	 * @return
	 */
	public static MonitorSyncReqBizData initCarInfo(com.ekfans.base.store.model.CarInfo info) {
		MonitorSyncReqBizData data = new MonitorSyncReqBizData();

		CarInfo carInfo = new CarInfo();
		carInfo.setId(info.getId());
		carInfo.setOrgId(MonitorSyncConst.ORG_ID);
		carInfo.setCompanyId(info.getStoreId());
		carInfo.setDeviceId(info.getDeviceId());
		carInfo.setCarNo(info.getCarNo());
		carInfo.setCarType(info.getCarType());
		carInfo.setFullWeight(info.getFullWeight() + "");
		carInfo.setStartTime(info.getStartTime());
		carInfo.setEndTime(info.getEndTime());
		carInfo.setYsNo(info.getYsNo());
		carInfo.setFullNum(info.getFullNum() + "");
		// 默认值
		carInfo.setStatus("0");
		carInfo.setXszFileIn(StringUtil.isEmpty(info.getXszFile()) ? null : FileUtil.getInputStream(MonitorSyncConst.REAL_PATH + info.getXszFile()));
		carInfo.setYszFileIn(StringUtil.isEmpty(info.getYszFile()) ? null : FileUtil.getInputStream(MonitorSyncConst.REAL_PATH + info.getYszFile()));

		List<CarInfo> carInfos = new ArrayList<>();
		carInfos.add(carInfo);
		data.setCarInfos(carInfos);
		return data;
	}

	/**
	 * 初始化车辆人员
	 * @return
	 */
	public static MonitorSyncReqBizData initCarUser(com.ekfans.base.store.model.CarUser user) {
		MonitorSyncReqBizData data = new MonitorSyncReqBizData();

		CarUser carUser = new CarUser();
		carUser.setId(user.getId());
		carUser.setOrgId(MonitorSyncConst.ORG_ID);
		carUser.setCompanyId(user.getStoreId());
		carUser.setName(user.getName());
		carUser.setMobile(user.getMobile());
		carUser.setCardNo(user.getCardNo());
		carUser.setType(user.getType());
		carUser.setDriverFileIn(StringUtil.isEmpty(user.getDriverFile()) ? null : FileUtil.getInputStream(MonitorSyncConst.REAL_PATH + user.getDriverFile()));
		carUser.setLicenseFileIn(StringUtil.isEmpty(user.getLicenseFile()) ? null : FileUtil.getInputStream(MonitorSyncConst.REAL_PATH + user.getLicenseFile()));
		carUser.setStartTime(user.getStartTime());
		carUser.setEndTime(user.getEndTime());
		carUser.setLinceseNum(user.getLicenseNum());
		// 默认值
		carUser.setStatus("0");

		List<CarUser> carInfos = new ArrayList<>();
		carInfos.add(carUser);
		data.setCarUsers(carInfos);
		return data;
	}

	/**
	 * 初始化任务信息
	 * @return
	 */
	public static MonitorSyncReqBizData initTask(WfOrder wfOrder) {
		MonitorSyncReqBizData data = new MonitorSyncReqBizData();

		IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);
		IUserService userService = SpringContextHolder.getBean(IUserService.class);
		IWfOrderService wfOrderService = SpringContextHolder.getBean(IWfOrderService.class);

		// 定义数据list 存放数据map
		List<Task> tasks = new ArrayList<Task>();
		List<TaskCars> taskCarInfos = new ArrayList<TaskCars>();
		List<TaskDrivers> drivers = new ArrayList<TaskDrivers>();

		// ===========订单信息=============
		// 获取卖家企业信息
		Store salStore = storeService.getStore(wfOrder.getSaleId());
		User salUser = userService.getUser(salStore.getId());
		// 获取卖家企业信息
		Store buyStore = storeService.getStore(wfOrder.getBuyId());
		User buyUser = userService.getUser(buyStore.getId());

		Task task = new Task();
		task.setId(wfOrder.getId());
		task.setOrgId(MonitorSyncConst.ORG_ID);
		// 卖家联系相关信息
		task.setSalContactMan(salStore.getContactName());
		task.setSalContactMobile(salUser.getMobile());
		task.setSalContactTel(salStore.getContactPhone());
		task.setSalContactFax(salStore.getContactFax());
		task.setSalId(wfOrder.getSaleId());
		// 买家联系相关信息
		task.setBuyId(wfOrder.getBuyId());
		task.setBuyContactMan(buyStore.getContactName());
		task.setBuyContactMobile(buyUser.getMobile());
		task.setBuyContactTel(buyStore.getContactPhone());
		task.setBuyContactFax(buyStore.getContactFax());
		// 危废订单信息
		task.setWfpName(wfOrder.getWfpName());
		task.setWfpTotal(new BigDecimal(wfOrder.getWfpTotal()));
		task.setWfpUnit("");
		task.setStatus(wfOrder.getStatus());
		task.setLineId(wfOrder.getLineId());
		tasks.add(task);

		// ===========订单相关信息=============
		// 所有运输信息
		List<WfOrderTransStore> storeList = wfOrderService.getWfOrderTransList(wfOrder.getId());
		for (WfOrderTransStore orderTransStore : storeList) {
			// 得到订单运输详情列表
			List<WfOrderTrans> orderTransList = orderTransStore.getTransList();
			for (WfOrderTrans orderTrans : orderTransList) {
				// 危废运输车辆相关信息
				TaskCars taskCar = new TaskCars();
				taskCar.setId(orderTrans.getId());
				taskCar.setOrgId(MonitorSyncConst.ORG_ID);
				taskCar.setTaskId(orderTrans.getWfOrderId());
				taskCar.setTransCompanyId(orderTrans.getTransStoreId());
				taskCar.setCarId(orderTrans.getCarInfoId());
				taskCar.setCarGoId(orderTrans.getSupercargo());
				taskCar.setTotal(new BigDecimal(orderTrans.getNum()));
				taskCar.setFiveSingleIn(StringUtil.isEmpty(orderTrans.getFiveSingle()) ? null : FileUtil.getInputStream(MonitorSyncConst.REAL_PATH + orderTrans.getFiveSingle()));
				taskCar.setUnit("吨");
				taskCarInfos.add(taskCar);

				// 获取TaskDrivers信息
				List<WfOrderTransDriver> orderTransDriverList = orderTrans.getDrivers();
				for (WfOrderTransDriver wfOrderTransDriver : orderTransDriverList) {
					TaskDrivers driver = new TaskDrivers();
					driver.setId(wfOrderTransDriver.getId());
					driver.setOrgId(MonitorSyncConst.ORG_ID);
					driver.setTaskId(wfOrderTransDriver.getWfOrderId());
					driver.setTaskCarsId(wfOrderTransDriver.getWfOrderTransId());
					driver.setDriverId(wfOrderTransDriver.getDriver());
					drivers.add(driver);
				}
			}
		}

		data.setTasks(tasks);
		data.setTaskCars(taskCarInfos);
		data.setDrivers(drivers);
		return data;
	}

//	/**
//	 * 初始化任务详细信息
//	 * @return
//	 */
//	public static MonitorSyncReqBizData initOrderTrans(WfOrder wfOrder) {
//		MonitorSyncReqBizData data = new MonitorSyncReqBizData();
//
//		return data;
//	}
}
