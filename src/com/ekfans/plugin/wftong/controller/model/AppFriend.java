package com.ekfans.plugin.wftong.controller.model;

import java.io.Serializable;

/**
 * AppFriend
 * @author 成都易科远见有限公司
 *
 */
public class AppFriend implements Serializable {

	private static final long serialVersionUID = -4201007319797747268L;

	private String nickName = "";
	private String avatar = "";
	private String userId = "";
	private String hxId = "";
	// 是否是朋友0不是,1是
	private String friend;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHxId() {
		return hxId;
	}

	public void setHxId(String hxId) {
		this.hxId = hxId;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}

}
