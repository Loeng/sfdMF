package com.ekfans.controllers.ccwccApp;

import java.math.BigDecimal;

/**
 * Created by liuguoyu on 2017/4/12.
 */
public class AppPreciousBean {
    private String id;
    private String categoryId;
    // 品名
    private String name;
    // 规格
    private String spec;
    // 计量单位
    private String unit;
    // 均价
    private BigDecimal price;
    // 价格区间：起始价
    private BigDecimal startPrice;
    // 价格区间：结束价
    private BigDecimal endPrice;
    // 行情日
    private String dateTime;
    // 涨跌金额
    private double riseAndDrop;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getRiseAndDrop() {
        return riseAndDrop;
    }

    public void setRiseAndDrop(double riseAndDrop) {
        this.riseAndDrop = riseAndDrop;
    }
}
