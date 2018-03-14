package com.ekfans.base.system.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 信息配置模板表
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
public class MessageConfigDetail extends BasicBean {
	// 模板名称
	public String name = "";

	// 排序
	public int index = 0;

	// 模板状态
	public Boolean status = false;

	// 发送类型 0-及时发送，1-异步发送，2-定时发送
	public String sendType = "";

	// 消息类型 0-全部，1-短信，2-邮件
	public String messageType = "";

	// 短信正文
	public String mobileContent = "";

	// 邮件标题
	public String mailTitle = "";

	// 邮件正文
	public String mailContent = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMobileContent() {
		return mobileContent;
	}

	public void setMobileContent(String mobileContent) {
		this.mobileContent = mobileContent;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

}
