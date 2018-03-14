package com.ekfans.base.finance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.finance.dao.IBankAccountDao;
import com.ekfans.base.finance.model.BankAccount;
import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.service.IBankService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 企业权限功能菜单Service接口实现
 * 
 * @ClassName: StorePurviewService
 * @author Lgy
 * @date 2014-03-21 上午11:35:38 
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BankAccountService implements IBankAccountService {

	@Autowired
	IBankAccountDao accountDao;

	@Override
	public boolean checkNameIsUsed(String accountName, String accountId) {
		if (StringUtil.isEmpty(accountName)) {
			return false;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select account.id from BankAccount as account where 1=1");
		sql.append(" and account.name = :accountName");
		paramMap.put("accountName", accountName.trim());
		if (!StringUtil.isEmpty(accountId)) {
			sql.append(" and account.id != :accountId");
			paramMap.put("accountId", accountId);
		}

		try {
			List accountList = accountDao.list(sql.toString(), paramMap);
			if (accountList != null && accountList.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	/**
	 * 获取银行的管理账户
	 * 
	 * @param accountId
	 * @return
	 */

	@Override
	public BankAccount getMainAccountByBankId(String bankId) {
		if (StringUtil.isEmpty(bankId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select account from BankAccount as account where 1=1");
		sql.append(" and account.bankId = :bankId");
		paramMap.put("bankId", bankId.trim());
		sql.append(" and (account.departmentId is null or account.departmentId = '')");

		try {
			List<BankAccount> accountList = accountDao.list(sql.toString(), paramMap);
			if (accountList != null && accountList.size() > 0) {
				return accountList.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@Override
	public List<BankAccount> getAccountList(String bankId, String name, String departmentId, String realName, String phone, Pager pager) {
		if (StringUtil.isEmpty(bankId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from BankAccount as account where 1=1");
		sql.append(" and account.bankId = :bankId");
		paramMap.put("bankId", bankId);

		if (!StringUtil.isEmpty(name)) {
			sql.append(" and account.name like :name");
			paramMap.put("name", "%" + name + "%");
		}
		if (!StringUtil.isEmpty(departmentId)) {
			sql.append(" and account.departmentId = :departmentId");
			paramMap.put("departmentId", departmentId);
		}
		if (!StringUtil.isEmpty(realName)) {
			sql.append(" and account.realName like :realName");
			paramMap.put("realName", "%" + realName + "%");
		}
		if (!StringUtil.isEmpty(phone)) {
			sql.append(" and account.phone like :phone");
			paramMap.put("phone", "%" + phone + "%");
		}

		sql.append(" order by account.createTime desc");
		try {
			List<BankAccount> accountList = accountDao.list(pager, sql.toString(), paramMap);
			return accountList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 根据ID删除对象
	 * 
	 * @param accountIds
	 * @return
	 */
	@Override
	public Boolean removeByIds(String[] accountIds) {
		if (accountIds == null || accountIds.length <= 0) {
			return false;
		}
		try {
			accountDao.deleteByIds(accountIds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 新增或编辑
	 * 
	 * @param account
	 * @param request
	 * @return
	 */
	@Override
	public boolean saveOrUpdate(BankAccount account, HttpServletRequest request) {
		if (account == null) {
			return false;
		}
		if (!StringUtil.isEmpty(account.getBankId())) {
			IBankService bankService = SpringContextHolder.getBean(IBankService.class);
			Bank bank = bankService.getBankById(account.getBankId());
			// 设置LOGO
			account.setBankLogo(bank.getLogo());
			// 设置银行名称
			account.setBankName(bank.getBankName());
		}
		if (StringUtil.isEmpty(account.getCreateTime())) {
			account.setCreateTime(DateUtil.getSysDateTimeString());
		}
		account.setUpdateTime(DateUtil.getSysDateTimeString());

		String oldPassword = request.getParameter("oldPassword");
		// 设置登陆密码(MD5格式)
		String password = request.getParameter("password");
		if (!StringUtil.isEmpty(oldPassword) && oldPassword.equals(password)) {
			account.setPassword(password);
		} else {
			account.setPassword(new MD5Util().getMD5ofStr(password));
		}

		// 设置是否重置密码
		String statusStr = request.getParameter("status");
		Boolean status = true;
		if (!StringUtil.isEmpty(statusStr)) {
			try {
				status = Boolean.valueOf(statusStr);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		account.setStatus(status);

		// 设置是否重置密码
		String resetPwdStr = request.getParameter("resetPwd");
		Boolean resetPwd = true;
		if (!StringUtil.isEmpty(resetPwdStr)) {
			try {
				resetPwd = Boolean.valueOf(resetPwdStr);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		account.setResetPwd(resetPwd);

		String department = request.getParameter("department");
		if (!StringUtil.isEmpty(department) && department.indexOf("-") != -1) {
			String[] ds = department.split("-");
			if (ds != null && ds.length > 0) {
				account.setDepartmentId(ds[0]);
				account.setDepartment(ds[1]);
			}
		}

		try {
			if (StringUtil.isEmpty(account.getId())) {
				accountDao.addBean(account);
			} else {
				accountDao.updateBean(account);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 根据ID获取对象
	 * 
	 * @param accountId
	 * @return
	 */
	public BankAccount getAccountById(String accountId) {
		if (StringUtil.isEmpty(accountId)) {
			return null;
		}
		try {
			return (BankAccount) accountDao.get(accountId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
