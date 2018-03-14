package com.ekfans.base.metal.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 品目价格--实体类
 * 
 * @ClassName: Inquiry
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class PreciousMetalDetail extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 贵金属ID
	private String metalId = "";
	// 价格类型 false:均价 true:价格区间
	private Boolean priceType = false;
	// 均价
	private BigDecimal price = new BigDecimal(0.00);
	// 价格区间：起始价
	private BigDecimal startPrice = new BigDecimal(0.00);
	// 价格区间：结束价
	private BigDecimal endPrice = new BigDecimal(0.00);
    //上海有色网金属ID
	private String smmId = "";
	// 行情日
	private String dateTime = "";
	// 涨跌金额
	private double riseAndDrop = 0.00;
	// 创建时间
	private String createTime = "";

	public String getMetalId() {
		return metalId;
	}

	public void setMetalId(String metalId) {
		this.metalId = metalId;
	}

	public Boolean getPriceType() {
		return priceType;
	}

	public void setPriceType(Boolean priceType) {
		this.priceType = priceType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(BigDecimal startPrice) {
		this.startPrice = startPrice;
	}

	public BigDecimal getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(BigDecimal endPrice) {
		this.endPrice = endPrice;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public double getRiseAndDrop() {
		return riseAndDrop;
	}

	public void setRiseAndDrop(double riseAndDrop) {
		this.riseAndDrop = riseAndDrop;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSmmId() {
		return smmId;
	}

	public void setSmmId(String smmId) {
		this.smmId = smmId;
	}
}
