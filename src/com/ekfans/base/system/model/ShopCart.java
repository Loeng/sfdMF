package com.ekfans.base.system.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 
* @ClassName: ShopCart
* @Description: TODO(购物车)
* @author 成都易科远见科技有限公司
* @date 2014-5-19 上午11:25:48
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Entity
public class ShopCart extends BasicBean{
 // 序列化
    private static final long serialVersionUID = 1L;
    
    //用户id
    private String userId = "";
    
    //商品id
    private String productId = "";
    
    //购物的数量
    private int productQuantity = 0; 
    
    //是否选择结算
    private boolean checked = false;
    
    // 模板扩展字段1
    private String infoName1 = "";
    // 值1
    private String infoValue1 = "";
    // 模板扩展字段2
    private String infoName2 = "";
    // 值2
    private String infoValue2 = "";
    // 模板扩展字段3
    private String infoName3 = "";
    // 值3
    private String infoValue3 = "";
    // 模板扩展字段4
    private String infoName4 = "";
    // 值4
    private String infoValue4 = "";
    private String productInfoDetailId = "";
    //显示的图片
    private String picture = "";
    
    //显示的商品名称
    private String productName = "";
    
    //显示的商场价格
    private BigDecimal unitPrice = new BigDecimal("0.00");
    
    //显示的店铺名字
    private String name = "";
    
    //显示的库存
    private int quantity = 0;
    
    //如果是同一家店铺值为true
    private boolean isStore = false;
    
    //用于显示的市场价格
    private BigDecimal listPrice = new BigDecimal("0.00");
    
    //用于显示的优惠价格
    private BigDecimal price = new BigDecimal("0.00");
    
    //用于显示的小计
    private BigDecimal totalPrice = new BigDecimal("0.00");
    
    //总的优惠价格
    private BigDecimal tempTotalPrefePrice = new BigDecimal("0.00");
    
    //用于显示的storeId
    private String storeId = "";
    
    //邮费
    private BigDecimal fare = new BigDecimal("0.00");
    
    //用于显示的店铺下的商品
    private List<ShopCart> childList = null;
    
    //用于显示的模板
    private List<ProductInfoDetail> childDetail = null;
    
    private String inquiryId = "";
    
    public String getInfoName1() {
        return infoName1;
    }

    public void setInfoName1(String infoName1) {
        this.infoName1 = infoName1;
    }

    public String getInfoValue1() {
        return infoValue1;
    }

    public void setInfoValue1(String infoValue1) {
        this.infoValue1 = infoValue1;
    }

    public String getInfoName2() {
        return infoName2;
    }

    public void setInfoName2(String infoName2) {
        this.infoName2 = infoName2;
    }

    public String getInfoValue2() {
        return infoValue2;
    }

    public void setInfoValue2(String infoValue2) {
        this.infoValue2 = infoValue2;
    }

    public String getInfoName3() {
        return infoName3;
    }

    public void setInfoName3(String infoName3) {
        this.infoName3 = infoName3;
    }

    public String getInfoValue3() {
        return infoValue3;
    }

    public void setInfoValue3(String infoValue3) {
        this.infoValue3 = infoValue3;
    }

    public String getInfoName4() {
        return infoName4;
    }

    public void setInfoName4(String infoName4) {
        this.infoName4 = infoName4;
    }

    public String getInfoValue4() {
        return infoValue4;
    }

    public void setInfoValue4(String infoValue4) {
        this.infoValue4 = infoValue4;
    }

    public List<ProductInfoDetail> getChildDetail() {
        return childDetail;
    }

    public void setChildDetail(List<ProductInfoDetail> childDetail) {
        this.childDetail = childDetail;
    }

    public String getProductInfoDetailId() {
        return productInfoDetailId;
    }

    public void setProductInfoDetailId(String productInfoDetailId) {
        this.productInfoDetailId = productInfoDetailId;
    }

    public BigDecimal getTempTotalPrefePrice() {
        return tempTotalPrefePrice;
    }

    public void setTempTotalPrefePrice(BigDecimal tempTotalPrefePrice) {
        this.tempTotalPrefePrice = tempTotalPrefePrice;
    }

    public List<ShopCart> getChildList() {
        return childList;
    }

    public void setChildList(List<ShopCart> childList) {
        this.childList = childList;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public boolean isStore() {
        return isStore;
    }

    public void setStore(boolean isStore) {
        this.isStore = isStore;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public String getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(String inquiryId) {
        this.inquiryId = inquiryId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }
}
