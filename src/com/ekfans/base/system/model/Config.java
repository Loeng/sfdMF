package com.ekfans.base.system.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商城配置实体类
 * 
 * @Title: Config.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class Config extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 配置名称
	private String name = "";
	
	// 配置内容
	private String content = "";
	
	// 配置类型
	private boolean type = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}
}