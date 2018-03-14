package com.ekfans.base.order.model;

import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;
/**
 * 危废品订单实体
* @ClassName: OrderWfp
* @Description: TODO(这里用一句话描述这个类的作用)
* @author Jin
* @date 2015-1-15 下午4:57:13
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Entity
public class OrderWfp extends BasicBean{

    private String storeId;//创建订单企业ID(卖家ID)
    private String buyStoreId;//买家ID
    private String ysStoreId;//运输企业ID
    private String orgId;//创建订单企业ID
    private String contractId;//合同ID
    private String scrapId;//报废申请ID
    private String wfpName;//危废品名称
    private String note;//备注
    private String wfpNumber;//危废品数量
    private String totalPrice;//订单总价
    private String price ;//单位计价价格
    private String unit;//单位计价单位
    private String habitat;//收货地址
    private String deliveAddress;//收货详细地址
    private String deliveType;//交货方式
    private String deliveData;//交货日期
    private String shoppingStatus ="0";// 0 买家填写收货地址等  1 运输企业确认中 2 运输中 3 处置企业接收(订单完成)  发货状态 
    private String status="0";//订单状态  0未完成 1已完成
    private String serviceStatus="0";//服务状态
    private String invoice ="0";//是否需要发票
    private String invoiceType;//发票类型
    private String invoiceContent;//发票内容
    private String invoiceTitle;//发票抬头
    private String logisticsNo;//物流单号
    private String createTime;//创建时间
    private String payMent;//支付方式
    private String actualPrice;//运费总价
    private String orderNumber ;//订单号
    private String  oneCouplet;//第一联单 供应商上传
    private String  twoCouplet;//第二联单 供应商上传
    private String  threeCouplet;//第三联单 运输企业上传
    private String  fourCouplet;//第四联单 处置企业上传
    private String  fiveCouplet;//第五联单 处置企业上传
    
    private String receiver;//收货人
    private String receiverTel;//收货人联系方式
    private String  receiverAddress;//收货地址
    
    /*****************虚拟数据****************************/
    private List<OrderValuation> valationList ;//订单含量集合
    private String contractPath;//订单合同路径
    private String ysName;//运输企业名称    
    private String second;//乙方
    
    public String getHabitat() {
        return habitat;
    }
    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }
    public String getScrapId() {
        return scrapId;
    }
    public void setScrapId(String scrapId) {
        this.scrapId = scrapId;
    }
    public String getBuyStoreId() {
        return buyStoreId;
    }
    public void setBuyStoreId(String buyStoreId) {
        this.buyStoreId = buyStoreId;
    }
    public String getSecond() {
        return second;
    }
    public void setSecond(String second) {
        this.second = second;
    }
    public String getYsName() {
        return ysName;
    }
    public void setYsName(String ysName) {
        this.ysName = ysName;
    }
   
    public String getOneCouplet() {
        return oneCouplet;
    }
    public void setOneCouplet(String oneCouplet) {
        this.oneCouplet = oneCouplet;
    }
    public String getTwoCouplet() {
        return twoCouplet;
    }
    public void setTwoCouplet(String twoCouplet) {
        this.twoCouplet = twoCouplet;
    }
    public String getThreeCouplet() {
        return threeCouplet;
    }
    public void setThreeCouplet(String threeCouplet) {
        this.threeCouplet = threeCouplet;
    }
    public String getFourCouplet() {
        return fourCouplet;
    }
    public void setFourCouplet(String fourCouplet) {
        this.fourCouplet = fourCouplet;
    }
    public String getFiveCouplet() {
        return fiveCouplet;
    }
    public void setFiveCouplet(String fiveCouplet) {
        this.fiveCouplet = fiveCouplet;
    }
    public String getContractPath() {
        return contractPath;
    }
    public void setContractPath(String contractPath) {
        this.contractPath = contractPath;
    }
    public List<OrderValuation> getValationList() {
        return valationList;
    }
    public void setValationList(List<OrderValuation> valationList) {
        this.valationList = valationList;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getReceiverTel() {
        return receiverTel;
    }
    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }
    public String getReceiverAddress() {
        return receiverAddress;
    }
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }
    public String getWfpNumber() {
        return wfpNumber;
    }
    public void setWfpNumber(String wfpNumber) {
        this.wfpNumber = wfpNumber;
    }
   
    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public String getYsStoreId() {
        return ysStoreId;
    }
    public void setYsStoreId(String ysStoreId) {
        this.ysStoreId = ysStoreId;
    }
    
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
    public String getWfpName() {
        return wfpName;
    }
    public void setWfpName(String wfpName) {
        this.wfpName = wfpName;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getDeliveAddress() {
        return deliveAddress;
    }
    public void setDeliveAddress(String deliveAddress) {
        this.deliveAddress = deliveAddress;
    }
    public String getDeliveType() {
        return deliveType;
    }
    public void setDeliveType(String deliveType) {
        this.deliveType = deliveType;
    }
    public String getDeliveData() {
        return deliveData;
    }
    public void setDeliveData(String deliveData) {
        this.deliveData = deliveData;
    }
    public String getShoppingStatus() {
        return shoppingStatus;
    }
    public void setShoppingStatus(String shoppingStatus) {
        this.shoppingStatus = shoppingStatus;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getServiceStatus() {
        return serviceStatus;
    }
    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
    public String getInvoice() {
        return invoice;
    }
    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
    public String getInvoiceType() {
        return invoiceType;
    }
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
    public String getInvoiceContent() {
        return invoiceContent;
    }
    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }
    public String getInvoiceTitle() {
        return invoiceTitle;
    }
    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }
    public String getLogisticsNo() {
        return logisticsNo;
    }
    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getPayMent() {
        return payMent;
    }
    public void setPayMent(String payMent) {
        this.payMent = payMent;
    }
    public String getActualPrice() {
        return actualPrice;
    }
    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    
}
