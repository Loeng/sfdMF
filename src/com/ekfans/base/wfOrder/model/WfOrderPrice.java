package com.ekfans.base.wfOrder.model;

import java.math.BigDecimal;

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
public class WfOrderPrice extends BasicBean {
	// 危废订单ID
	private String wfOrderId = "";
	// 合同含量ID
	private String contractContentId = "";
	// 品味名称
	private String contentName = "";
	// 含量
	private Double content = 0.00;
	// 金属量
	private Double contentQuantity = 0.00;
	// 网上均价
	private BigDecimal contentPrice = new BigDecimal(0.00);
	// 计价系数
	private double coefficient = 0.00;
	// 单价
	private BigDecimal finalPrice = new BigDecimal(0.00);
	// 品味总价格
	private BigDecimal contentTotalPrice = new BigDecimal(0.00);
	// 计价方式 0百分比 1重量
	private String chargingType = "0";
	// 计价单位
	private String chargingUnit = "";
	// 创建时间
	private String createTime = "";
	// 订单明细ID
	private String orderDetailId = "";
	// 排序位置
	private int position = 0;

	public String getWfOrderId() {
		return wfOrderId;
	}

	public void setWfOrderId(String wfOrderId) {
		this.wfOrderId = wfOrderId;
	}

	public String getContractContentId() {
		return contractContentId;
	}

	public void setContractContentId(String contractContentId) {
		this.contractContentId = contractContentId;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public Double getContent() {
		return content;
	}

	public void setContent(Double content) {
		this.content = content;
	}

	public BigDecimal getContentPrice() {
		return contentPrice;
	}

	public void setContentPrice(BigDecimal contentPrice) {
		this.contentPrice = contentPrice;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Double getContentQuantity() {
		return contentQuantity;
	}

	public void setContentQuantity(Double contentQuantity) {
		this.contentQuantity = contentQuantity;
	}

	public BigDecimal getContentTotalPrice() {
		return contentTotalPrice;
	}

	public void setContentTotalPrice(BigDecimal contentTotalPrice) {
		this.contentTotalPrice = contentTotalPrice;
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

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}

}
