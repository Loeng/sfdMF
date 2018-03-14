package com.ekfans.plugin.webService.monitor.vo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 任务车辆信息
 * 
 * @ClassName TaskCars
 * @Description TODO
 * @author ekfans
 * @date 2016-2-17
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class TaskCars extends VoBasicBean {
	private static final long serialVersionUID = 5126225496661985425L;

	// 任务ID
	private String taskId;
	// 运输企业ID
	private String transCompanyId;
	// 车辆ID
	private String carId;
	// 押运员ID
	private String carGoId;
	// 货物数量
	private BigDecimal total;
	// 计量单位
	private String unit;
	// 五联单
	private String fiveSingle;

	private Map<String, Object> fiveSingleIn;

	public String getTaskId() {
		return taskId;
	}

	public String getTransCompanyId() {
		return transCompanyId;
	}

	public String getCarId() {
		return carId;
	}

	public String getCarGoId() {
		return carGoId;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public String getUnit() {
		return unit;
	}

	public String getFiveSingle() {
		return fiveSingle;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setTransCompanyId(String transCompanyId) {
		this.transCompanyId = transCompanyId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public void setCarGoId(String carGoId) {
		this.carGoId = carGoId;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setFiveSingle(String fiveSingle) {
		this.fiveSingle = fiveSingle;
	}

	public Map<String, Object> getFiveSingleIn() {
		return fiveSingleIn;
	}

	public void setFiveSingleIn(Map<String, Object> fiveSingleIn) {
		this.fiveSingleIn = fiveSingleIn;
	}

}
