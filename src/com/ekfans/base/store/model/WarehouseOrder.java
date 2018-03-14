package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 仓储提货单表
 * 
 * @Title: Store.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
public class WarehouseOrder extends BasicBean {

    private static final long serialVersionUID = 1L;
	//商品id
    private String productId = "";
    //仓库id
    private String warehouseId = "";
    //店铺id
    private String storeId = "";
    //数量
    private String number = "";
    //单位
    private String unit = "";
    //订单id
    private String orderId = "";
    //创建时间
    private String createTime = "";
    //状态(true:已出库，false:未出库)
    private boolean status = false;
    //物流公司id
    private String wuliuId = "";
    //物流公司名字
    private String wuliuName = "";
    //以下伪造数据
    private String productName = "";
    private String wareHouseName = "";
    
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getWareHouseName() {
        return wareHouseName;
    }
    public void setWareHouseName(String wareHouseName) {
        this.wareHouseName = wareHouseName;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getWuliuId() {
        return wuliuId;
    }
    public void setWuliuId(String wuliuId) {
        this.wuliuId = wuliuId;
    }
    public String getWuliuName() {
        return wuliuName;
    }
    public void setWuliuName(String wuliuName) {
        this.wuliuName = wuliuName;
    }
    
    
}