package com.ekfans.base.user.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 用户虚拟账户日志表
 * 
 * @Title: User.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-13
 * @version 1.0
 */
@Entity
public class ViratualLog extends BasicBean {
	

	// 序列化
	private static final long serialVersionUID = 1L;
	// 会员主键
	private String userId = "";
	// 金额
	private BigDecimal amount = new BigDecimal(0.00);
	// 金额类型
	private String type = "";
	// 金额来源
	private String source = "";
	// 状态
	private boolean status = false;
	// 备注
	private String note = "";
	// 创建时间
	private String createTime = "";
	// 生效时间
	private String activationTime = "";
	// 操作人
	private String operator = "";
	//序号（临时字段）
	private String orderId = "";
	
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
   
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getActivationTime() {
        return activationTime;
    }
    public void setActivationTime(String activationTime) {
        this.activationTime = activationTime;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
	
	
	
}
