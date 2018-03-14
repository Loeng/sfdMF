package com.ekfans.base.wfOrder.model;

import java.util.List;

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
public class ScrapWfp extends BasicBean {
	// 报批类型 0 移出 1移入
	private String type = "0";
	// 创建人ID
	private String creator = "";
	// 甲方提交申报信息ID
	private String parentId = "";
	// 处置合同ID
	private String contractId = "";
	// 处置合同名称
	private String contractName = "";
	// 处置合同编号
	private String contractNo = "";
	// 产生单位ID
	private String salId = "";
	// 产生单位地址
	private String salAddress = "";
	// 产生单位联系人
	private String salMan = "";
	// 产生单位联系电话
	private String salPhone = "";
	// 产生单位邮编
	private String salZipCode = "";
	// 处置单位ID
	private String buyId = "";
	// 处置单位地址
	private String buyAddress = "";
	// 处置单位联系人
	private String buyMan = "";
	// 处置单位联系电话
	private String buyPhone = "";
	// 处置单位邮编
	private String buyZipCode = "";
	// 废物名称
	private String wfName = "";
	// 废物代码全称
	private String code = "";
	// 废物代码ID
	private String wfmlId = "";
	// 主要成分及比例
	private String ingeredients = "";
	// 危险特性 0毒性 1易燃性 2爆炸性 3腐蚀性 4传染性 5其他
	private String characters = "0";
	// 形态 0固态 1液态 2半固态 3其他
	private String shape = "0";
	// 此次转移数量
	private Double quantity = 0.00;
	// 拟定转移批次
	private int batch = 0;
	// 转移开始时间
	private String startTime = "";
	// 转移结束时间
	private String endTime = "";
	// 废物产生环节及生产工艺流程
	private String production = "";
	// 废物处置方式 0 利用 1贮存 2焚烧 3安全填埋
	private String handle = "0";
	// 废物处置说明
	private String handleNote = "";
	// 废物包装方式
	private String packs = "";
	// 废物运输方式
	private String transType = "";
	// 运输工具
	private String transTool = "";
	// 运输路线
	private String transLine = "";
	// 检测报告
	private String reports = "";
	// 申请报告
	private String applications = "";
	// 审核状态 0待审核 1审核通过 2审核失败
	private String checkStatus = "0";
	// 审核备注
	private String checkNote = "";
	// 审核时间
	private String checkTime = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";
	// 路线ID
	private String lineId;
	// 里程
	private String course;

	// ------临时数据
	private List<ScrapWfpTrans> childList = null;
	private String buyName = "";
	private String salName = "";

	// 目前状态说明
	private String nowStatus = "";
	// 是否可以生成订单
	private boolean canOrder = false;
	// 甲方选择运输企业还是乙方选择运输企业
	private String contractChoseAOrB = "";

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getSalName() {
		return salName;
	}

	public void setSalName(String salName) {
		this.salName = salName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getSalId() {
		return salId;
	}

	public void setSalId(String salId) {
		this.salId = salId;
	}

	public String getSalMan() {
		return salMan;
	}

	public void setSalMan(String salMan) {
		this.salMan = salMan;
	}

	public String getSalPhone() {
		return salPhone;
	}

	public void setSalPhone(String salPhone) {
		this.salPhone = salPhone;
	}

	public String getBuyId() {
		return buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	public String getBuyMan() {
		return buyMan;
	}

	public void setBuyMan(String buyMan) {
		this.buyMan = buyMan;
	}

	public String getBuyPhone() {
		return buyPhone;
	}

	public void setBuyPhone(String buyPhone) {
		this.buyPhone = buyPhone;
	}

	public String getWfName() {
		return wfName;
	}

	public void setWfName(String wfName) {
		this.wfName = wfName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWfmlId() {
		return wfmlId;
	}

	public void setWfmlId(String wfmlId) {
		this.wfmlId = wfmlId;
	}

	public String getIngeredients() {
		return ingeredients;
	}

	public void setIngeredients(String ingeredients) {
		this.ingeredients = ingeredients;
	}

	public String getCharacters() {
		return characters;
	}

	public void setCharacters(String characters) {
		this.characters = characters;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
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

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getHandleNote() {
		return handleNote;
	}

	public void setHandleNote(String handleNote) {
		this.handleNote = handleNote;
	}

	public String getPacks() {
		return packs;
	}

	public void setPacks(String packs) {
		this.packs = packs;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransTool() {
		return transTool;
	}

	public void setTransTool(String transTool) {
		this.transTool = transTool;
	}

	public String getTransLine() {
		return transLine;
	}

	public void setTransLine(String transLine) {
		this.transLine = transLine;
	}

	public String getReports() {
		return reports;
	}

	public void setReports(String reports) {
		this.reports = reports;
	}

	public String getApplications() {
		return applications;
	}

	public void setApplications(String applications) {
		this.applications = applications;
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

	public String getSalAddress() {
		return salAddress;
	}

	public void setSalAddress(String salAddress) {
		this.salAddress = salAddress;
	}

	public String getSalZipCode() {
		return salZipCode;
	}

	public void setSalZipCode(String salZipCode) {
		this.salZipCode = salZipCode;
	}

	public String getBuyAddress() {
		return buyAddress;
	}

	public void setBuyAddress(String buyAddress) {
		this.buyAddress = buyAddress;
	}

	public String getBuyZipCode() {
		return buyZipCode;
	}

	public void setBuyZipCode(String buyZipCode) {
		this.buyZipCode = buyZipCode;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public List<ScrapWfpTrans> getChildList() {
		return childList;
	}

	public void setChildList(List<ScrapWfpTrans> childList) {
		this.childList = childList;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNowStatus() {
		return nowStatus;
	}

	public void setNowStatus(String nowStatus) {
		this.nowStatus = nowStatus;
	}

	public boolean isCanOrder() {
		return canOrder;
	}

	public void setCanOrder(boolean canOrder) {
		this.canOrder = canOrder;
	}

	public String getContractChoseAOrB() {
		return contractChoseAOrB;
	}

	public void setContractChoseAOrB(String contractChoseAOrB) {
		this.contractChoseAOrB = contractChoseAOrB;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}
}
