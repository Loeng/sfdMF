package com.ekfans.base.store.dto;

/**
* @ClassName: AppraiseContDto
* @Description: TODO(显示 评价管理 评价的统计 )
* @author 成都易科远见科技有限公司
* @date 2014-5-12 上午9:29:13
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class AppraiseContDto {
    //评价类型
    private String type;
    //一周之内的数据
    private int weekNumber;
    //一个月之内的数据
    private int monthNumber;
    //六个月之内的数据
    private int sixNumber;
    //六个月之前的数据
    private int sixAgoNumber;
    //总数量
    private int allNumber;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    public int getSixNumber() {
        return sixNumber;
    }

    public void setSixNumber(int sixNumber) {
        this.sixNumber = sixNumber;
    }

    public int getSixAgoNumber() {
        return sixAgoNumber;
    }

    public void setSixAgoNumber(int sixAgoNumber) {
        this.sixAgoNumber = sixAgoNumber;
    }

    public int getAllNumber() {
        return allNumber;
    }

    public void setAllNumber(int allNumber) {
        this.allNumber = allNumber;
    }
    
    
}
