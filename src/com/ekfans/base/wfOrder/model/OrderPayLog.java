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
public class OrderPayLog extends BasicBean {
	// 订单类型 - 普通订单
	public static final String ORDER_TYPE_COMMON = "0";
	// 订单类型 - 直付订单
	public static final String ORDER_TYPE_ZF = "1";
	// 订单类型 - 绿色商城订单
	public static final String ORDER_TYPE_GREEN = "2";
	// 危废订单
	public static final String ORDER_TYPE_WF = "3";

	// 支付类型 - 普通支付
	public static final String PAY_TYPE_COMMON = "0";
	// 支付类型 - 支付预付金
	public static final String PAY_TYPE_YF = "1";
	// 支付类型 - 预付金结算
	public static final String PAY_TYPE_JS = "2";
	// 支付类型 - 最终支付
	public static final String PAY_TYPE_JSTK = "3";

	// 支付方式 - 余额支付
	public static final String PAYTYPE_YE = "0";
	// 支付方式 - 线下支付
	public static final String PAYTYPE_UL = "1";
	// 支付方式 - 在线支付
	public static final String PAYTYPE_OL = "2";
	// 支付方式 - 第三方支付
	public static final String PAYTYPE_OT = "3";

	// 订单ID
	private String orderId = "";
	// 企业ID
	private String storeId = "";
	// 支付用户
	private String userId = "";
	// 支付价格
	private BigDecimal price = new BigDecimal(0.00);
	// 支付状态 0:待支付 1:支付成功 2:支付失败(取消)
	private String status = "0";
	// 支付流水号（用于查账用）
	private String queryId = "";
	// 订单类型 参见上面的订单类型
	private String orderType = "";
	// 支付类型：参见上面的支付类型
	private String payType = "";
	// 支付方式:参见上面的支付方式
	private String payPattern = "0";
	// 支付银行ID,对应银行表ID
	private String bankId = "";
	// 创建时间
	private String createTime = "";
	//支付凭证(线下支付时为上传的凭证,/**余额支付时为回执单**/)
	private String payCert = "";

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = new BigDecimal(String.format("%.2f", price));
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayPattern() {
		return payPattern;
	}

	public void setPayPattern(String payPattern) {
		this.payPattern = payPattern;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getPayCert() {
		return payCert;
	}

	public void setPayCert(String payCert) {
		this.payCert = payCert;
	}
}
