package com.ekfans.controllers.store.order.orderManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.order.service.IOrderTreatDetailService;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: StoreOrderListController
 * @Description: TODO(跳转到待付款的订单的页面)
 * @author 成都易科远见科技有限公司
 * @date 2014-4-14 下午02:53:40
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreOrderWaitPayController extends BasicController {
	// 注入service
	@Autowired
	private IStoreOrderService storeOrderService;
	@Autowired
	private IOrderTreatDetailService orderTreatDetailService;
	@Autowired
	private IOrderDetailService orderDetailService;

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(待付款订单的集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/sure")
	public String list(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		// 获取当前的店铺Id
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String storeId = store.getId();
		// 调用缓存 获取店铺会员对应的权限集合 这里获取的是交易管理页面的权限集合
		String purviewId = "ORDER";
		StorePurview purview = Cache.getStorePurview(store.getRoleId(), purviewId, true);

		// 定义分页
		Pager pager = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);

		// 获得相关参数
		String orderId = request.getParameter("orderId");
		String userName = request.getParameter("userName");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");

		List<Order> orders = storeOrderService.getStoreOrderWaitPayByConditions(storeId, orderId, userName, beginDate, endDate, pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("currentpageStr", currentPage);
		request.setAttribute("pager", pager);

		// 把权限对应的权限放入session
		session.setAttribute("storePurview", purview);
		session.setAttribute("parentChose", purviewId); // 商户中心顶部的默认选中
		session.setAttribute("storeChose", "ORDER_SURE"); // 商户中心左侧菜单的默认选中
		return "/userCenter/store/order/orderManager/orderWaitPay";
	}

	/**
	 * 
	 * @Title: list2
	 * @Description: TODO(代付款集合的重写) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/sure2/{beginDate}/{endDate}/{status}")
	public String list2(@PathVariable String beginDate, @PathVariable String endDate, @PathVariable String status, HttpServletRequest request, HttpSession session) {
		// 从Session中获得store对象
		Store store = ((Store) session.getAttribute(SystemConst.STORE));
		// 获取当前的店铺Id
		String storeId = store.getId();

		// 调用缓存 获取店铺会员对应的权限集合 这里获取的是交易管理页面的权限集合
		String purviewId = "ORDER";
		StorePurview purview = Cache.getStorePurview(store.getRoleId(), purviewId, true);

		// 定义分页
		Pager pager = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);

		// 获得相关参数
		String orderId = request.getParameter("orderId");
		String userName = request.getParameter("userName");

		List<Order> orders = storeOrderService.getStoreOrderWaitPayByConditions(storeId, orderId, userName, beginDate, endDate, pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("currentpageStr", currentPage);
		request.setAttribute("pager", pager);

		// 把权限对应的权限放入session
		session.setAttribute("storePurview", purview);
		session.setAttribute("parentChose", purviewId); // 商户中心顶部的默认选中
		session.setAttribute("storeChose", "ORDER_SURE"); // 商户中心左侧菜单的默认选中

		return "/userCenter/store/order/orderManager/orderWaitPay";
	}

	/**
	 * 
	 * @Title: storeOrderHandel
	 * @Description: TODO(查询出代付款订单详情 并跳往处理界面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/orderWaitPayHandel/{orderId}")
	public String storeOrderHandel(@PathVariable String orderId, HttpServletRequest request) {
		// 查询出当前订单号所对应的商品信息
		// 查询出当前订单号所对应的商品信息
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetail(orderId);
		// 查询出订单的跟踪信息
		// List<OrderTreatDetail> treatDetails =
		// storeOrderService.findOrderTreatDetailByOrderId(orderId);
		List<OrderTreatDetail> orderTreatDetailList = orderTreatDetailService.getOrderTreatDetailListByOrderId(orderId);

		OrderAddress address = null;
		Order order = null;
		OrderTreatDetail treatDetail = null;
		// 查询出订单的详情信息
		Object[] objects = storeOrderService.getStoreOrderDetailById(orderId);
		if (objects != null) {
			// 从订单详情取出OrderAddress
			address = (OrderAddress) objects[0];
			// 从订单详情取出Order
			order = (Order) objects[1];
		}
		// 将订单的信息绑定到request对象上
		request.setAttribute("orderDetails", orderDetails);
		request.setAttribute("address", address);
		request.setAttribute("order", order);
		request.setAttribute("treatDetail", treatDetail);
		request.setAttribute("treatDetails", orderTreatDetailList);
		request.setAttribute("ordersTotalPrice", order.getFare().add(order.getTotalPrice()));
		// 返回视图
		return "/userCenter/store/order/orderManager/orderHandle/orderWaitPayListHandle";
	}

	/**
	 * 
	 * @Title: storeOrderHandel
	 * @Description: TODO(查询出供应商代付款订单详情 并跳往处理界面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/gongYingOrderWaitPayHandel/{orderId}/{actionType}/{orderType}")
	public String gongYingStoreOrderHandel(@PathVariable String orderId, @PathVariable String actionType, @PathVariable String orderType, HttpServletRequest request) {
		// 查询出当前订单号所对应的商品信息
		// 查询出当前订单号所对应的商品信息
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetail(orderId);
		// 查询出订单的跟踪信息
		// List<OrderTreatDetail> treatDetails =
		// storeOrderService.findOrderTreatDetailByOrderId(orderId);
		List<OrderTreatDetail> orderTreatDetailList = orderTreatDetailService.getOrderTreatDetailListByOrderId(orderId);

		OrderAddress address = null;
		Order order = null;
		OrderTreatDetail treatDetail = null;
		// 查询出订单的详情信息
		Object[] objects = storeOrderService.getStoreOrderDetailById(orderId);
		if (objects != null) {
			// 从订单详情取出OrderAddress
			address = (OrderAddress) objects[0];
			// 从订单详情取出Order
			order = (Order) objects[1];
		}
		// 将订单的信息绑定到request对象上
		request.setAttribute("orderDetails", orderDetails);
		request.setAttribute("address", address);
		request.setAttribute("order", order);
		request.setAttribute("treatDetail", treatDetail);
		request.setAttribute("treatDetails", orderTreatDetailList);
		request.setAttribute("ordersTotalPrice", order.getFare().add(order.getTotalPrice()));
		request.setAttribute("actionType", actionType);
		request.setAttribute("orderType", orderType);
		// 返回视图
		return "/userCenter/store/order/gongXiao/orderHandle/orderWaitPayListHandle";
	}

	/**
	 * 
	 * @Title: modifyStoreOrderPrice
	 * @Description: TODO(商户中心-交易管理-代付款订单修改价格) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param paid
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/modifyOrderPrice/{orderId}")
	@ResponseBody
	public Object modifyStoreOrderPrice(@PathVariable String orderId, HttpSession session, HttpServletRequest request) {
		boolean modifyOk = false;

		// 修改后的运费金额
		String newFare = request.getParameter("totalFreight");
		// 修改后的订单总金额
		String newProductPriceTotal = request.getParameter("ordersTotalPrice");
		// 获取当前店铺用户名
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		if (!StringUtil.isEmpty(newFare)) {
			modifyOk = orderTreatDetailService.updateOrderTotalPriceGuessPrice(orderId, store.getStoreName(), OrderConst.ORDER_TREAT_FREIGHT, newFare);
		}
		if (!StringUtil.isEmpty(newProductPriceTotal)) {
			modifyOk = false;
			modifyOk = orderTreatDetailService.updateOrderTotalPriceGuessPrice(orderId, store.getStoreName(), OrderConst.ORDER_TREAT_MODIFY, newProductPriceTotal);
		}

		// String createrId = store.getId();
		// 获取订单操作记录
		// boolean modifyOk =
		// storeOrderService.modifyStoreOrderPrice(orderId,totalFreight,createrId);
		return modifyOk;
	}
}
