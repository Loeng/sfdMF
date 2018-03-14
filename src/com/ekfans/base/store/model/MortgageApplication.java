package com.ekfans.base.store.model;

import java.math.BigDecimal;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderWfp;
import com.ekfans.base.wfOrder.model.Contract;

/**
 * 贷款申请--实体类
 * 
 * @ClassName: MortgageApplication
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class MortgageApplication implements java.io.Serializable {

	private static final long serialVersionUID = 6592182182405691582L;
	// 主键
	private String id = "";
	// 申请企业id
	private String storeId = "";
	// 创建时间
	private String createTime = "";
	// 申请金额
	private BigDecimal amount = new BigDecimal("0.00");
	// 类型（1:订单申请，2:信用申请，3:合同借贷，4:危废品借贷）
	private Integer type = 1;
	// 申请银行id
	private String bankId = "";
	// 订单id
	private String orderId = "";
	// 状态（1:提交中，2:审核中，3:审核成功）
	private Integer status = 1;
	// 银行是否同意（true:同意，false:不同意）
	private Boolean bankBackStatus = false;
	// 银行反馈
	private String bankFeedback = "";
	// 借款期数（月）
	private Integer manumber = 0;
	// 联系人
	private String contactName = "";
	// 联系电话
	private String contactPhone = "";
	// 备注
	private String notes = "";
	// 订单合同
	private String orderPdfFile = "";
	// 贷款用途
	private String loanUse = "";
	// 抵押物(描述)
	private String pawnDetail = "";
	// 抵押物(文件)
	private String pawnFile = "";
	// 贷款最长等待时间(天)
	private Integer loanMaxWaitTime = 0;
	// 合同Id
	private String contractId = "";
	
	// ===========  临时数据  ===========
	private Order order; // 订单
	private Contract contract; // 合同
	private String storeName; // 企业名称
	private String bankName; // 申请银行名称
	private OrderWfp orderWfp; // 危废品订单

	// ============  get  set  ============
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getBankBackStatus() {
		return bankBackStatus;
	}

	public void setBankBackStatus(Boolean bankBackStatus) {
		this.bankBackStatus = bankBackStatus;
	}

	public String getBankFeedback() {
		return bankFeedback;
	}

	public void setBankFeedback(String bankFeedback) {
		this.bankFeedback = bankFeedback;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getManumber() {
		return manumber;
	}

	public void setManumber(Integer manumber) {
		this.manumber = manumber;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getOrderPdfFile() {
		return orderPdfFile;
	}

	public void setOrderPdfFile(String orderPdfFile) {
		this.orderPdfFile = orderPdfFile;
	}

	public String getLoanUse() {
		return loanUse;
	}

	public void setLoanUse(String loanUse) {
		this.loanUse = loanUse;
	}

	public String getPawnDetail() {
		return pawnDetail;
	}

	public void setPawnDetail(String pawnDetail) {
		this.pawnDetail = pawnDetail;
	}

	public String getPawnFile() {
		return pawnFile;
	}

	public void setPawnFile(String pawnFile) {
		this.pawnFile = pawnFile;
	}

	public Integer getLoanMaxWaitTime() {
		return loanMaxWaitTime;
	}

	public void setLoanMaxWaitTime(Integer loanMaxWaitTime) {
		this.loanMaxWaitTime = loanMaxWaitTime;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public OrderWfp getOrderWfp() {
		return orderWfp;
	}

	public void setOrderWfp(OrderWfp orderWfp) {
		this.orderWfp = orderWfp;
	}
	
}