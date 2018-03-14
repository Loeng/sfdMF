package com.ekfans.base.channel.model;

import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 频道实体类
 * 
 * @Title: Channel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-24
 * @version 1.0
 */
@Entity
public class Channel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;

	// 频道名称
	private String name = "";

	// 频道位置
	private int position = 0;

	// 频道说明
	private String note = "";

	// 频道状态
	private boolean status = false;

	// 添加类型（空为总平台 优选商城0 大宗采购1）默认为优选商城
	private String type = "";
	// 模板路径
	private String viewPath = "";

	// 创建时间
	private String createTime = "";

	// 修改时间
	private String updateTime = "";

	// 频道类型 0:普通类型 1:连接类型 2:自定义
	private String channelType = "0";

	// 父频道ID
	private String parentId = "";

	// 链接路径
	private String linkUrl = "";

	// 自定义内容
	private String des = "";

	// 频道简介
	private String description = "";
	// 页面抬头KeyWords
	private String keyWords = "";

	private List<Channel> childs = null;

	// 频道访问是否需要登陆
	private boolean loginStatus = false;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getViewPath() {
		return viewPath;
	}

	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
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

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public boolean isLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<Channel> getChilds() {
		return childs;
	}

	public void setChilds(List<Channel> childs) {
		this.childs = childs;
	}
}