package com.ekfans.base.finance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.finance.dao.IBankClientDao;
import com.ekfans.base.finance.model.BankClient;
import com.ekfans.base.finance.model.BankDepartment;
import com.ekfans.pub.util.StringUtil;

/**
 * @ClassName BankClientService
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年5月26日
 */
@Service
public class BankClientService implements IBankClientService {

	@Autowired
	private IBankClientDao bankClientDao;

	@Override
	public Boolean addBankClient(BankClient bc) {
		// bc为空返回失败
		if (null == bc) {
			return false;
		}
		try {
			bankClientDao.addBean(bc);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Boolean deleteById(String id) {
		// id为空返回失败
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		try {
			bankClientDao.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public BankClient getById(String id) {
		// id为空返回null
		if (StringUtil.isEmpty(id)) {
			return null;
		}

		try {
			BankClient bc = (BankClient) bankClientDao.get(id);
			return bc;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean updateBankClient(BankClient bc) {
		// bc为空返回失败
		if (bc == null) {
			return false;
		}
		try {
			bankClientDao.updateBean(bc);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public BankClient getBankClientByStoreIdAndBankId(String storeId, String bankId) {
		return bankClientDao.getByStoreIdAndBankId(storeId, bankId);
	}
}
