package com.ekfans.base.wfOrder.model;

import com.ekfans.base.store.model.Store;
import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 合同实体类
 * 
 * @author Ekfans_zhangJin
 * @date 2015-1-12 上午10:46:03
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Contract extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 合同名称
	private String name = "";
	// 合同合同号
	private String contractNo = "";
	// 合同类型 0危废处置合同 1危废运输合同
	private String type = "0";
	// 合同甲方
	private String firstId = "";
	// 合同乙方
	private String secondId = "";
	// 合同开始时间
	private String startTime = "";
	// 合同结束时间
	private String endTime = "";
	// 合同危废总量
	private Double quantity = 0.00;
	// 危废品状态 0固态 1液态 2半固态 3其他
	private String wfType = "0";
	// 合同创建人
	private String creatorId = "";
	// 合同附件
	private String file = "";
	// 合同创建时间
	private String createTime = "";
	// 合同修改时间
	private String updateTime = "";
	// 合同备注
	private String note = "";
	// 组织结构
	private String orgId = "";
	// 是否删除
	private String isDel = "0";
	// 合同审核
	private String contractCheckStatus = "0";
	// 甲方or乙方选择运输企业 0是甲方 1是乙方
	private String partyAORpartyB = "0";
	// 甲方or乙方支付运费 0是甲方 1是乙方
	private String freightType = "0";
	// 预付金比例
	private Double marginScale = 0.00;
	// 合同状态
	private boolean status = false;
	// 计算类型(按含量计算,一口价计算) 1:一口价 2:含量
	private String payType = "1";
	// 一口价计费价格
	private Double ykjPrice = 0.00;
	// 运费计价方式 0-重量计费，1-公里计费
	private String weightType = "0";
	// 一口价计费模式时，干重还是毛重计费 - true:干重， false:毛重
	private Boolean ykjType = false;

	// ----------------临时数据
	private String firstName;// 甲方名称
	private String secondName;// 乙方名称
	private Store firstStore;
	private Store secondStore;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFirstId() {
		return firstId;
	}

	public void setFirstId(String firstId) {
		this.firstId = firstId;
	}

	public String getSecondId() {
		return secondId;
	}

	public void setSecondId(String secondId) {
		this.secondId = secondId;
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

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getContractCheckStatus() {
		return contractCheckStatus;
	}

	public void setContractCheckStatus(String contractCheckStatus) {
		this.contractCheckStatus = contractCheckStatus;
	}

	public String getPartyAORpartyB() {
		return partyAORpartyB;
	}

	public void setPartyAORpartyB(String partyAORpartyB) {
		this.partyAORpartyB = partyAORpartyB;
	}

	public String getFreightType() {
		return freightType;
	}

	public void setFreightType(String freightType) {
		this.freightType = freightType;
	}

	public Double getMarginScale() {
		return marginScale;
	}

	public void setMarginScale(Double marginScale) {
		this.marginScale = marginScale;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Double getYkjPrice() {
		return ykjPrice;
	}

	public void setYkjPrice(Double ykjPrice) {
		this.ykjPrice = ykjPrice;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWfType() {
		return wfType;
	}

	public void setWfType(String wfType) {
		this.wfType = wfType;
	}

	public Store getFirstStore() {
		return firstStore;
	}

	public void setFirstStore(Store firstStore) {
		this.firstStore = firstStore;
	}

	public Store getSecondStore() {
		return secondStore;
	}

	public void setSecondStore(Store secondStore) {
		this.secondStore = secondStore;
	}

	public String getWeightType() {
		return weightType;
	}

	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	public Boolean getYkjType() {
		return ykjType;
	}

	public void setYkjType(Boolean ykjType) {
		this.ykjType = ykjType;
	}

}
