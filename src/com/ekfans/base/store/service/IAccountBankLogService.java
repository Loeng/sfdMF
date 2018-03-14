package com.ekfans.base.store.service;

import org.hibernate.Session;

import com.ekfans.base.store.model.AccountBankLog;

/**
 * 
 * 
 * @ClassName: IStoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IAccountBankLogService {

	public void addLog(AccountBankLog bankLog, Session session);
	
	public void updateLog(AccountBankLog bankLog, Session session);
}