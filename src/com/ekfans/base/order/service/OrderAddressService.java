package com.ekfans.base.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderAddressDao;
import com.ekfans.base.order.model.OrderAddress;
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
public class OrderAddressService implements IOrderAddressService {
	// 定义DAO
	@Autowired
	private IOrderAddressDao orderAddressDao;
	// 定义一个错误日志文件
	public static Logger log = LoggerFactory.getLogger(OrderAddressService.class);

	/**
	 * 添加
	 */
	public boolean add(OrderAddress orderAddress) {
		try {
			orderAddressDao.addBean(orderAddress);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * @Title: getOrderAddressByOrderId
	 * @Description: TODO根据orderId取得OrderAddress对象 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return OrderTreatDetail 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public OrderAddress getOrderAddressByOrderId(String orderId) {
		StringBuffer sql = new StringBuffer("select oa from OrderAddress as oa,OrderDetail as od where 1=1");
		sql.append(" and oa.orderId = od.orderId");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(orderId)) {
			sql.append(" and oa.orderId = :orderId");
			map.put("orderId", orderId);
		}
		try {
			List<OrderAddress> list = orderAddressDao.list(sql.toString(), map);
			if (list.size() > 0 && list != null) {
				return list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

}