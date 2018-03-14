package com.ekfans.base.log.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 登陆日志--实体类
 * 
 * @ClassName: LoginLog
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class LoginLog extends BasicBean {

	private static final long serialVersionUID = 2026065500719167773L;
	// 登陆用户id
	private String userId = "";
	// 登陆用户IP
	private String loginIp = "";
	// 操作类型(1:登陆，2:注销)
	private Integer loginType = 1;
	// 类型(1:系统用户日志，2:前台用户日志)
	private Integer type = 1;
	// 登陆时间
	private String createTime = "";
	// 备注
	private String note = "";

	// ====================== get set ======================
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
