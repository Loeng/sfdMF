package com.ekfans.base.user.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 会员积分日志表
 * 
 * @ClassName: IntegralLog
 * @Description: 会员积分日志表
 * @author liuguoyu
 * @date 2014-3-31 下午05:52:58
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Entity
public class IntegralLog extends BasicBean {

    // 序列化
    private static final long serialVersionUID = 1L;

    // 会员主键
    private String userId = "";

    // 会员积分值
    private double integral = 0.00;

    // 订单主键
    private String orderId = "";

    // 积分类型 0:减少 1:增加
    private String type = "";

    // 积分来源
    private String source = "";

    // 备注
    private String note = "";

    // 创建时间
    private String createTime = "";

    // 类型
    private boolean status = false;

    // 积分有效期
    private String invalidTime = "";

    // 修改者
    private String operation = "";

    // 虚拟数据mannager的
    // 管理员真实姓名
    private String realName = "";

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
        this.integral = integral;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

}