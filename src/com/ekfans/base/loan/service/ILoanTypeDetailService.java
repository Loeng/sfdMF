package com.ekfans.base.loan.service;

import java.util.List;

import com.ekfans.base.loan.model.LoanTypeDetail;

/**
 * 贷款类型明细Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: ekfans
 * @author lgy
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanTypeDetailService {
	/**
	 * 根据贷款类型获取贷款所需提交明细
	 * @param typeId
	 * @return
	 */
	public List<LoanTypeDetail> getDetailsByTypeId(String typeId);
}
