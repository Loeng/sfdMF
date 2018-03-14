package com.ekfans.base.system.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 信息配置表
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-9
 * @version 1.0
 */
@SuppressWarnings("serial")
@Entity
public class MessageConfig extends BasicBean {
	// 发件服务器
	public String host = "";

	// 邮箱帐号
	public String userName = "";

	// 邮箱密码
	public String password = "";

	// 发信人中文名
	public String mailName = "";

	// 发信人邮箱地址
	public String mailAddress = "";

	// 短信用户名
	public String mobileName = "";

	// 短信密码
	public String mobilePsw = "";

	// 短信URL
	public String mobileUrl = "";

	// 发件正文
	public String content = "";

	// 创建时间
	public String createTime = "";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public String getMobileName() {
		return mobileName;
	}

	public void setMobileName(String mobileName) {
		this.mobileName = mobileName;
	}

	public String getMobilePsw() {
		return mobilePsw;
	}

	public void setMobilePsw(String mobilePsw) {
		this.mobilePsw = mobilePsw;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public String getMailName() {
		return mailName;
	}

	public void setMailName(String mailName) {
		this.mailName = mailName;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
}
