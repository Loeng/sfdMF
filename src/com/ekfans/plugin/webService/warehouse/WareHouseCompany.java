package com.ekfans.plugin.webService.warehouse;


/**
 * 企业表
 * 
 * @ClassName Company
 * @Description TODO
 * @author ekfans
 * @date 2016-2-17
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class WareHouseCompany{

	// id
	private String id;
	// 企业名称
	private String name;
	// 企业类型 （0：个人会员，1：产生企业，2：采购商，3：处置企业,4:运输企业,5:供应商）
	private String type;
	// 联系人姓名
	private String contactMen;
	// 联系人手机号
	private String contactMobile;
	// 联系人座机
	private String contactTel;
	// 联系传真
	private String contactFax;
	// 联系人邮箱
	private String contactMail;
	// 联系地址
	private String contactAddress;
	// 同步时间
	private String createTime;
	// 更新时间
	private String updateTime;
	// 关联user
	private WareHouseAccount account;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContactMen() {
		return contactMen;
	}

	public void setContactMen(String contactMen) {
		this.contactMen = contactMen;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactMail() {
		return contactMail;
	}

	public void setContactMail(String contactMail) {
		this.contactMail = contactMail;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
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

	public WareHouseAccount getAccount() {
		return account;
	}

	public void setAccount(WareHouseAccount account) {
		this.account = account;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
