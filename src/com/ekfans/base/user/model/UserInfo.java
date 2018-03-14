package com.ekfans.base.user.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 会员详细信息--实体类
 * 
 * @ClassName: UserInfo
 * @Description: 
 * @author zgm
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class UserInfo extends BasicBean {
	
	private static final long serialVersionUID = 1L;
	//会员ID
	private String userId = "";
	// 真实姓名
	private String realName = "";
	// 性别(false:女,true:男)
	private Boolean sex = false;
	// 住址
	private String areaId = "";
	// 家庭住址
	private String address = "";
	// 住宅电话
	private String homePhone = "";
	// 爱好
	private String hobby = "";
	// 出生年月日
	private String birthday = "";

	// ================ get set ================
	public String getUserId() {
	    return userId;
	}

	public void setUserId(String userId) {
	    this.userId = userId;
	}
	
	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getHobby() {
		return this.hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Boolean getSex() {
	    return sex;
	}

	public void setSex(Boolean sex) {
	    this.sex = sex;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
}