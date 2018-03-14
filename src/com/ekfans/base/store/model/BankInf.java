package com.ekfans.base.store.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 虚拟账户绑定表
 * 
 * @ClassName: Store
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
public class BankInf extends BasicBean {
	// 银行全称
	private String bankName = "";

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}