package com.ekfans.base.loan.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.finance.model.BankAccount;
import com.ekfans.base.loan.dao.ILoanTypeDao;
import com.ekfans.base.loan.dao.ILoanTypeDetailDao;
import com.ekfans.base.loan.dao.ILoanTypeRoleDao;
import com.ekfans.base.loan.model.LoanType;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款类型实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2014-4-25
 * @version 1.0
 */
@Service
public class LoanTypeService implements ILoanTypeService {
	public static Logger log = LoggerFactory.getLogger(LoanTypeService.class);
	// 定義DAO
	@Autowired
	private ILoanTypeDao loanTypeDao;

	@Autowired
	private ILoanTypeDetailDao detailDao;

	@Autowired
	private ILoanTypeRoleDao roleDao;

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
		// 调用DAO查询集合并返回
		return loanTypeDao.getLoanTypeByBankId(pager, bankId, status, name);
	}

	/**
	 * 新增或保存贷款类型
	 * 
	 * @param loanType
	 * @param request
	 * @return
	 */
	@Override
	public Boolean saveOrUpdate(LoanType loanType, BankAccount account, HttpServletRequest request) {
		if (loanType == null) {
			return false;
		}
		// 设置银行ID
		loanType.setBankId(account.getBankId());
		// 设置更新时间
		loanType.setUpdateTime(DateUtil.getSysDateTimeString());

		// 校验字符串，用来校验是更新还是新增 true:新增 false:更新
		Boolean isAdd = false;

		// 如果ID为空，则是新增
		if (StringUtil.isEmpty(loanType.getId())) {
			// 设置创建时间
			loanType.setCreateTime(DateUtil.getSysDateTimeString());
			// 设置创建人
			loanType.setCreator(account.getId());
			// 设置创建人真实姓名
			loanType.setCreatorRealName(account.getRealName());
			isAdd = true;
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = loanTypeDao.createSession();
			transaction = session.beginTransaction();

			// 如果是新增，则调用带事务处理的新增DAO
			if (isAdd) {
				loanTypeDao.addBean(loanType, session);
			} else {
				// 如果是更新，则调用带事务处理的更新DAO
				loanTypeDao.updateBean(loanType, session);
			}

			if (!isAdd) {
				// 调用DAO删除明细
				detailDao.deleteByTypeId(loanType.getId(), session);
				// 调用DAO删除权限
				roleDao.deleteByTypeId(loanType.getId(), session);
			}

			// 调用DAO新增明细集合
			detailDao.addDetails(loanType, request, session);
			roleDao.addRoles(loanType, request, session);

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 根据类型ID获取类型的全部数据(包括子数据)
	 * 
	 * @param typeId
	 * @return
	 */
	public LoanType getLoanTypeFullById(String typeId) {
		if (StringUtil.isEmpty(typeId)) {
			return null;
		}
		try {
			// 调用DAO获取对象
			LoanType loanType = (LoanType) loanTypeDao.get(typeId);
			// 查询明细集合，并放入对象
			loanType.setDetails(detailDao.getDetailsByTypeId(typeId));
			// 查询权限集合并放入对象
			loanType.setRoles(roleDao.getRolesByTypeId(typeId));
			return loanType;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 根据类型ID获取对象
	 * 
	 * @param typeId
	 * @return
	 */
	public LoanType getLoanTypeById(String typeId) {
		if (StringUtil.isEmpty(typeId)) {
			return null;
		}
		try {
			return (LoanType) loanTypeDao.get(typeId);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
}