package com.ekfans.base.wfOrder.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContractDetails
 * @Description: TODO(危废品订单表)
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
@Entity
public class WfOrderTransDriver extends BasicBean {
	// 合同订单ID
	private String wfOrderId = "";
	// 合同订单车辆信息表ID
	private String wfOrderTransId = "";
	// 运输企业ID
	private String transStoreId = "";
	// 驾驶员ID
	private String driver = "";
	// 驾驶员名称
	private String driverName = "";
	// 驾驶员手机号
	private String driverPhone = "";
	// 驾驶员身份证号
	private String driverCarNo = "";
	// 驾驶员从业资格证号
	private String driverLicenseNum = "";
	// 创建时间
	private String createTime = "";
	// 数据状态
	private boolean dataStatus;

	public String getWfOrderId() {
		return wfOrderId;
	}

	public void setWfOrderId(String wfOrderId) {
		this.wfOrderId = wfOrderId;
	}

	public String getWfOrderTransId() {
		return wfOrderTransId;
	}

	public void setWfOrderTransId(String wfOrderTransId) {
		this.wfOrderTransId = wfOrderTransId;
	}

	public String getTransStoreId() {
		return transStoreId;
	}

	public void setTransStoreId(String transStoreId) {
		this.transStoreId = transStoreId;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public String getDriverCarNo() {
		return driverCarNo;
	}

	public void setDriverCarNo(String driverCarNo) {
		this.driverCarNo = driverCarNo;
	}

	public String getDriverLicenseNum() {
		return driverLicenseNum;
	}

	public void setDriverLicenseNum(String driverLicenseNum) {
		this.driverLicenseNum = driverLicenseNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public boolean isDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(boolean dataStatus) {
		this.dataStatus = dataStatus;
	}


}
