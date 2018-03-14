package com.ekfans.base.store.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IAccountBankLogDao;
import com.ekfans.base.store.dao.IAccountLogDao;
import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.AccountBankLog;
import com.ekfans.base.store.model.AccountLog;
import com.ekfans.base.store.model.Store;
import com.ekfans.plugin.webService.bcs.BCSClientService;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商户资金日志Service
 * 
 * @ClassName: AccountLogService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class AccountLogService implements IAccountLogService {

	private Logger log = LoggerFactory.getLogger(AccountLogService.class);
	@Autowired
	private IAccountLogDao accountLogDao;
	@Autowired
	private IStoreDao storeDao;
	@Autowired
	private IAccountBankLogDao bankLogDao;

	@Override
	public void addLog(AccountLog accountLog, Session session) {
		if (accountLog == null) {
			return;
		}
		try {
			if (session != null) {
				accountLogDao.addBean(accountLog, session);
			} else {
				accountLogDao.addBean(accountLog);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	@Override
	public void updateLog(AccountLog accountLog, Session session) {
		if (accountLog == null) {
			return;
		}
		try {
			if (session != null) {
				accountLogDao.updateBean(accountLog, session);
			} else {
				accountLogDao.updateBean(accountLog);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	@Override
	public BigDecimal getAvlBal(String storeId) {
		return BCSClientService.getAvlBal(storeId)[1];
	}

	/**
	 * 客户入金操作（充值--仅先绑定长沙银行账户的客户）
	 * 
	 * @param price
	 */
	@Override
	public boolean sufficient(String storeId, BigDecimal price) {
		if (StringUtil.isEmpty(storeId)) {
			return false;
		}
		Store store = null;
		try {
			store = (Store) storeDao.get(storeId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (store == null) {
			return false;
		}

		AccountLog accountLog = new AccountLog();
		try {
			accountLog.setUserId(storeId);
			accountLog.setPrice(price);
			accountLogDao.addBean(accountLog);
		} catch (Exception e) {
			return false;
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = accountLogDao.createSession();
			transaction = session.beginTransaction();

			AccountBankLog bankLog = new AccountBankLog();
			bankLog.setUserId(storeId);
			bankLog.setCreateTime(DateUtil.getSysDateTimeString());
			bankLog.setMchTransNo(accountLog.getId().substring(16, accountLog.getId().length()));
			Boolean status = BCSClientService.sufficient(store, bankLog, accountLog, price);

			if (status) {
				store.setAccount(store.getAccount().add(accountLog.getPrice()));
				storeDao.updateBean(store, session);
			}
			bankLogDao.addBean(bankLog, session);
			accountLogDao.updateBean(accountLog, session);

			session.flush();
			transaction.commit();
			session.close();
			return status;
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

	/**
	 * 客户出金操作（提现）
	 * 
	 * @param price
	 */
	@Override
	public boolean takeMoney(String storeId, BigDecimal price, BigDecimal charge) {
		if (StringUtil.isEmpty(storeId)) {
			return false;
		}
		 Store store = null;
		 try {
		 	store = (Store) storeDao.get(storeId);
		 } catch (Exception e) {
		 // TODO: handle exception
		 }
		 if (store == null) {
		 	return false;
		 }

		AccountLog accountLog = new AccountLog();
		try {
			accountLog.setUserId(storeId);
			accountLog.setPrice(price.add(charge));
			accountLogDao.addBean(accountLog);
		} catch (Exception e) {
			return false;
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = accountLogDao.createSession();
			transaction = session.beginTransaction();

			AccountBankLog bankLog = new AccountBankLog();
			bankLog.setUserId(storeId);
			bankLog.setCreateTime(DateUtil.getSysDateTimeString());
			bankLogDao.addBean(bankLog, session);

			Boolean status = BCSClientService.takeMony(storeId,"",price,charge,bankLog,accountLog);
//			 Boolean status = BCSClientService.takeMony(storeId, price, bankLog, accountLog);

			 if (status) {
			 //
				 store.setAccount(store.getAccount().divide(accountLog.getPrice()));
				 storeDao.updateBean(store, session);
			 }
			bankLogDao.updateBean(bankLog, session);
			accountLogDao.updateBean(accountLog, session);

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	/**
	 * 在线交易
	 * 
	 * @param storeId
	 *            支付方ID
	 * @param receiveId
	 *            收款方ID
	 * @param orderId
	 *            订单ID
	 * @param type
	 *            类型 - 1-部分付款；2-完结付款3：违约付款（功能号为2、3时，该笔订单不允许再发起资金冻结、付款交易）
	 * @param price
	 *            - 支付金额
	 * @return
	 */
	public boolean payForOrder(String storeId, String receiveId, String orderId, String type, BigDecimal price) {
		if (StringUtil.isEmpty(storeId) || StringUtil.isEmpty(receiveId) || StringUtil.isEmpty(orderId) || price.doubleValue() == 0.00) {
			return false;
		}

		AccountLog accountLog = new AccountLog();
		AccountBankLog bankLog = new AccountBankLog();
		try {
			accountLog.setUserId(storeId);
			accountLog.setReceiveId(receiveId);
			accountLog.setCreateTime(DateUtil.getSysDateTimeString());
			accountLog.setOrderId(orderId);
			accountLog.setPrice(price);
			accountLog.setStatus("3");
			accountLogDao.addBean(accountLog);

			bankLog.setUserId(storeId);
			bankLog.setPrice(price);
			bankLog.setCreateTime(DateUtil.getSysDateTimeString());
			bankLogDao.addBean(bankLog);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// 暂时将支付状态设置为成功ture
		Boolean status = BCSClientService.takeMony(storeId,receiveId,price,new BigDecimal(0.00),bankLog,accountLog);
//		Boolean status = BCSClientService.payForOrder(storeId, receiveId, orderId, type, price, bankLog, accountLog);
		// Boolean status = true;

		Store store = null;
		Store reStore = null;
		try {
			store = (Store) storeDao.get(storeId);
			reStore = (Store) storeDao.get(receiveId);
		} catch (Exception e) {
			return false;
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = accountLogDao.createSession();
			transaction = session.beginTransaction();
			if (status) {
				store.setAccount(store.getAccount().subtract(price));
				reStore.setAccount(reStore.getAccount().add(price));
				storeDao.updateBean(store, session);
				storeDao.updateBean(reStore, session);
			}
			accountLogDao.updateBean(accountLog, session);
			bankLogDao.updateBean(bankLog, session);

			session.flush();
			transaction.commit();
			session.close();
			return status;
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

	/**
	 * 获取银行行号
	 * 
	 * @param bankNo
	 * @param bankName
	 * @param pager
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBankNo(String storeId, String bankNo, String bankName, Pager pager) {
		try {
			AccountBankLog bankLog = new AccountBankLog();
			bankLog.setCreateTime(DateUtil.getSysDateTimeString());
			bankLog.setUserId(storeId);
			bankLog.setType("UPP3009");
			bankLogDao.addBean(bankLog);

			List<Map<String, Object>> banks = BCSClientService.getBankNo(bankNo, bankName, pager, bankLog);
			bankLogDao.updateBean(bankLog);
			return banks;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 获取跨行出金手续费
	 * 
	 * @return
	 */
	@Override
	public Double getCharge(BigDecimal price, String storeId) {
		try {
			AccountBankLog bankLog = new AccountBankLog();
			bankLog.setCreateTime(DateUtil.getSysDateTimeString());
			bankLog.setUserId(storeId);
			bankLog.setType("FMSPAY0001");
			bankLogDao.addBean(bankLog);

			Double charge = BCSClientService.getCharge(price, storeId, bankLog);
			bankLogDao.updateBean(bankLog);
			return charge;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0.00;
	}
}