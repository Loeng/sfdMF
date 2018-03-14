package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IAccountBankDao;
import com.ekfans.base.store.dao.IAccountBankLogDao;
import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.AccountBank;
import com.ekfans.base.store.model.AccountBankLog;
import com.ekfans.base.store.model.Store;
import com.ekfans.plugin.webService.bcs.BCSClientService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

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
@SuppressWarnings("unchecked")
public class AccountBankService implements IAccountBankService {

	private Logger log = LoggerFactory.getLogger(AccountBankService.class);
	@Autowired
	private IAccountBankDao accountBankDao;

	@Autowired
	private IStoreDao storeDao;

	@Autowired
	private IAccountBankLogDao bankLogDao;

	@Override
	public AccountBank getBnkById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}

		try {
			AccountBank bank = (AccountBank) accountBankDao.get(id);
			return bank;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@Override
	public AccountBank getBanksByUserId(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from AccountBank as bank where 1=1");
		sql.append(" and bank.userId = :userId");
		paramMap.put("userId", userId);
		sql.append(" and bank.status != :status");
		paramMap.put("status", "4");
		try {
			List<AccountBank> banks = accountBankDao.list(sql.toString(), paramMap);
			if (banks != null && banks.size() > 0) {
				return banks.get(0);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean saveOrUpdate(AccountBank bank, Store store) {
		if (bank == null) {
			return false;
		}

		Session session = null;
		Transaction transaction = null;

		try {
			session = accountBankDao.createSession();
			transaction = session.beginTransaction();

			if (StringUtil.isEmpty(bank.getId())) {
				accountBankDao.addBean(bank, session);
			} else {
				accountBankDao.updateBean(bank, session);
			}

			if (store != null) {
				storeDao.updateBean(store, session);
			}

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}

			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}

		}

		return false;
	}

	@Override
	public List<AccountBank> getCheckList(String storeName, Pager pager) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer("from AccountBank as bank where 1=1");
		if (!StringUtil.isEmpty(storeName)) {
			sql.append(" and bank.companyName like :storeName");
			paramMap.put("storeName", "%" + storeName + "%");
		}

		sql.append(" and bank.status = :status");
		paramMap.put("status", "2");

		sql.append(" order by bank.createTime desc");

		try {
			List<AccountBank> banks = null;

			if (pager != null) {
				banks = accountBankDao.list(pager, sql.toString(), paramMap);
			} else {
				banks = accountBankDao.list(sql.toString(), paramMap);
			}
			return banks;
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean checkBank(AccountBank bank, HttpServletRequest request) {
		if (bank == null) {
			return false;
		}
		if (!"1".equals(bank.getStatus())) {
			try {

				accountBankDao.updateBean(bank);

			} catch (Exception e) {
				log.error(e.getMessage());
			}
			return true;
		} else {
			Store store = null;
			try {
				store = (Store) storeDao.get(bank.getUserId());
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (store == null) {
				return false;
			}
			AccountBankLog bankLog = new AccountBankLog();
			bankLog.setUserId(bank.getUserId());

			Session session = null;
			Transaction transaction = null;
			try {
				session = accountBankDao.createSession();
				transaction = session.beginTransaction();
				bankLogDao.addBean(bankLog, session);
				Boolean status = BCSClientService.regdit(store, bank, bankLog);
				if (status) {
					storeDao.updateBean(store, session);
					accountBankDao.updateBean(bank, session);
				}
				bankLogDao.updateBean(bankLog, session);

				session.flush();
				transaction.commit();
				session.close();
				if (status) {
					return true;
				} else {
					request.setAttribute("errorMessage", bankLog.getErrorMessage());
					return false;
				}
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
			} finally {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
		}

		return false;
	}

	@Override
	public Boolean unRegidst(AccountBank bank, HttpServletRequest request) {
		if (bank == null) {
			return false;
		}
		Store store = null;
		try {
			store = (Store) storeDao.get(bank.getUserId());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (store == null) {
			return false;
		}
		AccountBankLog bankLog = new AccountBankLog();
		bankLog.setUserId(bank.getUserId());

		Session session = null;
		Transaction transaction = null;
		try {
			session = accountBankDao.createSession();
			transaction = session.beginTransaction();
			bankLogDao.addBean(bankLog, session);
			Boolean status = BCSClientService.unregdit(store, bankLog);
			if (status) {
				storeDao.updateBean(store, session);
				accountBankDao.delete(bank, session);
			}
			bankLogDao.updateBean(bankLog, session);

			session.flush();
			transaction.commit();
			session.close();
			if (status) {
				return true;
			} else {
				request.setAttribute("errorMessage", bankLog.getErrorMessage());
				return false;
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}
}