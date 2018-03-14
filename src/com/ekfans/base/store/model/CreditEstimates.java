package com.ekfans.base.store.model;

/**
 * 信用测算信息--实体类
 * 
 * @ClassName: CreditEstimates
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class CreditEstimates implements java.io.Serializable {
	
	private static final long serialVersionUID = -292159348259291025L;
	// 主键
	private String id;
	// 企业id
	private String companyId;
	// 流动资产
	private Double currentAssets;
	// 流动负债
	private Double currentLiabilities;
	// 速动资产
	private Double quickAssets;
	// 资产负债总额
	private Double totalAssetsLiabili;
	// 资产总额
	private Double totalAssets;
	// 所有者权益总额
	private Double totalEquity;
	// 周转额
	private Double turnover;
	// 资产平均余额
	private Double averageBalanceAssets;
	// 税后年营业利润
	private Double operatingPat;
	// 年收入
	private Double revenue;
	// 息税前利润总额
	private Double tppsInterest;
	// 净利润
	private Double netProfit;
	// 净资产
	private Double netAssets;
	// 成本费用总额
	private Double totalCost;
	// 本年营业收入增长额
	private Double targty;
	// 上年营业收入
	private Double revenueLastYear;
	// 扣除客观因素后的年末所有者权益总额
	private Double kegsytotal;
	// 年初所有者权益总额
	private Double beginningTotal;
	// 本年总资产增长额
	private Double totalAmountAsset;
	// 上年营业利润总额
	private Double operatingYear;
	// 年初资产总额
	private Double earlyTotalAssets;
	// 本年营业利润增长额
	private Double operatingProfit;
	// 测评结果
	private String creditResult;
	// 测评状态（0:未测评,1:测评中,2:测评完成）
	private Integer creditStatus;
	
	// ====================  临时数据  ====================
	private String storeName;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Double getCurrentAssets() {
		return this.currentAssets;
	}

	public void setCurrentAssets(Double currentAssets) {
		this.currentAssets = currentAssets;
	}

	public Double getCurrentLiabilities() {
		return this.currentLiabilities;
	}

	public void setCurrentLiabilities(Double currentLiabilities) {
		this.currentLiabilities = currentLiabilities;
	}

	public Double getQuickAssets() {
		return this.quickAssets;
	}

	public void setQuickAssets(Double quickAssets) {
		this.quickAssets = quickAssets;
	}

	public Double getTotalAssetsLiabili() {
		return this.totalAssetsLiabili;
	}

	public void setTotalAssetsLiabili(Double totalAssetsLiabili) {
		this.totalAssetsLiabili = totalAssetsLiabili;
	}

	public Double getTotalAssets() {
		return this.totalAssets;
	}

	public void setTotalAssets(Double totalAssets) {
		this.totalAssets = totalAssets;
	}

	public Double getTotalEquity() {
		return this.totalEquity;
	}

	public void setTotalEquity(Double totalEquity) {
		this.totalEquity = totalEquity;
	}

	public Double getTurnover() {
		return this.turnover;
	}

	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}

	public Double getAverageBalanceAssets() {
		return this.averageBalanceAssets;
	}

	public void setAverageBalanceAssets(Double averageBalanceAssets) {
		this.averageBalanceAssets = averageBalanceAssets;
	}

	public Double getOperatingPat() {
		return this.operatingPat;
	}

	public void setOperatingPat(Double operatingPat) {
		this.operatingPat = operatingPat;
	}

	public Double getRevenue() {
		return this.revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public Double getTppsInterest() {
		return this.tppsInterest;
	}

	public void setTppsInterest(Double tppsInterest) {
		this.tppsInterest = tppsInterest;
	}

	public Double getNetProfit() {
		return this.netProfit;
	}

	public void setNetProfit(Double netProfit) {
		this.netProfit = netProfit;
	}

	public Double getNetAssets() {
		return this.netAssets;
	}

	public void setNetAssets(Double netAssets) {
		this.netAssets = netAssets;
	}

	public Double getTotalCost() {
		return this.totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Double getTargty() {
		return this.targty;
	}

	public void setTargty(Double targty) {
		this.targty = targty;
	}

	public Double getRevenueLastYear() {
		return this.revenueLastYear;
	}

	public void setRevenueLastYear(Double revenueLastYear) {
		this.revenueLastYear = revenueLastYear;
	}

	public Double getKegsytotal() {
		return this.kegsytotal;
	}

	public void setKegsytotal(Double kegsytotal) {
		this.kegsytotal = kegsytotal;
	}

	public Double getBeginningTotal() {
		return this.beginningTotal;
	}

	public void setBeginningTotal(Double beginningTotal) {
		this.beginningTotal = beginningTotal;
	}

	public Double getTotalAmountAsset() {
		return this.totalAmountAsset;
	}

	public void setTotalAmountAsset(Double totalAmountAsset) {
		this.totalAmountAsset = totalAmountAsset;
	}

	public Double getOperatingYear() {
		return this.operatingYear;
	}

	public void setOperatingYear(Double operatingYear) {
		this.operatingYear = operatingYear;
	}

	public Double getEarlyTotalAssets() {
		return this.earlyTotalAssets;
	}

	public void setEarlyTotalAssets(Double earlyTotalAssets) {
		this.earlyTotalAssets = earlyTotalAssets;
	}

	public Double getOperatingProfit() {
		return this.operatingProfit;
	}

	public void setOperatingProfit(Double operatingProfit) {
		this.operatingProfit = operatingProfit;
	}

	public String getCreditResult() {
		return this.creditResult;
	}

	public void setCreditResult(String creditResult) {
		this.creditResult = creditResult;
	}

	public Integer getCreditStatus() {
		return this.creditStatus;
	}

	public void setCreditStatus(Integer creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

}