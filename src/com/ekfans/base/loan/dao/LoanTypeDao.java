package com.ekfans.base.loan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.base.loan.model.LoanType;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款类型DAO接口的实现
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
public class LoanTypeDao extends GenericDao implements ILoanTypeDao {
	public LoanTypeDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.loan.model.LoanType";
	}

	public static Logger log = LoggerFactory.getLogger(LoanTypeDao.class);

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
	@Override
	public List<LoanType> getLoanTypeByBankId(Pager pager, String bankId, String status, String name) {
		// 定义SQL参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" from LoanType as loan where 1=1");
		// 如果银行ID不为空，则关联银行ID
		if (!StringUtil.isEmpty(bankId)) {
			sql.append(" and loan.bankId = :bankId");
			paramMap.put("bankId", bankId);
		}
		// 如果状态不为空，则按照状态查询
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and loan.status = :status");
			paramMap.put("status", Boolean.valueOf(status));
		}
		// 如果名称不为空，则关联名称模糊查询
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and loan.loanName like :name");
			paramMap.put("name", "%" + name + "%");
		}
		// 按照创建时间倒序排序
		sql.append(" order by loan.createTime desc");

		try {
			List<LoanType> typeList = null;
			if (pager != null) {
				typeList = super.list(pager, sql.toString(), paramMap);
			} else {
				typeList = super.list(sql.toString(), paramMap);
			}
			return typeList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
}
