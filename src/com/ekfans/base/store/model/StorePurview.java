package com.ekfans.base.store.model;

import java.util.ArrayList;
import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 企业权限功能菜单--实体类
 * 
 * @ClassName: StorePurview
 * @author zgm
 * @date 2014-01-06 上午11:35:38 
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class StorePurview extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 父级ID
	private String parentId = "";
	// 权限名称
	private String className = "";
	// 访问路径
	private String fullPath = "";
	// 状态（true:启用，false:禁用）
	private Boolean status = false;
	// 排序
	private Integer position = 0;
	// 等级
	private Integer level = 0;
	// 图标
	private String icon = "";
	// 默认打开功能ID
	private String defaultOpen = "";
	// 顶级菜单ID
	private String topId = "";
	
	// =================== 临时数据字段 ===================
	private Boolean checked = false; // 是否选中（true:选中，false:未选中）
	private List<StorePurview> childList = new ArrayList<StorePurview>(); // 子权限集合

	// =================== get set ===================
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDefaultOpen() {
		return defaultOpen;
	}

	public void setDefaultOpen(String defaultOpen) {
		this.defaultOpen = defaultOpen;
	}

	public String getTopId() {
		return topId;
	}

	public void setTopId(String topId) {
		this.topId = topId;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<StorePurview> getChildList() {
		return childList;
	}

	public void setChildList(List<StorePurview> childList) {
		this.childList = childList;
	}

}