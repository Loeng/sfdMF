package com.ekfans.base.order.model;

import java.math.BigDecimal;

import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 订单明细--实体类
 * 
 * @ClassName: BuyerCompany
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class OrderDetail extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 订单ID -- 对应订单表(Order)主键
	private String orderId = "";
	// 店铺ID -- 对应店铺表(Store)主键
	private String storeId = "";
	// 会员ID -- 对应会员表(User)主键
	private String userId = "";
	// 商品ID -- 对应商品牌表(Product)主键
	private String productId = "";
	// 商品扩展字段详情ID
	private String productInfoDetailId = "";
	// 商品名称
	private String productName = "";
	// 购买数量
	private int quantity = 0;
	// 单价
	private BigDecimal price = new BigDecimal("0.00");
	// 小计
	private BigDecimal totalPrice = new BigDecimal("0.00");
	// 商品模版ID
	private String tempId = "";
	// 模版字段名
	private String tempName = "";
	// 模版值
	private String tempValue = "";
	// 可获得积分
	private int integral = 0;
	// 商品中图
	private String productImage = "";
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
    // 商品编号
    private String productNo = "";
	//虚拟的数据
	// 类型：1，好评，2，中评，3差评
	private String type;
	
	// 商品编号
	private String productNumber = "";
	
	// 商品小图
	private String smallPicture = "";
	
	
	// 模拟数据
	   // 成交时间
	private String dealTime = "";
	   // 购买者
	private String userName = "";
	
	// 模拟数据
	//商品原图
	private String picture = "";
	//用户头像
	private String headPhoto = "";
	
	private ProductInfoDetail productInfoDetail;
	
	
	public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getDealTime() {
	    return dealTime;
	}

	public String getUserName() {
	    return userName;
	}

	public void setUserName(String userName) {
	    this.userName = userName;
	}

	public void setDealTime(String dealTime) {
	    this.dealTime = dealTime;
	}

	public String getSmallPicture()
	{
	    return smallPicture;
	}

	public void setSmallPicture(String smallPicture)
	{
	    this.smallPicture = smallPicture;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getTempValue() {
		return tempValue;
	}

	public void setTempValue(String tempValue) {
		this.tempValue = tempValue;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}
	
	public String getProductInfoDetailId() {
        return productInfoDetailId;
    }

    public void setProductInfoDetailId(String productInfoDetailId) {
        this.productInfoDetailId = productInfoDetailId;
    }

    public String getType()
	{
	    return type;
	}

	public void setType(String type)
	{
	    this.type = type;
	}

	public String getProductNumber()
	{
	    return productNumber;
	}

	public void setProductNumber(String productNumber)
	{
	    this.productNumber = productNumber;
	}

	public String getPicture() {
	    return picture;
	}

	public void setPicture(String picture) {
	    this.picture = picture;
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

    public ProductInfoDetail getProductInfoDetail() {
        return productInfoDetail;
    }

    public void setProductInfoDetail(ProductInfoDetail productInfoDetail) {
        this.productInfoDetail = productInfoDetail;
    }
	
}