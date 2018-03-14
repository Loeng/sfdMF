package com.ekfans.base.order.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 收藏实体类
 * 
 * @Title: AppCollect
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年5月6日
 * @version 1.0
 */
public class AppCollect extends BasicBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6224294542904243793L;
	// 收藏的类型 供/求0、车/货1 (备注：供需后面由于需求变动取消了收藏功能)
	private String contentType = "0";
	// 收藏的ID
	private String contentId = "";
	// 评论人ID
	private String userId = "";
	// 创建时间
	private String createTime = "";
	// 修改时间
	private String updateTime = "";


	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
}
