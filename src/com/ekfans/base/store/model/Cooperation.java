package com.ekfans.base.store.model;

import org.springframework.scheduling.quartz.SimpleTriggerBean;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 合作机构表
 * @ClassName Cooperation
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月15日
 */
public class Cooperation extends BasicBean {

	private static final long serialVersionUID = 1L;
	
	// 合作机构名称
	private String orgName;
	
	// 联系人
	private String contactMan;
	
	// 联系电话
	private String contactPhone;
	
	// 邮件
	private String email;
	
	// 合作机构类型(0:环保合作机构 1:金融合作机构)
	private String type;
	
	// 创建时间
	private String createTime;
	
	// 更新时间
	private String updateTime;
	
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getContactMan() {
		return contactMan;
	}

	public void setContactMan(String contactMan) {
		this.contactMan = contactMan;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
