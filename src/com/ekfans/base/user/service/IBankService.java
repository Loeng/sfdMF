package com.ekfans.base.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.user.model.Bank;
import com.ekfans.pub.util.Pager;

/**
 * 合作银行实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IBankService {

	/**
	 * 新增合作银行
	 * 
	 * @param bank
	 * @param request
	 * @return
	 */
	public Boolean saveOrUpdateBank(Bank bank, HttpServletRequest request);

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(合作银行列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param
	 * @param status
	 * @return Warehouse 返回类型
	 * @throws
	 */
	public List<Bank> list(Pager pager, String bankName, String fullName,String status,String forenStatus,String accountPay,String onlinePay,String companyPay,String commonPay );

	/**
	 * 
	 * @Title: delete
	 * @Description: TODO(删除) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean delete(String id);

	/**
	 * 
	 * @Title: getWarehouseById
	 * @Description: TODO(根据id获得合作银行) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return Warehouse 返回类型
	 * @throws
	 */
	public Bank getBankById(String id);

	// 查询银行列表
	public List<Bank> getBank();
}
