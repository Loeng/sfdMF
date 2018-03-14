package com.ekfans.controllers.web.vo;

public class NumBerAttribute {

	// 个人用户注册数量
	private Integer userNumber;
	// 企业用户注册数量
	private Integer companyNumber;
	// 多少企业收到款
	private Integer companyIn;

	// =============  get set  =============
	public Integer getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(Integer userNumber) {
		this.userNumber = userNumber;
	}

	public Integer getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(Integer companyNumber) {
		this.companyNumber = companyNumber;
	}

	public Integer getCompanyIn() {
		return companyIn;
	}

	public void setCompanyIn(Integer companyIn) {
		this.companyIn = companyIn;
	}

}
