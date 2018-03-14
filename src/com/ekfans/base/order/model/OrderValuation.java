package com.ekfans.base.order.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

public class OrderValuation extends BasicBean {
	
    // 序列化
    private static final long serialVersionUID = 1L;
    //商品ID
    private String orderId = "";
    //计价分类ID
    private String valuationId = "";
    //计价单价
    private BigDecimal valuationPrice = new BigDecimal("0.00");
    //计价单位
    private String valuationUnit = "";
    //分类名称
    private String specName = "";
    //计价数量
    private BigDecimal valuationNumber = new BigDecimal("0.00");
    //计价创建时间
    private String createTime = "";
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getValuationId() {
        return valuationId;
    }
    public void setValuationId(String valuationId) {
        this.valuationId = valuationId;
    }
    
    
    public BigDecimal getValuationPrice() {
        return valuationPrice;
    }
    public void setValuationPrice(BigDecimal valuationPrice) {
        this.valuationPrice = valuationPrice;
    }
    public String getValuationUnit() {
        return valuationUnit;
    }
    public void setValuationUnit(String valuationUnit) {
        this.valuationUnit = valuationUnit;
    }
    public String getSpecName() {
        return specName;
    }
    public void setSpecName(String specName) {
        this.specName = specName;
    }
    public BigDecimal getValuationNumber() {
        return valuationNumber;
    }
    public void setValuationNumber(BigDecimal valuationNumber) {
        this.valuationNumber = valuationNumber;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
}
