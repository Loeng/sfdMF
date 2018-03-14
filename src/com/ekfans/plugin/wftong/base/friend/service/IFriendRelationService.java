package com.ekfans.plugin.wftong.base.friend.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.plugin.wftong.base.friend.model.FriendRelation;
import com.ekfans.plugin.wftong.controller.model.AppFriend;

/**
 * 好友关系表Service接口
 * 
 * @Title: IUserRelationService
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author yx
 * @date 2015年7月7日
 * @version 1.0
 */
public interface IFriendRelationService {

	/**
	 * 添加好友
	 * 
	 * @param userId 当前用户ID
	 * @param friendId 被添加好友的ID
	 * @param status 状态
	 * @return
	 */
	public boolean addFriend(FriendRelation friendRelation);
	
	
	/**
	 * 直接添加好友，不判断status
	 * 
	 * @param userId 当前用户ID
	 * @param friendId 被添加好友的ID
	 * @param status 状态
	 * @return
	 */
	boolean addFriendDirectly(FriendRelation friendRelation);

	/**
	 * 根据ID查看用户是否为该用户的好友
	 * @param userId
	 * @return
	 */
	public FriendRelation chekdFriend(String userId, String friendId, String status);

	/**
	 * 查询两者之间关系  0不是好友 1好友 2好友的好友
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public String getFriendStatus(String userId, String friendId);
	
	/**
	 * 更新好友关系
	 * @param friendRelation
	 * @return
	 */
	public boolean update(FriendRelation friendRelation);

	/**
	 * 删除好友关系
	 * @param friendRelation
	 * @return
	 */
	public boolean delete(FriendRelation friendRelation);

	/**
	 * 通过id获取朋友对象
	 * @param id
	 * @return
	 */
	public AppFriend getAppFriendById(String id, HttpServletRequest request);

	/**
	 * 根据userId查找好友列表
	 * @param userId
	 * @return
	 */
	public List<AppFriend> getFriendList(String userId, HttpServletRequest request);

	/**
	 * 获得所有用户
	 * @return
	 */
	public List<AppFriend> getAllFriendList(String userId, String searchKey, HttpServletRequest request);

	/**
	 * 根据userId查找二度人脉
	 * @param userId
	 * @return
	 */
	public List<AppFriend> getAppSecondFriendList(String userId, HttpServletRequest request);
	
	/**
	 * 根据userId查找二度人脉
	 * @param userId
	 * @return
	 */
	public List<String> getAppSecondFriendIds(String userId);
	


//	/**
//	 * 根据userId查找二度人脉
//	 * @param userId
//	 * @return
//	 */
//	public List<String> getSecondFriendList(String userId, HttpServletRequest request);

}
