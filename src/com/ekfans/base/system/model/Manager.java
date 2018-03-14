package com.ekfans.base.system.model;

import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 系统后台用户--实体类
 * 
 * @ClassName: Manager
 * @author zgm
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Manager extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 用户名
	private String name = "";
	// 密码
	private String password = "";
	// 角色ID
	private String roleId = "";
	// 真实姓名
	private String realName = "";
	// 状态(true:启用，false:禁用)
	private Boolean status = false;
	// 手机号
	private String mobile = "";
	// 邮箱
	private String email = "";
	// 管理员权限集合
	private List<ShopPurview> shopPurviewList;
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";
	
	// =================== 临时数据字段 ===================
	private String roleName; // 角色名称
	
	// =================== get set ===================
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<ShopPurview> getShopPurviewList() {
		return shopPurviewList;
	}

	public void setShopPurviewList(List<ShopPurview> shopPurviewList) {
		this.shopPurviewList = shopPurviewList;
	}
	
}