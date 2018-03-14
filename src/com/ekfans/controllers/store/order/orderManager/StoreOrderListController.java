package com.ekfans.controllers.store.order.orderManager;

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
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.service.IOrderTreatDetailService;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: StoreOrderListController
 * @Description: TODO(跳转到交易管理的页面--所有订单)
 * @author 成都易科远见科技有限公司
 * @date 2014-4-14 下午02:53:40
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreOrderListController extends BasicController {

	// 定义service
	@Autowired
	private IStoreOrderService storeOrderService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IOrderTreatDetailService orderTreatDetailService;

	@Autowired
	private IProductService productService;

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(跳转到storeList页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/list")
	public String list(HttpServletRequest request, HttpSession session) {
		// 获取当前的店铺Id
		String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
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
		// 订单状态

		List<Order> orders = storeOrderService.getStoreOrderByConditions(storeId, orderId, userName, beginDate, endDate, null, "0", "1", pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentPage);
		return "/userCenter/store/order/orderManager/orderList";
	}

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(跳转到供应商中心的供应订单页面页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/gongXiaoOrder/list")
	public String gongXiaoList(HttpServletRequest request, HttpSession session) {
		// 获取当前的店铺Id
		String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
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
		// 订单状态

		List<Order> orders = storeOrderService.getStoreOrderByConditions(storeId, orderId, userName, beginDate, endDate, null, "2", "1", pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentPage);
		return "/userCenter/store/order/gongXiao/orderList";
	}

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(跳转到采购商的采购订单页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/caiOrder/list")
	public String caiGouList(HttpServletRequest request, HttpSession session) {
		// 获取当前的店铺Id
		String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
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
		// 订单状态

		List<Order> orders = storeOrderService.getCaiStoreOrder(storeId, orderId, userName, beginDate, endDate, null, "1", pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentPage);
		return "/userCenter/store/order/caiGouOrder/orderList";
	}

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(跳转到核心企业的采购订单页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/gongOrder/list")
	public String gongYingList(HttpServletRequest request, HttpSession session) {
		// 获取当前的店铺Id
		String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
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
		// pager.setRowsPerPage(10);
		// 获得相关参数
		String orderId = request.getParameter("orderId");
		String userName = request.getParameter("userName");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		// 订单状态
		List<Order> orders = storeOrderService.getCaiStoreOrder(storeId, orderId, userName, beginDate, endDate, null, "1", pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentPage);
		return "/userCenter/store/order/gongYingOrder/orderList";
	}

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(跳转到采购商预订单页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/ydOrder/list")
	public String ydList(HttpServletRequest request, HttpSession session) {
		// 获取当前的店铺Id
		String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
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
		// 订单状态
		List<Order> orders = storeOrderService.getCaiStoreOrder(storeId, orderId, userName, beginDate, endDate, null, "0", pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentPage);
		return "/userCenter/store/order/ydOrder/orderList";
	}

	/**
	 * 取消订单操作
	 */
	@RequestMapping(value = "/store/caigou/orderDelete/{id}")
	public String deleteCaiGouOrder(@PathVariable String id, HttpSession session) {
		String storeName = ((Store) session.getAttribute(SystemConst.STORE)).getStoreName();
		// 页面传来的订单号字符串，用逗号分隔
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			// 得到对应的订单信息
			Order o = orderService.getOrderByOrderId(ids[i]);
			// 设置订单状态为取消
			o.setStatus(OrderConst.ORDER_STATUS_CANCEL);
			// 修改
			// 设置订单跟踪信息
			OrderTreatDetail otd = new OrderTreatDetail();
			otd.setCreateTime(DateUtil.getSysDateTimeString());
			otd.setCreater(storeName);
			otd.setOrderId(o.getId());
			otd.setNote("【" + storeName + "】已取消订单");
			otd.setType(OrderConst.ORDER_TREAT_CANCEL);
			orderTreatDetailService.add(otd);
			if (!orderService.modifyOrder(o)) {

				return "error";
			}
		}
		return "redirect:/store/caiOrder/list";
	}

	/**
	 * 取消订单操作
	 */
	@RequestMapping(value = "/store/gongYing/orderDelete/{id}")
	public String deleteGongYingOrder(@PathVariable String id, HttpSession session) {
		String storeName = ((Store) session.getAttribute(SystemConst.STORE)).getStoreName();
		// 页面传来的订单号字符串，用逗号分隔
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			// 得到对应的订单信息
			Order o = orderService.getOrderByOrderId(ids[i]);
			// 设置订单状态为取消
			o.setStatus(OrderConst.ORDER_STATUS_CLOSE);
			// 修改

			// 设置订单跟踪信息
			OrderTreatDetail otd = new OrderTreatDetail();
			otd.setCreateTime(DateUtil.getSysDateTimeString());
			otd.setCreater(storeName);
			otd.setOrderId(o.getId());
			otd.setNote("【" + storeName + "】已取消订单");
			otd.setType(OrderConst.ORDER_TREAT_CANCEL);
			orderTreatDetailService.add(otd);
			if (!orderService.modifyOrder(o)) {
				return "error";
			}
		}
		return "redirect:/store/gongOrder/list";
	}

	/**
	 * 取消预订单操作
	 */
	@RequestMapping(value = "/store/yd/orderDelete/{id}")
	public String deleteYdOrder(@PathVariable String id, HttpSession session) {
		String storeName = ((Store) session.getAttribute(SystemConst.STORE)).getStoreName();
		// 页面传来的订单号字符串，用逗号分隔
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			// 得到对应的订单信息
			Order o = orderService.getOrderByOrderId(ids[i]);
			// 设置订单状态为取消
			o.setStatus(OrderConst.ORDER_STATUS_CLOSE);
			// 修改

			// 设置订单跟踪信息
			OrderTreatDetail otd = new OrderTreatDetail();
			otd.setCreateTime(DateUtil.getSysDateTimeString());
			otd.setCreater(storeName);
			otd.setOrderId(o.getId());
			otd.setNote("【" + storeName + "】已取消订单");
			otd.setType(OrderConst.ORDER_TREAT_CANCEL);
			orderTreatDetailService.add(otd);
			if (!orderService.modifyOrder(o)) {
				return "error";
			}
		}
		return "redirect:/store/ydOrder/list";
	}

	/**
	 * 
	 * @Title: confirm
	 * @Description: TODO 采购商中心采购订单确定收货，将status设为完成"4" 详细业务流程:
	 *               根据orderId取得orderDetail对象，在根据orderId取得order对象
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/caiGouOrder/confirm/{id}")
	public String confirm(@PathVariable String id, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		Order order = orderService.getOrderByOrderId(id);
		order.setStatus(OrderConst.ORDER_STATUS_WAIT_APP);
		if (orderService.modifyOrder(order)) {
			OrderTreatDetail otd = new OrderTreatDetail();

			otd.setOrderId(id);
			otd.setType(OrderConst.ORDER_TREAT_RECEIPT);
			otd.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			otd.setCreater(store.getId());
			otd.setType("确认收货");

			this.orderTreatDetailService.add(otd);
			return "redirect:/store/caiOrder/list";
		} else {
			return "redirect:/store/caiOrder/list";
		}
	}

	/**
	 * 
	 * @Title: confirm
	 * @Description: TODO 核心企业采购订单确定收货，将status设为完成"4" 详细业务流程:
	 *               根据orderId取得orderDetail对象，在根据orderId取得order对象
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/coreOrder/confirm/{id}")
	public String coreConfirm(@PathVariable String id, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		Order order = orderService.getOrderByOrderId(id);
		order.setStatus(OrderConst.ORDER_STATUS_WAIT_APP);
		if (orderService.modifyOrder(order)) {
			OrderTreatDetail otd = new OrderTreatDetail();

			otd.setOrderId(id);
			otd.setType(OrderConst.ORDER_TREAT_RECEIPT);
			otd.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			otd.setCreater(store.getId());
			otd.setType("确认收货");

			this.orderTreatDetailService.add(otd);
			return "redirect:/store/gongOrder/list";
		} else {
			return "redirect:/store/gongOrder/list";
		}
	}

	/**
	 * 
	 * @Title: confirm
	 * @Description: TODO 采购商中心采购订单确定收货，将status设为完成"4" 详细业务流程:
	 *               根据orderId取得orderDetail对象，在根据orderId取得order对象
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/confirmSh")
	public @ResponseBody String confirmSh(String id, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		Order order = orderService.getOrderByOrderId(id);
		order.setStatus(OrderConst.ORDER_STATUS_WAIT_APP);
		if (orderService.modifyOrder(order)) {
			OrderTreatDetail otd = new OrderTreatDetail();

			otd.setOrderId(id);
			otd.setType(OrderConst.ORDER_TREAT_RECEIPT);
			otd.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			otd.setCreater(store.getId());
			otd.setType("确认收货");

			return "1";
		}
		return "0";
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
	@RequestMapping(value = "/web/caiGouOrder/userByNow/{orderId}")
	public String userByNow(@PathVariable String orderId, HttpServletRequest request, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		// 查询出Order
		Order order = storeOrderService.getOrderById(orderId);
		// 获取订单总额
		String sumOrders = order.getPaid().toString();

		// 绑定对象到页面上
		request.setAttribute("totalOrderId", order.getPayId());
		// 绑定其他商品到页面
		List<Product> products = productService.getProductSalesRanking(4, "2");
		// 绑定总价格和集合到页面上
		request.setAttribute("products", products);
		request.setAttribute("orderType", order.getType());
		request.setAttribute("sum", sumOrders);
		return "/web/gouwu/shopCartPay";
	}

	/**
	 * 
	 * @Title: list2
	 * @Description: TODO(根据条件查询出订单记录-商户中心结算) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @param session
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/list2/{beginDate}/{endDate}/{status}")
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

		// 订单状态
		List<Order> orders = storeOrderService.getStoreOrderByConditions(storeId, orderId, userName, beginDate, endDate, status, "0", "1", pager);
		request.setAttribute("orders", orders);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentPage);

		// 把权限对应的权限放入session
		session.setAttribute("storePurview", purview);
		session.setAttribute("parentChose", purviewId); // 商户中心顶部的默认选中
		session.setAttribute("storeChose", purview.getDefaultOpen()); // 商户中心左侧菜单的默认选中

		return "/userCenter/store/order/orderManager/orderList";
	}

	/**
	 * 
	 * @Title: storeOrderHandel
	 * @Description: TODO(订单列表的处理) 详细业务流程: (根据状态的不同返回不同的处理，并进行不同的处理)
	 * @param @param orderId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/order/orderListHandel")
	public String storeOrderHandel(HttpServletRequest request) {
		String orderId = request.getParameter("orderId");
		String orderStatus = request.getParameter("status");
		String shippingStatus = request.getParameter("shippingStatus");
		String userApp = request.getParameter("userApp");
		String jiaGeType = request.getParameter("jiaGeType");
		// String faHuoType = request.getParameter("faHuoType");
		String actType = request.getParameter("actType");
		String orderType = request.getParameter("orderType");
		// 返回视图
		if ("2".equals(jiaGeType) && OrderConst.ORDER_STATUS_WAIT_PAY.equals(orderStatus)) {
			// 操作类型跳转到供应商修改价格
			return "redirect:/store/order/gongYingOrderWaitPayHandel/" + orderId + "/" + actType + "/" + orderType;
		} else if ("1".equals(jiaGeType) && OrderConst.ORDER_STATUS_WAIT_PAY.equals(orderStatus)) {
			// 操作类型跳转到核心企业修改价格
			return "redirect:/store/order/gylOrderWaitPayHandel/" + orderId + "/" + actType + "/" + orderType;
		} else if (OrderConst.ORDER_STATUS_WAIT_SEND.equals(orderStatus) && "false".equalsIgnoreCase(shippingStatus)) {
			// 操作类型为待发货
			return "redirect:/store/order/gylOrderWaitShipmentsHandel/" + orderId + "/" + actType + "/" + orderType;
		} else if (OrderConst.ORDER_STATUS_WAIT_PAY.equals(orderStatus)) {
			// 操作类型为代付款
			return "redirect:/store/order/orderWaitPayHandel/" + orderId;
		} else if (OrderConst.ORDER_STATUS_WAIT_REC.equals(orderStatus) && "true".equalsIgnoreCase(shippingStatus)) {
			// 操作类型为待收货
			return "redirect:/store/order/orderWaitShipmentsHandel/" + orderId;
		} else if (OrderConst.ORDER_STATUS_FINISH.equals(orderStatus) && "true".equalsIgnoreCase(userApp)) {
			// 操作类型为需要商户评价订单
			return "redirect:/store/order/orderEvaluateHandel/" + orderId;
		} else {
			// 操作类型为查看详情
			return "redirect:/store/order/orderRefundHandel/" + orderId;
		}
	}
}
