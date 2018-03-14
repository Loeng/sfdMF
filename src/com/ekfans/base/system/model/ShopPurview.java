package com.ekfans.base.system.model;

import java.util.ArrayList;
import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 系统权限菜单--实体类
 * 
 * @ClassName: ShopPurview
 * @Description: 
 * @author zgm
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ShopPurview extends BasicBean {

	private static final long serialVersionUID = -5658166119533604985L;
	// 父级id
	private String parentId = "";
	// 菜单名称
	private String className = "";
	// 排序
	private Integer position = 0;
	// 菜单级别
	private Integer level = 0;
	// 状态（true:启用，false:禁用）
	private Boolean status = false;
	// 访问路径
	private String fullPath = "";
	// 菜单图标
	private String icon = "";
	// 顶级菜单id
	private String topId = "";

	// =================== 临时数据 ===================
	// 是否默认打开
	private Boolean checked = false;
	// 下级菜单
	private List<ShopPurview> childList = new ArrayList<ShopPurview>();

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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public List<ShopPurview> getChildList() {
		return childList;
	}

	public void setChildList(List<ShopPurview> childList) {
		this.childList = childList;
	}
	
}