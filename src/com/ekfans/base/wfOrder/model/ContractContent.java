package com.ekfans.base.wfOrder.model;

import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContractDetails
 * @Description: TODO(合同含量信息表)
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
@Entity
public class ContractContent extends BasicBean {
	// 合同表ID
	private String contractId;
	// 含量名称
	private String name;
	// 含量类型 false:非水分 true:水分
	private boolean type = false;
	// 计价方式 0百分比 1重量
	private String chargingType = "0";
	// 计价单位
	private String chargingUnit = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";

	// ------临时数据
	private List<ContractDetails> childList = null;

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public String getChargingType() {
		return chargingType;
	}

	public void setChargingType(String chargingType) {
		this.chargingType = chargingType;
	}

	public String getChargingUnit() {
		return chargingUnit;
	}

	public void setChargingUnit(String chargingUnit) {
		this.chargingUnit = chargingUnit;
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

	public List<ContractDetails> getChildList() {
		return childList;
	}

	public void setChildList(List<ContractDetails> childList) {
		this.childList = childList;
	}

}
