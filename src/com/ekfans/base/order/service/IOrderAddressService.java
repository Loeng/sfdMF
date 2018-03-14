package com.ekfans.base.order.service;

import com.ekfans.base.order.model.OrderAddress;

/**
 * 订单发货地址实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IOrderAddressService {
    /**
     * 
    * @Title: add
    * @Description: TODO(添加订单地址)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param orderAddress
    * @return boolean 返回类型
    * @throws
     */
    public boolean add(OrderAddress orderAddress);
    
    /**
     * 
    * @Title: getOrderAddressByOrderId
    * @Description: TODO 根据orderId取得OrderAddress对象
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param orderId
    * @param @return    设定文件
    * @return OrderAddress    返回类型
    * @throws
     */
    public OrderAddress getOrderAddressByOrderId(String orderId);
}