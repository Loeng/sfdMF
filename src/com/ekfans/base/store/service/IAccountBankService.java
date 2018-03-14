package com.ekfans.base.store.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.store.model.AccountBank;
import com.ekfans.base.store.model.Store;
import com.ekfans.pub.util.Pager;

/**
 * 
 * 
 * @ClassName: IStoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IAccountBankService {

	public AccountBank getBnkById(String id);

	public AccountBank getBanksByUserId(String userId);

	public Boolean saveOrUpdate(AccountBank bank, Store store);

	public List<AccountBank> getCheckList(String storeName, Pager pager);

	/**
	 * 审核（长沙银行注册账号）
	 * 
	 * @param bank
	 * @param request
	 * @return
	 */
	public Boolean checkBank(AccountBank bank, HttpServletRequest request);

	/**
	 * 解约
	 * 
	 * @param bank
	 * @param request
	 * @return
	 */
	public Boolean unRegidst(AccountBank bank, HttpServletRequest request);
}