package com.ekfans.base.wfOrder.service;

import java.util.List;

import com.ekfans.base.wfOrder.model.WfOrderPrice;

public interface IWfOrderPriceService {
	/**
	 * 根据危废订单明细ID获取品位详情
	 * 
	 * @param wfOrderId
	 * @return
	 */
	public List<WfOrderPrice> getWfOrderPrices(String detailId);
	
	
}
