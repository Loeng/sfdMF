package com.ekfans.base.order.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 订单处理日志实体类(订单跟踪)
 * 
 * @Title: OrderTreatDetail.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class OrderTreatDetail extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 订单ID-- 对应订单表(Order)主键
	private String orderId = "";
	
	// 操作类型    操作类型从 com.ekfans.base.order.util.OrderConst中取  共10个状态
	private String type = "";
	
	// 处理时间
	private String createTime = "";
	
	// 处理员
	private String creater = "";
	
	// 处理备注
	private String note = "";
	
	//原价格
	private BigDecimal originalPrice = new BigDecimal("0.00");
	
	//修改后的价格
	private BigDecimal nowPrice = new BigDecimal("0.00");

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(BigDecimal nowPrice) {
        this.nowPrice = nowPrice;
    }

    @Override
	public String toString() {
	    return "OrderTreatDetail [createTime=" + createTime + ", creater=" + creater
		   + ", note=" + note + ", orderId=" + orderId + ", type=" + type + ", originalPrice=" + originalPrice + ", nowPrice=" + nowPrice + "]";
	}
}