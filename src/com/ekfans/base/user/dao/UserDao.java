package com.ekfans.base.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.StringUtil;

/**
 * 会员DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Repository
public class UserDao extends GenericDao implements IUserDao {
	public UserDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.user.model.User";
	}

	@Override
	public boolean updateUseData(Class clz, String ids, boolean use) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String objClass = clz.getName();
			String sql = "update " + objClass + " set useData = '" + (use ? "1" : "0") + "' where id in(";
			if (!StringUtil.isEmpty(ids)) {
				String[] idArr = ids.split(",");
				for (int i = 0; i < idArr.length; i++) {
					sql += "'" + idArr[i] + "'";
					if (i + 1 < idArr.length) {
						sql += ",";
					}
				}

			}
			sql += ")";
			super.updateBean(sql, params);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<User> getUserWithApp(String searchKey) {
		try {
			// 定义map
			Map<String, Object> map = new HashMap<String, Object>();
			// 查账号
			StringBuffer sql = new StringBuffer("select user from User as user where 1 = 1 ");
			// 当有搜索条件时,按照条件进行模糊查询
			if (!StringUtil.isEmpty(searchKey)) {
				sql.append(" AND (user.name like :searchKey OR user.mobile like :searchKey OR user.email like :searchKey OR user.nickName like :searchKey)");
				sql.append(" AND user.type in (1,3,4)");
				map.put("searchKey", "%" + searchKey + "%");
			}
			sql.append(" order by user.createTime desc");
			return this.list(sql.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getUserIdByHxId(String hxId) {
		try {
			// 定义map
			Map<String, Object> map = new HashMap<String, Object>();

			// 查账号
			StringBuffer sql = new StringBuffer("select user from User as user where 1 = 1 ");
			if (!StringUtil.isEmpty(hxId)) {
				sql.append(" AND user.hxUserName = :hxId");
				map.put("hxId", hxId);
			}
			List<User> userList = this.list(sql.toString(), map);
			if (userList != null && userList.size() > 0) {
				return userList.get(0).getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<User> getAllUserNoRegHx() {
		try {
			// type范围
			String[] types = { "1","3","4"}; 
			
			// 定义map
			Map<String, Object> map = new HashMap<String, Object>();
			// 查账号
			StringBuffer sql = new StringBuffer("select user from User as user where 1 = 1 ");
			sql.append(" and (hxUserName is null or hxUserName  = '')");
			sql.append(" and (type in :types or touristType = :touristType)");
			map.put("types", types);
			map.put("touristType", "1");
			sql.append(" order by user.createTime desc");
			return this.list(sql.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
