package com.ekfans.base.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.finance.dao.IBankAccountDao;
import com.ekfans.base.finance.dao.IBankDepartmentDao;
import com.ekfans.base.finance.dao.IBankPurviewRelDao;
import com.ekfans.base.finance.model.BankAccount;
import com.ekfans.base.finance.model.BankPurviewRel;
import com.ekfans.base.user.dao.IBankDao;
import com.ekfans.base.user.model.Bank;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 合作银行业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class BankService implements IBankService {
	// 定义DAO
	@Resource
	private IBankDao bankDao;
	@Resource
	private IBankAccountDao bankAccountDao;
	@Resource
	private IBankPurviewRelDao relDao;
	@Resource
	private IBankDepartmentDao departMentDao;

	// 定义错误处理日志
	private Logger log = LoggerFactory.getLogger(BankService.class);

	/**
	 * 新增合作银行
	 * 
	 * @param bank
	 * @param request
	 * @return
	 */
	@Override
	public Boolean saveOrUpdateBank(Bank bank, HttpServletRequest request) {
		// 定义事务处理
		Transaction transaction = null;
		Session session = null;

		// 定义一个新银行账户对象
		BankAccount account = new BankAccount();
		account.setId(request.getParameter("bankAccountId"));
		// 设置LOGO
		account.setBankLogo(bank.getLogo());
		// 设置银行名称
		account.setBankName(bank.getBankName());
		// 设置创建时间和更新时间
		String createTime = request.getParameter("bankAccountCreateTime");
		account.setCreateTime(!StringUtil.isEmpty(createTime) ? createTime : DateUtil.getSysDateTimeString());
		account.setUpdateTime(DateUtil.getSysDateTimeString());
		// 设置用户名
		account.setName(request.getParameter("accountName"));
		String oldPassword = request.getParameter("oldPassword");
		// 设置登陆密码(MD5格式)
		String password = request.getParameter("password");
		if (!StringUtil.isEmpty(oldPassword) && oldPassword.equals(password)) {
			account.setPassword(password);
		} else {
			account.setPassword(new MD5Util().getMD5ofStr(password));
		}
		// 设置联系手机号
		account.setPhone(request.getParameter("phone"));
		// 设置真实姓名
		account.setRealName(request.getParameter("realName"));
		account.setStatus(true);
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
		// 获取页面选择的权限集合
		String[] purviewIds = request.getParameterValues("purviewId");
		try {
			// 创建Session并开启事务处理
			session = bankDao.createSession();
			transaction = session.beginTransaction();

			if (!StringUtil.isEmpty(bank.getId())) {
				bankDao.updateBean(bank, session);
			} else {
				// 保存银行信息
				bankDao.addBean(bank, session);
			}

			// 保存银行管理账户信息
			account.setBankId(bank.getId());

			Boolean addType = true;

			if (!StringUtil.isEmpty(account.getId())) {
				bankAccountDao.updateBean(account, session);
				addType = false;
			} else {
				addType = true;
				bankAccountDao.addBean(account, session);
			}

			if (!addType) {
				// 创建删除SQL删除权限
				StringBuffer delSql = new StringBuffer(" from BankPurviewRel as spr where 1=1");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				// 关联角色ID
				delSql.append(" and spr.roleId = :roleId");
				paramMap.put("roleId", account.getBankId());
				// 关联类型
				delSql.append(" and spr.type = :type");
				paramMap.put("type", false);
				relDao.delete(delSql.toString(), paramMap, session);
			}

			// 如果传进来的子账户权限集合不为空，则循环将权限插入权限中间表
			if (purviewIds != null && purviewIds.length > 0) {
				// 便利传进来的权限ID集合
				for (int i = 0; i < purviewIds.length; i++) {
					// 获取当前循环到的ID
					String purviewId = purviewIds[i];
					// 如果ID不为空，则新定义一个权限关系对象
					if (!StringUtil.isEmpty(purviewId)) {
						BankPurviewRel purviewRel = new BankPurviewRel();
						// 设置权限ID
						purviewRel.setPurviewId(purviewId);
						// 设置子账户ID
						purviewRel.setRoleId(bank.getId());
						// 类型为非子账户
						purviewRel.setType(false);
						purviewRel.setBankId(bank.getId());
						// 调用DAO保存
						relDao.addBean(purviewRel, session);
					}
				}
			}
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

	@Override
	public List<Bank> list(Pager pager, String bankName, String fullName,String status,String forenStatus,String accountPay,String onlinePay,String companyPay,String commonPay) {

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select bank from Bank as bank where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(bankName)) {
			sql.append(" and bank.bankName like :bankName");
			paramMap.put("bankName", "%" + bankName + "%");
		}
		// 如果查询条件输入了status，添加查询条件
		if (!StringUtil.isEmpty(fullName)) {
			sql.append(" and bank.fullName like :fullName");
			paramMap.put("fullName", "%" + fullName + "%");
		}
		if(!StringUtil.isEmpty(status)){
			sql.append(" and bank.status = :status");
			paramMap.put("status",status);
		}
		if(!StringUtil.isEmpty(forenStatus)){
			sql.append(" and bank.forenStatus = :forenStatus");
			paramMap.put("forenStatus","1".equals(forenStatus)?true:false);
		}
		if(!StringUtil.isEmpty(accountPay)){
			sql.append(" and bank.accountPay = :accountPay");
			paramMap.put("accountPay","1".equals(accountPay)?true:false);
		}
		if(!StringUtil.isEmpty(onlinePay)){
			sql.append(" and bank.onlinePay = :onlinePay");
			paramMap.put("onlinePay","1".equals(onlinePay)?true:false);
		}
		if(!StringUtil.isEmpty(companyPay)){
			sql.append(" and bank.companyPay = :companyPay");
			paramMap.put("companyPay","1".equals(companyPay)?true:false);
		}
		if(!StringUtil.isEmpty(commonPay)){
			sql.append(" and bank.commonPay = :commonPay");
			paramMap.put("commonPay","1".equals(commonPay)?true:false);
		}

		sql.append(" order by bank.id asc");


		try {
			// 调用DAO方法查找角色
			List<Bank> list = null;
			if(pager != null){
				list = bankDao.list(pager, sql.toString(), paramMap);
			}else{
				list = bankDao.list(sql.toString(),paramMap);
			}
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean delete(String id) {
		Session session = null;
		Transaction transaction = null;

		try {
			session = bankDao.createSession();
			transaction = session.beginTransaction();
			bankDao.deleteById(id);

			// 创建删除SQL删除用户账户
			StringBuffer accountSQL = new StringBuffer(" from BankAccount as spr where 1=1");
			Map<String, Object> accountMap = new HashMap<String, Object>();
			// 关联角色ID
			accountSQL.append(" and spr.bankId = :bankId");
			accountMap.put("bankId", id);
			bankAccountDao.delete(accountSQL.toString(), accountMap, session);

			// 创建删除SQL删除权限
			StringBuffer purviewSql = new StringBuffer(" from BankPurviewRel as spr where 1=1");
			Map<String, Object> purviewMap = new HashMap<String, Object>();
			// 关联角色ID
			purviewSql.append(" and spr.bankId = :bankId");
			purviewMap.put("bankId", id);
			relDao.delete(purviewSql.toString(), purviewMap, session);

			// 创建删除SQL删除部门
			StringBuffer deparmentSQL = new StringBuffer(" from BankDepartment as spr where 1=1");
			Map<String, Object> deparmentMap = new HashMap<String, Object>();
			// 关联角色ID
			deparmentSQL.append(" and spr.bankId = :bankId");
			deparmentMap.put("bankId", id);
			departMentDao.delete(deparmentSQL.toString(), deparmentMap, session);

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
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

	@Override
	public Bank getBankById(String id) {
		try {
			return (Bank) bankDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public List<Bank> getBank() {
		StringBuffer buffer = new StringBuffer("from Bank");
		try {
			List<Bank> bank = bankDao.list(buffer.toString(), null);
			if (bank != null) {
				return bank;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
