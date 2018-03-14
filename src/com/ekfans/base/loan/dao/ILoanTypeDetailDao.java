package com.ekfans.base.loan.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.ekfans.base.loan.model.LoanType;
import com.ekfans.base.loan.model.LoanTypeDetail;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 贷款类型明细DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanTypeDetailDao extends IGenericDao {
	/**
	 * 根据类型ID删除明细
	 * 
	 * @param typeId
	 */
	public void deleteByTypeId(String typeId, Session session);

	/**
	 * 新增
	 * 
	 * @param loanType
	 * @param request
	 * @param session
	 */
	public void addDetails(LoanType loanType, HttpServletRequest request, Session session);

	/**
	 * 根据类型ID获取明细
	 * 
	 * @param typeId
	 * @return
	 */
	public List<LoanTypeDetail> getDetailsByTypeId(String typeId);
}
