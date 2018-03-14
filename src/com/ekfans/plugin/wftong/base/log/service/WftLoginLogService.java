package com.ekfans.plugin.wftong.base.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.plugin.wftong.base.log.dao.IWftLoginLogDao;
import com.ekfans.plugin.wftong.base.log.model.WftLoginLog;

/**
 * 用户登录信息Service接口实现
 */
@Service
public class WftLoginLogService implements IWftLoginLogService {
	@Autowired
	public IWftLoginLogDao loginLogDao;

	public boolean save(WftLoginLog loginLog) {
		try {
			loginLogDao.addBean(loginLog);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
