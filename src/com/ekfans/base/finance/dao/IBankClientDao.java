package com.ekfans.base.finance.dao;

import java.util.List;

import com.ekfans.base.finance.model.BankClient;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;

/**
 * 银行客户DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IBankClientDao extends IGenericDao {

	/**
	 * 根据参数查找银行客户
	 * @param storeId
	 * @return
	 */
	public List<BankClient> getBankClientsByParams(String storeId, String bankId, String type, String storeName, Pager pager);

	/**
	 * 通过企业ID和银行ID查询
	 * @param storeId
	 * @param bankId
	 */
	public BankClient getByStoreIdAndBankId(String storeId, String bankId);
   
}
