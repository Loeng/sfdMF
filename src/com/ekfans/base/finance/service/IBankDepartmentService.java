package com.ekfans.base.finance.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.finance.model.BankDepartment;

/**
 * 店铺权限功能菜单与角色关联Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IBankDepartmentService {
	/**
	 * 根据银行ID获取该银行所属部门集合
	 * 
	 * @param bankId
	 * @return
	 */
	public List<BankDepartment> getRootDepartmentsByBankId(String bankId);

	/**
	 * 根据银行ID获取该银行所属部门集合
	 * 
	 * @param bankId
	 * @return
	 */
	public List<BankDepartment> getDepartmentsByBankId(String bankId);

	/**
	 * 根据父ID获取子部门集合
	 * 
	 * @param parentId
	 * @return
	 */
	public List<BankDepartment> getChildDepartments(String parentId);

	/**
	 * 新增或保存
	 * 
	 * @param department
	 * @param request
	 * @return
	 */
	public Boolean saveOrUpdate(BankDepartment department, HttpServletRequest request);

	/**
	 * 根据ID获取对象
	 * 
	 * @param departmentId
	 * @return
	 */
	public BankDepartment getDepartmentById(String departmentId);

	/**
	 * 根据ID删除对象
	 * 
	 * @param departmentId
	 * @return
	 */
	public Boolean deleteDepartmentById(String departmentId);

	/**
	 * 校验部门是否可删除
	 * 
	 * @param departmentId
	 * @return true:可以删除，false:不可删除
	 */
	public Boolean checkDepartmentCanRemove(String departmentId);
}