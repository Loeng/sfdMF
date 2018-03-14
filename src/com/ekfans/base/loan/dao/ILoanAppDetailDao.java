package com.ekfans.base.loan.dao;

import java.util.List;

import org.hibernate.Session;

import com.ekfans.base.loan.model.LoanAppDetail;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 贷款申请DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanAppDetailDao extends IGenericDao {

	/**
	 * 根据融资申请ID删除明细
	 * 
	 * @param loanAppId
	 * @param session
	 */
	public void deleteByAppId(String loanAppId, Session session);

	/**
	 * 根据申请ID获取明细集合
	 * 
	 * @param loanAppId
	 * @param session
	 * @return
	 */
	public List<LoanAppDetail> getDetailsByAppId(String loanAppId, Session session);

	/**
	 * 获取所有融资中的订单
	 * @return
	 */
	public Object[] getAllLoaningOrderIds();
}
