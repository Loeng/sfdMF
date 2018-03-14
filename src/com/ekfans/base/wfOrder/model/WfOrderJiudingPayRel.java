package com.ekfans.base.wfOrder.model;

import javax.persistence.Entity;

/**
 * @author lh
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 * @ClassName: ContractDetails
 * @Description: 因危废类订单可能存在多次支付的情况，并且九鼎支付接口只支持一个订单ID对应一条支付记录，故危废订单需要关联一个新的id来发起支付(危费订单九鼎支付id关联关系表)
 * @date 2017年11月17日17:08:38
 */
@SuppressWarnings("serial")
@Entity
public class WfOrderJiudingPayRel {
    // 订单支付用id
    private String id;
    // 危废订单id
    private String wfOrderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWfOrderId() {
        return wfOrderId;
    }

    public void setWfOrderId(String wfOrderId) {
        this.wfOrderId = wfOrderId;
    }
}
