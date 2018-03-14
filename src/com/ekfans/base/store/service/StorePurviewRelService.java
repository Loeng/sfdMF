package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStorePurviewRelDao;
import com.ekfans.base.store.model.StorePurviewRel;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 店铺权限与角色关联实现Service
 * 
 * @Title: StorePurviewService.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-21 上午11:35:38 
 * @version V1.0
 */
@Service
@SuppressWarnings("unchecked")
public class StorePurviewRelService implements IStorePurviewRelService {

	@Autowired
	private IStorePurviewRelDao storePurviewRelDao;

	/**
	 * 新增店铺权限关联信息
	 */
	public boolean addStorePurviewRel(StorePurviewRel spr) {
		if (spr == null) {
			return false;
		}
		try {
			// 调用DAO方法添加
			storePurviewRelDao.addBean(spr);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 根据id删除
	 */
	public boolean delete(String id) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			// 调用SERVICE的方法删除店铺
			storePurviewRelDao.deleteById(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改
	 */
	public boolean modify(StorePurviewRel spr) {
		// 如果传进来的店铺对象为空，则返回false
		if (spr == null) {
			return false;
		}
		try {
			// 调用DAO的方法修改店铺
			storePurviewRelDao.updateBean(spr);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据roleId删除对应数据
	 */
	public boolean getRoleIdDelete(String roleId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" delete spr from StorePurviewRel as spr where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 如果roleId不为空
		if (!StringUtil.isEmpty(roleId)) {
			// 添加查询条件
			sql.append(" and spr.roleId = :roleId");
			paramMap.put("roleId", roleId);
		}
		try {
			storePurviewRelDao.delete(sql.toString(), paramMap);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据roleId得到集合
	 */
	public List<StorePurviewRel> getStorePurviewRelByRoleId(String roleId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select spr from StorePurviewRel as spr where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 如果roleId不为空
		if (!StringUtil.isEmpty(roleId)) {
			// 添加查询条件
			sql.append(" and spr.roleId = :roleId");
			paramMap.put("roleId", roleId);
		}
		try {
			List<StorePurviewRel> list = storePurviewRelDao.list(sql.toString(), paramMap);
			if (list == null || list.size() <= 0) {
				return null;
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
