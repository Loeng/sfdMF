package com.ekfans.base.tender.service;

import com.ekfans.base.tender.model.TenderLog;

/**
 * 登陆日志Service接口
 * 
 * @ClassName: ILoginLogService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ITenderLogService {

	/**
	 * 报名
	 * 
	 * @param userId
	 * @return true:成功，false:失败
	 */
	public Boolean addTenderLog(TenderLog tenderLog);

	/**
	 * 查询是否已报名
	 * 
	 * @param storeId
	 * @param tenderId
	 * @return
	 */
	public Boolean checkHasLog(String storeId, String tenderId);
}
