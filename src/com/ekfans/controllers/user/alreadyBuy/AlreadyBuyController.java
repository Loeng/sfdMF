package com.ekfans.controllers.user.alreadyBuy;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.ekfans.base.order.service.IOrderAddressService;
import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.service.IOrderTreatDetailService;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.system.service.IShopCartService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: AlreadyBuyController
 * @Description: TODO 已买到的宝贝
 * @author 成都易科远见科技有限公司
 * @date May 14, 2014 6:10:58 AM
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class AlreadyBuyController extends BasicController {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IStoreOrderService storeOrderService;
	@Autowired
	private IOrderTreatDetailService orderTreatDetailService;
	@Autowired
	private IOrderDetailService orderDetailService;
	@Autowired
	private IOrderAddressService orderAddressService;
	@Autowired
	private IShopCartService shopCartService;
	@Autowired
	private IProductService productService;

	/**
	 * 
	 * @Title: view
	 * @Description: TODO 查询出满足条件的已买到宝贝 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/alreadyBuy")
	public String view(HttpServletRequest request) {
		// 获得状态
		String status = request.getParameter("status");
		// 获得shippingStatus
		String shippingStatus = request.getParameter("shippingStatus");
		// 获得userApp
		String userApp = request.getParameter("userApp");

		// 从缓存中取得userId
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String userId = user.getId();
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取分页数据
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(10);
		List<Order> orders = orderService.getMyOrders(pager, userId, status, shippingStatus, userApp);

		request.setAttribute("currentPage", currentPage);
		request.setAttribute("orders", orders);
		// 选中项
		request.setAttribute("cur", "orderList");
		request.setAttribute("pager", pager);
		return "/userCenter/customer/alreadyBuy/userAlreadyBuy";
	}

	/**
	 * 
	 * @Title: view
	 * @Description: TODO(查询出满足条件的已买到宝贝 load用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/loaduser/alreadyBuyforload")
	public String viewforload(HttpServletRequest request) {
		// 获得状态
		String status = request.getParameter("status");
		// 获得shippingStatus
		String shippingStatus = request.getParameter("shippingStatus");
		// 获得userApp
		String userApp = request.getParameter("userApp");

		// 从缓存中取得userId
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String userId = user.getId();
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取分页数据
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(10);
		List<Order> orders = orderService.getMyOrders(pager, userId, status, shippingStatus, userApp);

		request.setAttribute("currentPage", currentPage);
		request.setAttribute("orders", orders);
		// 选中项
		request.setAttribute("purviewId", "alreadyBuy");
		request.setAttribute("pager", pager);

		request.setAttribute("status", status);
		request.setAttribute("shippingStatus", shippingStatus);
		request.setAttribute("userApp", userApp);
		return "/userCenter/customer/alreadyBuy/noReceive";
	}

	/**
	 * 跳转到（退/换货申请）页面
	 */
	@RequestMapping(value = "/user/returnProduct/{productId}/{orderId}")
	public String orderDatails(@PathVariable String productId, @PathVariable String orderId, HttpServletRequest request) {
		OrderDetail orderDetail = this.orderDetailService.getOrderDetailByProductId(productId, orderId);
		// 根据orderId取得orderDetail对象
		Order order = orderService.getOrderId(orderId);
		// 根据orderId取得orderTreatDetail对象
		OrderTreatDetail orderTreatDetail = orderTreatDetailService.getOrderTreatDetailByOrderId(orderId);
		// 根据orderId取得orderAddress对象
		OrderAddress orderAddress = orderAddressService.getOrderAddressByOrderId(orderId);
		
		request.setAttribute("order", order);
		request.setAttribute("orderDetail", orderDetail);
		request.setAttribute("orderTreatDetail", orderTreatDetail);
		request.setAttribute("orderAddress", orderAddress);
		request.setAttribute("cur", "orderList");
		
		return "/userCenter/customer/alreadyBuy/productExchange";
	}

	/**
	 * 
	 * @Title: storeOrderHandel
	 * @Description: TODO 已购买的宝贝中点击“查看详情” 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/returnProduct/view/{orderId}")
	public String storeOrderHandel(@PathVariable String orderId, HttpServletRequest request) {
		// 查询出当前订单号所对应的商品信息
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetail(orderId);
		// 查询出订单的跟踪信息
		List<OrderTreatDetail> treatDetails = storeOrderService.findOrderTreatDetailByOrderId(orderId);
		// 查询出订单的详情信息
		Object[] objects = orderService.getOrderDetailByOrderId(orderId);

		OrderAddress address = null;
		Order order = null;
		if (objects != null) {
			// 从订单详情取出OrderAddress
			address = (OrderAddress) objects[0];
			// 从订单详情取出Order
			order = (Order) objects[1];
		}
		// 从订单详情取出OrderTreatDetail
		OrderTreatDetail treatDetail = (OrderTreatDetail) objects[2];

		if ("3".equals(order.getStatus()) || "4".equals(order.getStatus()) || "5".equals(order.getStatus())) {
			for (OrderTreatDetail td : treatDetails) {
				if (OrderConst.ORDER_TREAT_PAY.equals(td.getType())) {
					treatDetail.setCreateTime(td.getCreateTime());
				}
			}
		} else {
			treatDetail.setCreateTime("");
		}

		// 将订单的信息绑定到request对象上
		request.setAttribute("orderDetails", orderDetails);
		request.setAttribute("address", address);
		request.setAttribute("order", order);
		request.setAttribute("treatDetail", treatDetail);
		request.setAttribute("treatDetails", treatDetails);
		request.setAttribute("cur", "orderList");
		// 返回视图
		return "/userCenter/customer/alreadyBuy/productExchangeDetails";
	}

	/**
	 * 
	 * @Title: confirm
	 * @Description: TODO 确定收货，将status设为完成"4" 详细业务流程:
	 *               根据orderId取得orderDetail对象，在根据orderId取得order对象
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/alreadyBuy/confirm/{id}")
	@ResponseBody
	public Object confirm(@PathVariable String id, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);

		Order order = orderService.getOrderByOrderId(id);
		order.setStatus(OrderConst.ORDER_STATUS_WAIT_APP);
		if (orderService.modifyOrder(order)) {
			OrderTreatDetail otd = new OrderTreatDetail();

			otd.setOrderId(id);
			otd.setType(OrderConst.ORDER_TREAT_RECEIPT);
			otd.setCreateTime(DateUtil.getSysDateTimeString());
			if (StringUtil.isEmpty(user.getNickName())) {
				otd.setCreater(user.getName());
			}else{
				otd.setCreater(user.getNickName());
			}
			otd.setNote("确认收货");
			otd.setType(OrderConst.ORDER_TREAT_RECEIPT);

			this.orderTreatDetailService.add(otd);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @Title: cancel
	 * @Description: TODO 取消订单 将status设为取消“0” 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/alreadyBuy/cancel/{id}")
	@ResponseBody
	public Object cancel(@PathVariable String id, HttpServletRequest request) {
		Order order = orderService.getOrderByOrderId(id);
		order.setStatus(OrderConst.ORDER_STATUS_CANCEL);
		if (orderService.modifyOrder(order)) {
			return true;
		}
		return null;
	}

	/**
	 * 
	 * @Title: evaluation
	 * @Description: TODO 将未评价的订单获取到另一个页面 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/order/evaluation/{id}")
	public String evaluation(@PathVariable String id, HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取分页数据
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);

		// 根据传回来的Id取得order对象
		List<Order> orders = orderService.getAlreadyProductOrder(id, pager);
		/*
		 * if(orders != null){ // 查询订单下面的商品 for(Order o : orders){
		 * o.setDetails(orderDetailService.getOrderDetail(id)); } }
		 */
		request.setAttribute("cur", "orderList");
		request.setAttribute("orders", orders);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pager", pager);
		return "/userCenter/customer/alreadyBuy/alreadyBuyEvaluation";
	}

	/**
	 * 
	 * @Title: appraisal
	 * @Description: TODO 对订单的商品进行评价 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/order/evaluate")
	public String appraisal(HttpServletRequest request, HttpSession session) {
		// 获取评价类型【好，中，差评】
		String type = request.getParameter("etype");
		// 评价内容
		String[] note = request.getParameterValues("note");
		// 商品id
		String[] productId = request.getParameterValues("productId");
		// 订单id
		String orderId = request.getParameter("orderId");
		// 店铺id
		String storeId = request.getParameter("storeId");
		// 当前用户信息
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		// 获取订单详情id
		String[] orderDetailId = request.getParameterValues("orderDetailId");

		/*
		 * String[] noteArray = request.getParameterValues("note"); String[]
		 * typeArray = request.getParameterValues("etype"); String[]
		 * productIdArray = request.getParameterValues("productId"); String
		 * orderId = request.getParameter("orderId"); String storeId =
		 * request.getParameter("storeId");
		 */
		// User user=(User) request.getSession().getAttribute(SystemConst.USER);
		// 评论者名字 为当前店主的名字
		// String observer = user.getId();
		// String name = user.getName();
		// boolean addOk =
		// orderService.evaluateOrder(noteArray,typeArray,productIdArray,orderId,observer,storeId);
		// Order o = orderService.getOrderByOrderId(orderId);
		// o.setUserApp(true);
		// orderService.modifyOrder(o);
		// OrderTreatDetail oTD = new OrderTreatDetail();
		// oTD.setOrderId(orderId);
		// oTD.setType(OrderConst.ORDER_TREAT_USER_APP);
		// oTD.setCreateTime(DateUtil.getSysDateTimeString());
		// oTD.setCreater(name);
		// boolean addOtd = orderTreatDetailService.add(oTD);

		boolean judgment = this.orderService.appraiseProductInfo(orderId, storeId, productId, type, note, user, orderDetailId, 1);

		if (judgment) {
			return "redirect:/user/alreadyBuy";
		} else {
			return "redirect:/user/consult";
		}
	}

	/**
	 * 
	 * @Title: userByNow
	 * @Description: TODO(立即对买到的宝贝进行付款) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/buyNowUser/order/userByNow")
	public String userByNow(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SystemConst.USER);
		// 获取订单号
		String orderId = request.getParameter("orderId");
		// 查询出Order
		Order order = storeOrderService.getOrderById(orderId);
		// 获取订单总额
		String sumOrders = order.getPaid().toString();

		// 绑定对象到页面上
		request.setAttribute("totalOrderId", order.getPayId());
		// 绑定其他商品到页面
		List<Product> products = productService.getProductSalesRanking(4, "1");
		// 绑定总价格和集合到页面上
		request.setAttribute("products", products);
		request.setAttribute("sum", sumOrders);
		return "/web/shop/cart/shopCartPay";
	}
}
