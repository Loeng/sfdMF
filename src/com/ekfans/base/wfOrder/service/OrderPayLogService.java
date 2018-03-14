package com.ekfans.base.wfOrder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.wfOrder.dao.IOrderPayLogDao;
import com.ekfans.base.wfOrder.model.OrderPayLog;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class OrderPayLogService implements IOrderPayLogService {
	Logger log = LoggerFactory.getLogger(OrderPayLogService.class);

	@Autowired
	public IOrderPayLogDao logDao;

	/**
	 * 新增支付日志
	 * 
	 * @param payLog
	 * @return
	 */
	@Override
	public OrderPayLog saveOrderPayLog(OrderPayLog payLog) {
		if (payLog == null) {
			return null;
		}
		try {
			logDao.addBean(payLog);
			return payLog;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据ID获取订单对象
	 * 
	 * @param logId
	 * @return
	 */
	@Override
	public OrderPayLog getLogById(String logId) {
		if (StringUtil.isEmpty(logId)) {
			return null;
		}
		try {
			OrderPayLog payLog = (OrderPayLog) logDao.get(logId);
			return payLog;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 更新状态
	 * 
	 * @param status
	 * @param logId
	 * @return
	 */
	@Override
	public Boolean updateLogStatus(String status, OrderPayLog payLog) {
		if (payLog == null) {
			return false;
		}

		try {
			if (!StringUtil.isEmpty(status)) {
				payLog.setStatus(status);
			}
			logDao.updateBean(payLog);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	/**
	 * 根据订单ID和支付类型获取对象
	 * 
	 * @param orderId
	 * @param payType
	 * @return
	 */
	@Override
	public OrderPayLog getLogByOrderIdAndPayType(String orderId, String orderType, String payType) {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(orderId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from OrderPayLog as log where 1=1");
		sql.append(" and log.orderId = :orderId");
		paramMap.put("orderId", orderId);
		sql.append(" and log.payType = :payType");
		paramMap.put("payType", payType);
		sql.append(" and log.orderType = :orderType");
		paramMap.put("orderType", orderType);
		try {
			List<OrderPayLog> list = logDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
