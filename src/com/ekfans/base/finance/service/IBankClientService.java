package com.ekfans.base.finance.service;

import com.ekfans.base.finance.model.BankClient;


/**
 * 银行客户实现Service接口
 * @ClassName IBankClientService
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年5月26日
 */
public interface IBankClientService {
	

	/**
	 * 新增银行客户
	 * @param bc
	 * @return
	 */
	public Boolean addBankClient(BankClient bc);
	
	/**
	 * 根据id删除银行客户
	 * @param id
	 * @return
	 */
	public Boolean deleteById(String id);
	
	/**
	 * 根据id查找银行客户
	 * @param id
	 * @return
	 */
	public BankClient getById(String id);
	
	/**
	 * 更新银行客户
	 * @param bc
	 * @return
	 */
	public Boolean updateBankClient(BankClient bc);

	/**
	 * 通过企业ID和银行ID查询
	 * @param storeId
	 * @param bankId
	 */
	public BankClient getBankClientByStoreIdAndBankId(String storeId, String bankId);
}
