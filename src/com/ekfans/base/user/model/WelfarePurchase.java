package com.ekfans.base.user.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 
* @ClassName: WelfarePurchase
* @Description: TODO 福利采购
* @author ekfans
* @date 2014-11-21 上午9:29:19
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class WelfarePurchase extends BasicBean {

    private static final long serialVersionUID = 1L;

    // 用户id
    private String userId = "";

    // 商品id
    private String productId = "";

    // 店铺id
    private String storeId = "";

    // 商品编号
    private String productNo = "";

    // 期望到货时间
    private String reciveTime = "";

    // 数量
    private int quantity = 0;

    // 企业名
    private String companyName = "";

    // 联系人
    private String linkMan = "";

    // 手机
    private String mobile = "";

    // 联系时间
    private String linkTime = "";

    // 状态 0待处理 1处理完 2关闭
    private String status = "0";

    // 创建时间
    private String createTime = "";

    // 更新时间
    private String updateTime = "";
    
    // 备注
    private String note = "";
    
    // 虚拟数据
    private String unit = "";
    
    // 核心企业名
    private String storeName = "";
    
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getReciveTime() {
        return reciveTime;
    }

    public void setReciveTime(String reciveTime) {
        this.reciveTime = reciveTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLinkTime() {
        return linkTime;
    }

    public void setLinkTime(String linkTime) {
        this.linkTime = linkTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}
