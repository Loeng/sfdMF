package com.ekfans.plugin.wftong.base.friend.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 好友关系表
 * 
 * @Title: FriendRelation
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author yx
 * @date 2015年7月22日
 * @version 1.0
 */
@Entity
public class FriendRelation extends BasicBean{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3976959787730312800L;
	
	// 添加好友的Id
	private String userId = "";
	// 被添加好友的Id(注：如果userId为001，friendId为002，则他们相互为朋友)
	private String friendId = "";
	// 状态 0:申请,1:好友关系,2：拒绝
	private String status = "0";
	// 创建时间
	private String createTime = "";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
