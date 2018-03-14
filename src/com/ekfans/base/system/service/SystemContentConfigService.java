package com.ekfans.base.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.dao.ISystemContentConfigDao;
import com.ekfans.base.system.model.SystemContentConfig;

/**
 * 系统信息配置实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2014-4-25
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class SystemContentConfigService implements ISystemContentConfigService {
	public static Logger log = LoggerFactory.getLogger(SystemContentConfigService.class);
	// 定義DAO
	@Autowired
	private ISystemContentConfigDao systemContentConfigDao;

	// 将数据库的数据获取
	@Override
	public List<SystemContentConfig> getConfigList() {

		StringBuffer sql = new StringBuffer("select s from  SystemContentConfig as s where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<SystemContentConfig> list = systemContentConfigDao.list(sql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 向数据库中添加value值
	@Override
	public boolean addContentConfig(SystemContentConfig systemContentConfig) {
		if (systemContentConfig == null) {
			return false;
		}

		try {
			systemContentConfigDao.addBean(systemContentConfig);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 修改
	 */
	public boolean updateContentConfig(SystemContentConfig systemContentConfig) {
		try {
			//
			systemContentConfigDao.updateBean(systemContentConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 根据ID获取
	public SystemContentConfig getById(String id) {
		if (id == null) {
			return null;
		}
		try {
			SystemContentConfig systemContentConfig = (SystemContentConfig) systemContentConfigDao.get(id);
			return systemContentConfig;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 根据id删除
	public boolean delete(String id) {
		if (id == null) {
			return false;
		}
		try {
			systemContentConfigDao.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}