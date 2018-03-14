package com.ekfans.base.user.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.user.model.Account;
import com.ekfans.pub.util.Pager;

/**
 * 会员实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IAccountService {

	/**
	 * 根据ID获取子账户对象
	 * 
	 * @param id
	 * @return
	 */
	public Account getAccountById(String id);

	/**
	 * 根据ID获取子账户对应的权限Map
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Boolean> getAccountPurviews(String id);

	/**
	 * 根据id删除对象
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);

	/**
	 * 根据ID批量删除对象
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteById(String[] ids);

	/**
	 * 新增保存子账户
	 * 
	 * @param purviewIds
	 *            权限ID
	 * @param account
	 *            子账户对象
	 * @return
	 */
	public boolean saveOrUpdateAccount(Account account, String[] purviewIds, HttpServletRequest request);

	/**
	 * 校验用户名
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkName(String id, String name);

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
	public List<Account> getAccountsByStoreId(String storeId, String name, String department, String contactName, Pager page);

	/**
	 * 用户登陆查询
	 * 
	 * @param userName
	 * @param type
	 * @return
	 */
	public Account getAccountLogin(String userName, String type);
	
	/**
	 * 根据用户名查找登录用户
	 * @param userName
	 * @return
	 */
	public Account getAccountLoginByName(String userName);

	public boolean updateAccount(Account account);
}
