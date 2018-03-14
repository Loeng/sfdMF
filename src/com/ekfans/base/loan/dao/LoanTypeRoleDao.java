package com.ekfans.base.loan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.base.loan.model.LoanType;
import com.ekfans.base.loan.model.LoanTypeRole;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款类型规则DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2016-1-3
 * @version 1.0
 */
@Repository
@SuppressWarnings("unchecked")
public class LoanTypeRoleDao extends GenericDao implements ILoanTypeRoleDao {
	public LoanTypeRoleDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.loan.model.LoanTypeRole";
	}

	public static Logger log = LoggerFactory.getLogger(LoanTypeRoleDao.class);

	/**
	 * 根据类型ID产出权限
	 * 
	 * @param typeId
	 */
	@Override
	public void deleteByTypeId(String typeId, Session session) {
		if (StringUtil.isEmpty(typeId)) {
			return;
		}

		// 定义SQL参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer(" from LoanTypeRole as role where 1=1");
		sql.append(" and role.typeId = :typeId");
		paramMap.put("typeId", typeId);

		try {
			if (session != null) {
				super.delete(sql.toString(), paramMap, session);
			} else {
				super.delete(sql.toString(), paramMap);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 新增
	 * 
	 * @param loanType
	 * @param request
	 * @param session
	 */
	@Override
	public void addRoles(LoanType loanType, HttpServletRequest request, Session session) {
		// 如果传过来的类型为空则返回
		if (loanType == null) {
			return;
		}
		// 从页面获取前缀
		String[] roleChoseBoxs = request.getParameterValues("roleChoseBox");
		// 如果前缀为空，或者长度小于或等于0，则返回
		if (roleChoseBoxs == null || roleChoseBoxs.length <= 0) {
			return;
		}
		try {

			for (int i = 0; i < roleChoseBoxs.length; i++) {
				// 获取循环中的前缀
				String roleChoseBox = roleChoseBoxs[i];
				// 如果前缀为空，则进入下个循环
				if (StringUtil.isEmpty(roleChoseBox)) {
					continue;
				}
				// 定义新的对象
				LoanTypeRole role = new LoanTypeRole();
				// 设置常规数据 - 银行ID
				role.setBankId(loanType.getBankId());
				// 设置常规数据 - 类型ID
				role.setTypeId(loanType.getId());
				// 设置名称
				role.setRoleName(request.getParameter(roleChoseBox + "RoleName"));

				// 设置名称
				role.setRoleType(request.getParameter(roleChoseBox + "NameType"));

				if (session != null) {
					super.addBean(role, session);
				} else {
					super.addBean(role);
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * 根据类型ID获取权限集合
	 * 
	 * @param typeId
	 * @return
	 */
	@Override
	public List<LoanTypeRole> getRolesByTypeId(String typeId) {
		if (StringUtil.isEmpty(typeId)) {
			return null;
		}

		// 定义SQL参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer(" from LoanTypeRole as role where 1=1");
		sql.append(" and role.typeId = :typeId");
		paramMap.put("typeId", typeId);
		sql.append(" order by role.position asc");

		try {
			List<LoanTypeRole> roleList = super.list(sql.toString(), paramMap);
			return roleList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
}
