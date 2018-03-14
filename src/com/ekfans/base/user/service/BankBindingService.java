package com.ekfans.base.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.IBankBindingDao;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.model.BankBinding;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 银行卡绑定Service接口实现
 * 
 * @ClassName: BankBindingService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class BankBindingService implements IBankBindingService {

	private Logger log = LoggerFactory.getLogger(BankBindingService.class);
	@Resource
	private IBankBindingDao bankBindingDao;
	@Resource
	private IBankBinDingLogService bankBindingLogService;
	@Resource
	private IUserDao userDao;
	@Autowired
	private IUserService userService;

	@SuppressWarnings("unchecked")
	@Override
	public List<BankBinding> getBankBindingByStoreId(String storeId, String type) {
		if (StringUtil.isEmpty(storeId)) {
			return null;
		}
		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select bb,b.bankName from BankBinding bb,Bank b where bb.bankId=b.id");
		// setting data
		if (!StringUtil.isEmpty(storeId)) {
			hql.append(" and bb.storeId=:storeId");
			params.put("storeId", storeId);
		}
		if (!StringUtil.isEmpty(type)) {
			hql.append(" and bb.status=:status");
			params.put("status", Boolean.valueOf(type));
		}
		try {
			List<Object[]> list = this.bankBindingDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<BankBinding> bblist = new ArrayList<BankBinding>();
				for (Object[] obj : list) {
					BankBinding bb = (BankBinding) obj[0];
					bb.setBankName(obj[1].toString());
					bblist.add(bb);
				}
				return bblist;
			}
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean CheckBankBindIng(BankBinding bank) {
		if (bank == null)
			return false;
		StringBuffer buffer = new StringBuffer("from BankBinding where storeId='" + bank.getStoreId() + "' and bankId='" + bank.getBankId() + "'");
		try {
			List<Object[]> list = bankBindingDao.list(buffer.toString(), null);
			// 该用户在该银行没绑定银行卡
			if (list.size() < 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除银行卡信息
	 */
	@Override
	public String deletBank(String id, int count, User user) {
		Session session = null; // 定义Session
		Transaction transaction = null; // 定义事物处理

		try {
			session = bankBindingDao.createSession(); // 创建Session
			transaction = session.beginTransaction(); // 开启事物处理

			// 根据ID查询银行卡信息
			BankBinding bank = (BankBinding) bankBindingDao.get(id, session);
			// 根据ID删除银行卡信息
			bankBindingDao.deleteById(id, session);
			// 记录日志
			bankBindingLogService.addBankLog(bank, "3", session);
			// 判断是否是删除最后一张银行卡,如果是最后一张则修改user表中的银行卡字段信息
			if (count - 1 == 0) {
				// 更新user表
				user.setIsAssociatedBank(false);
				userDao.updateBean(user, session);
			}

			transaction.commit();
			session.flush();

			return "1";
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			if (transaction != null) {
				transaction.rollback(); // 保存失败,回滚事务
			}
			return "0";
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	@Override
	public String addBank(BankBinding bank) {
		Session session = null; // 定义Session
		Transaction transaction = null; // 定义事物处理

		try {
			session = bankBindingDao.createSession(); // 创建Session
			transaction = session.beginTransaction(); // 开启事物处理
			bankBindingDao.addBean(bank, session);
			// 保存新增银行卡日志信息
			bankBindingLogService.addBankLog(bank, "1", session);
			transaction.commit();
			session.flush();

			return "1";
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			if (transaction != null) {
				transaction.rollback(); // 保存失败,回滚事务
			}
			return "0";
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public String updateBank(BankBinding bank) {
		Session session = null; // 定义Session
		Transaction transaction = null; // 定义事物处理

		try {
			session = bankBindingDao.createSession(); // 创建Session
			transaction = session.beginTransaction(); // 开启事物处理

			bankBindingDao.updateBean(bank, session);
			// 保存新增银行卡日志信息
			bankBindingLogService.addBankLog(bank, "2", session);

			transaction.commit();
			session.flush();

			return "1";
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			if (transaction != null) {
				transaction.rollback(); // 保存失败,回滚事务
			}
			return "0";
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * 后台查询所有银行卡
	 */
	@Override
	public List<BankBinding> allBank(Pager pager, String id, String name, String banknum) {
		StringBuffer buffer = new StringBuffer("select bb.id,bb.address,bb.cardNo,bb.fullName,b.bankName,bb.storeId,bb.status,bb.createTime from BankBinding bb,Bank b where b.id = bb.bankId");
		if (!StringUtil.isEmpty(name)) {
			buffer.append(" and bb.address like '%" + name + "%'");
		}
		if (!StringUtil.isEmpty(banknum)) {
			buffer.append(" and bb.bankId ='" + banknum + "'");
		}
		List<BankBinding> banklist = new ArrayList<BankBinding>();
		try {
			List<Object[]> list = bankBindingDao.list(pager, buffer.toString(), null);
			if (list != null && list.size() > 0) {
				for (Object[] obj : list) {
					BankBinding b = new BankBinding();
					b.setId((String) obj[0]);
					b.setAddress((String) obj[1]);
					b.setCardNo((String) obj[2]);
					b.setFullName((String) obj[3]);
					b.setBankName((String) obj[4]);
					b.setStoreId((String) obj[5]);
					b.setStatus(Boolean.parseBoolean(obj[6].toString()));
					b.setCreateTime((String) obj[7]);
					// 根据获取到的用户id 比对账户所属
					User user = userService.getUser(b.getStoreId());
					// 是所选择的角色则放入list
					/*
					 * if(user.getType().equals(id)){ banklist.add(b); }
					 */
					banklist.add(b);
				}
			}
			return banklist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据ID获取对象
	 * 
	 * @param bindingId
	 * @return
	 */
	public BankBinding getBankBindingById(String bindingId) {
		if (StringUtil.isEmpty(bindingId)) {
			return null;
		}
		try {
			BankBinding binding = (BankBinding) bankBindingDao.get(bindingId);
			if (binding != null) {
				IBankService bankService = SpringContextHolder.getBean(IBankService.class);
				Bank bank = bankService.getBankById(binding.getBankId());
				if (bank != null) {
					binding.setBankName(bank.getBankName());
					binding.setBank(bank);
				}
				return binding;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}
