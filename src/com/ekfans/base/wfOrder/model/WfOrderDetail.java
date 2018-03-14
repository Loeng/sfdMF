package com.ekfans.base.wfOrder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContractDetails
 * @Description: TODO(危废品订单明细表)
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
@Entity
public class WfOrderDetail extends BasicBean {
	// 危废订单ID
	private String wfOrderId = "";
	// 毛重
	private BigDecimal weight = new BigDecimal(0);
	// 水分含量
	private BigDecimal moistureContent = new BigDecimal(0);
	// 干重
	private BigDecimal dryWeight = new BigDecimal(0);

	// 单价 - 在一口价计价时使用
	private BigDecimal price = new BigDecimal(0);
	// 总价 - 在一口价计价时使用
	private BigDecimal totalPrice = new BigDecimal(0);

	// 检测报告
	private String testReport = "";
	// 排序
	private int position = 0;

	// -----临时数据
	private List<WfOrderPrice> prices = new ArrayList<WfOrderPrice>();

	public String getWfOrderId() {
		return wfOrderId;
	}

	public void setWfOrderId(String wfOrderId) {
		this.wfOrderId = wfOrderId;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getMoistureContent() {
		return moistureContent;
	}

	public void setMoistureContent(BigDecimal moistureContent) {
		this.moistureContent = moistureContent;
	}

	public BigDecimal getDryWeight() {
		return dryWeight;
	}

	public void setDryWeight(BigDecimal dryWeight) {
		this.dryWeight = dryWeight;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<WfOrderPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<WfOrderPrice> prices) {
		this.prices = prices;
	}

	public String getTestReport() {
		return testReport;
	}

	public void setTestReport(String testReport) {
		this.testReport = testReport;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
