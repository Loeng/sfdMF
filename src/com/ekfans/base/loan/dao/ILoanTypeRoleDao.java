package com.ekfans.base.loan.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.ekfans.base.loan.model.LoanType;
import com.ekfans.base.loan.model.LoanTypeRole;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 贷款类型规则DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanTypeRoleDao extends IGenericDao {

	/**
	 * 根据类型ID产出权限
	 * 
	 * @param typeId
	 */
	public void deleteByTypeId(String typeId, Session session);

	/**
	 * 新增
	 * 
	 * @param loanType
	 * @param request
	 * @param session
	 */
	public void addRoles(LoanType loanType, HttpServletRequest request, Session session);

	/**
	 * 根据类型ID获取权限集合
	 * 
	 * @param typeId
	 * @return
	 */
	public List<LoanTypeRole> getRolesByTypeId(String typeId);
}
