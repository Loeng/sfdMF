package com.ekfans.base.loan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.base.loan.model.LoanAppDetail;
import com.ekfans.base.loan.util.LoanAppUtil;
import com.ekfans.base.loan.util.LoanTypeUtil;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款申请DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2016-1-3
 * @version 1.0
 */
@Repository
@SuppressWarnings("unchecked")
public class LoanAppDetailDao extends GenericDao implements ILoanAppDetailDao {
	public LoanAppDetailDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.loan.model.LoanAppDetail";
	}

	public static Logger log = LoggerFactory.getLogger(LoanAppDetailDao.class);

	/**
	 * 根据融资申请ID删除明细
	 * 
	 * @param loanAppId
	 * @param session
	 */
	@Override
	public void deleteByAppId(String loanAppId, Session session) {
		if (StringUtil.isEmpty(loanAppId)) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from LoanAppDetail as detail where 1=1");
		sql.append(" and detail.appId = :loanAppId");
		paramMap.put("loanAppId", loanAppId);
		try {
			if (session != null) {
				super.delete(sql.toString(), paramMap, session);
			} else {
				super.delete(sql.toString(), paramMap);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 根据申请ID获取明细集合
	 * 
	 * @param loanAppId
	 * @param session
	 * @return
	 */
	public List<LoanAppDetail> getDetailsByAppId(String loanAppId, Session session) {
		if (StringUtil.isEmpty(loanAppId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from LoanAppDetail as detail where 1=1");
		sql.append(" and detail.appId = :loanAppId");
		paramMap.put("loanAppId", loanAppId);
		sql.append(" order by detail.position asc");
		try {
			List<LoanAppDetail> detailList = null;
			if (session != null) {
				detailList = super.list(sql.toString(), paramMap, session);
			} else {
				detailList = super.list(sql.toString(), paramMap);
			}
			return detailList;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Object[] getAllLoaningOrderIds() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select lad.value from LoanApp as la, LoanAppDetail as lad where 1 = 1");
		sql.append(" and la.id = lad.appId");
		sql.append(" and lad.nameCode = :nameCode");
		paramMap.put("nameCode", LoanTypeUtil.LOAN_DETAIL_CODE_ORDER);
		// 除'取消'状态的融资订单，其他订单均视为正在流程中
		sql.append(" and la.appStatus != :appStatus");
		paramMap.put("appStatus", LoanAppUtil.APP_STATUS_CANCEL);
		try {
			Object[] idArr = (Object[]) super.list(sql.toString(), paramMap).toArray();
			return idArr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
