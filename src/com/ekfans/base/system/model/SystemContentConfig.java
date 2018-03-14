package com.ekfans.base.system.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 系统信息配置实体类
 * 
 * @Title: SystemContentConfig.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2014-4-25
 * @version 1.0
 */
@Entity
public class SystemContentConfig extends BasicBean {
    
    private static final long serialVersionUID = 1L;
    
    // 名称
    private String key = "";
    
    // 值
    private String value = "";
    
    // 值的类型
    private String valueType = "";
    
    // 水印位置
    private String markArea = "";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getMarkArea() {
        return markArea;
    }

    public void setMarkArea(String markArea) {
        this.markArea = markArea;
    }
}
