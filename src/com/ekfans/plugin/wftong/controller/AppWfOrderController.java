package com.ekfans.plugin.wftong.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.plugin.wftong.util.MonitorUrlUtil;
import org.json.JSONObject;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderLog;
import com.ekfans.base.wfOrder.model.WfOrderTrans;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.base.wfOrder.service.IScrapWfpService;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.base.wfOrder.util.WfOrderTransStore;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wftong.AppServerController;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.plugin.wftong.controller.model.FiveSingleModel;
import com.ekfans.plugin.wftong.util.MyJSONObject;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppWfOrderController {
	// 相应主体
	private MyJSONObject jsonResponse;
	// 请求主体
	private MyJSONObject jsonRequest;
	// request
	private HttpServletRequest request;
	// 当前用户对象
	private AppUser appUser;

	private IWfOrderService wfOrderService = SpringContextHolder.getBean(IWfOrderService.class);
	private IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);
	private IContractService contractService = SpringContextHolder.getBean(IContractService.class);
	private IScrapWfpService wfpService = SpringContextHolder.getBean(IScrapWfpService.class);

	/**
	 * 构造
	 * @param jsonRequest 请求主体，用于获取请求参数
	 * @param jsonResponse 响应主体，用于返回结果数据
	 * @param appUser 当前用户
	 * @param request 
	 */
	public AppWfOrderController(MyJSONObject jsonRequest, MyJSONObject jsonResponse, AppUser appUser, HttpServletRequest request) {
		this.jsonRequest = jsonRequest;
		this.jsonResponse = jsonResponse;
		this.appUser = appUser;
		this.request = request;
	}

	public void wfOrderList() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "pageNo", "viewType", "orderStatus");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String viewType = jsonRequest.getString("viewType");
		String orderStatus = jsonRequest.getString("orderStatus");
		String pageNo = jsonRequest.getString("pageNo");

		String storeId = this.appUser.getStoreId();
		int currentPageNo = 0;
		try {
			currentPageNo = Integer.parseInt(pageNo);
		} catch (Exception e) {
		}
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(10);

		List<WfOrder> orderList = wfOrderService.getWfOrderList(null, null, null, orderStatus, null, null, null, null, null, null, pager, viewType, storeId);

		if (orderList != null && orderList.size() > 0) {
			List<WfOrderTrans> wfTrans = new ArrayList<WfOrderTrans>();
			for (WfOrder wfOrder : orderList) {
				wfTrans = new ArrayList<WfOrderTrans>();
				System.out.println(wfOrder.getId());
				// 附加运输车辆信息
				// 得到当前订单所有企业运输信息
				List<WfOrderTransStore> storeList = wfOrderService.getWfOrderTransList(wfOrder.getId());
				if (storeList != null && storeList.size() > 0) {
					// 遍历得到运输企业下面所有的车辆信息
					for (WfOrderTransStore wfOrderTransStore : storeList) {
						wfTrans.addAll(wfOrderTransStore.getTransList());
					}
				}
				wfOrder.setWfTrans(wfTrans);

				// 附加车辆监控url
				wfOrder.setMonitorUrl(MonitorUrlUtil.getUrl(MonitorUrlUtil.MonitorViewTypeEnum.VIEW_ORDER, wfOrder.getId()));
			}
		}

		if (orderList != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(orderList));
			jsonResponse.put("pageNo", currentPageNo);
			jsonResponse.put("totalPage", pager.getTotalPage());
		}
	}

	public void wfOrderDetails() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "wfOrderId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 复制来源：@RequestMapping(/store/order/wfOrderView/{wfOrderId}/{viewType}/{pageType})
		String wfOrderId = jsonRequest.getString("wfOrderId");
		// 订单
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		// 附加车辆监控url
		wfOrder.setMonitorUrl(MonitorUrlUtil.getUrl(MonitorUrlUtil.MonitorViewTypeEnum.VIEW_ORDER, wfOrder.getId()));
		// 日志
		List<WfOrderLog> orderLogs = wfOrderService.getOrderLog(wfOrderId);
		if (wfOrder != null) {
			// 合同
			Contract contract = contractService.getContractById(wfOrder.getContractId());
			// 运输企业信息
			List<WfOrderTransStore> storeList = wfOrderService.getWfOrderTransList(wfOrderId);

			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("contract", new JSONObject(contract));
			jsonResponse.put("storeList", JsonUtil.convertToJsonArray(storeList));
			jsonResponse.put("wfOrder", new JSONObject(wfOrder));
			jsonResponse.put("orderLogs", JsonUtil.convertToJsonArray(orderLogs));
		}

	}

	public void wfOrderFh() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "wfOrderId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String wfOrderId = jsonRequest.getString("wfOrderId");
		
		String storeId = this.appUser.getStoreId();

		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		Store store = storeService.getStore(storeId);

		if (wfOrderService.faHuo(wfOrder, store)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	public void wfOrderSh() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "wfOrderId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String wfOrderId = jsonRequest.getString("wfOrderId");
		String fiveSingleModelStr = jsonRequest.getString("fiveSingleList");

		// 将五联单字符串格式化为对象
		List<FiveSingleModel> fiveSingleModelList = new ArrayList<FiveSingleModel>();
		Type type = new TypeToken<List<FiveSingleModel>>() {
		}.getType();
		Gson gson = new Gson();
		fiveSingleModelList = gson.fromJson(fiveSingleModelStr, type);

		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		Store store = storeService.getStore(this.appUser.getStoreId());

		if (wfOrderService.queRenshApp(wfOrder, store, fiveSingleModelList, request)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	public void wfOrderQx() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "wfOrderId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String wfOrderId = jsonRequest.getString("wfOrderId");
		String storeId = this.appUser.getStoreId();

		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		Store store = storeService.getStore(storeId);

		if (wfOrderService.quXiao(wfOrder, store)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}
}
