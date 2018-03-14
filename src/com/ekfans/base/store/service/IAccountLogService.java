package com.ekfans.base.store.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.ekfans.base.store.model.AccountLog;
import com.ekfans.pub.util.Pager;

/**
 * 
 * 
 * @ClassName: IStoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IAccountLogService {

	/**
	 * 保存商户资金日志
	 * 
	 * @param accountLog
	 * @param session
	 */
	public void addLog(AccountLog accountLog, Session session);

	/**
	 * 保存商户资金日志
	 * 
	 * @param accountLog
	 * @param session
	 */
	public void updateLog(AccountLog accountLog, Session session);

	/**
	 * 获取商户可用资金余额
	 * 
	 * @param storeId
	 * @return
	 */
	public BigDecimal getAvlBal(String storeId);

	/**
	 * 客户入金操作（充值--仅先绑定长沙银行账户的客户）
	 * 
	 * @param store
	 * @param bankLog
	 * @param accountLog
	 * @param price
	 */
	public boolean sufficient(String storeId, BigDecimal price);

	/**
	 * 客户出金操作（提现）
	 * 
	 * @param store
	 * @param bankLog
	 * @param accountLog
	 * @param price
	 */
	public boolean takeMoney(String storeId, BigDecimal price, BigDecimal charge);

	/**
	 * 在线交易
	 * 
	 * @param storeId
	 *            支付方ID
	 * @param receiveId
	 *            收款方ID
	 * @param orderId
	 *            订单ID
	 * @param type
	 *            类型 - 1-部分付款；2-完结付款3：违约付款（功能号为2、3时，该笔订单不允许再发起资金冻结、付款交易）
	 * @param price
	 *            - 支付金额
	 * @return
	 */
	public boolean payForOrder(String storeId, String receiveId, String orderId, String type, BigDecimal price);

	/**
	 * 获取银行行号
	 * 
	 * @param bankNo
	 * @param bankName
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getBankNo(String storeId, String bankNo, String bankName, Pager pager);

	/**
	 * 获取跨行出金手续费
	 * 
	 * @param bankNo
	 * @param bankName
	 * @param pager
	 * @return
	 */
	public Double getCharge(BigDecimal price, String storeId);
}