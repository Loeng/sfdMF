package com.ekfans.base.product.model;

import java.util.ArrayList;
import java.util.List;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品分类--实体类
 * 
 * @ClassName: ProductCategory
 * @Description: 
 * @author lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ProductCategory extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 分类名称
	private String name = "";
	// 分类编号
	private String no = "";
	// 父分类ID
	private String parentId = "";
	// 分类描述
	private String note = "";
	// 排序
	private Integer position = 0;
	// 分类图标
	private String icon = "";
	// 全路径
	private String fullPath = "";
	// 状态
	private Boolean status = false;
	// 关联模版
	private String templateId = "";
	// 店铺ID -- 对应店铺表(Store)主键
	private String storeId = "";
	// 默认是否展开于分类
	private Boolean showSub = false;
	// 审核状态
	private Integer checkStatus = 0;
	// 审核人
	private String checkMan = "";
	// 审核时间
	private String checkTime = "";
	// 审核说明
	private String checkNote = "";
	// 创建时间
	private String createTime = "";
	// 商品分类短路径 - 临时字段，不操作数据库
	private String linkUrl = "";

	// ===================  临时数据  ===================
	// 下级菜单 商品所属子分类
	private List<ProductCategory> childList = new ArrayList<ProductCategory>();

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Boolean isShowSub() {
		return showSub;
	}

	public void setShowSub(Boolean showSub) {
		this.showSub = showSub;
	}

	public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<ProductCategory> getChildList() {
		return childList;
	}

	public void setChildList(List<ProductCategory> childList) {
		this.childList = childList;
	}

}