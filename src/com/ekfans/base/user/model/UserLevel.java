package com.ekfans.base.user.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 会员等级实体类
 * 
 * @Title: UserLevel.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-23
 * @version 1.0
 */
@Entity
public class UserLevel extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 等级名称
	private String name = "";
	
	// 等级标志
	private String icon = "";
	
	// 最低升级条件
	private double loConditions = 0.00f;
	
	// 最高降级条件
	private double demote = 0.00f;
	
	// 类型
	private boolean type = false;
	
	// 等级说明
	private String note = "";

	public String getName()
	{
	    return name;
	}

	public void setName(String name)
	{
	    this.name = name;
	}

	public String getIcon()
	{
	    return icon;
	}

	public void setIcon(String icon)
	{
	    this.icon = icon;
	}

	public double getLoConditions()
	{
	    return loConditions;
	}

	public void setLoConditions(double loConditions)
	{
	    this.loConditions = loConditions;
	}

	public double getDemote()
	{
	    return demote;
	}

	public void setDemote(double demote)
	{
	    this.demote = demote;
	}

	public boolean isType()
	{
	    return type;
	}

	public void setType(boolean type)
	{
	    this.type = type;
	}

	public String getNote()
	{
	    return note;
	}

	public void setNote(String note)
	{
	    this.note = note;
	}

}