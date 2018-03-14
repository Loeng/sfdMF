package com.ekfans.controllers.ccwccApp;

import java.math.BigDecimal;

/**
 * Created by liuguoyu on 2017/4/12.
 */
public class AppPreciousDetailBean {
    // 均价
    private BigDecimal price;
    // 行情日
    private String dateTime;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
