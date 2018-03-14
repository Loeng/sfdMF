package com.ekfans.controllers.user.refund;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.model.Refund;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dto.RefundDto;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IRefundService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class refundController extends BasicController {

	@Resource
	private IRefundService refundService;

	/**
	 * 退换/换货的列表
	 */
	@RequestMapping(value = "/user/refund/list")
	public String refundList(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String orderId = request.getParameter("orderSn"); // 订单编号
		String freightSn = request.getParameter("freightSn"); // 货运单号
		String currentpageStr = request.getParameter("pageNum"); // 获取当前页码

		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		pager.setCurrentPage(currentPage);

		List<RefundDto> lrf = refundService.getRefund(orderId, freightSn, user, pager);
		request.setAttribute("refunds", lrf);

		return "/userCenter/customer/refund/refundList";

	}

	/**
	 * 获取 退换货的信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "user/refund/edit/{id}")
	public String getRefund(@PathVariable String id, HttpServletRequest request) {
		Map<String, Object> map = refundService.getRefundInfo(id);

		Refund refund = (Refund) map.get("refund");
		Product product = (Product) map.get("product");
		Order order = (Order) map.get("order");
		OrderAddress orderAddress = (OrderAddress) map.get("orderAddress");
		
		List<OrderTreatDetail> lotd = (List<OrderTreatDetail>) map.get("lotd");

		request.setAttribute("refund", refund);
		request.setAttribute("product", product);
		request.setAttribute("order", order);
		request.setAttribute("address", orderAddress);
		request.setAttribute("lotds", lotd);

		return "/userCenter/customer/refund/refundEdit";
	}

}
