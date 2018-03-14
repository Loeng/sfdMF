package com.ekfans.base.store.service;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IAccountBankLogDao;
import com.ekfans.base.store.model.AccountBankLog;

/**
 * 运输企业车辆信息Service接口实现
 * 
 * @ClassName: CreditEstimatesService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class AccountBankLogService implements IAccountBankLogService {

	private Logger log = LoggerFactory.getLogger(AccountBankLogService.class);
	@Autowired
	private IAccountBankLogDao accountBankLogDao;

	@Override
	public void addLog(AccountBankLog bankLog, Session session) {
		// TODO Auto-generated method stub
		if (bankLog == null) {
			return;
		}

		try {
			if (session != null) {
				accountBankLogDao.addBean(bankLog, session);
			} else {
				accountBankLogDao.addBean(bankLog);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void updateLog(AccountBankLog bankLog, Session session) {
		// TODO Auto-generated method stub
		if (bankLog == null) {
			return;
		}

		try {
			if (session != null) {
				accountBankLogDao.updateBean(bankLog, session);
			} else {
				accountBankLogDao.updateBean(bankLog);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}