package com.ekfans.base.wfOrder.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContractDetails
 * @Description: TODO(危废品订单表)
 * @author ZJin
 * @date 2015-3-23 上午9:35:50
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
@Entity
public class WfOrderLog extends BasicBean {
	// 危废订单ID
	private String wfOrderId = "";
	// 操作人
	private String creator = "";
	// 操作人名称
	private String creatorName = "";
	// 操作日志
	private String note = "";
	// 操作时间
	private String createTime = "";

	public String getWfOrderId() {
		return wfOrderId;
	}

	public void setWfOrderId(String wfOrderId) {
		this.wfOrderId = wfOrderId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
