package com.ekfans.controllers.system.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.service.IContractContentService;
import com.ekfans.base.wfOrder.service.IContractDetailsService;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.controllers.system.order.orderVo.MyOrderVo;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemOrderController {

	private Logger log = LoggerFactory.getLogger(SystemOrderController.class);
	@Autowired
	private IOrderService orderService;

	@Autowired
	private IContractContentService ccService;
	@Autowired
	private ISystemAreaService areaService;
	@Autowired
	private IContractContentService contentService;
	@Autowired
	private IContractDetailsService contractDetailService;
	@Autowired
	private IContractService contractService;
	@Autowired
	private IStoreService storeService;

	/**
	 * 后台-查询订单列表
	 * 
	 * @param type
	 *            订单类型（0：预订单，1：订单）
	 */
	@RequestMapping(value = "/system/order/list/{type}")
	public String list(@PathVariable int type, HttpServletRequest request) {
		String orderNum = request.getParameter("orderNum"); // 订单号
		String beginDate = request.getParameter("beginDate"); // 订单开始日期
		String endDate = request.getParameter("endDate"); // 订单结束日期
		String beginPrice = request.getParameter("beginPrice"); // 订单小金额
		String endPrice = request.getParameter("endPrice"); // 订单大金额
		String shippingStatus = request.getParameter("shippingStatus"); // 订单状态
		String pageNum = request.getParameter("pageNum"); // 页码

		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPage = Integer.parseInt(pageNum);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		pager.setCurrentPage(currentPage);

		List<Order> orders = orderService.listOrder(pager, orderNum, beginDate, endDate, beginPrice, endPrice, shippingStatus, type);

		// 绑定页面显示需要的数据
		request.setAttribute("orders", orders);
		request.setAttribute("pager", pager);
		request.setAttribute("orderNum", orderNum);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("beginPrice", beginPrice);
		request.setAttribute("endPrice", endPrice);
		request.setAttribute("shippingStatus", shippingStatus);

		return "/system/order/orderList";
	}

	/**
	 * 
	 * @Title: detail
	 * @Description: TODO(后台-查看订单详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param id
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/order/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		try {
			Object[] objects = orderService.getOrderDetailById(id);
			if (objects != null) {
				Order order = (Order) objects[0];
				OrderAddress orderAddress = (OrderAddress) objects[1];
				String storeName = (String) objects[2];
				String userName = (String) objects[3];
				OrderDetail orderDetail = (OrderDetail) objects[4];
				request.setAttribute("order", order);
				request.setAttribute("orderAddress", orderAddress);
				request.setAttribute("storeName", storeName);
				request.setAttribute("userName", userName);
				request.setAttribute("orderDetail", orderDetail);
				return "system/order/orderDetail";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	/**
	 * 后台-查看线下订单详情
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/order/orderXXDetail/{id}")
	public String orderXXDetail(@PathVariable String id, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		Order order = orderService.getOrderByOrderId(id);
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
		return "system/order/orderXXDetail";
	}

	// 检查ID是否存在
	@RequestMapping(value = "/system/order/checkId/{id}")
	@ResponseBody
	public Object checkId(@PathVariable String id) {
		try {
			if (!orderService.checkId(id)) {
				// 如果id不存在，返回true
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询出商店id
	 */
	@RequestMapping(value = "/system/order/getStoreId/{storeName}")
	@ResponseBody
	public Object getStoreId(@PathVariable String storeName) {
		try {
			String storeId = orderService.getStoreIdByName(storeName);
			return storeId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询出用户id
	 */
	@RequestMapping(value = "/system/order/getUserId/{userName}")
	@ResponseBody
	public Object getUserId(@PathVariable String userName) {
		try {
			String userId = orderService.getUserIdByName(userName);
			return userId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: servicePlaceOrder
	 * @Description: TODO(跳转到客服下单页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param myOrderVo
	 * @param @param request
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/order/servicePlaceOrder")
	public String servicePlaceOrder() {
		return "/system/order/orderAdd";
	}

	/**
	 * 
	 * @Title: serviceOrderAdd
	 * @Description: TODO(执行客服下单操作) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderVo
	 * @param @param request
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/order/serviceOrderAdd")
	@ResponseBody
	public Object serviceOrderAdd(MyOrderVo orderVo, HttpServletRequest request) {
		return false;
	}
}
