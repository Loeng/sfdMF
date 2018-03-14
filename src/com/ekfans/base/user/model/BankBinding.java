package com.ekfans.base.user.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行卡绑定--实体类
 * 
 * @ClassName: BankBinding
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class BankBinding extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 企业ID
	private String storeId = "";
	// 银行ID
	private String bankId = "";
	// 开户人姓名
	private String fullName = "";
	// 开户手机号
	private String phone = "";
	// 银行卡号
	private String cardNo = "";
	// 开户行所在地
	private String address = "";
	// 绑定日期
	private String createTime = "";
	// 状态(绑定状态)
	private Boolean status = false;

	// ============= 临时数据 =============
	private String bankName; // 银行名称
	private Bank bank;// 银行

	// ============= get set =============
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

}
