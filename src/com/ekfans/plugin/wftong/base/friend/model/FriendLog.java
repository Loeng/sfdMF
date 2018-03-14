package com.ekfans.plugin.wftong.base.friend.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/** 
 * 好友日志
 * 
 * @Title: FriendLog
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @author yx
 * @date 2015年7月22日
 * @version 1.0
 */
public class FriendLog extends BasicBean{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 532830200239275719L;
	// 登陆用户id
	private String userId = "";
	// 添加时间
	private String createTime = "";
	// 操作
	private String operate = "";
	// 备注
	private String note = "";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
