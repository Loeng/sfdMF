package com.ekfans.controllers.store.order.ReturnManagement.vo;

import com.ekfans.base.order.model.Refund;

/**
 * 
 * @ClassName: RefundVO
 * @Description: TODO(拥有展示数据的实体类)
 * @author ekfans
 * @date 2014-5-14 上午11:32:37
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("serial")
public class RefundVO extends Refund {
	// 货运单号
	private String freightNum;

	// 退款金额
	private double refundMoney;

	public String getFreightNum() {
		return freightNum;
	}

	public void setFreightNum(String freightNum) {
		this.freightNum = freightNum;
	}

	public double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(double refundMoney) {
		this.refundMoney = refundMoney;
	}

}
