package com.ekfans.base.store.model;

/**
 * 企业法人信息--实体类
 * 
 * @ClassName: LegalResume
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class LegalResume implements java.io.Serializable {

	private static final long serialVersionUID = 6592182182405691582L;
	// 主键
	private String id;
	// 企业ID
	private String companyId;
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 在何地区何单位
	private String detailInfo;
	// 职务
	private String position;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDetailInfo() {
		return this.detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}