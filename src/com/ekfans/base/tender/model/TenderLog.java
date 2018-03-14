package com.ekfans.base.tender.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 招标报名表
 * 
 * @ClassName: TenderLog
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class TenderLog extends BasicBean {

	private static final long serialVersionUID = 2026065500719167773L;
	// 招标文件ID
	private String tenderId = "";
	// 投标报名企业ID
	private String creator = "";
	// 投标报名企业名称
	private String creatorName = "";
	// 投标报名联系人姓名
	private String name = "";
	// 投标报名联系人手机号
	private String phone = "";
	// 投标报名联系人邮箱
	private String mail = "";
	// 投标报名时间
	private String createTime = "";

	public String getTenderId() {
		return tenderId;
	}

	public void setTenderId(String tenderId) {
		this.tenderId = tenderId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
