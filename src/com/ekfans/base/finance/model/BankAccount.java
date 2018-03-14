package com.ekfans.base.finance.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行账户实体类
 * 
 * @Title: StoreRole.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author lgy
 * @date 2016-04-21
 * @version 1.0
 */
@Entity
public class BankAccount extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 银行ID
	private String bankId = "";
	// 银行名称
	private String bankName = "";
	// 银行LOGO
	private String bankLogo = "";
	// 用户名
	private String name = "";
	// 密码
	private String password = "";
	// 部门ID
	private String departmentId = "";
	// 部门名称
	private String department = "";
	// 真实姓名
	private String realName = "";
	// 手机号
	private String phone = "";
	// 状态:false不启用，true启用
	private Boolean status = false;
	// 创建时间
	private String createTime = "";
	// 修改时间
	private String updateTime = "";
	// 备注
	private String note = "";
	// 是否重置密码 true:重置，false:不重置
	private Boolean resetPwd = false;

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankLogo() {
		return bankLogo;
	}

	public void setBankLogo(String bankLogo) {
		this.bankLogo = bankLogo;
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

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Boolean getResetPwd() {
		return resetPwd;
	}

	public void setResetPwd(Boolean resetPwd) {
		this.resetPwd = resetPwd;
	}
}