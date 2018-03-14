package com.ekfans.base.wfOrder.service;

import com.ekfans.base.wfOrder.model.WfOrderJiudingPayRel;

public interface IWfOrderJiudingPayRelService {

	/**
	 * 创建支付关联Id
	 * @return
	 */
	WfOrderJiudingPayRel create(String wfOrderId);

	/**
	 * 通过引用id查询危废id
	 * @param relId
	 * @return
	 */
	String getWfOrderIdByRelId(String relId);

}
