package com.ekfans.base.wfOrder.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContractDetails
 * @Description: TODO(订单余额日志表)
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
@Entity
public class ScrapPricesLog extends BasicBean {
	// 订单余额表ID
	private String scrapPricesId = "";
	// 使用虚拟金额的危废订单ID
	private String payOrderId = "";
	// 使用金额
	private BigDecimal price = new BigDecimal(0.00);
	// 企业ID
	private String payStoreId = "";
	// 余额所存放的企业ID
	private String salStoreId = "";
	// 使用时间
	private String createTime = "";

	public String getScrapPricesId() {
		return scrapPricesId;
	}

	public void setScrapPricesId(String scrapPricesId) {
		this.scrapPricesId = scrapPricesId;
	}

	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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
