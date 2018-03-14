package com.ekfans.base.user.service;

import org.hibernate.Session;

import com.ekfans.base.user.model.BankBinding;

public interface IBankBinDingLogService {

	/**
	 * 添加日志信息
	 * 
	 */
	public boolean addBankLog(BankBinding bank, String status, Session session);
}
