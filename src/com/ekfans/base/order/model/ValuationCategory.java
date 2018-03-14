package com.ekfans.base.order.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

@Entity
public class ValuationCategory extends BasicBean{
    
    private String specName;//含量名称
    
    // 1--启用  0--禁用
    private String status;//是否启用

    private String createTime;//创建时间

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
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

}
