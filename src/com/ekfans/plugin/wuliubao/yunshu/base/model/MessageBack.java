package com.ekfans.plugin.wuliubao.yunshu.base.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 客户留言反馈实体
 * 
 * @author Administrator
 *
 */
@Entity
public class MessageBack extends BasicBean {

	private static final long serialVersionUID = 1L;
    //用户留言内容
	private String content;
	//用户id
	private String userId;
	//用户账号
	private String userName;
	//用户昵称
	private String nickName;
    //创建时间
	private String createTime;
	//是否反馈(1:已反馈,2:未反馈)
	private String type="2";
	//反馈内容
	private String feedbackContent;
	//反馈人ID
	private String feedbackID;
	//反馈人
	private String feedbackMan;
	//反馈时间
	private String feedbackTime;
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public String getFeedbackID() {
		return feedbackID;
	}

	public void setFeedbackID(String feedbackID) {
		this.feedbackID = feedbackID;
	}

	public String getFeedbackMan() {
		return feedbackMan;
	}

	public void setFeedbackMan(String feedbackMan) {
		this.feedbackMan = feedbackMan;
	}

	public String getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(String feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
