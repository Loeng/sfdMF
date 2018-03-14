package com.ekfans.base.loan.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.finance.model.BankAccount;
import com.ekfans.base.loan.model.LoanType;
import com.ekfans.pub.util.Pager;

/**
 * 贷款类型Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: ekfans
 * @author lgy
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanTypeService {

	/**
	 * 根据银行ID查询贷款类型
	 * 
	 * @param pager
	 *            翻页对象
	 * @param bankId
	 *            银行ID
	 * @param status
	 *            状态
	 * @return
	 */
	public List<LoanType> getLoanTypeByBankId(Pager pager, String bankId, String status, String name);

	/**
	 * 新增或保存贷款类型
	 * 
	 * @param loanType
	 * @param request
	 * @return
	 */
	public Boolean saveOrUpdate(LoanType loanType, BankAccount account, HttpServletRequest request);

	/**
	 * 根据类型ID获取类型的全部数据(包括子数据)
	 * 
	 * @param typeId
	 * @return
	 */
	public LoanType getLoanTypeFullById(String typeId);

	/**
	 * 根据类型ID获取对象
	 * 
	 * @param typeId
	 * @return
	 */
	public LoanType getLoanTypeById(String typeId);
}
