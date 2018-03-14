package com.ekfans.plugin.webService.monitor.vo.containar;

import java.util.List;

import com.ekfans.base.store.model.Warehouse;
import com.ekfans.plugin.webService.monitor.util.MonitorSyncConst;
import com.ekfans.plugin.webService.monitor.vo.CarInfo;
import com.ekfans.plugin.webService.monitor.vo.CarUser;
import com.ekfans.plugin.webService.monitor.vo.Company;
import com.ekfans.plugin.webService.monitor.vo.Task;
import com.ekfans.plugin.webService.monitor.vo.TaskCars;
import com.ekfans.plugin.webService.monitor.vo.TaskDrivers;
import com.ekfans.plugin.webService.warehouse.WareHouseCompany;

/**
 * 三分地平台 -> 监控平台,业务请求主体
 * @author 成都易科远见有限公司
 *
 */
public class MonitorSyncReqBizData extends MonitorSyncReqHeader {
	// 企业
	protected List<Company> companies;
	// 车辆
	protected List<CarInfo> carInfos;
	// 车辆
	protected List<CarUser> carUsers;
	// 任务(危废订单)
	protected List<Task> tasks;

	protected List<TaskCars> taskCars;

	protected List<TaskDrivers> drivers;
	// 仓库企业
	protected List<WareHouseCompany> hosueCompanies;
	
	// 操作的id集
	protected String ids;

	public MonitorSyncReqBizData() {
		super(MonitorSyncConst.ORG_ID, MonitorSyncConst.KEY);
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public List<CarInfo> getCarInfos() {
		return carInfos;
	}

	public void setCarInfos(List<CarInfo> carInfos) {
		this.carInfos = carInfos;
	}

	public List<CarUser> getCarUsers() {
		return carUsers;
	}

	public void setCarUsers(List<CarUser> carUsers) {
		this.carUsers = carUsers;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<TaskCars> getTaskCars() {
		return taskCars;
	}

	public void setTaskCars(List<TaskCars> taskCars) {
		this.taskCars = taskCars;
	}

	public List<TaskDrivers> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<TaskDrivers> drivers) {
		this.drivers = drivers;
	}

	public List<WareHouseCompany> getHosueCompanies() {
		return hosueCompanies;
	}

	public void setHosueCompanies(List<WareHouseCompany> hosueCompanies) {
		this.hosueCompanies = hosueCompanies;
	}

}