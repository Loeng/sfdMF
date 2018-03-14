package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.OrderTreatDetail;

/**
 * 订单处理日志实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IOrderTreatDetailService {
    /**
     * 
    * @Title: add
    * @Description: TODO(增加订单日志)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param orderTreatDetail
    * @return boolean 返回类型
    * @throws
     */
    public boolean add(OrderTreatDetail orderTreatDetail);
    /**
     * 
    * @Title: getOrderTreatDetailByOrderId
    * @Description: TODO 根据orderId取得OrderTreatDetail对象
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param orderId
    * @param @return    设定文件
    * @return OrderTreatDetail    返回类型
    * @throws
     */
    public OrderTreatDetail getOrderTreatDetailByOrderId(String orderId);
    
    public boolean update(OrderTreatDetail orderTreatDetail);
    
    /**
     * 
    * @Title: getOrderTreatDetailListByOrderId
    * @Description: TODO(根据订单id跟踪订单信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param orderId
    * @return List<OrderTreatDetail> 返回类型
    * @throws
     */
    public List<OrderTreatDetail> getOrderTreatDetailListByOrderId(String orderId);
    
    /**
    * @Title: updateOrderTotalPriceGuessPrice
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param userName
    * @param type
    * @return Boolean 返回类型
    * @throws
     */
    public Boolean updateOrderTotalPriceGuessPrice(String orderId, String userName, String type, String price); 
}