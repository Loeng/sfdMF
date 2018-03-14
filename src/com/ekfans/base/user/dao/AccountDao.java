package com.ekfans.base.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.base.user.model.Account;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.StringUtil;

/**
 * 店铺子账号DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Repository
public class AccountDao extends GenericDao implements IAccountDao {
	public AccountDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.user.model.Account";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> getAccountsInterface() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("select ac from Account ac where 1=1");
		// 条件：未被同步过
		sql.append("and ac.useData = 0");
		// 条件：状态正常
		sql.append("and ac.status = 1 ");
		// 根据权限的级别倒序，位置升序排序
		sql.append(" order by ac.createTime desc");
		try {
			return this.list(sql.toString(), paramMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public List<Account> getAccountWithApp(String searchKey) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer("select account from Account as account where 1 = 1 ");
			// 当有搜索条件时,按照条件进行模糊查询
			if (!StringUtil.isEmpty(searchKey)) {
				sql.append(" AND (account.name like :searchKey OR account.contactPhone like :searchKey OR account.contactName like :searchKey)");
				map.put("searchKey", "%" + searchKey + "%");
			}
			sql.append(" order by account.createTime desc");

			return this.list(sql.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAccountIdByHxId(String hxId) {
		try {
			// 定义map
			Map<String, Object> map = new HashMap<String, Object>();

			// 查账号
			StringBuffer sql = new StringBuffer("select account from Account as account where 1 = 1 ");
			if (!StringUtil.isEmpty(hxId)) {
				sql.append(" AND account.hxUserName = :hxId");
				map.put("hxId", hxId);
			}
			List<Account> accountList = this.list(sql.toString(), map);
			if (accountList != null && accountList.size() > 0) {
				return accountList.get(0).getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Account> getAllAccountNoRegHx() {
		try {
			// 定义map
			Map<String, Object> map = new HashMap<String, Object>();
			// 查账号
			StringBuffer sql = new StringBuffer("select account from Account as account where 1 = 1 ");
			sql.append(" and hxUserName is null or hxUserName  = ''");
			sql.append(" order by account.createTime desc");
			return this.list(sql.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
