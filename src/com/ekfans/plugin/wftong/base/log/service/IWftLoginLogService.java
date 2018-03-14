package com.ekfans.plugin.wftong.base.log.service;

import com.ekfans.plugin.wftong.base.log.model.WftLoginLog;

/**
 * 用户登录信息Service接口
 */
public interface IWftLoginLogService {

	public boolean save(WftLoginLog loginLog);

}
