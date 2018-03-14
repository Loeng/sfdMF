package com.ekfans.base.store.model;

/**
 * 企业法人简历信息--实体类
 * 
 * @ClassName: StoreLegalResume
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class StoreLegalResume implements java.io.Serializable {

	private static final long serialVersionUID = 6592182182405691582L;
	// 主键
	private String id;
	// 企业ID
	private String storeId;
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 在何地区何单位
	private String detailInfo;
	// 职务
	private String position;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}