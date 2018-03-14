package com.ekfans.base.wfOrder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
public class WfOrder extends BasicBean {
	// 危废申报ID
	private String scrapId = "";
	// 合同表ID
	private String contractId = "";
	// 合同名称
	private String contractName = "";
	// 创建企业ID
	private String creator = "";
	// 卖家ID
	private String saleId = "";
	// 卖家名称
	private String saleName = "";
	// 买家ID
	private String buyId = "";
	// 买家名称
	private String buyName = "";
	// 危废品名称
	private String wfpName = "";
	// 危废品总量
	private Double wfpTotal = 0.00;
	// 总里程数
	private Double mileage = 0.00;
	// 是否买家支付运费
	private Boolean freightBuyer = false;
	// 总运费
	private BigDecimal freight = new BigDecimal(0.00);
	// 计价类型(是否一口价) false不是一口价 true一口价
	private boolean countType = false;
	// 一口价计费模式时，干重还是毛重计费 - true:干重， false:毛重
	private Boolean ykjType = false;
	// 合同单价
	private BigDecimal contractOriginal = new BigDecimal(0.00);
	// 初步单价（如果是一口价，合同原价+微量元素；如果非一口价，则是微量元素的总和）
	private BigDecimal actualPrice = new BigDecimal(0.00);
	// 订单初步总价
	private BigDecimal totalPrice = new BigDecimal(0.00);
	// 最终单价（如果是一口价，合同原价+微量元素；如果非一口价，则是微量元素的总和）
	private BigDecimal realPrice = new BigDecimal(0.00);
	// 订单最终总价
	private BigDecimal realTotalPrice = new BigDecimal(0.00);
	// 预付款类型 - true：收款。false：付款
	private boolean yfType = false;
	// 预付金额
	private BigDecimal yfPrice = new BigDecimal(0.00);
	// 已付金额
	private BigDecimal payPrice = new BigDecimal(0.00);
	// 支付状态 0:待支付 1:已支付预付金 2:已全额支付
	private String payStatus = "0";
	// 订单状态 00新下单待确认 01已确认订单 02待支付预付金 // 03已支付待发货 04已发货待收货 05已收货
	// 06待卖家确认品位单以及品味含量 07已确认品味信息待双方确认金额 08待最终支付 09订单完成
	private String status = "00";
	// 买家确认品味状态
	private boolean buySureStatus = false;
	// 卖家确认品味状态
	private boolean saleSureStatus = false;
	// 收款银行账号ID
	private String payBankId = "";
	// 发货地址
	private String sendAddress = "";
	// 发货联系人
	private String sendLinkMan = "";
	// 发货联系方式
	private String sendLinkPhone = "";
	// 收货地址
	private String takeAddress = "";
	// 收货联系人
	private String takeLinkMan = "";
	// 收货联系方式
	private String takeLinkPhone = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";
	// 订单余额状态
	private String listStatus = "0";
	// 退款ID
	private String returnPayId = "";
	// 数据状态
	private boolean dataStatus;
	// 路线ID
	private String lineId;

	private String bankOrderId = "";

	// --------------临时数据：app获取订单列表用------------
	// 订单内车辆信息
	List<WfOrderTrans> wfTrans = new ArrayList<WfOrderTrans>();
	// app监控url
	private String monitorUrl;

	public String getMonitorUrl() {
		return monitorUrl;
	}

	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}

	public List<WfOrderTrans> getWfTrans() {
		return wfTrans;
	}

	public void setWfTrans(List<WfOrderTrans> wfTrans) {
		this.wfTrans = wfTrans;
	}

	public String getScrapId() {
		return scrapId;
	}

	public void setScrapId(String scrapId) {
		this.scrapId = scrapId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getBuyId() {
		return buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getWfpName() {
		return wfpName;
	}

	public void setWfpName(String wfpName) {
		this.wfpName = wfpName;
	}

	public Double getWfpTotal() {
		return wfpTotal;
	}

	public void setWfpTotal(Double wfpTotal) {
		this.wfpTotal = wfpTotal;
	}

	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public boolean isCountType() {
		return countType;
	}

	public void setCountType(boolean countType) {
		this.countType = countType;
	}

	public BigDecimal getContractOriginal() {
		return contractOriginal;
	}

	public void setContractOriginal(BigDecimal contractOriginal) {
		this.contractOriginal = contractOriginal;
	}

	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(BigDecimal realPrice) {
		this.realPrice = realPrice;
	}

	public BigDecimal getRealTotalPrice() {
		return realTotalPrice;
	}

	public void setRealTotalPrice(BigDecimal realTotalPrice) {
		this.realTotalPrice = realTotalPrice;
	}

	public BigDecimal getYfPrice() {
		return yfPrice;
	}

	public void setYfPrice(BigDecimal yfPrice) {
		this.yfPrice = yfPrice;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isBuySureStatus() {
		return buySureStatus;
	}

	public void setBuySureStatus(boolean buySureStatus) {
		this.buySureStatus = buySureStatus;
	}

	public boolean isSaleSureStatus() {
		return saleSureStatus;
	}

	public void setSaleSureStatus(boolean saleSureStatus) {
		this.saleSureStatus = saleSureStatus;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSendLinkMan() {
		return sendLinkMan;
	}

	public void setSendLinkMan(String sendLinkMan) {
		this.sendLinkMan = sendLinkMan;
	}

	public String getSendLinkPhone() {
		return sendLinkPhone;
	}

	public void setSendLinkPhone(String sendLinkPhone) {
		this.sendLinkPhone = sendLinkPhone;
	}

	public String getTakeAddress() {
		return takeAddress;
	}

	public void setTakeAddress(String takeAddress) {
		this.takeAddress = takeAddress;
	}

	public String getTakeLinkMan() {
		return takeLinkMan;
	}

	public void setTakeLinkMan(String takeLinkMan) {
		this.takeLinkMan = takeLinkMan;
	}

	public String getTakeLinkPhone() {
		return takeLinkPhone;
	}

	public void setTakeLinkPhone(String takeLinkPhone) {
		this.takeLinkPhone = takeLinkPhone;
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

	public Boolean getFreightBuyer() {
		return freightBuyer;
	}

	public void setFreightBuyer(Boolean freightBuyer) {
		this.freightBuyer = freightBuyer;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public boolean isYfType() {
		return yfType;
	}

	public void setYfType(boolean yfType) {
		this.yfType = yfType;
	}

	public String getPayBankId() {
		return payBankId;
	}

	public void setPayBankId(String payBankId) {
		this.payBankId = payBankId;
	}

	public Boolean getYkjType() {
		return ykjType;
	}

	public void setYkjType(Boolean ykjType) {
		this.ykjType = ykjType;
	}

	public String getListStatus() {
		return listStatus;
	}

	public void setListStatus(String listStatus) {
		this.listStatus = listStatus;
	}

	public String getReturnPayId() {
		return returnPayId;
	}

	public void setReturnPayId(String returnPayId) {
		this.returnPayId = returnPayId;
	}

	public void setDataStatus(boolean dataStatus) {
		this.dataStatus = dataStatus;
	}

	public boolean isDataStatus() {
		return dataStatus;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getBankOrderId() {
		return bankOrderId;
	}

	public void setBankOrderId(String bankOrderId) {
		this.bankOrderId = bankOrderId;
	}
}
