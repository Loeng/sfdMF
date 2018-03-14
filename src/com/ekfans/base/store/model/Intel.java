package com.ekfans.base.store.model;

/**
 * 资质--实体类
 * 
 * @ClassName: Intel
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Intel implements java.io.Serializable {

	private static final long serialVersionUID = -3707610447988133029L;
	// 主键
	private String id = "";
	// 资质名称
	private String name = "";
	// 状态（0:不启用,1:启用）
	private Boolean status = false;
	// 备注
	private String note = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";
	
	// ============= geter seter  =============
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getStatus() {
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
