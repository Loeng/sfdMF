package com.ekfans.plugin.wftong.base.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.plugin.wftong.base.log.dao.IWftLogDao;
import com.ekfans.plugin.wftong.base.log.model.WftLog;

/**
 * Wft信息Service接口实现
 */
@Service
public class WftLogService implements IWftLogService {
	@Autowired
	IWftLogDao logDao;

	@Override
	public boolean save(WftLog log) {
		// TODO Auto-generated method stub
		if (log == null) {
			return false;
		}
		try {
			logDao.addBean(log);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
