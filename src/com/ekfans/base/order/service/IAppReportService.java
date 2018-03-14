package com.ekfans.base.order.service;

import com.ekfans.base.order.model.AppReport;


/**
 * 举报Service接口
 * 
 * @Title: IAppReportService
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年5月6日
 * @version 1.0
 */
public interface IAppReportService {

	/**
	 * 判断用户是否收举报该供需
	 * 
	 * @param contentId
	 * @param userId
	 * @return
	 */
	public Boolean checkUserHasReport(String contentType, String supplyId, String userId);

	/**
	 * 举报
	 * 
	 * @param collect
	 * @return
	 */
	public Boolean save(AppReport report);

}
