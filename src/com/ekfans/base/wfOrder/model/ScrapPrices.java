package com.ekfans.base.wfOrder.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContractDetails
 * @Description: TODO(订单余额表)
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
@Entity
public class ScrapPrices extends BasicBean {
	// 危废申报ID
	private String scrapId = "";
	// 危废订单ID
	private String orderId = "";
	// 多余总额
	private BigDecimal price = new BigDecimal(0.00);
	// 余额
	private BigDecimal listPrice = new BigDecimal(0.00);
	// 状态
	private String status = "0";
	// 企业ID
	private String payStoreId = "";
	// 余额所存放的企业ID
	private String salStoreId = "";
	// 创建时间
	private String createTime = "";

	public String getScrapId() {
		return scrapId;
	}

	public void setScrapId(String scrapId) {
		this.scrapId = scrapId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayStoreId() {
		return payStoreId;
	}

	public void setPayStoreId(String payStoreId) {
		this.payStoreId = payStoreId;
	}

	public String getSalStoreId() {
		return salStoreId;
	}

	public void setSalStoreId(String salStoreId) {
		this.salStoreId = salStoreId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
