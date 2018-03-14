package com.ekfans.base.order.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 举报实体类
 * 
 * @Title: AppReport
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年5月6日
 * @version 1.0
 */
public class AppReport extends BasicBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6224294542904243793L;
	// 举报的类型 供/求0、车/货1
	private String contentType = "0";
	// 举报的ID
	private String contentId = "";
	// 评论人ID
	private String userId = "";
	// 举报说明
	private String note = "";
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
