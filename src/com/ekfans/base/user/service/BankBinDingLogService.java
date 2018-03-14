package com.ekfans.base.user.service;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.BankBindingLogDao;
import com.ekfans.base.user.model.BankBinding;
import com.ekfans.base.user.model.BankBindingLog;
import com.ekfans.pub.util.DateUtil;

@Service
public class BankBinDingLogService implements IBankBinDingLogService {

	private Logger log = LoggerFactory.getLogger(BankBinDingLogService.class);
	@Autowired
	private BankBindingLogDao bankBindingDao;

	@Override
	public boolean addBankLog(BankBinding bank, String status, Session session) {
		BankBindingLog bblog = new BankBindingLog();
		bblog.setStoreId(bank.getStoreId());
		bblog.setArea(bank.getBankId());
		bblog.setAddress(bank.getAddress());
		bblog.setFullName(bank.getFullName());
		bblog.setIdCardNo("");
		bblog.setCardNo(bank.getCardNo());
		bblog.setCreateTime(DateUtil.getSysDateTimeString());
		bblog.setStatus(status);

		try {
			bankBindingDao.addBean(bblog, session);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			return false;
		}
		return true;
	}
}
