package com.ekfans.plugin.webService.monitor.vo;

import java.math.BigDecimal;

/**
 * 任务表
 * 
 * @ClassName Task
 * @Description TODO
 * @author ekfans
 * @date 2016-2-17
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Task extends VoBasicBean {

	private static final long serialVersionUID = 1L;

	// 产生企业Id
	private String salId = "";
	// 产生企业联系人
	private String salContactMan = "";
	// 产生企业联系人手机号
	private String salContactMobile = "";
	// 产生企业联系人座机
	private String salContactTel = "";
	// 产生企业联系人传真
	private String salContactFax = "";
	// 处置企业Id
	private String buyId = "";
	// 处置企业联系人
	private String buyContactMan = "";
	// 处置企业联系人手机号
	private String buyContactMobile = "";
	// 处置企业联系人座机
	private String buyContactTel = "";
	// 处置企业联系传真
	private String buyContactFax = "";
	// 货物名称
	private String wfpName = "";
	// 货物总量
	private BigDecimal wfpTotal = new BigDecimal(0);
	// 货物计量单位
	private String wfpUnit = "";
	// 发货仓库id
	private String SendHouseId = "";
	// 收货仓库Id
	private String receiveHouseId = "";
	// 路线id
	private String lineId = "";
	// 任务状态(这里真正做的只是暂存，同步到监控系统后，再根据对应逻辑调整)
	private String status = "";
	// 开始运输时间
	private String startTime = "";
	// 运输完成时间
	private String endTime = "";
	// 同步时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";

	public String getSalId() {
		return salId;
	}

	public void setSalId(String salId) {
		this.salId = salId;
	}

	public String getSalContactMan() {
		return salContactMan;
	}

	public void setSalContactMan(String salContactMan) {
		this.salContactMan = salContactMan;
	}

	public String getSalContactMobile() {
		return salContactMobile;
	}

	public void setSalContactMobile(String salContactMobile) {
		this.salContactMobile = salContactMobile;
	}

	public String getSalContactTel() {
		return salContactTel;
	}

	public void setSalContactTel(String salContactTel) {
		this.salContactTel = salContactTel;
	}

	public String getSalContactFax() {
		return salContactFax;
	}

	public void setSalContactFax(String salContactFax) {
		this.salContactFax = salContactFax;
	}

	public String getBuyId() {
		return buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	public String getBuyContactMan() {
		return buyContactMan;
	}

	public void setBuyContactMan(String buyContactMan) {
		this.buyContactMan = buyContactMan;
	}

	public String getBuyContactMobile() {
		return buyContactMobile;
	}

	public void setBuyContactMobile(String buyContactMobile) {
		this.buyContactMobile = buyContactMobile;
	}

	public String getBuyContactTel() {
		return buyContactTel;
	}

	public void setBuyContactTel(String buyContactTel) {
		this.buyContactTel = buyContactTel;
	}

	public String getBuyContactFax() {
		return buyContactFax;
	}

	public void setBuyContactFax(String buyContactFax) {
		this.buyContactFax = buyContactFax;
	}

	public String getWfpName() {
		return wfpName;
	}

	public void setWfpName(String wfpName) {
		this.wfpName = wfpName;
	}

	public BigDecimal getWfpTotal() {
		return wfpTotal;
	}

	public void setWfpTotal(BigDecimal wfpTotal) {
		this.wfpTotal = wfpTotal;
	}

	public String getWfpUnit() {
		return wfpUnit;
	}

	public void setWfpUnit(String wfpUnit) {
		this.wfpUnit = wfpUnit;
	}

	public String getSendHouseId() {
		return SendHouseId;
	}

	public void setSendHouseId(String sendHouseId) {
		SendHouseId = sendHouseId;
	}

	public String getReceiveHouseId() {
		return receiveHouseId;
	}

	public void setReceiveHouseId(String receiveHouseId) {
		this.receiveHouseId = receiveHouseId;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
