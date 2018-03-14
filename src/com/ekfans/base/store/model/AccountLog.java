package com.ekfans.base.store.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 虚拟账户日志表
 * 
 * @ClassName: Store
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
public class AccountLog extends BasicBean {
	// 用户ID
	private String userId = "";
	// 对方ID(收款、付款方ID)
	private String receiveId = "";
	// 订单ID
	private String orderId = "";
	// 父级日志ID（在收款方时使用）
	private String parentId = "";
	// 服务提供方流水号
	private String proRef = "";
	// 资金监管系统交易流水号
	private String fmsTransNo = "";
	// 原市场入金交易流水号
	private String oriMchTransNo = "";
	// 金额
	private BigDecimal price = new BigDecimal(0.00);
	// 操作类型
	private String type = "";
	// 操作时间
	private String createTime = "";
	// 备注
	private String notes = "";
	// 交易状态 0:申请中；1:交易成功；2：交易失败；3：处理中
	private String status = "0";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getFmsTransNo() {
		return fmsTransNo;
	}

	public void setFmsTransNo(String fmsTransNo) {
		this.fmsTransNo = fmsTransNo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOriMchTransNo() {
		return oriMchTransNo;
	}

	public void setOriMchTransNo(String oriMchTransNo) {
		this.oriMchTransNo = oriMchTransNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getProRef() {
		return proRef;
	}

	public void setProRef(String proRef) {
		this.proRef = proRef;
	}

}