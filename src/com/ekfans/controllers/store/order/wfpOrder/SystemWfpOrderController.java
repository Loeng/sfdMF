package com.ekfans.controllers.store.order.wfpOrder;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderLog;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.base.wfOrder.util.WfOrderTransStore;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemWfpOrderController extends BasicController {
	@Autowired
	private IWfOrderService wfOrderService;

	@RequestMapping(value = "/system/wfpOrder/getWfpOrderList")
	public String getWfpOrderList(String orderId, String wfName, String status, String startTime, String endTime, String buyName, String saleName, HttpServletRequest request, HttpServletResponse response) {
		Pager page = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);
		List<WfOrder> list = new ArrayList<WfOrder>();
		list = wfOrderService.getWfOrderList(null, null, wfName, null, status, startTime, endTime, orderId, saleName, buyName, page, null, null);
		request.setAttribute("wfOrderList", list);
		request.setAttribute("pager", page);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("orderId", orderId);
		request.setAttribute("wfName", wfName);
		request.setAttribute("status", status);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("buyName", buyName);
		request.setAttribute("saleName", saleName);

		return "/system/order/orderWfp/orderWfpList";
	}

	@RequestMapping(value = "/system/wfpOrder/getWfOrderById")
	public String getWfOrderById(HttpSession session, HttpServletRequest request, String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		WfOrder wfOrder = wfOrderService.getWfOrderById(id);

		if (wfOrder != null) {
			List<WfOrderTransStore> storeList = wfOrderService.getWfOrderTransList(id);
			double listQuantity = wfOrderService.getSurplusQuantityByScrapWfpId(wfOrder.getScrapId(), id);
			// List<WfOrderPrice> prices = wfOrderService.getWfOrderPrices(id);
			request.setAttribute("storeList", storeList);
			request.setAttribute("listQuantity", listQuantity);
			// request.setAttribute("prices", prices);
		}
		List<WfOrderLog> orderLogs = wfOrderService.getOrderLog(id);
		request.setAttribute("wfOrder", wfOrder);
		request.setAttribute("orderLogs", orderLogs);

		return "/system/order/orderWfp/wfOrderShow";
	}
}
