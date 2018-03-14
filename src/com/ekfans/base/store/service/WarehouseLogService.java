package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IWarehouseLogDao;
import com.ekfans.base.store.model.WarehouseLog;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 仓库日志实现Service
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
public class WarehouseLogService implements IWarehouseLogService {

	// 定义DAO
	@Resource
	private IWarehouseLogDao warehouseLogDao;

	// 定义错误处理日志
	private Logger log = LoggerFactory.getLogger(WarehouseLogService.class);

	@Override
	public Boolean add(WarehouseLog whl) {
		try {
			warehouseLogDao.addBean(whl);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<WarehouseLog> list(Pager pager, String name, String status) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select whl.id,whl.number,whl.type,p.name,whl.createTime from WarehouseLog as whl,Product as p where 1=1");

		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" and whl.productId = p.id");
		// 如果查询条件输入了status，添加查询条件
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and whl.type = :status");
			paramMap.put("status", Boolean.parseBoolean(status));
		}

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and p.name like :name");
			paramMap.put("name", "%" + name + "%");
		}
		sql.append(" order by whl.createTime desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = warehouseLogDao.list(pager, sql.toString(),
					paramMap);
			List<WarehouseLog> WarehouseLogs = new ArrayList<WarehouseLog>();
			for (Object[] object : list) {
				WarehouseLog whl = new WarehouseLog();
				whl.setId((String) object[0]);
				whl.setNumber((Integer) object[1]);
				whl.setType((Boolean) object[2]);
				whl.setProductId((String) object[3]);
				whl.setCreateTime((String) object[4]);
				WarehouseLogs.add(whl);
			}

			return WarehouseLogs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public WarehouseLog getWarehouseLogById(String id) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select whl.id,whl.number,whl.type,p.name,whl.createTime,whl.note,whl.pic,m.name from WarehouseLog as whl,Product as p,Manager as m where 1=1");

		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" and whl.productId = p.id");
		sql.append(" and whl.creator = m.id");
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(id)) {
			sql.append(" and whl.id = :id");
			paramMap.put("id", id);
		} else {
			return null;
		}
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = warehouseLogDao.list(null, sql.toString(),
					paramMap);
			List<WarehouseLog> WarehouseLogs = new ArrayList<WarehouseLog>();
			for (Object[] object : list) {
				WarehouseLog whl = new WarehouseLog();
				whl.setId((String) object[0]);
				whl.setNumber((Integer) object[1]);
				whl.setType((Boolean) object[2]);
				whl.setProductId((String) object[3]);
				whl.setCreateTime((String) object[4]);
				whl.setNote((String) object[5]);
				whl.setPic((String) object[6]);
				whl.setCreator((String) object[7]);
				WarehouseLogs.add(whl);
			}

			if (WarehouseLogs != null && WarehouseLogs.size() > 0) {
				return WarehouseLogs.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}