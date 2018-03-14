package com.ekfans.base.wfOrder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.base.store.model.CarInfo;
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
public class WfOrderTrans extends BasicBean {
	// 合同订单ID
	private String wfOrderId = "";
	// 支付企业ID
	private String payStoreId = "";
	// 运输企业ID
	private String transStoreId = "";
	// 运输企业名称
	private String transStoreName = "";
	// 车辆信息ID
	private String carInfoId = "";
	// 车牌
	private String carNum = "";
	// 数量
	private Double num = 0.00;
	// 五联单
	private String fiveSingle = "";
	// 押运员ID
	private String supercargo = "";
	// 押运员名称
	private String supercargoName = "";
	// 押运员手机号
	private String supercargoPhone = "";
	// 押运员身份证号
	private String supercargoCarNo = "";
	// 押运员从业资格证号
	private String supercargoLicenseNum = "";
	// 运费
	private BigDecimal freight = new BigDecimal("0.00");
	// 创建时间
	private String createTime = "";
	// 创建企业ID
	private String creator = "";
	// 数据状态
	private boolean dataStatus;


	// ------临时数据，不做数据库保存操作
	private List<WfOrderTransDriver> drivers = new ArrayList<WfOrderTransDriver>();
	private CarInfo carInfo = null;

	public String getWfOrderId() {
		return wfOrderId;
	}

	public void setWfOrderId(String wfOrderId) {
		this.wfOrderId = wfOrderId;
	}

	public String getPayStoreId() {
		return payStoreId;
	}

	public void setPayStoreId(String payStoreId) {
		this.payStoreId = payStoreId;
	}

	public String getTransStoreId() {
		return transStoreId;
	}

	public void setTransStoreId(String transStoreId) {
		this.transStoreId = transStoreId;
	}

	public String getTransStoreName() {
		return transStoreName;
	}

	public void setTransStoreName(String transStoreName) {
		this.transStoreName = transStoreName;
	}

	public String getCarInfoId() {
		return carInfoId;
	}

	public void setCarInfoId(String carInfoId) {
		this.carInfoId = carInfoId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public String getFiveSingle() {
		return fiveSingle;
	}

	public void setFiveSingle(String fiveSingle) {
		this.fiveSingle = fiveSingle;
	}

	public String getSupercargo() {
		return supercargo;
	}

	public void setSupercargo(String supercargo) {
		this.supercargo = supercargo;
	}

	public String getSupercargoName() {
		return supercargoName;
	}

	public void setSupercargoName(String supercargoName) {
		this.supercargoName = supercargoName;
	}

	public String getSupercargoPhone() {
		return supercargoPhone;
	}

	public void setSupercargoPhone(String supercargoPhone) {
		this.supercargoPhone = supercargoPhone;
	}

	public String getSupercargoCarNo() {
		return supercargoCarNo;
	}

	public void setSupercargoCarNo(String supercargoCarNo) {
		this.supercargoCarNo = supercargoCarNo;
	}

	public String getSupercargoLicenseNum() {
		return supercargoLicenseNum;
	}

	public void setSupercargoLicenseNum(String supercargoLicenseNum) {
		this.supercargoLicenseNum = supercargoLicenseNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public List<WfOrderTransDriver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<WfOrderTransDriver> drivers) {
		this.drivers = drivers;
	}

	public void setCarInfo(CarInfo carInfo) {
		this.carInfo = carInfo;
	}

	public CarInfo getCarInfo() {
		return carInfo;
	}

	public void setDataStatus(boolean dataStatus) {
		this.dataStatus = dataStatus;
	}

	public boolean isDataStatus() {
		return dataStatus;
	}
}
