package com.ekfans.base.user.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行实体类
 * 
 * @Title: User.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-13
 * @version 1.0
 */
@Entity
public class Bank extends BasicBean {
	

	// 序列化
	private static final long serialVersionUID = 1L;

	// 银行名称
	private String bankName = "";
	// 银行所在地
	private String areaId = "";
	// 银行地址
	private String address = "";
	// 支行全称
	private String fullName = "";
	// LOGO
	private String logo = "";
    // 银行状态  0:下线,1:正常
    private String status = "0";
    // 前台显示状态  true:显示   false:不显示
    private Boolean forenStatus = false;
    // 是否支持余额支付 true:支持
    private Boolean accountPay = false;
    // 是否支持线上支付 true:支持
    private Boolean onlinePay = false;
    // 是否支持企业支付  true:支持
    private Boolean companyPay = false;
    // 是否支持个人支付  true:支持
    private Boolean commonPay = false;
    // 占位符,用于调用后台接口!
    private String perch = "";

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getForenStatus() {
        return forenStatus;
    }

    public void setForenStatus(Boolean forenStatus) {
        this.forenStatus = forenStatus;
    }

    public Boolean getAccountPay() {
        return accountPay;
    }

    public void setAccountPay(Boolean accountPay) {
        this.accountPay = accountPay;
    }

    public Boolean getOnlinePay() {
        return onlinePay;
    }

    public void setOnlinePay(Boolean onlinePay) {
        this.onlinePay = onlinePay;
    }

    public Boolean getCompanyPay() {
        return companyPay;
    }

    public void setCompanyPay(Boolean companyPay) {
        this.companyPay = companyPay;
    }

    public Boolean getCommonPay() {
        return commonPay;
    }

    public void setCommonPay(Boolean commonPay) {
        this.commonPay = commonPay;
    }

    public String getPerch() {
        return perch;
    }

    public void setPerch(String perch) {
        this.perch = perch;
    }
}
