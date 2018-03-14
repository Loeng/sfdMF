package com.ekfans.plugin.wftong.base.log.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * App操作日志--实体类
 */
public class WftLog extends BasicBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2787746878988368374L;
	// 消息报文正文
	private String content = "";
	// 操作时间
	private String createTime = "";
	// 用户ID
	private String userId = "";
	// 用户昵称
	private String userName = "";
	// 操作类型
	private String source = "";
	// 操作的设备型号
	private String deviceModel = "";

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

}
