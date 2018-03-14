package com.ekfans.base.loan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.base.loan.model.LoanAppLog;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款申请日志DAO接口的实现
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
public class LoanAppLogDao extends GenericDao implements ILoanAppLogDao {
	public LoanAppLogDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.loan.model.LoanAppLog";
	}

	public static Logger log = LoggerFactory.getLogger(LoanAppLogDao.class);

	/**
	 * 根据申请ID获取申请的日志集合
	 * 
	 * @param loanAppId
	 * @param session
	 * @return
	 */
	@Override
	public List<LoanAppLog> getLogsByLoanAppId(String loanAppId, Session session) {
		if (StringUtil.isEmpty(loanAppId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from LoanAppLog as appLog where 1=1");
		sql.append(" and appLog.appId = :loanAppId");
		paramMap.put("loanAppId", loanAppId);
		sql.append(" order by appLog.createTime desc");
		try {
			List<LoanAppLog> logList = null;
			if (session != null) {
				logList = super.list(sql.toString(), paramMap, session);
			} else {
				logList = super.list(sql.toString(), paramMap);
			}
			return logList;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
