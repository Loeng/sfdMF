package com.ekfans.base.loan.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.loan.dao.ILoanTypeDetailDao;
import com.ekfans.base.loan.model.LoanTypeDetail;

/**
 * 贷款类型明细实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2014-4-25
 * @version 1.0
 */
@Service
public class LoanTypeDetailService implements ILoanTypeDetailService {
	public static Logger log = LoggerFactory.getLogger(LoanTypeDetailService.class);
	// 定義DAO
	@Autowired
	private ILoanTypeDetailDao detailDao;

	/**
	 * 根据贷款类型获取贷款所需提交明细
	 * 
	 * @param typeId
	 * @return
	 */
	@Override
	public List<LoanTypeDetail> getDetailsByTypeId(String typeId) {
		return detailDao.getDetailsByTypeId(typeId);
	}
}