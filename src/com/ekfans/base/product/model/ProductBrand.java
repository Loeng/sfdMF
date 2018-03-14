package com.ekfans.base.product.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 品牌--实体类
 * 
 * @ClassName: BuyerCompany
 * @Description: 
 * @author zgm
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ProductBrand extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 品牌名称
	private String name = "";
	// 用于修改页面的选中属性
	private Boolean checked = false;
	// 品牌图标
	private String icon = "";
	// 链接地址
	private String linkUrl = "";
	// 创建时间
	private String createTime = "";
	// 排序
	private Integer position = 0;
	// 状态
	private Boolean status = false;
	// 品牌描述
	private String note = "";

	// ===========  get set  ===========
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean isChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}