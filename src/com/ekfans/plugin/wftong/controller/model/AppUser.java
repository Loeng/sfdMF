package com.ekfans.plugin.wftong.controller.model;

import java.io.Serializable;

/**
 * AppUser App用户对象（ 不进行物理保存，只用于缓存）
 * （注释：由于平台用户表分User和Account两个，为解决操作两个表繁琐的判断,
 * 引入AppUser作为载体，来集中处理App用户进行的所有CURD；见UserService服务类）
 * @author 成都易科远见有限公司
 *
 */
public class AppUser implements Serializable {

	private static final long serialVersionUID = -6472492918791442820L;

	// token令牌
	private String token;
	// 用户id
	private String userId;
	// 用户名
	private String name;
	// 显示名
	private String nickName;
	// 企业id
	private String storeId;
	// 企业名称
	private String companyName;
	// 环信账号
	private String hxUserName = "";
	// 环信密码
	private String hxPassword = "";
	// 用户类型：0：企业账号，1：子账号
	private String userType;
	// 企业简称
	private String storeRefer;
	// 企业电话
	private String phone;
	// 企业邮箱
	private String email;
	// 企业头像
	private String avatar = "";
	// 企业类型（1：产生企业 3：处置企业 4:运输企业）
	private String companyType = "";
	// 企业简介
	private String notes;
	// 企业联系人
	private String contactName;
	// 企业联系电话
	private String contactPhone;
	// 基础认证(0,1)
	private String commoned = "";
	// 处置资质认证(0,1)
	private String buyered = "";
	// 产生资质认证(0,1)
	private String salered = "";
	// 运输资质认证(0,1)
	private String transed = "";
	// 向好友显示电话开关(0 不显示 1显示)
	private boolean friendPhoneSwitch;
	// 向所有人显示电话开关(0 不显示 1显示)
	private boolean allPhoneSwitch;
	// 添加好友验证开关(0 不验证 1验证)
	private boolean friendValidSwitch;
	// 0 非好友，1好友，2好友的好友
	private String friendStatus;
	// 0为正式用户，1为待完善基本信息的游客
	private String touristType = "0";

	// private String authStatus;
	// private String department;
	// private String jobTitle;
	// private String companyType;

	public String getUserId() {
		return userId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	// public String getAuthStatus() {
	// return authStatus;
	// }
	//
	// public void setAuthStatus(String authStatus) {
	// this.authStatus = authStatus;
	// }
	//
	// public String getDepartment() {
	// return department;
	// }
	//
	// public void setDepartment(String department) {
	// this.department = department;
	// }
	//
	// public String getJobTitle() {
	// return jobTitle;
	// }
	//
	// public void setJobTitle(String jobTitle) {
	// this.jobTitle = jobTitle;
	// }

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getHxUserName() {
		return hxUserName;
	}

	public void setHxUserName(String hxUserName) {
		this.hxUserName = hxUserName;
	}

	public String getHxPassword() {
		return hxPassword;
	}

	public void setHxPassword(String hxPassword) {
		this.hxPassword = hxPassword;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getBuyered() {
		return buyered;
	}

	public void setBuyered(String buyered) {
		this.buyered = buyered;
	}

	public String getSalered() {
		return salered;
	}

	public void setSalered(String salered) {
		this.salered = salered;
	}

	public String getTransed() {
		return transed;
	}

	public void setTransed(String transed) {
		this.transed = transed;
	}

	public String getCommoned() {
		return commoned;
	}

	public void setCommoned(String commoned) {
		this.commoned = commoned;
	}

	public boolean getFriendPhoneSwitch() {
		return friendPhoneSwitch;
	}

	public void setFriendPhoneSwitch(boolean friendPhoneSwitch) {
		this.friendPhoneSwitch = friendPhoneSwitch;
	}

	public boolean getAllPhoneSwitch() {
		return allPhoneSwitch;
	}

	public void setAllPhoneSwitch(boolean allPhoneSwitch) {
		this.allPhoneSwitch = allPhoneSwitch;
	}

	public boolean getFriendValidSwitch() {
		return friendValidSwitch;
	}

	public void setFriendValidSwitch(boolean friendValidSwitch) {
		this.friendValidSwitch = friendValidSwitch;
	}

	public String getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(String realetionShap) {
		this.friendStatus = realetionShap;
	}

	public String getStoreRefer() {
		return storeRefer;
	}

	public void setStoreRefer(String storeRefer) {
		this.storeRefer = storeRefer;
	}

	public String getTouristType() {
		return touristType;
	}

	public void setTouristType(String touristType) {
		this.touristType = touristType;
	}
	// public String getCompanyType() {
	// return companyType;
	// }
	//
	// public void setCompanyType(String companyType) {
	// this.companyType = companyType;
	// }

}
