package com.ekfans.base.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderLogDao;
import com.ekfans.base.order.model.OrderLog;
import com.ekfans.pub.util.StringUtil;

/**
 * 订单发货地址业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class OrderLogService implements IOrderLogService {
	// 定义DAO
	@Autowired
	private IOrderLogDao orderLogDao;

	// 定义一个错误日志文件
	public static Logger log = LoggerFactory.getLogger(OrderLogService.class);

	@Override
	public List<OrderLog> getOrderLogs(String orderId) {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(orderId)) {
			return null;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from OrderLog as log where 1=1");
		sql.append(" and log.orderId = :orderId");
		paramMap.put("orderId", orderId);
		sql.append(" order by log.createTime desc");

		try {
			List<OrderLog> logList = orderLogDao.list(sql.toString(), paramMap);
			return logList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}