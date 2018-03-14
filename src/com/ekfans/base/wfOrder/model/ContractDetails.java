package com.ekfans.base.wfOrder.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

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
public class ContractDetails extends BasicBean {
	// 含量名称
	private String contentName = "";
	// 含量系数开始
	private double startRegion = 0.00;
	// 含量系数结束
	private double endRegion = 0.00;
	// 付费价格
	private BigDecimal price = new BigDecimal("0.0000");
	// 合同ID
	private String contractId = "";
	// 付费类型 0:收费 1:付费
	private String type = "0";
	// 合同含量表ID
	private String contractContentId = "";
	// 合同含量名称
	private String contractContentName = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public double getStartRegion() {
		return startRegion;
	}

	public void setStartRegion(double startRegion) {
		this.startRegion = startRegion;
	}

	public double getEndRegion() {
		return endRegion;
	}

	public void setEndRegion(double endRegion) {
		this.endRegion = endRegion;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContractContentId() {
		return contractContentId;
	}

	public void setContractContentId(String contractContentId) {
		this.contractContentId = contractContentId;
	}

	public String getContractContentName() {
		return contractContentName;
	}

	public void setContractContentName(String contractContentName) {
		this.contractContentName = contractContentName;
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

}
