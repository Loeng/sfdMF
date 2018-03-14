package com.ekfans.base.finance.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.finance.dao.IBankAccountDao;
import com.ekfans.base.finance.dao.IBankDepartmentDao;
import com.ekfans.base.finance.dao.IBankPurviewRelDao;
import com.ekfans.base.finance.model.BankDepartment;
import com.ekfans.base.finance.model.BankPurviewRel;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 店铺部门与角色关联实现Service
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BankDepartmentService implements IBankDepartmentService {

	@Autowired
	private IBankDepartmentDao bankDepartmentDao;

	@Autowired
	private IBankPurviewRelDao relDao;

	/**
	 * 根据银行ID获取该银行所属部门集合
	 * 
	 * @param bankId
	 * @return
	 */
	@Override
	public List<BankDepartment> getRootDepartmentsByBankId(String bankId) {
		if (StringUtil.isEmpty(bankId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from BankDepartment as dp where 1=1");
		sql.append(" and dp.bankId = :bankId");
		paramMap.put("bankId", bankId);
		sql.append(" and (dp.parentId is null or dp.parentId = 'null' or dp.parentId = ' ')");
		sql.append(" order by dp.level desc,dp.position asc,dp.createTime asc");
		try {
			List<BankDepartment> list = bankDepartmentDao.list(sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 根据银行ID获取该银行所属部门集合
	 * 
	 * @param bankId
	 * @return
	 */
	@Override
	public List<BankDepartment> getDepartmentsByBankId(String bankId) {
		if (StringUtil.isEmpty(bankId)) {
			return null;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from BankDepartment as dp where 1=1");
		sql.append(" and dp.bankId = :bankId");
		paramMap.put("bankId", bankId);
		sql.append(" order by dp.level desc,dp.position asc,dp.createTime asc");

		try {
			List<BankDepartment> list = bankDepartmentDao.list(sql.toString(), paramMap);
			if (list == null || list.size() <= 0) {
				return null;
			}
			List<BankDepartment> rList = new LinkedList<BankDepartment>();

			// 定义一个MAP用来遍历集合
			Map<String, BankDepartment> map = new HashMap<String, BankDepartment>();

			// 循环查询出的list
			for (int i = 0; i < list.size(); i++) {
				// 获取对象
				BankDepartment department = (BankDepartment) list.get(i);

				// 如果获取的对象为空，则跳过
				if (department == null) {
					continue;
				}

				if (StringUtil.isEmpty(department.getParentId())) {
					// 如果Map中已经存在该对象，则将该对象的ChildList放入 purview中，并将purView重新放入Map
					if (map.containsKey(department.getId())) {
						// 从Map中的对象中取出ChildList并放入purView的ChildList中
						department.setChildList(map.get(department.getId()).getChildList());
						// 从Map中移除该部门对象
						map.remove(department.getId());
					}
					// 将该对象放入返回集合中
					rList.add(department);
				} else {

					// 判断Map中是否存在该对象，如果存在，则将Map中对象的childList取出放入开对象中
					if (map.containsKey(department.getId())) {
						department.setChildList(map.get(department.getId()).getChildList());
					}

					// 如果该对象的父ID不为空，则判断map中存在该对象的父对象，则将该对象放入父对象的ChildList中。
					// 否则，则创建一个新的部门对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					BankDepartment parentDepartment = null;
					// 如果map中存在该对象的父对象则创建一个新的部门对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					if (map.containsKey(department.getParentId())) {
						// 从Map中获取该对象的父对象
						parentDepartment = map.get(department.getParentId());
						// 如果获取的父对象不为空，则将该对象放入父对象的ChildList中
						if (parentDepartment != null) {
							parentDepartment.getChildList().add(department);
						}
						map.remove(parentDepartment.getId());
						// 将该父对象放入MAP中
						map.put(parentDepartment.getId(), parentDepartment);
					} else {
						// 创建伪父对象
						parentDepartment = new BankDepartment();
						// 将该对象放入父对象的ChildList中
						parentDepartment.getChildList().add(department);
						// set 父对象的ID
						parentDepartment.setId(department.getParentId());
						// 将该父对象放入MAP中
						map.put(parentDepartment.getId(), parentDepartment);
					}
				}
			}
			return rList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 根据父ID获取子部门集合
	 * 
	 * @param parentId
	 * @return
	 */
	@Override
	public List<BankDepartment> getChildDepartments(String parentId) {
		if (StringUtil.isEmpty(parentId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from BankDepartment as dp where 1=1");
		sql.append(" and dp.parentId = :parentId");
		paramMap.put("parentId", parentId);
		sql.append(" order by dp.level desc,dp.position asc,dp.createTime asc");
		try {
			List<BankDepartment> list = bankDepartmentDao.list(sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 新增或保存
	 * 
	 * @param department
	 * @param request
	 * @return
	 */
	@Override
	public Boolean saveOrUpdate(BankDepartment department, HttpServletRequest request) {

		if (department == null) {
			return false;
		}
		Boolean isUpdate = true;
		if (StringUtil.isEmpty(department.getId())) {
			department.setCreateTime(DateUtil.getSysDateTimeString());
			isUpdate = false;
		}
		department.setUpdateTime(DateUtil.getSysDateTimeString());

		// 获取页面选择的权限集合
		String[] purviewIds = request.getParameterValues("purviewId");

		Session session = null;
		Transaction transaction = null;
		try {
			session = bankDepartmentDao.createSession();
			transaction = session.beginTransaction();
			if (isUpdate) {
				bankDepartmentDao.updateBean(department, session);

				// 创建删除SQL删除权限
				StringBuffer delSql = new StringBuffer(" from BankPurviewRel as spr where 1=1");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				// 关联角色ID
				delSql.append(" and spr.roleId = :roleId");
				paramMap.put("roleId", department.getId());
				// 关联类型
				delSql.append(" and spr.type = :type");
				paramMap.put("type", true);
				relDao.delete(delSql.toString(), paramMap, session);
			} else {
				bankDepartmentDao.addBean(department, session);
			}

			// 如果传进来的子账户权限集合不为空，则循环将权限插入权限中间表
			if (purviewIds != null && purviewIds.length > 0) {
				// 便利传进来的权限ID集合
				for (int i = 0; i < purviewIds.length; i++) {
					// 获取当前循环到的ID
					String purviewId = purviewIds[i];
					// 如果ID不为空，则新定义一个权限关系对象
					if (!StringUtil.isEmpty(purviewId)) {
						BankPurviewRel purviewRel = new BankPurviewRel();
						// 设置权限ID
						purviewRel.setPurviewId(purviewId);
						// 设置子账户ID
						purviewRel.setRoleId(department.getId());
						// 类型为非子账户
						purviewRel.setType(true);
						purviewRel.setBankId(department.getBankId());
						// 调用DAO保存
						relDao.addBean(purviewRel, session);
					}
				}
			}

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	/**
	 * 根据ID获取对象
	 * 
	 * @param departmentId
	 * @return
	 */
	@Override
	public BankDepartment getDepartmentById(String departmentId) {
		if (StringUtil.isEmpty(departmentId)) {
			return null;
		}
		try {
			return (BankDepartment) bankDepartmentDao.get(departmentId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 根据ID删除对象
	 * 
	 * @param departmentId
	 * @return
	 */
	public Boolean deleteDepartmentById(String departmentId) {

		if (StringUtil.isEmpty(departmentId)) {
			return false;
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = bankDepartmentDao.createSession();
			transaction = session.beginTransaction();

			bankDepartmentDao.deleteById(departmentId, session);
			// 创建删除SQL删除权限
			StringBuffer delSql = new StringBuffer(" from BankPurviewRel as spr where 1=1");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 关联角色ID
			delSql.append(" and spr.roleId = :roleId");
			paramMap.put("roleId", departmentId);
			// 关联类型
			delSql.append(" and spr.type = :type");
			paramMap.put("type", true);
			relDao.delete(delSql.toString(), paramMap, session);

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	/**
	 * 校验部门是否可删除
	 * 
	 * @param departmentId
	 * @return true:可以删除，false:不可删除
	 */
	public Boolean checkDepartmentCanRemove(String departmentId) {
		if (StringUtil.isEmpty(departmentId)) {
			return false;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select account.id from BankAccount as account where 1=1");
		sql.append(" and account.departmentId = :departmentId");
		paramMap.put("departmentId", departmentId);

		try {
			IBankAccountDao accountDao = SpringContextHolder.getBean(IBankAccountDao.class);
			List list = accountDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return false;
			}

			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			StringBuffer sql2 = new StringBuffer("select dem.id from BankDepartment as dem where 1=1");
			sql2.append(" and dem.parentId = :departmentId");
			paramMap2.put("departmentId", departmentId);
			List list2 = bankDepartmentDao.list(sql2.toString(), paramMap2);
			if (list2 == null || list2.size() <= 0) {
				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}
}
