package com.ekfans.plugin.wftong.base.friend.dao;

import java.util.List;

import com.ekfans.plugin.wftong.base.friend.model.FriendRelation;

import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 好友关系Dao接口
 * 
 * @Title: IUserRelationDao
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author yx
 * @date 2015年7月7日
 * @version 1.0
 */
public interface IFriendRelationDao extends IGenericDao{

	/**
	 * 检查关系
	 * @param userId
	 * @param friendId
	 * @param status
	 * @return
	 */
	public FriendRelation checkFriend(String userId, String friendId, String status);
	
	/**
	 * 获取好友ID集合（我加别人和别人加我的）
	 * @param userId
	 * @param status
	 * @return
	 */
	public List<String> getFriendIds(String userId);

	/**
	 * 获取二度人脉（朋友的朋友）用户ID集合
	 * @param userId
	 * @return
	 */
	public List<String> getSecondFriendIds(String userId);

	/**
	 * 按条件获取所有朋友id，认识和不认识的
	 * @param userId
	 * @param sear
	 * @return
	 */
	public List<String> getAllFriendIds(String userId, String searchKey);
}
