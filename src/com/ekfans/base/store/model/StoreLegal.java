package com.ekfans.base.store.model;

/**
 * 企业法人信息--实体类
 * 
 * @ClassName: StoreLegal
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class StoreLegal implements java.io.Serializable {

	private static final long serialVersionUID = 5841666664377010938L;
	// 主键
	private String id;
	// 法人姓名
	private String legalName;
	// 法人身份证号
	private String legalIdCard;
	// 法人民族
	private String legalNation;
	// 法人出生年月
	private String legalBirth;
	// 法人性别(true:男，false:女)
	private Boolean legalSex;
	// 法人政治面貌
	private String legalPan;
	// 法人文化程度
	private String legalEducation;
	// 法人籍贯
	private String legalBplace;
	// 法人职称
	private String jobTitle;
	// 法人任现职时间
	private Integer hisCurrentTime;

	// ================== get set ==================
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getLegalIdCard() {
		return legalIdCard;
	}

	public void setLegalIdCard(String legalIdCard) {
		this.legalIdCard = legalIdCard;
	}

	public String getLegalNation() {
		return legalNation;
	}

	public void setLegalNation(String legalNation) {
		this.legalNation = legalNation;
	}

	public String getLegalBirth() {
		return legalBirth;
	}

	public void setLegalBirth(String legalBirth) {
		this.legalBirth = legalBirth;
	}

	public Boolean getLegalSex() {
		return legalSex;
	}

	public void setLegalSex(Boolean legalSex) {
		this.legalSex = legalSex;
	}

	public String getLegalPan() {
		return legalPan;
	}

	public void setLegalPan(String legalPan) {
		this.legalPan = legalPan;
	}

	public String getLegalEducation() {
		return legalEducation;
	}

	public void setLegalEducation(String legalEducation) {
		this.legalEducation = legalEducation;
	}

	public String getLegalBplace() {
		return legalBplace;
	}

	public void setLegalBplace(String legalBplace) {
		this.legalBplace = legalBplace;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Integer getHisCurrentTime() {
		return hisCurrentTime;
	}

	public void setHisCurrentTime(Integer hisCurrentTime) {
		this.hisCurrentTime = hisCurrentTime;
	}

}