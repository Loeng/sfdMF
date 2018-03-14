package com.ekfans.base.store.model;

/**
 * 企业详细信息--实体类
 * 
 * @ClassName: StoreInfo
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class StoreInfo implements java.io.Serializable {

	private static final long serialVersionUID = 5841666664377010938L;
	// 主键
	private String id;
	// 法人姓名
	private String legalName;
	// 法人性别(true:男，false:女)
	private Boolean legalSex;
	// 法人出生年月
	private String legalBirth;
	// 法人电话
	private String legalMobile;
	// 法人民族
	private String legalNation;
	// 法人籍贯
	private String legalBplace;
	// 法人文化程度
	private String legalEducation;
	// 法人政治面貌
	private String legalPan;
	// 法人身份证号
	private String legalIdNumber;
	// 注册时间
	private String regTime;
	// 占地面积(单位：平方米)
	private Double areaNumber;
	// 通讯地址
	private String mailingAddress;
	// 邮政编码
	private Integer zipCode;
	// 单位类型（1:个人独资企业，2:合伙企业）
	private Integer unitType;
	// 注册资金（单位：万元）
	private Double regCapital;
	// 职工人数
	private Integer numberEmployees;
	// 联系人姓名
	private String contactName;
	// 联系人性别（false:女，true:男）
	private Boolean contactSex;
	// 联系时间
	private String contactTime;
	// 联系人电话
	private String contactPhone;
	// 联系人传真
	private String contactFax;
	// 经营范围
	private String businessScope;
	// 未来事业 发展方向
	private String planning;
	// 营业执照号
	private String businessLicenseNumber;
	// 工商登记机关
	private String bureau;
	// 工商登记机关（登记时间）
	private String bureauTime;
	// 开户银行
	private String bank;
	// 开户银行（卡号）
	private String bankNumber;
	// 开户时间
	private String openingTime;
	// 贷款卡号
	private String reditCard;
	// 职称
	private String jobTitle;
	// 任现职时间
	private String hisCurrentTime;

	// ================== get set ==================
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLegalName() {
		return this.legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public Boolean getLegalSex() {
		return this.legalSex;
	}

	public void setLegalSex(Boolean legalSex) {
		this.legalSex = legalSex;
	}

	public String getLegalBirth() {
		return this.legalBirth;
	}

	public void setLegalBirth(String legalBirth) {
		this.legalBirth = legalBirth;
	}
	
	public String getLegalMobile() {
		return legalMobile;
	}

	public void setLegalMobile(String legalMobile) {
		this.legalMobile = legalMobile;
	}

	public String getLegalNation() {
		return this.legalNation;
	}

	public void setLegalNation(String legalNation) {
		this.legalNation = legalNation;
	}

	public String getLegalBplace() {
		return this.legalBplace;
	}

	public void setLegalBplace(String legalBplace) {
		this.legalBplace = legalBplace;
	}

	public String getLegalEducation() {
		return this.legalEducation;
	}

	public void setLegalEducation(String legalEducation) {
		this.legalEducation = legalEducation;
	}

	public String getLegalPan() {
		return this.legalPan;
	}

	public void setLegalPan(String legalPan) {
		this.legalPan = legalPan;
	}

	public String getLegalIdNumber() {
		return this.legalIdNumber;
	}

	public void setLegalIdNumber(String legalIdNumber) {
		this.legalIdNumber = legalIdNumber;
	}

	public String getRegTime() {
		return this.regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public Double getAreaNumber() {
		return this.areaNumber;
	}

	public void setAreaNumber(Double areaNumber) {
		this.areaNumber = areaNumber;
	}

	public String getMailingAddress() {
		return this.mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public Integer getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}

	public Integer getUnitType() {
		return this.unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}

	public Double getRegCapital() {
		return this.regCapital;
	}

	public void setRegCapital(Double regCapital) {
		this.regCapital = regCapital;
	}

	public Integer getNumberEmployees() {
		return this.numberEmployees;
	}

	public void setNumberEmployees(Integer numberEmployees) {
		this.numberEmployees = numberEmployees;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Boolean getContactSex() {
		return this.contactSex;
	}

	public void setContactSex(Boolean contactSex) {
		this.contactSex = contactSex;
	}

	public String getContactTime() {
		return this.contactTime;
	}

	public void setContactTime(String contactTime) {
		this.contactTime = contactTime;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactFax() {
		return this.contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getBusinessScope() {
		return this.businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public String getPlanning() {
		return this.planning;
	}

	public void setPlanning(String planning) {
		this.planning = planning;
	}

	public String getBusinessLicenseNumber() {
		return this.businessLicenseNumber;
	}

	public void setBusinessLicenseNumber(String businessLicenseNumber) {
		this.businessLicenseNumber = businessLicenseNumber;
	}

	public String getBureau() {
		return this.bureau;
	}

	public void setBureau(String bureau) {
		this.bureau = bureau;
	}

	public String getBureauTime() {
		return this.bureauTime;
	}

	public void setBureauTime(String bureauTime) {
		this.bureauTime = bureauTime;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankNumber() {
		return this.bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public String getOpeningTime() {
		return this.openingTime;
	}

	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}

	public String getReditCard() {
		return this.reditCard;
	}

	public void setReditCard(String reditCard) {
		this.reditCard = reditCard;
	}

	public String getJobTitle() {
		return this.jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getHisCurrentTime() {
		return this.hisCurrentTime;
	}

	public void setHisCurrentTime(String hisCurrentTime) {
		this.hisCurrentTime = hisCurrentTime;
	}
	
}