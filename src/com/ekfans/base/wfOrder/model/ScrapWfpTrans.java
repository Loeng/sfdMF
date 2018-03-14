package com.ekfans.base.wfOrder.model;

import javax.persistence.Entity;

import com.ekfans.base.store.model.Store;
import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContractDetails
 * @Description: TODO(合同含量信息价格区间表)
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
@Entity
public class ScrapWfpTrans extends BasicBean {

	// 处置合同ID
	private String contractId = "";
	// 运输合同ID
	private String transContractId = "";
	// 申报单ID
	private String scrapWfpId = "";
	// 运输单位ID
	private String transId = "";
	// 运输单位地址
	private String transAddress = "";
	// 运输单位联系人
	private String transMan = "";
	// 运输单位联系电话
	private String transPhone = "";
	// 运输单位邮编
	private String transZipCode = "";
	// 申报单有效开始时间
	private String startTime = "";
	// 申报单有效结束时间
	private String endTime = "";
	// 道路紧急预案
	private String emergencyPlan = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";

	// ------临时数据
	private Store transStore = null;

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getTransContractId() {
		return transContractId;
	}

	public void setTransContractId(String transContractId) {
		this.transContractId = transContractId;
	}

	public String getScrapWfpId() {
		return scrapWfpId;
	}

	public void setScrapWfpId(String scrapWfpId) {
		this.scrapWfpId = scrapWfpId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransMan() {
		return transMan;
	}

	public void setTransMan(String transMan) {
		this.transMan = transMan;
	}

	public String getTransPhone() {
		return transPhone;
	}

	public void setTransPhone(String transPhone) {
		this.transPhone = transPhone;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEmergencyPlan() {
		return emergencyPlan;
	}

	public void setEmergencyPlan(String emergencyPlan) {
		this.emergencyPlan = emergencyPlan;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getTransAddress() {
		return transAddress;
	}

	public void setTransAddress(String transAddress) {
		this.transAddress = transAddress;
	}

	public String getTransZipCode() {
		return transZipCode;
	}

	public void setTransZipCode(String transZipCode) {
		this.transZipCode = transZipCode;
	}

	public Store getTransStore() {
		return transStore;
	}

	public void setTransStore(Store transStore) {
		this.transStore = transStore;
	}

}
