package com.ekfans.base.order.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 转发实体
 * 
 * @Title: AppForwarding
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年5月6日
 * @version 1.0
 */
public class AppForwarding extends BasicBean {

	private static final long serialVersionUID = -8988300216229456696L;
	// 转发的类型 供/求0、车/货1
	private String contentType = "0";
	// 转发的ID
	private String contentId = "";
	private String userId; // 操作人
	private String recipientId; // 接收者id
	private String recipientType = "0"; // 接收者类型0用户 1群组
	private String createTime;
	private String updateTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public String getRecipientType() {
		return recipientType;
	}

	public void setRecipientType(String recipientType) {
		this.recipientType = recipientType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
