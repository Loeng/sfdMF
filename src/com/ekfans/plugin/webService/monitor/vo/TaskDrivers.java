package com.ekfans.plugin.webService.monitor.vo;

/**
 * 任务司机信息
 * 
 * @ClassName CarInfo
 * @Description TODO
 * @author ekfans
 * @date 2016-2-17
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class TaskDrivers extends VoBasicBean {

	private static final long serialVersionUID = 5126225496661985425L;
	// 任务ID
	private String taskId;
	// 任务车辆ID
	private String taskCarsId;
	// 司机ID
	private String driverId;

	// 临时数据
	private CarUser carUser;

	public CarUser getCarUser() {
		return carUser;
	}

	public void setCarUser(CarUser carUser) {
		this.carUser = carUser;
	}

	public String getTaskId() {
		return taskId;
	}

	public String getTaskCarsId() {
		return taskCarsId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setTaskCarsId(String taskCarsId) {
		this.taskCarsId = taskCarsId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

}
