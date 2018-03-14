package com.ekfans.base.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.dao.IShopRoleDao;
import com.ekfans.base.system.model.RolePurviewRel;
import com.ekfans.base.system.model.ShopRole;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 后台角色Service接口实现
 * 
 * @ClassName: ShopRoleService
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class ShopRoleService implements IShopRoleService {
	
	private Logger log = LoggerFactory.getLogger(ShopRoleService.class);
	@Resource
	private IShopRoleDao shopRoleDao;

	@Override
	public int addShopRole(ShopRole role, String[] purviewIds) {
		// 1:角色名称为空，2:角色名称6-20字符组成，3:保存失败，4:保存成功
		if(role == null){
			return 1;
		}
		if(StringUtil.isEmpty(role.getName())){
			return 1;
		}
		role.setName(role.getName().trim()); // 去除角色名称前后空格
		if((role.getName()).length() < 6 || (role.getName()).length() > 20){
			return 2;
		}
		
		Session session = null;
		Transaction tran = null;
		try {
			session = shopRoleDao.createSession();
			tran = session.beginTransaction();
			
			role.setCreateTime(DateUtil.getSysDateTimeString());
			shopRoleDao.addBean(role, session);
			
			if (purviewIds != null && purviewIds.length > 0) {
				for (int i = 0; i < purviewIds.length; i++) {
					RolePurviewRel rpr = new RolePurviewRel();
					rpr.setRoleId(role.getId());
					rpr.setPurviewId(purviewIds[i]);
					shopRoleDao.addBean(rpr, session);
				}
			}
			
			tran.commit();
			session.flush();
			
			return 4;
		} catch (Exception e) {
			log.error("新增用户角色失败，错误信息：" + e.getMessage());
			if(tran != null){
				tran.rollback();
			}
			return 3;
		} finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<ShopRole> listRole(Pager pager, String name) {
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询hql
		StringBuffer hql = new StringBuffer("from ShopRole");
		
		if (!StringUtil.isEmpty(name)) {
			hql.append(" where name like :name");
			paramMap.put("name", "%" + name + "%");
		}

		try {
			return shopRoleDao.list(pager, hql.toString(), paramMap);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	@Override
	public boolean deleteRole(String id) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		
		Session session = null;
		Transaction tran = null;
		try {
			session = shopRoleDao.createSession();
			tran = session.beginTransaction();
			
			StringBuffer uid = new StringBuffer("");
			if(id.indexOf(",") > 0){
				String[] ids = id.split(",");
				for (int i = 0; i < ids.length; i++) {
					uid.append((i == 0 ? "'" + ids[i] : ",'" +ids[i]) + "'");
				}
			} else {
				uid.append("'" + id + "'");
			}
			String hql1 = "delete from ShopRole where id in (" + uid.toString() + ")";
			String hql2 = "delete from RolePurviewRel where roleId in (" + uid.toString() + ")";
			
			shopRoleDao.delete(hql1, null, session);
			shopRoleDao.delete(hql2, null, session);
			
			tran.commit();
			session.flush();
			
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			if(tran != null){
				tran.rollback();
			}
			
			return false;
		} finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	}
	
	@Override
	public ShopRole getRoleById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		
		try {
			return (ShopRole) shopRoleDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public int updateRole(ShopRole role, String[] purviewIds) {
		// 1:角色名称为空，2:角色名称6-20字符组成，3:修改失败，4:修改成功
		if(role == null){
			return 1;
		}
		if(StringUtil.isEmpty(role.getName())){
			return 1;
		}
		role.setName(role.getName().trim());  // 去除角色名称前后空格
		if((role.getName()).length() < 6 || (role.getName()).length() > 20){
			return 2;
		}
		
		Session session = null;
		Transaction tran = null;
		try {
			session = shopRoleDao.createSession();
			tran = session.beginTransaction();
			
			role.setUpdateTime(DateUtil.getSysDateTimeString());
			shopRoleDao.updateBean(role, session);
			
			// 删除角色和菜单关联
			String hql = "delete from RolePurviewRel where roleId='" + role.getId() + "'";
			shopRoleDao.delete(hql, null, session);
			
			// 重新保存角色和菜单关联
			if(purviewIds != null && purviewIds.length > 0){
				for (String str : purviewIds) {
					RolePurviewRel rpr = new RolePurviewRel();
					rpr.setPurviewId(str);
					rpr.setRoleId(role.getId());
					shopRoleDao.addBean(rpr, session);
				}
			}
			
			tran.commit();
			session.flush();
			
			return 4;
		} catch (Exception e) {
			log.error("修改用户角色失败，错误信息：" + e.getMessage());
			if(tran != null){
				tran.rollback();
			}
			return 3;
		} finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getRoleNameById(String roleId) {
		if(StringUtil.isEmpty(roleId)){
			return null;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String hql = "select sr.name from ShopRole sr where sr.id=:roleId";
		paramMap.put("roleId", roleId);
		
		try {
			List<Object> list = shopRoleDao.list(hql, paramMap);
			if (list != null && list.size() > 0) {
				return (String) list.get(0);
			}
		} catch (Exception e) {
			log.error("查询用户所属角色失败，错误信息：" + e.getMessage());
		}
		return null;
	}
	
}
