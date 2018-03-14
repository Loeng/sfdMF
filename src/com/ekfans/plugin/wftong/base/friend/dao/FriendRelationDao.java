package com.ekfans.plugin.wftong.base.friend.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.plugin.wftong.base.friend.model.FriendRelation;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.StringUtil;

/**
 * 好友关系Dao接口实现
 * 
 * @Title: UserRelationDao
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author yx
 * @date 2015年7月7日
 * @version 1.0
 */
@Repository
@SuppressWarnings("unchecked")
public class FriendRelationDao extends GenericDao implements IFriendRelationDao {

	public FriendRelationDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.user.model.FriendRelation";
	}

	@Override
	public FriendRelation checkFriend(String userId, String friendId, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (userId.equals(friendId)) {
			return null;
		}
		StringBuffer sql = new StringBuffer("select fr from FriendRelation as fr where 1 = 1");

		// 传入userId交叉查询
		sql.append(" and (fr.userId = :userId or fr.friendId = :userId)");
		map.put("userId", userId);
		// 传入friendId查询
		sql.append(" and (fr.userId = :friendId or fr.friendId = :friendId)");
		map.put("friendId", friendId);
		// 当status为1时 用户进行同意操作 需要查询好友状态为0的数据
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and fr.status = :status");
			map.put("status", status);
		}

		try {
			// service查询好表是否存在数据
			List<FriendRelation> list = this.list(sql.toString(), map);
			// 存在则返回好友信息
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getFriendIds(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		try {
			// 可能是我加别人
			String sql1 = " SELECT friendId as uId FROM FriendRelation WHERE userId = :userId and status = 1 ";
			List<String> friends1 = this.list(sql1, map);

			// 也可能是别人加我
			String sql2 = " SELECT userId as uId FROM FriendRelation WHERE friendId = :userId and status = 1 ";
			List<String> friends2 = this.list(sql2, map);

			// 合并&去重
			friends1.removeAll(friends2);
			friends1.addAll(friends2);
			return friends1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getSecondFriendIds(String userId) {
		/*
		 * 不支持嵌套子查询，只有操作集合
		 */
		StringBuffer sql = null;
		try {
			// 第一步：查询出我朋友的ID
			List<String> friendIds = getFriendIds(userId);
			StringBuffer sqlFriends = new StringBuffer();
			// 拼接
			if (friendIds != null && friendIds.size() > 0) {
				for (int i = 0; i < friendIds.size(); i++) {
					sqlFriends.append("'").append(friendIds.get(i)).append("'");
					if (i < friendIds.size() - 1) {
						sqlFriends.append(",");
					}
				}
			}

			// 第二步：根据我的好友去查询他们的好友ID,由于不支持Union查询,做两次查询
			// 1.正面
			List<String> frisFriList1 = new ArrayList<String>();
			sql = new StringBuffer();
			sql.append("SELECT friendId as ffId FROM FriendRelation WHERE 1=1");
			if (sqlFriends.length() > 0) {
				sql.append(" AND userId in (");
				sql.append(sqlFriends.toString());
				sql.append(" ) ");
			}
			sql.append("	AND status = 1");
			frisFriList1 = this.list(sql.toString(), null);
			// 2.反面
			List<String> frisFriList2 = new ArrayList<String>();
			sql = new StringBuffer();
			sql.append("	SELECT userId as ffId from FriendRelation WHERE 1=1");
			if (sqlFriends.length() > 0) {
				sql.append(" AND friendId in (");
				sql.append(sqlFriends.toString());
				sql.append(" ) ");
			}
			sql.append("	AND status = 1");
			frisFriList2 = this.list(sql.toString(), null);
			// 合并
			frisFriList1.removeAll(frisFriList2);
			frisFriList1.addAll(frisFriList2);

			// 第三步：剔除用户自己，和用户原有的好友
			while (frisFriList1.contains(userId)) {
				frisFriList1.remove(userId);
			}
			if (friendIds != null) {
				frisFriList1.removeAll(friendIds);
			}

			return frisFriList1;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getAllFriendIds(String userId, String searchKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
