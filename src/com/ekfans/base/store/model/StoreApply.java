package com.ekfans.base.store.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 
* @ClassName: StoreApply
* @Description: TODO(申请店铺的实体类)
* @author ekfans
* @date 2014-5-16 上午11:12:23
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Entity
public class StoreApply extends BasicBean{
    // 序列化
    private static final long serialVersionUID = 1L;
    //企业名称
    private String companyName = "";
    //联系人
    private String contacts = "";
    //手机
    private String mobile = "";
    //邮箱
    private String email = "";
    //详细地址
    private String address = "";
    //邮政编码
    private String zipCode = "";
    //企业网站
    private String domain = "";
    //备注
    private String note = "";
    //创建时间
    private String createTime = "";
    //修改时间
    private String updateTime = "";
    //操作人
    private String operation = "";
    //审核状态
    private int status = 0;
    
    //伪造数据用于显示
   
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
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getContacts() {
        return contacts;
    }
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
