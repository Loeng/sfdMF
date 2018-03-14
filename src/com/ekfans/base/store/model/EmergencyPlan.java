package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 运输企业道路紧急预案日志实体类
 * 
 * @ClassName: CarInfo
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class EmergencyPlan extends BasicBean {

	private static final long serialVersionUID = 1L;
	// 运输企业ID
	private String storeId = "";
	// 道路紧急预案附件
	private String file = "";
	//是否有效  0--无效   1--有效
	private String type="";
	// 道路紧急预案有效期开始时间
	private String startTime = "";
	// 道路紧急预案有效期结束时间
	private String endTime = "";
	// 创建时间
	private String createTime = "";
	// 更新时间
	private String updateTime = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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