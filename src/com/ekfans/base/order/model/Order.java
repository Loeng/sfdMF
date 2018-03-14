package com.ekfans.base.order.model;

import java.math.BigDecimal;
import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 订单--实体类
 * 
 * @ClassName: Order
 * @Description:
 * @author lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Order extends BasicBean {
	private static final long serialVersionUID = 1L;
	// 店铺ID -- 对应店铺表(Store)主键---卖
	private String storeId = "";
	// 用户等级ID -- 对应会员表(User)主键---买
	private String userId = "";
	// 配送方式ID
	private String shipmentId = "";
	// 支付方式ID
	private String paymentId = "";
	// 订单备注
	private String note = "";
	// 产品金额
	private BigDecimal productPrice = new BigDecimal("0.00");
	// 配送费
	private BigDecimal fare = new BigDecimal("0.00");
	// 应收金额
	private BigDecimal totalPrice = new BigDecimal("0.00");
	// 实收金额
	private BigDecimal paid = new BigDecimal("0.00");
	// 发货状态
	private Boolean shippingStatus = false;
	// 订单状态(OrderConst中定义)
	private String status = "";
	// 服务状态(0：申请退款，1：退款中，2：完成)
	private String serviceStatus = "";
	// 买家是否已评价
	private Boolean userApp = false;
	// 卖家是否已评价
	private Boolean storeApp = false;
	// 是否需要发票
	private Boolean invoice = false;
	// 发票类型 false,普通发票 true,增值税发票
	private Boolean invoiceType = false;
	// 发票抬头
	private String invoiceTitle = "";
	// 发票内容
	private String invoiceContent = "";
	// 物流公司
	private String logisticsName = "";
	// 物流单号
	private String logisticsNo = "";
	// 订单可获积分
	private Integer integral = 0;
	// 下单时间
	private String createTime = "";
	// 配送方式名称
	private String shipment = "";
	// 支付方式名称
	private String payment = "";
	// 支付类型 0:余额支付，1:线下转账，2:在线支付
	private String payType = "0";
	// 支付凭证（当支付类型为线下转账时使用）
	private String payCert = "";
	// 总的订单号
	private String payId = "";
	// 实际运费
	private BigDecimal actualPrice = new BigDecimal("0.00");
	// 订单类型（0:普通订单，1:直付订单  2:绿色商城订单）
	private Integer type = 0;
	// 合同类型：true:选择合同(contractId有值)，false:上传合同(contract有值)
	private Boolean contractType = false;
	// 合同附件
	private String contract = "";
	// 合同ID-对应Contract表ID
	private String contractId = "";
	//支付时给银行的ID,最长仅限十位
	private String bankOrderId = "";

	// ================ 临时数据 ================
	// 商品名称
	private String productName = "";
	// 购买数量
	private Integer quantity = 0;
	// 收货人姓名
	private String name = "";
	// 商品ID -- 对应商品牌表(Product)主键
	private String productId = "";
	// 商品小图
	private String smallPicture = "";
	// 数量单位
	private String unit = "";
	// 用户昵称
	private String nickName = "";
	private Boolean checked = false;
	private List<OrderDetail> details;
	private OrderAddress orderAddress;

	// 买家昵称
	private String buyName = "";
	// 卖家昵称
	private String salName = "";
	// 合同名称
	private String contractName = "";

	// ============== get set ===============
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

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public Boolean getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Boolean shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public Boolean getUserApp() {
		return userApp;
	}

	public void setUserApp(Boolean userApp) {
		this.userApp = userApp;
	}

	public Boolean getStoreApp() {
		return storeApp;
	}

	public void setStoreApp(Boolean storeApp) {
		this.storeApp = storeApp;
	}

	public Boolean getInvoice() {
		return invoice;
	}

	public void setInvoice(Boolean invoice) {
		this.invoice = invoice;
	}

	public Boolean getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Boolean invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getShipment() {
		return shipment;
	}

	public void setShipment(String shipment) {
		this.shipment = shipment;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSmallPicture() {
		return smallPicture;
	}

	public void setSmallPicture(String smallPicture) {
		this.smallPicture = smallPicture;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}

	public OrderAddress getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(OrderAddress orderAddress) {
		this.orderAddress = orderAddress;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Boolean getContractType() {
		return contractType;
	}

	public void setContractType(Boolean contractType) {
		this.contractType = contractType;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayCert() {
		return payCert;
	}

	public void setPayCert(String payCert) {
		this.payCert = payCert;
	}

	public String getSalName() {
		return salName;
	}

	public void setSalName(String salName) {
		this.salName = salName;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getBankOrderId() {
		return bankOrderId;
	}

	public void setBankOrderId(String bankOrderId) {
		this.bankOrderId = bankOrderId;
	}
}