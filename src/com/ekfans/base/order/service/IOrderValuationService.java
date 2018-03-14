package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.OrderValuation;

public interface IOrderValuationService {
    /**
    * @Title: addOrderValuation
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return boolean 返回类型
    * @throws
     */
    public boolean addOrderValuation(OrderValuation ov);
    
    public List<OrderValuation> getOrderValuationById(String id);
}
