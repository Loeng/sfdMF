package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreRoleDao;
import com.ekfans.base.store.model.StoreRole;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 店铺角色实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
public class StoreRoleService implements IStoreRoleService {
	
	private Logger log = LoggerFactory.getLogger(StoreRoleService.class);
	@Autowired
	private IStoreRoleDao storeRoleDao;

	/**
	 * 添加角色
	 */
	public boolean addRole(StoreRole role) {
		// 如果角色为空，返回false
		if (role == null) {
			return false;
		}
		try {
			// 设置创建时间为当前系统时间
			role.setCreateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法添加角色
			storeRoleDao.addBean(role);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据id查找角色
	 */
	public StoreRole getRoleById(String id) {
		// 如果id为空，返回null
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			// 调用DAO方法查找角色
			return (StoreRole) storeRoleDao.get(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据id删除角色
	 */
	public boolean deleteRole(String id) {
		// 如果id为空，返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		try {
			// 调用DAO方法删除角色
			storeRoleDao.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改角色
	 */
	public boolean modifyRole(StoreRole role) {
		try {
			// 设置修改时间为当前系统时间
			role.setUpdateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法修改角色
			storeRoleDao.updateBean(role);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查找角色列表
	 */
	@SuppressWarnings("unchecked")
	public List<StoreRole> listRole(Pager pager, String name) {

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select role from StoreRole as role where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and role.name like :name");
			paramMap.put("name", "%" + name + "%");
		}

		try {
			// 调用DAO方法查找角色
			List<StoreRole> list = storeRoleDao.list(pager, sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getRoleNameById(String roleId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select role.name from StoreRole as role where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(roleId)) {
			sql.append(" and role.id = :roleId");
			paramMap.put("roleId", roleId);
		}

		try {
			// 调用DAO方法查找角色
			List list = storeRoleDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return (String) list.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<StoreRole> getRoleByName(String name){
		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		String hql = "from StoreRole where name=:name";
		// setting param
		params.put("name", name);
		
		try {
			List<StoreRole> list = this.storeRoleDao.list(hql, params);
			if(list == null || list.size() <= 0){
				return null;
			}
			
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
}