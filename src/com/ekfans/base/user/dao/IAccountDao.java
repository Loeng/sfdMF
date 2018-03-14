package com.ekfans.base.user.dao;

import java.util.List;

import com.ekfans.base.user.model.Account;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 店铺子账号DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IAccountDao extends IGenericDao {
	/**
	 * 获取接口Account数据
	 * @return
	 */
	public List<Account> getAccountsInterface();

	/**
	 * 模糊查询子账号，过滤掉采购和供应商
	 * @return
	 */
	public List<Account> getAccountWithApp(String searchKey);

	/**
	 * 通过环信id查询子账号Id
	 * @return
	 */
	public String getAccountIdByHxId(String hxId);

	/**
	 * 获取所有未注册环信用户
	 * @return
	 */
	public List<Account> getAllAccountNoRegHx();
}
