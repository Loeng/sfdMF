package com.ekfans.base.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.dao.ISystemParamConfigDao;
import com.ekfans.base.system.model.SystemParamConfig;
import com.ekfans.pub.util.StringUtil;

/**
 * 系统参数配置实现Service
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
public class SystemParamConfigService implements ISystemParamConfigService {
	// 定义Log
	public static Logger log = LoggerFactory.getLogger(SystemParamConfigService.class);
	// 定義DAO
	@Autowired
	private ISystemParamConfigDao systemParamConfigDao;

	@Override
	public List<SystemParamConfig> getConfigList(String type) {

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select spc from SystemParamConfig as spc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果参数类型不为空添加查询条件
		if (!StringUtil.isEmpty(type)) {
			sql.append(" and spc.type  = :type");
			paramMap.put("type", type);
		}

		try {
			// 调用DAO方法查找系统参数配置
			List<SystemParamConfig> list = systemParamConfigDao.list(sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public SystemParamConfig getConfigById(String id) {
		// 如果id为空返回null
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			// 调用DAO方法查找系统参数配置
			SystemParamConfig config = (SystemParamConfig) systemParamConfigDao.get(id);
			return config;
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean updateConfig(SystemParamConfig config) {
		// 如果参数配置为空返回false
		if (config == null) {
			return false;
		}
		try {
			// 更新参数配置返回true
			systemParamConfigDao.updateBean(config);
			return true;
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}
		return false;
	}

}
