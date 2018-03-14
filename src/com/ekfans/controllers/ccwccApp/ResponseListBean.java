package com.ekfans.controllers.ccwccApp;

import java.util.Collection;

/**
 * Created by liuguoyu on 2017/4/12.
 */
public class ResponseListBean {
    private int totalPage;
    private int currentPage;
    private Collection itemList;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Collection getItemList() {
        return itemList;
    }

    public void setItemList(Collection itemList) {
        this.itemList = itemList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
