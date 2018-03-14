package com.ekfans.base.order.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 
* @ClassName: Refund
* @Description: TODO询价明细表
* @author 成都易科远见科技有限公司
* @date 2014-5-12 下午2:47:53
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class InquiryDetail  extends BasicBean{

    // 序列化
    private static final long serialVersionUID = 1L;
    // 询价表ID
    private String inquiryId = "";
    // 买家ID
    private String buyersId = "";
    // 卖家ID
    private String sellersId = "";
    // 卖家备注
    private String sellersNote = "";
    // 买家备注
    private String buyersNote = "";
    public String getInquiryId() {
        return inquiryId;
    }
    public void setInquiryId(String inquiryId) {
        this.inquiryId = inquiryId;
    }
    public String getBuyersId() {
        return buyersId;
    }
    public void setBuyersId(String buyersId) {
        this.buyersId = buyersId;
    }
    public String getSellersId() {
        return sellersId;
    }
    public void setSellersId(String sellersId) {
        this.sellersId = sellersId;
    }
    public String getSellersNote() {
        return sellersNote;
    }
    public void setSellersNote(String sellersNote) {
        this.sellersNote = sellersNote;
    }
    public String getBuyersNote() {
        return buyersNote;
    }
    public void setBuyersNote(String buyersNote) {
        this.buyersNote = buyersNote;
    }
    
    
    
}
