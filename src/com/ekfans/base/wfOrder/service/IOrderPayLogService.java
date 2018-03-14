package com.ekfans.base.wfOrder.service;

import com.ekfans.base.wfOrder.model.OrderPayLog;

public interface IOrderPayLogService {
	/**
	 * 新增支付日志
	 * 
	 * @param payLog
	 * @return
	 */
	public OrderPayLog saveOrderPayLog(OrderPayLog payLog);

	/**
	 * 根据ID获取订单对象
	 * 
	 * @param logId
	 * @return
	 */
	public OrderPayLog getLogById(String logId);

	/**
	 * 更新状态
	 * 
	 * @param status
	 * @param logId
	 * @return
	 */
	public Boolean updateLogStatus(String status, OrderPayLog payLog);

	/**
	 * 根据订单ID和支付类型获取对象
	 * 
	 * @param orderId
	 * @param payType
	 * @return
	 */
	public OrderPayLog getLogByOrderIdAndPayType(String orderId, String orderType,String payType);

}
