package com.ekfans.plugin.wftong.base.friend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.IAccountDao;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IAccountService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.plugin.wftong.AppServerController;
import com.ekfans.plugin.wftong.base.friend.dao.IFriendRelationDao;
import com.ekfans.plugin.wftong.base.friend.model.FriendRelation;
import com.ekfans.plugin.wftong.controller.model.AppFriend;
import com.ekfans.plugin.wftong.controller.model.AppUser;

/**
 * 好友关系Service接口实现
 * 
 * @Title: UserRelationService
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author yx
 * @date 2015年7月7日
 * @version 1.0
 */
@Service
public class FriendRelationService implements IFriendRelationService {
	@Autowired
	private IFriendRelationDao dao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IAccountDao accountDao;
	@Autowired
	private IUserService userService;

	@Override
	public boolean addFriend(FriendRelation friendRelation) {
		// 状态 0:申请
		if ("0".equals(friendRelation.getStatus())) {
			// 调用service方法
			try {
				dao.addBean(friendRelation);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean addFriendDirectly(FriendRelation friendRelation) {
		// 调用service方法
		try {
			dao.addBean(friendRelation);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	@Override
	public FriendRelation chekdFriend(String userId, String friendId, String status) {
		return dao.checkFriend(userId, friendId, status);
	}

	@Override
	public boolean update(FriendRelation friendRelation) {
		try {
			// 更新好友信息
			dao.updateBean(friendRelation);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(FriendRelation friendRelation) {
		try {
			// 删除好友
			dao.delete(friendRelation);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<AppFriend> getFriendList(String userId, HttpServletRequest request) {
		// 创建list map
		List<AppFriend> list = null;

		IUserService userService = SpringContextHolder.getBean(IUserService.class);
		IAccountService accountService = SpringContextHolder.getBean(IAccountService.class);
		// 找到我的好友
		List<String> friendIds = dao.getFriendIds(userId);
		try {
			list = new ArrayList<AppFriend>();
			BasicRequest br = new BasicRequest(request);
			// 判断查询条数
			if (friendIds != null && friendIds.size() > 0) {
				for (String friendId : friendIds) {
					AppFriend appFriend = null;
					User user = null;
					Account account = null;

					// ID可能存在两个表中
					user = userService.getUser(friendId);
					if (user != null) {
						// 查询账号
						appFriend = new AppFriend();
						appFriend.setAvatar(br.getImage(user.getHeadPortrait()));
						appFriend.setHxId(user.getHxUserName());
						appFriend.setNickName(user.getNickName());
						appFriend.setUserId(user.getId());
					} else {
						// 查询子账号
						account = accountService.getAccountById(friendId);

						appFriend = new AppFriend();
						appFriend.setAvatar(br.getImage(account.getHeadPortrait()));
						appFriend.setHxId(account.getHxUserName());
						appFriend.setNickName(account.getContactName());
						appFriend.setUserId(account.getId());
					}

					// 保存到list
					if (appFriend != null) {
						list.add(appFriend);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<AppFriend> getAllFriendList(String userId, String searchKey, HttpServletRequest request) {
		// common init
		List<AppFriend> list = null;
		try {
			list = new ArrayList<AppFriend>();
			BasicRequest br = new BasicRequest(request);

			// 查账号
			List<User> userList = userDao.getUserWithApp(searchKey);
			// 查子账号
			List<Account> accountList = accountDao.getAccountWithApp(searchKey);

			if (accountList != null) {
				for (Account account : accountList) {
					// 构造AppUser模拟对象，整合账号信息，用于传输和缓存
					AppFriend friend = new AppFriend();
					friend.setAvatar(br.getImage(account.getHeadPortrait()));
					friend.setNickName(account.getContactName());
					friend.setUserId(account.getId());
					friend.setHxId(account.getHxUserName());
					list.add(friend);
				}
			}
			if (userList != null) {
				for (User user : userList) {
					AppFriend friend = new AppFriend();
					friend.setAvatar(br.getImage(user.getHeadPortrait()));
					friend.setNickName(user.getNickName());
					friend.setUserId(user.getId());
					friend.setHxId(user.getHxUserName());
					list.add(friend);
				}
			}

			// 更新是否是好友状态
			for (AppFriend appFriend : list) {
				// 获取用户好友状态
				FriendRelation friendRelation = this.chekdFriend(userId, appFriend.getUserId(), null);
				// 是/不是
				if (friendRelation != null && "1".equals(friendRelation.getStatus())) {
					appFriend.setFriend("1");
				} else {
					appFriend.setFriend("0");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<AppFriend> getAppSecondFriendList(String userId, HttpServletRequest request) {
		List<AppFriend> appFriendList = new ArrayList<AppFriend>();
		List<String> secondUserList = dao.getSecondFriendIds(userId);
		if (secondUserList != null) {
			for (String secondUserId : secondUserList) {
				AppFriend appFriend = getAppFriendById(secondUserId, request);
				if (appFriend != null) {
					appFriendList.add(appFriend);
				}
			}
		}
		return appFriendList;
	}

	@Override
	public AppFriend getAppFriendById(String id, HttpServletRequest request) {
		AppFriend friend = null;
		AppUser user = userService.getAppUserById(id);
		if (user != null) {
			friend = new AppFriend();
			friend.setAvatar(user.getAvatar());
			friend.setHxId(user.getHxUserName());
			friend.setNickName(user.getNickName());
			friend.setUserId(user.getUserId());
		}
		return friend;
	}

	@Override
	public List<String> getAppSecondFriendIds(String userId) {
		return dao.getSecondFriendIds(userId);
	}

	@Override
	public String getFriendStatus(String userId, String friendId) {
		String status = "0";
		// 查询是否为好友关系
		FriendRelation friendRelation = this.chekdFriend(userId, friendId, "1");
		// 当存在好友关系显示好友
		if (friendRelation != null && (!friendRelation.getFriendId().equals(userId) || !friendRelation.getUserId().equals(userId))) {
			status = "1";
		} else {
			// 当不存在好友关系时,查询是否为好友的好友(二度人脉)
			List<String> ids = this.getAppSecondFriendIds(userId);
			if (ids != null) {
				for (String string : ids) {
					// 供需发布者ID匹配二度好友
					if (friendId.equals(string)) {
						status = "2";
						break;
					}
				}
			}
		}
		return status;
	}
}

// @SuppressWarnings("unchecked")
// @Override
// public List<String> getSecondFriendList(String userId, HttpServletRequest
// request) {
// Map<String, Object> map = new HashMap<String, Object>();
// // sql查询 根据UserId 查询friendId
// StringBuffer sql = new
// StringBuffer("select fr.friendId from FriendRelation as fr where 1 = 1");
// sql.append(" and fr.status = 1");
// sql.append(" and fr.userId IN " + "(SELECT friendId " +
// "FROM FriendRelation " + "WHERE status = 1 " + "AND (userId = :userId " +
// "OR friendId = :userId))");
// sql.append(" and fr.friendId not IN " + "(SELECT friendId " +
// "FROM FriendRelation " + "WHERE status = 1 " + "AND (userId = :userId " +
// "OR friendId = :userId))");
// sql.append(" and (fr.userId != :userId or fr.friendId != :userId)");
// map.put("userId", userId);
//
// // sql查询 根据friendId 查询userId
// StringBuffer sql2 = new
// StringBuffer("select fr.userId from FriendRelation as fr where 1 = 1");
// sql2.append(" and fr.status = 1");
// sql2.append(" and fr.friendId IN " + "(SELECT userId " +
// "FROM FriendRelation " + "WHERE status = 1 " + "AND (userId = :userId " +
// "OR friendId = :userId))");
// sql2.append(" and fr.userId not IN " + "(SELECT userId " +
// "FROM FriendRelation " + "WHERE status = 1 " + "AND (userId = :userId " +
// "OR friendId = :userId))");
// sql2.append(" and (fr.userId != :userId or fr.friendId != :userId)");
// map.put("userId", userId);
//
// try {
// // 导入sql查询
// List<String> strList = dao.list(sql.toString(), map);
// List<String> srtList2 = dao.list(sql2.toString(), map);
// if (strList == null || strList.size() < 0) {
// return srtList2;
// }
// return strList;
// } catch (Exception e) {
// e.printStackTrace();
// }
// return null;
// }