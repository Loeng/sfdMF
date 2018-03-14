package com.ekfans.plugin.webService.warehouse;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 管理账号信息--实体类
 * 
 * @ClassName: Account
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class WareHouseAccount extends BasicBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6306342485062472820L;
	// 所属企业ID
	private String companyId = "";
	//角色Id
	private String roleId ="";
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
	
	
	public String getCompanyId() {
		return companyId;
	}
	public String getRoleId() {
		return roleId;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getType() {
		return type;
	}
	public String getDepartment() {
		return department;
	}
	public String getContactName() {
		return contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public Boolean getStatus() {
		return status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public String getNote() {
		return note;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	

}
