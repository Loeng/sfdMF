package com.ekfans.base.loan.dao;

import java.util.List;

import org.hibernate.Session;

import com.ekfans.base.loan.model.LoanAppLog;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 贷款申请日志DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanAppLogDao extends IGenericDao {

	/**
	 * 根据申请ID获取申请的日志集合
	 * 
	 * @param loanAppId
	 * @param session
	 * @return
	 */
	public List<LoanAppLog> getLogsByLoanAppId(String loanAppId, Session session);
}
