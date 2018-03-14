package com.ekfans.base.finance.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.finance.model.BankAccount;
import com.ekfans.pub.util.Pager;

/**
 * 企业权限功能菜单Service接口
 * 
 * @ClassName: IStorePurviewService
 * @author Lgy
 * @date 2014-01-06 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IBankAccountService {

	/**
	 * 根据用户名判断该用户名是否存在
	 * 
	 * @param accountName
	 * @return
	 */
	public boolean checkNameIsUsed(String accountName, String accountId);

	/**
	 * 获取银行的管理账户
	 * 
	 * @param bankId
	 * @return
	 */
	public BankAccount getMainAccountByBankId(String bankId);

	/**
	 * 查询会员集合
	 * 
	 * @param name
	 * @param departmentId
	 * @param realName
	 * @param phone
	 * @param pager
	 * @return
	 */
	public List<BankAccount> getAccountList(String bankId, String name, String departmentId, String realName, String phone, Pager pager);

	/**
	 * 根据ID删除对象
	 * 
	 * @param accountIds
	 * @return
	 */
	public Boolean removeByIds(String[] accountIds);

	/**
	 * 新增或编辑
	 * 
	 * @param account
	 * @param request
	 * @return
	 */
	public boolean saveOrUpdate(BankAccount account, HttpServletRequest request);

	/**
	 * 根据ID获取对象
	 * 
	 * @param accountId
	 * @return
	 */
	public BankAccount getAccountById(String accountId);
}