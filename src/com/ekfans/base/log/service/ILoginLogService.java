package com.ekfans.base.log.service;

/**
 * 登陆日志Service接口
 * 
 * @ClassName: ILoginLogService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ILoginLogService {

	/**
	 * 保存登陆或注销日志
	 * 
	 * @param userId
	 * @return true:成功，false:失败
	 */
	public Boolean addLoginLog(String userId, String ip, int loginType, int type, String note);
}
