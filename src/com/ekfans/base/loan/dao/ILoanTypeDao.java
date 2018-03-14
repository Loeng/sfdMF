package com.ekfans.base.loan.dao;

import java.util.List;

import com.ekfans.base.loan.model.LoanType;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;

/**
 * 贷款类型DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanTypeDao extends IGenericDao {

	/**
	 * 根据银行ID查询贷款类型
	 * 
	 * @param pager
	 *            翻页对象
	 * @param bankId
	 *            银行ID
	 * @param status
	 *            状态
	 * @return
	 */
	public List<LoanType> getLoanTypeByBankId(Pager pager, String bankId, String status, String name);
}
