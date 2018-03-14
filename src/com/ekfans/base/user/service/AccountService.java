package com.ekfans.base.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStorePurviewRelDao;
import com.ekfans.base.store.dao.StorePurviewRelDao;
import com.ekfans.base.store.model.StorePurviewRel;
import com.ekfans.base.user.dao.IAccountDao;
import com.ekfans.base.user.dao.UserDao;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.util.UUIDUtil;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.HttpUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 会员业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Service
public class AccountService implements IAccountService {

	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(AccountService.class);

	// 定义userDao
	@Autowired
	private IAccountDao accountDao;

	/**
	 * 根据ID获取子账户对象
	 * 
	 * @param id
	 * @return
	 */
	public Account getAccountById(String id) {
		try {
			return (Account) accountDao.get(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		Session session = accountDao.createSession();
		Transaction transaction = session.beginTransaction();
		try {
			accountDao.delete(id, session);
			// 创建删除SQL删除权限
			StringBuffer delSql = new StringBuffer(" from StorePurviewRel as spr where 1=1");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 关联角色ID
			delSql.append(" and spr.roleId = :roleId");
			paramMap.put("roleId", id);
			// 关联类型
			delSql.append(" and spr.type = :type");
			paramMap.put("type", true);
			// 通过反射机制获取DAO
			IStorePurviewRelDao purviewRelDao = SpringContextHolder.getBean(IStorePurviewRelDao.class);
			purviewRelDao.delete(delSql.toString(), paramMap, session);
			// 刷新SESSION
			session.flush();
			// 提交事务
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (session != null && transaction != null) {
				transaction.rollback();
				session.close();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 根据ID批量删除对象
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteById(String[] ids) {
		if (ids == null || ids.length <= 0) {
			return false;
		}
		Session session = accountDao.createSession();
		Transaction transaction = session.beginTransaction();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String idStr = "";
			int a = 0;
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				if (!StringUtil.isEmpty(id)) {
					if (a != 0) {
						idStr = idStr + ",";
					}
					idStr = idStr + "'" + id + "'";
					a++;
				}
			}

			StringBuffer sql = new StringBuffer(" from Account as account where 1=1");
			sql.append(" and account.id in (").append(idStr).append(")");
			accountDao.delete(sql.toString(), paramMap, session);

			// 创建删除SQL删除权限
			StringBuffer delSql = new StringBuffer(" from StorePurviewRel as spr where 1=1");
			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			// 关联角色ID
			delSql.append(" and spr.roleId in (").append(idStr).append(")");
			// 关联类型
			delSql.append(" and spr.type = :type");
			paramMap2.put("type", true);
			// 通过反射机制获取DAO
			IStorePurviewRelDao purviewRelDao = SpringContextHolder.getBean(IStorePurviewRelDao.class);
			purviewRelDao.delete(delSql.toString(), paramMap2, session);

			// 刷新SESSION
			session.flush();
			// 提交事务
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (session != null && transaction != null) {
				transaction.rollback();
				session.close();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 根据ID获取子账户对应的权限Map
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Boolean> getAccountPurviews(String id) {
		Map<String, Boolean> returnMap = new HashMap<String, Boolean>();
		// 如果传过来的id为空，则返回
		if (StringUtil.isEmpty(id)) {
			return returnMap;
		}
		// 定义SQL的参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer("select spr.purviewId from StorePurviewRel as spr where 1=1");
		sql.append(" and spr.roleId = :roleId");
		paramMap.put("roleId", id);
		sql.append(" and spr.type = :type");
		paramMap.put("type", true);

		try {
			// 反射获取DAO
			StorePurviewRelDao relDao = SpringContextHolder.getBean(StorePurviewRelDao.class);
			// 查询SQL
			List list = relDao.list(sql.toString(), paramMap);
			// 如果查出的集合不为空，并且长度大于0，则便利该集合，将值放入MAP
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String purviewId = (String) list.get(i);
					if (!StringUtil.isEmpty(purviewId)) {
						returnMap.put(purviewId, true);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return returnMap;
	}

	@Override
	public boolean saveOrUpdateAccount(Account account, String[] purviewIds, HttpServletRequest request) {
		// 如果传进来的子账户对象为空，则返回false
		if (account == null) {
			return false;
		}
		// 创建Session 与 事务处理机制
		Session session = accountDao.createSession();
		Transaction transaction = session.beginTransaction();

		try {

			// 调用方法校验用户名，如果返回FALSE则表示改用户名存在，返回false
			if (!checkName(account.getId(), account.getName())) {
				return false;
			}

			// 通过反射机制获取DAO
			IStorePurviewRelDao purviewRelDao = SpringContextHolder.getBean(IStorePurviewRelDao.class);
			// 如果传进来的子账户对象ID不为空，则调用更新方法更新对象
			if (!StringUtil.isEmpty(account.getId())) {
				Account oldAccount = (Account) accountDao.get(account.getId());
				if (!oldAccount.getPassword().equals(account.getPassword())) {
					MD5Util m = new MD5Util();
					account.setPassword(m.getMD5ofStr(account.getPassword()));
				}
				// 创建删除SQL删除权限
				StringBuffer delSql = new StringBuffer(" from StorePurviewRel as spr where 1=1");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				// 关联角色ID
				delSql.append(" and spr.roleId = :roleId");
				paramMap.put("roleId", account.getId());
				// 关联类型
				delSql.append(" and spr.type = :type");
				paramMap.put("type", true);
				purviewRelDao.delete(delSql.toString(), paramMap, session);
				account.setUpdateTime(DateUtil.getSysDateTimeString());
				accountDao.updateBean(account, session);
			} else {
				// 将密码作MD5加密
				MD5Util m = new MD5Util();
				account.setId(UUIDUtil.getMarkedUUID32(Account.SINGLE_MARK));
				account.setPassword(m.getMD5ofStr(account.getPassword()));
				account.setCreateTime(DateUtil.getSysDateTimeString());
				account.setUpdateTime(DateUtil.getSysDateTimeString());
				// 调用DAO添加对象
				accountDao.addBean(account, session);
				
			}

			// 如果传进来的子账户权限集合不为空，则循环将权限插入权限中间表
			if (purviewIds != null && purviewIds.length > 0) {
				// 便利传进来的权限ID集合
				for (int i = 0; i < purviewIds.length; i++) {
					// 获取当前循环到的ID
					String purviewId = purviewIds[i];
					// 如果ID不为空，则新定义一个权限关系对象
					if (!StringUtil.isEmpty(purviewId)) {
						StorePurviewRel purviewRel = new StorePurviewRel();
						// 设置权限ID
						purviewRel.setPurviewId(purviewId);
						// 设置子账户ID
						purviewRel.setRoleId(account.getId());
						// 类型为子账户
						purviewRel.setType(true);
						// 调用DAO保存
						purviewRelDao.addBean(purviewRel, session);
					}
				}
			}

			// 刷新SESSION
			session.flush();
			// 提交事务
			transaction.commit();
			session.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (session != null && transaction != null) {
				transaction.rollback();
				session.close();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	/**
	 * 校验用户名
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean checkName(String id, String name) {
		// 如果传过来的name为空，则表示用户名不合法，返回false
		if (StringUtil.isEmpty(name)) {
			return false;
		}

		// 从User表查询
		StringBuffer sql = new StringBuffer("select user.id from User as user where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		sql.append(" and (user.name = :name or user.mobile = :mobile or user.email = :email)");
		paramMap.put("name", name.trim());
		paramMap.put("mobile", name.trim());
		paramMap.put("email", name.trim());

		try {
			// 反射获取用户的UserDao
			UserDao userDao = SpringContextHolder.getBean(UserDao.class);
			// 查询SQL
			List userList = userDao.list(sql.toString(), paramMap);
			// 如果查出的集合不为空并且长度大于0，则返回false表示已存在
			if (userList != null && userList.size() > 0) {
				return false;
			}

			// 查询子账户表
			StringBuffer accSql = new StringBuffer("select acc.id from Account as acc where 1=1");
			Map<String, Object> accParamMap = new HashMap<String, Object>();
			accSql.append(" and acc.name = :name");
			accParamMap.put("name", name.trim());
			List accList = accountDao.list(accSql.toString(), accParamMap);
			// 如果查出来的集合为空或者长度小于或等于0，则返回true
			if (accList == null || accList.size() <= 0) {
				return true;
			}

			// 如果传过来的ID为空，则表示该用户名已存在
			if (StringUtil.isEmpty(id)) {
				return false;
			}
			// 如果查出来的ID和传过来的ID一致，则表示可用
			String listId = (String) accList.get(0);
			if (id.equals(listId)) {
				return true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询子账户
	 * 
	 * @param storeId
	 * @param name
	 * @param department
	 * @param contactName
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Account> getAccountsByStoreId(String storeId, String name, String department, String contactName, Pager page) {
		// 定义参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" from Account as account where 1=1");
		// 关联店铺ID
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and account.storeId = :storeId");
			paramMap.put("storeId", storeId);
		}

		// 关联用户名
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and account.name like :name");
			paramMap.put("name", "%" + name + "%");
		}

		// 关联部门
		if (!StringUtil.isEmpty(department)) {
			sql.append(" and account.department like :department");
			paramMap.put("department", "%" + department + "%");
		}

		// 关联联系人
		if (!StringUtil.isEmpty(contactName)) {
			sql.append(" and account.contactName like :contactName");
			paramMap.put("contactName", "%" + contactName + "%");
		}

		sql.append(" order by account.updateTime desc account.createTime desc");
		try {
			List<Account> accountList = null;
			if (page != null) {
				accountList = accountDao.list(page, sql.toString(), paramMap);
			} else {
				accountList = accountDao.list(sql.toString(), paramMap);
			}
			return accountList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用户登陆查询
	 * 
	 * @param userName
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Account getAccountLogin(String userName, String type) {
		// 如果传进来的用户名为空，则返回NULL
		if (StringUtil.isEmpty(userName)) {
			return null;
		}

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" from Account as account where name = :name and type = :type");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", userName);
		paramMap.put("type", type);
		try {
			List<Account> accountList = accountDao.list(sql.toString(), paramMap);
			if (accountList != null && accountList.size() > 0) {
				return accountList.get(0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Account getAccountLoginByName(String userName) {
		// 如果传进来的用户名为空，则返回NULL
		if (StringUtil.isEmpty(userName)) {
			return null;
		}

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" from Account as account where name = :name");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", userName);
		try {
			List<Account> accountList = accountDao.list(sql.toString(), paramMap);
			if (accountList != null && accountList.size() > 0) {
				return accountList.get(0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public boolean updateAccount(Account account) {
		try {
			this.accountDao.updateBean(account);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
}