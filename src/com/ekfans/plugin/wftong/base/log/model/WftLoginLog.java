package com.ekfans.plugin.wftong.base.log.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 用户登录日志--实体类
 */
public class WftLoginLog extends BasicBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -310853083270691680L;
	// 用户ID
	private String userId = "";
	// 操作时间
	private String createTime = "";
	// 操作时所在地
	private String address = "";
	// 操作类型 1登陆 0退出
	private String type = "";
	// 操作设备
	private String deviceModel = "";

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

}
