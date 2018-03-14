package com.ekfans.base.wfOrder.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderDetail;

public interface IWfOrderDetailService {
	/**
	 * 根据订单ID获取订单明细集合
	 * 
	 * @param wfOrderId
	 * @return
	 */
	public List<WfOrderDetail> getDetailsByWfOrderId(String wfOrderId);

	/**
	 * 新增或修改保存订单明细
	 * 
	 * @param order
	 * @param request
	 * @param response
	 * @return
	 */
	public Boolean saveOrUpdateDetail(WfOrder order, HttpServletRequest request, HttpServletResponse response);

}
