package com.ekfans.base.store.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IBankInfDao;
import com.ekfans.base.store.model.BankInf;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商户资金日志Service
 * 
 * @ClassName: AccountLogService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("unchecked")
public class BankInfService implements IBankInfService {

	private Logger log = LoggerFactory.getLogger(BankInfService.class);
	@Autowired
	private IBankInfDao bankInfDao;

	@Override
	public boolean setBankInfs(String filePath) {
		Session session = null;
		Transaction transaction = null;

		try {
			session = bankInfDao.createSession();
			transaction = session.beginTransaction();

			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] txts = lineTxt.split(",");
					String bankCode = txts[0];
					String bankName = txts[1];
					BankInf bankInf = new BankInf();
					bankInf.setId(bankCode);
					bankInf.setBankName(bankName);
					bankInfDao.addBean(bankInf, session);
				}
				read.close();
			}

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
	public BankInf getBankInfById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			return (BankInf) bankInfDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<BankInf> getBankInfos(String key, Pager pager) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from BankInf as inf where 1=1");
		if (!StringUtil.isEmpty(key)) {
			sql.append(" and inf.bankName like :bankName");
			paramMap.put("bankName", StringUtil.sqlToLike(key));
		}
		sql.append(" order by inf.id asc");
		try {
			List<BankInf> infs = null;
			if (pager != null) {
				infs = bankInfDao.list(pager, sql.toString(), paramMap);
			} else {
				infs = bankInfDao.list(sql.toString(), paramMap);
			}
			return infs;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}