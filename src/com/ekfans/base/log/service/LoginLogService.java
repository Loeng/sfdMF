package com.ekfans.base.log.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.log.dao.ILoginLogDao;
import com.ekfans.base.log.model.LoginLog;
import com.ekfans.pub.util.DateUtil;

/**
 * 登陆日志Service接口实现
 * 
 * @ClassName: LoginLogService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class LoginLogService implements ILoginLogService {

	private Logger log = LoggerFactory.getLogger(LoginLogService.class);
	@Resource
	private ILoginLogDao loginLogDao;
	
	@Override
	public Boolean addLoginLog(String userId, String ip, int loginType, int type, String note) {
		LoginLog ll = new LoginLog();
		ll.setUserId(userId);
		ll.setLoginIp(ip);
		ll.setLoginType(loginType);
		ll.setType(type);
		ll.setCreateTime(DateUtil.getSysDateTimeString());
		ll.setNote(note);
		
		try {
			this.loginLogDao.addBean(ll);
			return true;
		} catch (Exception e) {
			log.error("保存登陆或注销日志报错：" + e.getMessage());
		}
		return false;
	}

}
