package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IWarehouseOrderDao;
import com.ekfans.base.store.model.WarehouseOrder;
import com.ekfans.pub.util.StringUtil;

/**
 * 提货单实现Service
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
public class WarehouseOrderService implements IWarehouseOrderService {

	// 定义DAO
	@Resource
	private IWarehouseOrderDao warehouseOrderDao;

	// 定义错误处理日志
	private Logger log = LoggerFactory.getLogger(WarehouseOrderService.class);

	@Override
	public WarehouseOrder getWarehouseOrderById(String id) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select p.id,p.name,wh.id,wh.name,who.number,who.id from WarehouseOrder as who,Warehouse as wh,Product as p where 1=1");

		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" and wh.id = who.warehouseId");
		sql.append(" and who.productId = p.id");
		sql.append(" and who.status = false");
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(id)) {
			sql.append(" and who.id = :id");
			paramMap.put("id", id);
		} else {
			return null;
		}
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = warehouseOrderDao.list(null, sql.toString(),
					paramMap);
			List<WarehouseOrder> WarehouseOrders = new ArrayList<WarehouseOrder>();
			for (Object[] object : list) {
				WarehouseOrder who = new WarehouseOrder();
				who.setProductId((String) object[0]);
				who.setProductName((String) object[1]);
				who.setWarehouseId((String) object[2]);
				who.setWareHouseName((String) object[3]);
				who.setNumber((String) object[4]);
				who.setId((String) object[5]);
				WarehouseOrders.add(who);
			}

			if (WarehouseOrders != null && WarehouseOrders.size() > 0) {
				return WarehouseOrders.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public WarehouseOrder getById(String id) {
		try {
			return (WarehouseOrder) warehouseOrderDao.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean update(WarehouseOrder who) {
		try {
			warehouseOrderDao.updateBean(who);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}