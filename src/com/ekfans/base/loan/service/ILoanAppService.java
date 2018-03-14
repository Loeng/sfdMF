package com.ekfans.base.loan.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.loan.model.LoanApp;
import com.ekfans.pub.util.Pager;

/**
 * 贷款申请Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: ekfans
 * @author lgy
 * @date 2016-4-25
 * @version 1.0
 */
public interface ILoanAppService {
	/**
	 * 查询所有可加载的订单
	 * 
	 * @param orderId
	 * @param orderType
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	public List<Object[]> getLoadOrders(String storeId, String orderId, String orderType, String startTime, String endTime, Pager pager);

	/**
	 * 新增或更新融资申请
	 * 
	 * @param request
	 * @return
	 */
	public Boolean saveOrUpdateLoanApp(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 根据申请ID获取申请对象
	 * 
	 * @param loanAppId
	 *            申请对象ID
	 * @param type
	 *            false:单纯的对象，true:包含子
	 * @return
	 */
	public LoanApp getAppById(String loanAppId, Boolean type);

	/**
	 * 列表查询
	 * 
	 * @param bankId
	 * @param loanTypeId
	 * @param appStatus
	 * @param pager
	 * @return
	 */
	public List<LoanApp> listLoanApp(String storeId, String bankId, String loanTypeId, String appStatus, Pager pager);

	/**
	 * 修改融资申请的状态
	 * 
	 * @param app
	 * @return
	 */
	public Boolean changeAppStatus(LoanApp app, String appStatus, String creator, String creatorId, String creatorType);

	/**
	 * 获取与下游企业之间的往来流水
	 * @param storeId 企业id
	 * @param wlqyId	往来企业id
	 * @param orderId 	订单id
	 * @param orderType	订单类型 （0：普通 2：绿色商城 3：危废）
	 * @param startTime	开始时间	
	 * @param endTime	结束时间
	 * @param pager		分页
	 * @return
	 */
	public List<Object[]> getWllsOrders(String storeId, String wlqyId, String orderId,
			String orderType, String startTime, String endTime, Pager pager);

	/**
	 * 查询所有历史订单
	 * 
	 * @param orderId
	 * @param orderType
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	public List<Object[]> getHistoreOrders(String storeId, String orderId, String orderType, String startTime, String endTime, Pager pager);

	/**
	 * 导出普通订单或者绿色商城订单
	 * @param orderType
	 * @param orders
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean exportOrderExcel(String orderType, List<Object[]> orders,
			HttpServletResponse response) throws Exception;

	/**
	 * 导出危废订单
	 * @param orders
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean exportWfOrderExcel(List<Object[]> orders,
			HttpServletResponse response) throws Exception;

	/**
	 * 导出历史订单
	 * @param orders
	 * @param response
	 * @return
	 * @throws Exception
	 */
	boolean exportHistoryOrderExcel(List<Object[]> orders, HttpServletResponse response) throws Exception;
}
