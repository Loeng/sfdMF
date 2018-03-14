package com.ekfans.base.order.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 退换货处理--实体类
 * 
 * @ClassName: Refund
 * @Description: 
 * @author 成都易科远见科技有限公司
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Refund extends BasicBean {

    private static final long serialVersionUID = -5245402381520680034L;
    // 退货人
    private String userId;
    // 订单id
    private String orderId;
    // 商品id
    private String productId;
    // 0:换货；1：退货
    private String type;
    // 0：缺货；1：拍错了；2：不想要了
    private String refundType;
    //0：等待卖家确认；1:确认,不同意退货/换货； 2：确认,退货/换货中；3：退货/换货完成；
    private String status;
    //创建时间
    private String createTime;
    //理由
    private String note;
    //图片一
    private String imageOne;
    //图片二
    private String imageTwo;
    //图片三
    private String imageThree;
    //图片四
    private String imageFour;
    
    // =========== 临时数据字段 ===========
    private BigDecimal totalPrice; // 商品小计
    private String receiptName; // 收货人姓名
    
    // =================== get set ===================
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageOne() {
        return imageOne;
    }

    public void setImageOne(String imageOne) {
        this.imageOne = imageOne;
    }

    public String getImageTwo() {
        return imageTwo;
    }

    public void setImageTwo(String imageTwo) {
        this.imageTwo = imageTwo;
    }

    public String getImageThree() {
        return imageThree;
    }

    public void setImageThree(String imageThree) {
        this.imageThree = imageThree;
    }

    public String getImageFour() {
        return imageFour;
    }

    public void setImageFour(String imageFour) {
        this.imageFour = imageFour;
    }

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getReceiptName() {
		return receiptName;
	}

	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}
    
}
