package com.ekfans.controllers.store.order.orderManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
 * @Description: TODO(商户中心-订单管理-待发货订单)
 * @author 成都易科远见科技有限公司
 * @date 2014-4-14 下午02:53:40
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreOrderShipmentsController extends BasicController {
	@Autowired
	private IStoreOrderService storeOrderService;
	@Autowired
	private IOrderTreatDetailService orderTreatDetailService;
	@Autowired
	private IOrderDetailService orderDetailService;

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(待发货订单的集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/ship")
	public String list(HttpServletRequest request, HttpSession session) {
		// 获取当前的店铺Id
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String storeId = store.getId();
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

		List<Order> orders = storeOrderService.getStoreOrderWaitShipmentsByConditions(storeId, orderId, userName, beginDate, endDate, pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("currentpageStr", currentPage);
		request.setAttribute("pager", pager);

		// 将当前店铺的权限集合绑定回去
		request.setAttribute("parentChose", purviewId);
		request.setAttribute("storePurview", purview);
		request.setAttribute("storeChose", "ORDER_SHIP");

		return "/userCenter/store/order/orderManager/orderShipments";
	}

	/**
	 * 
	 * @Title: list2
	 * @Description: TODO(待发货订单集合的重写) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/ship2/{beginDate}/{endDate}/{status}")
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

		List<Order> orders = storeOrderService.getStoreOrderWaitShipmentsByConditions(storeId, orderId, userName, beginDate, endDate, pager);
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
		session.setAttribute("storeChose", "ORDER_SHIP"); // 商户中心左侧菜单的默认选中

		return "/userCenter/store/order/orderManager/orderShipments";
	}

	/**
	 * 
	 * @Title: storeOrderHandel
	 * @Description: TODO(待发货订单操作) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/orderWaitShipmentsHandel/{orderId}")
	public String storeOrderHandel(@PathVariable String orderId,HttpServletRequest request, HttpSession session) {
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetail(orderId);
		// 查询出订单的跟踪信息
		List<OrderTreatDetail> treatDetails = orderTreatDetailService.getOrderTreatDetailListByOrderId(orderId);
		// 获取当前的店铺Id
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String purviewId = "ORDER";
		StorePurview purview = Cache.getStorePurview(store.getRoleId(), purviewId, true);
		// 查询出当前订单号所对应的商品信息
		@SuppressWarnings("unused")
		List<Product> products = storeOrderService.getProductByOrderId(orderId);
		OrderAddress address = null;
		Order order = null;
		// 查询出订单的详情信息
		Object[] objects = storeOrderService.getStoreOrderDetailById(orderId);
		// 对于已付款的订单查询出付款时间
		OrderTreatDetail treatDetail = null;
		// 预Java内防空指针
		if (objects != null) {
			// 从订单详情取出OrderAddress
			address = (OrderAddress) objects[0];
			// 从订单详情取出Order
			order = (Order) objects[1];
			// 已付款的订单查询出付款时间
			if (!"0".equals(order.getStatus()) && !"1".equals(order.getStatus()) && !"2".equals(order.getStatus())) {
				treatDetail = new OrderTreatDetail();
				treatDetail.setCreateTime(storeOrderService.getOrderPayTimeOrderId(orderId));
			}
		}
		// 统计支付金额【实际订单收入金额加商品运费】
		String paymentPrice = order.getPaid().add(order.getFare()).toString();
		// 将订单的信息绑定到request对象上
		request.setAttribute("orderDetails", orderDetails);
		request.setAttribute("address", address);
		request.setAttribute("order", order);
		session.setAttribute("storePurview", purview);
		session.setAttribute("parentChose", purviewId);
		request.setAttribute("treatDetail", treatDetail);
		request.setAttribute("treatDetails", treatDetails);
		request.setAttribute("paymentPrice", paymentPrice);
		// 返回视图
		return "/userCenter/store/order/orderManager/orderHandle/orderWaitShipmentsListHandle";
	}

	/**
	 * 
	 * @Title: modifyStoreOrderPrice
	 * @Description: TODO(商户中心-交易管理-代发货订单发货) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param paid
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/deliveryStoreOrder")
	@ResponseBody
	public Object deliveryStoreOrder(HttpSession session, HttpServletRequest request) {
		// 获取订单号
		String orderId = request.getParameter("orderId");
		// 获取物流公司名字
		String logisticsName = request.getParameter("logisticsName");
		// 获取运单号码
		String logisticsNo = request.getParameter("logisticsNo");
		// 获取实际运费
		String actualShipping = request.getParameter("actualShipping");

		// 获取当前店铺用户名
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		// 获取订单操作记录
		return storeOrderService.deliveryStoreOrder(orderId, store.getStoreName(), logisticsName, logisticsNo, actualShipping);
	}

	/**
	 * 
	 * @Title: modifyStoreOrderPrice
	 * @Description: TODO(商户中心-交易管理-供应商发货订单发货) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param paid
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/gongYingFaHuo")
	@ResponseBody
	public Object gYDeliveryStoreOrder(HttpSession session, HttpServletRequest request) {
		// 获取订单号
		String orderId = request.getParameter("orderId");

		// 获取当前店铺用户名
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		// 获取订单操作记录
		return storeOrderService.deliveryStoreOrder(orderId, store.getStoreName(), null, null, null);
	}
}
