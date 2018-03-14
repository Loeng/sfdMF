package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.AppForwarding;
import com.ekfans.pub.util.Pager;

/**
 * 转发Service接口
 * 
 * @Title: IAppForwardingService
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年5月6日
 * @version 1.0
 */
public interface IAppForwardingService {

	/**
	 * 保存
	 * 
	 * @param collect
	 * @return
	 */
	public Boolean save(AppForwarding collect);

	/**
	 * 获取我转发的集合
	 * @param userId
	 * @return
	 */
	public List<AppForwarding> getMyForwardingList(Pager pager, String contentType, String userId);

}
