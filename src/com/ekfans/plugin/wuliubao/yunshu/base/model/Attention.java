package com.ekfans.plugin.wuliubao.yunshu.base.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 物流宝用户关注实体
 * @author Administrator
 *
 */
@Entity
public class Attention extends BasicBean{

	private static final long serialVersionUID = 1L;
	//关注人用户id
	private String userId="";
	//被关注用户id
	private String friendId="";
	// 关注时间
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
	
	
}
