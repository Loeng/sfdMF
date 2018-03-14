package com.ekfans.base.product.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * --商品计价表类
 * 
 * @ClassName: ProductValuation
 * @Description:
 * @author ekfans
 * @date 2014-6-25 下午10:38:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ProductValuation extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	//商品ID
	private String productId = "";
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
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
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