package com.ekfans.plugin.wftong.base.easemob.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 群组
 * @author Administrator
 *
 */
public class Groups extends BasicBean{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5143285339484241775L;

	// 群组ID
	private String groupsId = "";
	
	// 群主ID
	private String userId = "";
	
	// 创建时间
	private String createTime = "";
	
	// 群组名称
	private String groupName = "";


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getGroupsId() {
		return groupsId;
	}

	public void setGroupsId(String groupsId) {
		this.groupsId = groupsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
