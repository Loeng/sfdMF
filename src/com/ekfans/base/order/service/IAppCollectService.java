package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.AppCollect;
import com.ekfans.pub.util.Pager;


/**
 * 收藏Service接口
 * 
 * @Title: IAppCollectService
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年5月6日
 * @version 1.0
 */
public interface IAppCollectService {

	/**
	 * 判断用户是否收藏该资讯
	 * 
	 * @param contentId
	 * @param userId
	 * @return
	 */
	public Boolean checkUserHasCollect(String contentType, String contentId, String userId);

	/**
	 * 保存收藏
	 * 
	 * @param collect
	 * @return
	 */
	public Boolean save(AppCollect collect);
	
	/**
	 * 获取我收藏的集合
	 * @param pager 
	 * @param userId
	 * @return
	 */
	public List<AppCollect> getMyCollectList(Pager pager, String contentType, String userId);

	/**
	 * 取消收藏
	 * 
	 * @param userId
	 * @param collectId
	 * @return
	 */
	public Boolean delByUser(String userId,String contentType, String contentId);

}
