package com.ekfans.base.loan.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

/**
 * 贷款申请Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: ekfans
 * @author lgy
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanAppDetailService {

	/**
	 * 新增明细
	 * 
	 * @param request
	 * @param loanAppId
	 * @param loanTypeId
	 * @param session
	 */
	public void addDetails(HttpServletRequest request, HttpServletResponse response, String loanAppId, String loanTypeId, Session session);

}
