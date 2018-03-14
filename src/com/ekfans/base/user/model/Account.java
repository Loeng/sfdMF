package com.ekfans.base.user.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 会员账号信息--实体类
 * 
 * @ClassName: Store
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Account extends BasicBean {
	// ID头：用于多表唯一性
	public static final String SINGLE_MARK = "MA";

	private static final long serialVersionUID = 1L;
	// 所属店铺ID
	private String storeId = "";
	// 用户名
	private String name = "";
	// 密码
	private String password = "";
	// 会员类型（继承User）
	private String type = "0";
	// 部门名称
	private String department = "";
	// 联系人名称
	private String contactName = "";
	// 联系人手机
	private String contactPhone = "";
	// 用户状态
	private Boolean status = false;
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";
	// 备注
	private String note = "";
	// 组织结构ID
	private String orgId = "";
	// 头像
	private String headPortrait = "";
	// 是否被第三方使用
	private boolean useData = false;
	// 环信ID
	private String hxUserName = "";
	// 环信PWD
	private String hxPassWord = "";
	// 向所有人显示电话开关(0 不显示 1显示)
	private boolean friendPhoneSwitch;
	// 向好友显示电话开关(0 不显示 1显示)
	private boolean allPhoneSwitch;
	// 添加好友验证开关(0 不验证 1验证)
	private boolean friendValidSwitch;

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public boolean isUseData() {
		return useData;
	}

	public void setUseData(boolean useData) {
		this.useData = useData;
	}

	public String getHxUserName() {
		return hxUserName;
	}

	public void setHxUserName(String hxUserName) {
		this.hxUserName = hxUserName;
	}

	public String getHxPassWord() {
		return hxPassWord;
	}

	public void setHxPassWord(String hxPassWord) {
		this.hxPassWord = hxPassWord;
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

}
