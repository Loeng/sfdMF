package com.ekfans.base.loan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.loan.dao.ILoanTypeRoleDao;

/**
 * 贷款类型规则实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2014-4-25
 * @version 1.0
 */
@Service
public class LoanTypeRoleService implements ILoanTypeRoleService {
	public static Logger log = LoggerFactory.getLogger(LoanTypeRoleService.class);
	// 定義DAO
	@Autowired
	private ILoanTypeRoleDao roleDao;

}