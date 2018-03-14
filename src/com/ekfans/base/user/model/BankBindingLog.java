package com.ekfans.base.user.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 银行卡绑定--日志类
 * 
 * @ClassName: BankBinding
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class BankBindingLog extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 企业ID
	private String storeId = "";
	// 银行ID
	private String area = "";
	// 开户人姓名
	private String address = "";
	// 开户手机号
	private String fullName = "";
	// 开户人身份证
	private String idCardNo = "";
	// 银行卡号
	private String cardNo = "";
	// 银行卡密码(预留字段)
	private String cardPwd = "";
	// 绑定日期
	private String createTime = "";
	// 状态(删除(3)、更新(2)、新增(1))
	private String status = "";
	
	// =============  临时数据  =============
	private String bankName; // 银行名称
	private Bank bank;// 银行

	// =============  get set  =============
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardPwd() {
		return cardPwd;
	}

	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
