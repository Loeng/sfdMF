package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.OrderLog;

public interface IOrderLogService {

	public List<OrderLog> getOrderLogs(String orderId);
}
