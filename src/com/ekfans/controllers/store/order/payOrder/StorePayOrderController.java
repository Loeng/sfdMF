package com.ekfans.controllers.store.order.payOrder;

import java.math.BigDecimal;
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
import com.ekfans.base.order.model.OrderLog;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.order.service.IOrderLogService;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccountLogService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 
 * @ClassName: StoreOrderListController
 * @Description: TODO(大宗订单)
 * @author ekfans
 * @date 2014-4-14 下午02:53:40
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */

@Controller
public class StorePayOrderController extends BasicController {

	// 定义service
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IContractService contractService;
	@Autowired
	private IOrderLogService orderLogService;
	@Autowired
	private IAccountLogService accountLogService;
	@Autowired
	private IStoreOrderService storeOrderService;
	@Autowired
	private IOrderDetailService orderDetailService;

	/**
	 * 新增直付订单
	 * 
	 * @Title: add @Description: TODO(跳转到storeList页面) 详细业务流程:
	 * (详细描述此方法相关的业务处理流程) @param @param request @param @param
	 * response @param @param session @param @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	@RequestMapping(value = "/store/order/payorder/add")
	public String addPayOrder(HttpServletRequest request, HttpSession session) {
		// Store store = (Store) session.getAttribute(SystemConst.STORE);
		// if (store != null) {
		// store = storeService.getStore(store.getId());
		// session.setAttribute(SystemConst.STORE, store);
		// if (store.getAccountStatus() && store.getAccountSuccess()) {
		// BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
		// request.setAttribute("accountPrices", prices);
		// }
		// }
		return "/userCenter/store/payOrder/orderAdd";
	}

	/**
	 * 查看直付订单
	 * 
	 * @Title: add @Description: TODO(跳转到storeList页面) 详细业务流程:
	 * (详细描述此方法相关的业务处理流程) @param @param request @param @param
	 * response @param @param session @param @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	@RequestMapping(value = "/store/order/payorder/view/{orderId}/{type}")
	public String payOrderView(@PathVariable String orderId, @PathVariable String type, HttpServletRequest request, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		session.setAttribute(SystemConst.STORE, store);
		Order order = orderService.getOrderByOrderId(orderId);
		if (order != null) {
			if (OrderConst.ORDER_STATUS_WAIT_PAY.equals(order.getStatus()) && OrderConst.ORDER_PAY_TYPE_YUE.equals(order.getPayType())) {
				Store salStore = storeService.getStore(order.getStoreId());
				request.setAttribute("salStore", salStore);
			}

			if (store.getId().equals(order.getUserId())) {
				Store salStore = storeService.getStoreById(order.getStoreId());
				if (salStore != null) {
					order.setSalName(salStore.getStoreName());
				}
				order.setBuyName(store.getStoreName());
			} else {
				Store buyStore = storeService.getStoreById(order.getUserId());
				if (buyStore != null) {
					order.setBuyName(buyStore.getStoreName());
				}
				order.setSalName(store.getStoreName());
			}
			order.setBuyName(storeService.getStoreById(order.getUserId()).getStoreName());
			if (order.getContractType()) {
				Contract contract = contractService.getContractById(order.getContractId());
				if (contract != null) {
					order.setContractName(contract.getName());
				}
			}
		}
		request.setAttribute("order", order);
		if ("edit".equals(type)) {
			return "/userCenter/store/payOrder/orderEdit";
		} else {
			List<OrderLog> logList = orderLogService.getOrderLogs(orderId);
			request.setAttribute("orderLogs", logList);
			return "/userCenter/store/payOrder/orderView";
		}
	}

	/**
	 * 余额支付
	 * 
	 * @param orderId
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/payorder/byYue/{orderId}")
	public String payByYue(@PathVariable String orderId, HttpServletRequest request, HttpSession session) {

		List<OrderDetail> orderDetails = orderDetailService.getOrderDetail(orderId);
		// 查询出订单的跟踪信息
		List<OrderTreatDetail> treatDetails = storeOrderService.findOrderTreatDetailByOrderId(orderId);
		// 查询出订单的详情信息
		OrderAddress address = null;
		Order order = null;
		Object[] objects = orderService.getOrderDetailByOrderId(orderId);
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

		Store store = (Store) session.getAttribute(SystemConst.STORE);
		Store buyStore = null;
		Store salStore = null;
		store = storeService.getStore(store.getId());
		session.setAttribute(SystemConst.STORE, store);
		Order order_1 = orderService.getOrderByOrderId(orderId);
		if (!order_1.getUserId().isEmpty()) {
			order.setUserId(order_1.getUserId());
		}
		if (!order_1.getStoreId().isEmpty()) {
			order.setStoreId(order_1.getStoreId());
		}
		if (order != null) {
			if(store.getId().equals(order.getUserId())){
				salStore = storeService.getStore(order.getStoreId());
				buyStore = store;
			}else{
				salStore = store;
				buyStore = storeService.getStore(order.getUserId());
			}
			if (salStore != null) {
				order.setSalName(salStore.getStoreName());

			}

			if (buyStore != null) {
				order.setBuyName(buyStore.getStoreName());
			}
			if (order.getContractType()) {
				Contract contract = contractService.getContractById(order.getContractId());
				if (contract != null) {
					order.setContractName(contract.getName());
				}
			}
		}
		// 将订单的信息绑定到request对象上
		request.setAttribute("salStore", salStore);
		request.setAttribute("orderDetails", orderDetails);
		request.setAttribute("address", address);
		request.setAttribute("treatDetail", treatDetail);
		request.setAttribute("treatDetails", treatDetails);
		request.setAttribute("order", order);

		return "/userCenter/store/payOrder/orderPayView";

	}

	/**
	 * 查看直付订单
	 * 
	 * @Title: add @Description: TODO(跳转到storeList页面) 详细业务流程:
	 * (详细描述此方法相关的业务处理流程) @param @param request @param @param
	 * response @param @param session @param @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	@RequestMapping(value = "/store/order/payunlie")
	@ResponseBody
	public String payUnline(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String orderId = request.getParameter("orderId");
		Order order = orderService.getOrderByOrderId(orderId);
		order.setNickName(store.getStoreName());
		// 设置图标保存路径
		String currentPath = "/customerfiles/store/" + store.getId() + "/order/" + DateUtil.getNoSpSysDateString();
		// 调用方法获取分类图标，返回图标路径
		String certPath = FileUploadHelper.uploadFile("payCert", currentPath, request, response);
		order.setPayCert(certPath);
		order.setPayType(OrderConst.ORDER_PAY_TYPE_UNLINE);
		if (orderService.changePayOrderStatus(order, OrderConst.ORDER_STATUS_WAIT_PAY_SURE)) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 查看直付订单
	 * 
	 * @Title: add @Description: TODO(跳转到storeList页面) 详细业务流程:
	 * (详细描述此方法相关的业务处理流程) @param @param request @param @param
	 * response @param @param session @param @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	@RequestMapping(value = "/store/order/payorder/sure")
	@ResponseBody
	public String payOrderSure(String orderId, HttpServletRequest request, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		Order order = orderService.getOrderByOrderId(orderId);
		order.setNickName(store.getStoreName());
		String status = request.getParameter("status");
		boolean flag = false;
		if (OrderConst.ORDER_STATUS_WAIT_PAY.equals(status)) {
			flag = orderService.changePayOrderStatus(order, OrderConst.ORDER_STATUS_WAIT_PAY);
		}
		if (OrderConst.ORDER_STATUS_CLOSE.equals(status)) {
			flag = orderService.changePayOrderStatus(order, OrderConst.ORDER_STATUS_CLOSE);
		}
		if (flag) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 新增直付订单
	 * 
	 * @Title: add @Description: TODO(跳转到storeList页面) 详细业务流程:
	 * (详细描述此方法相关的业务处理流程) @param @param request @param @param
	 * response @param @param session @param @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	@RequestMapping(value = "/store/order/payordersave")
	@ResponseBody
	public String savePayOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		Order order = new Order();
		order.setUserId(store.getId());
		String storeId = request.getParameter("storeId");
		order.setStoreId(storeId);
		String totalPrice = request.getParameter("totalPrice");
		order.setTotalPrice(new BigDecimal(totalPrice));
		String note = request.getParameter("note");
		order.setNote(note);
		String contractType = request.getParameter("contractType");
		if ("1".equals(contractType)) {
			order.setContractType(true);
			String contractId = request.getParameter("contractId");
			order.setContractId(contractId);
		}

		String payType = request.getParameter("payType");
		order.setPayType(payType);

		order.setProductPrice(order.getTotalPrice());
		order.setStatus(OrderConst.ORDER_STATUS_NEW_ORDER);
		order.setCreateTime(DateUtil.getSysDateTimeString());
		order.setType(OrderConst.ORDER_TYPE_ZF);
		order.setPaid(order.getTotalPrice());
		if (!order.getContractType()) {
			// 设置图标保存路径
			String currentPath = "/customerfiles/store/" + store.getId() + "/order/" + DateUtil.getNoSpSysDateString();
			// 调用方法获取分类图标，返回图标路径
			String contractPath = FileUploadHelper.uploadFile("contract", currentPath, request, response);
			order.setContract(contractPath);
		}
		order.setNickName(store.getStoreName());
		if (orderService.add(order, null, null, null)) {
			return "true";
		}
		return "false";
	}

	/**
	 * 新增直付订单
	 * 
	 * @Title: add @Description: TODO(跳转到storeList页面) 详细业务流程:
	 * (详细描述此方法相关的业务处理流程) @param @param request @param @param
	 * response @param @param session @param @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	@RequestMapping(value = "/store/order/payorderupdate")
	@ResponseBody
	public String updatePayOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String id = request.getParameter("id");
		Order order = orderService.getOrderByOrderId(id);
		if (order == null) {
			order = new Order();
		}
		order.setUserId(store.getId());
		String storeId = request.getParameter("storeId");
		order.setStoreId(storeId);
		String totalPrice = request.getParameter("totalPrice");
		order.setTotalPrice(new BigDecimal(totalPrice));
		String note = request.getParameter("note");
		order.setNote(note);
		String contractType = request.getParameter("contractType");
		if ("1".equals(contractType)) {
			order.setContractType(true);
			String contractId = request.getParameter("contractId");
			order.setContractId(contractId);
		}

		String payType = request.getParameter("payType");
		order.setPayType(payType);

		order.setProductPrice(order.getTotalPrice());
		order.setStatus(OrderConst.ORDER_STATUS_NEW_ORDER);
		order.setCreateTime(DateUtil.getSysDateTimeString());
		order.setType(OrderConst.ORDER_TYPE_ZF);
		order.setPaid(order.getTotalPrice());
		if (!order.getContractType()) {
			// 设置图标保存路径
			String currentPath = "/customerfiles/store/" + store.getId() + "/order/" + DateUtil.getNoSpSysDateString();
			// 调用方法获取分类图标，返回图标路径
			String contractPath = FileUploadHelper.uploadFile("contract", currentPath, request, response);
			order.setContract(contractPath);
		}
		order.setNickName(store.getStoreName());
		if (orderService.update(order, null, null, null)) {
			return "true";
		}
		return "false";
	}

	/**
	 * 直付订单管理
	 * 
	 * @Title: add @Description: TODO(跳转到storeList页面) 详细业务流程:
	 * (详细描述此方法相关的业务处理流程) @param @param request @param @param
	 * response @param @param session @param @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	@RequestMapping(value = "/store/order/payorder/list/{listType}")
	public String payOrderList(@PathVariable String listType, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String orderId = request.getParameter("orderId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		if (!StringUtil.isEmpty(startTime)) {
			startTime = startTime + " 00:00:00";
		}
		if (!StringUtil.isEmpty(endTime)) {
			endTime = endTime + " 23:59:59";
		}
		String buyName = request.getParameter("buyName");
		String salName = request.getParameter("salName");
		Store store = (Store) session.getAttribute(SystemConst.STORE);

		String userId = "";
		String storeId = "";

		if ("rec".equals(listType)) {
			storeId = store.getId();
		} else {
			userId = store.getId();
		}

		// 从页面获取页码
		String pageNum = request.getParameter("pageNum");
		int currentPageNo = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPageNo = Integer.parseInt(pageNum);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// 定义翻页对象
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(6);

		List<Order> orderList = orderService.getPayOrderList(pager, OrderConst.ORDER_TYPE_ZF, userId, storeId, buyName, salName, orderId, startTime, endTime);

		request.setAttribute("orderId", orderId);
		request.setAttribute("buyName", buyName);
		request.setAttribute("salName", salName);
		request.setAttribute("pager", pager);
		request.setAttribute("listType", listType);
		request.setAttribute("orderList", orderList);

		return "/userCenter/store/payOrder/orderList";
	}

	/**
	 * 选择收款企业的列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/payorder/storelist")
	public String loadStoreList(HttpServletRequest request, HttpSession session) {
		// 从页面获取企业名称
		String storeName = request.getParameter("storeName");
		// 从页面获取企业类型
		String storeType = request.getParameter("storeType");
		// 从页面获取页码
		String pageNum = request.getParameter("pageNum");
		System.out.println(storeName);
		System.out.println(storeType);
		int currentPageNo = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPageNo = Integer.parseInt(pageNum);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// 定义翻页对象
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(6);

		// 调用Service查询企业集合
		List<Store> stores = storeService.listStore(pager, storeType, true, storeName, null, null, null);

		request.setAttribute("storeList", stores);
		request.setAttribute("storeName", storeName);
		request.setAttribute("storeType", storeType);
		request.setAttribute("pager", pager);
		return "/userCenter/store/payOrder/storeLoad";
	}

	/**
	 * 选择收款企业的列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/payorder/contractList")
	public String loadContractList(HttpServletRequest request, HttpSession session) {
		// 从页面获取企业名称
		String storeName = request.getParameter("storeName");
		// 从页面获取合同名称
		String contractName = request.getParameter("contractName");
		// 从页面获取合同编号
		String contractNo = request.getParameter("contractNo");
		// 从页面获取页码
		String pageNum = request.getParameter("pageNum");
		int currentPageNo = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPageNo = Integer.parseInt(pageNum);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// 定义翻页对象
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(6);

		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		// 调用Service查询企业集合
		List<Contract> contractList = contractService.orderChoseContracts(pager, store.getId(), contractName, storeName, null, null, null, contractNo, null);

		request.setAttribute("storeName", storeName);
		request.setAttribute("contractName", contractName);
		request.setAttribute("contractNo", contractNo);
		request.setAttribute("contractList", contractList);
		request.setAttribute("pager", pager);
		return "/userCenter/store/payOrder/contractLoad";
	}

	@RequestMapping(value = "/store/payorder/bcsPayWait")
	public String bcsPayWait(String orderId, String type, String orderType, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		BigDecimal price = accountLogService.getAvlBal(store.getId());
		request.setAttribute("listPrice", price);
		request.setAttribute("orderId", orderId);
		Order order = orderService.getOrderByOrderId(orderId);
		if (order.getType() == 1) {// 直付订单
			BigDecimal payPrice = order.getTotalPrice();
			request.setAttribute("payPrice", payPrice);
		} else {
			BigDecimal payPrice = order.getPaid();
			request.setAttribute("payPrice", payPrice);
		}
		request.setAttribute("payType", type);
		request.setAttribute("orderType", orderType);
		return "/userCenter/store/payOrder/bcsPayWait";
	}

	@RequestMapping(value = "/store/payorder/getStoreListPrice")
	@ResponseBody
	public String getListPrice(String orderId, String type, String orderType, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		BigDecimal price = accountLogService.getAvlBal(store.getId());
		return price.floatValue() + "";
	}

	@RequestMapping(value = "/store/payorder/bcsPay")
	@ResponseBody
	public String bcsPay(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);

		String orderId = request.getParameter("orderId");
		String type = request.getParameter("payType");
		// String orderType = request.getParameter("orderType");
		String payPassword = request.getParameter("payPassword");
		MD5Util md5Util = new MD5Util();
		if ((!StringUtil.isEmpty(store.getPayPassword()) && StringUtil.isEmpty(payPassword)) || (!StringUtil.isEmpty(store.getPayPassword()) && !store.getPayPassword().equals(md5Util.getMD5ofStr(payPassword)))) {
			return "0";
		}
//		Boolean payStatus = orderService.orderBCSPay(orderId, store, type, request);
		Boolean payStatus = false;
		if (payStatus) {
			return "1";
		} else {
			return "2";
		}
	}

	@RequestMapping(value = "/store/payorder/paysure/{orderId}")
	@ResponseBody
	public String paySure(@PathVariable String orderId, HttpServletRequest request, HttpServletResponse response) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		Order order = orderService.getOrderByOrderId(orderId);
		order.setNickName(store.getStoreName());
		Boolean payStatus = orderService.changePayOrderStatus(order, OrderConst.ORDER_STATUS_FINISH);
		if (payStatus) {
			return "1";
		} else {
			return "2";
		}
	}
}
