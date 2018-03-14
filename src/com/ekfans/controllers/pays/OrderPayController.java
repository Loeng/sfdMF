package com.ekfans.controllers.pays;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.service.IBankService;
import com.ekfans.base.wfOrder.model.OrderPayLog;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.base.wfOrder.util.WfOrderHelper;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.webService.bcs.BCSClientService;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * Created by liuguoyu on 2016/11/15.
 */
@Controller
public class OrderPayController extends BasicController {
	@Autowired
	IBankService bankService;
	@Autowired
	IStoreService storeService;

	/**
	 * 跳转到登录界面
	 * 
	 * @param orderId
	 * @param orderType
	 * @param payType
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/sfdorder/payload/{orderId}/{orderType}/{payType}")
	public String orderGoPay(@PathVariable String orderId, @PathVariable String orderType, @PathVariable String payType, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		List<Bank> bankList = bankService.list(null, null, null, "1", null, "1", null, null, null);

		List<Bank> onlineBanks = bankService.list(null, null, null, "1", null, null, "1", null, null);
		Store ontherStore;
		if (OrderPayLog.ORDER_TYPE_WF.equals(orderType)) {
			IWfOrderService orderService = SpringContextHolder.getBean(IWfOrderService.class);
			WfOrder order = orderService.getWfOrderById(orderId);
			if (order == null) {
				request.setAttribute("errorMsg", "noOrder");
				return "/sfdPay/orderPayLoad";
			}
			BigDecimal payPrice = new BigDecimal(0.00);
			switch (payType){
				case OrderPayLog.PAY_TYPE_YF:
					if (!WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF.equals(order.getStatus())) {
						request.setAttribute("errorMsg", "notPay");
						return "/sfdPay/orderPayLoad";
					}
					payPrice = order.getYfPrice();
					break;
				case OrderPayLog.PAY_TYPE_JS:
					if (!WfOrderHelper.WFORDER_STATUS_WAIT_PAY.equals(order.getStatus())) {
						request.setAttribute("errorMsg", "notPay");
						return "/sfdPay/orderPayLoad";
					}

					payPrice = order.getTotalPrice().subtract(order.getPayPrice());
					if (payPrice.doubleValue() <= 0) {
						payPrice = new BigDecimal(0.00).subtract(payPrice);
					}
					break;
				case OrderPayLog.PAY_TYPE_JSTK:
					if (!WfOrderHelper.WFORDER_STATUS_WAIT_PAY.equals(order.getStatus())) {
						request.setAttribute("errorMsg", "notPay");
						return "/sfdPay/orderPayLoad";
					}

					payPrice = order.getPayPrice().subtract(order.getTotalPrice());
					if (payPrice.doubleValue() <= 0) {
						payPrice = new BigDecimal(0.00).subtract(payPrice);
					}
					break;
			}

			if (store.getId().equals(order.getBuyId())) {
				ontherStore = storeService.getStoreById(order.getSaleId());
			} else {
				ontherStore = storeService.getStoreById(order.getBuyId());
			}

			request.setAttribute("payAmt", payPrice);
		} else {
			IOrderService orderService = SpringContextHolder.getBean(IOrderService.class);
			Order order = orderService.getOrderByOrderId(orderId);
			if (order == null) {
				request.setAttribute("errorMsg", "noOrder");
				return "/sfdPay/orderPayLoad";
			}
			if (!OrderConst.ORDER_STATUS_WAIT_PAY.equals(order.getStatus())) {
				request.setAttribute("errorMsg", "notPay");
				return "/sfdPay/orderPayLoad";
			}
			request.setAttribute("payAmt", order.getPaid());

			if (OrderConst.ORDER_STATUS_WAIT_PAY.equals(order.getStatus()) && OrderConst.ORDER_PAY_TYPE_YUE.equals(order.getPayType())) {
				Store salStore = storeService.getStore(order.getStoreId());
				request.setAttribute("salStore", salStore);
			}

			if (store.getId().equals(order.getUserId())) {
				ontherStore = storeService.getStoreById(order.getStoreId());
			} else {
				ontherStore = storeService.getStoreById(order.getUserId());
			}
		}
		request.setAttribute("bankList", bankList);
		request.setAttribute("payOrder", orderId);
		request.setAttribute("orderType", orderType);
		request.setAttribute("payType", payType);
		request.setAttribute("otherStore", ontherStore);
		request.setAttribute("onlineBanks", onlineBanks);
		return "/sfdPay/orderPayLoad";
	}

	/**
	 * 根据条件动态调用银行列表数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sfdorder/payment/bankload")
	@ResponseBody
	public void payLoadBanks(HttpServletRequest request, HttpServletResponse response) {
		String accountPay = request.getParameter("accountPay");
		String onlinePay = request.getParameter("onlinePay");
		String companyPay = request.getParameter("companyPay");
		String commonPay = request.getParameter("commonPay");

		List<Bank> bankList = bankService.list(null, null, null, "1", null, accountPay, onlinePay, companyPay, commonPay);
		List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
		if (bankList != null && bankList.size() > 0) {
			for (Bank bank : bankList) {
				if (bank == null) {
					continue;
				}
				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("bankId", bank.getId());
				jsonMap.put("bankName", bank.getBankName());
				jsonMap.put("bankLogo", bank.getLogo());
				jsonMap.put("companyPay", bank.getCommonPay());
				jsonMap.put("commonPay", bank.getCommonPay());
				jsonList.add(jsonMap);
			}
		}
		backWriteStr(response, JsonUtil.listToJson(jsonList));

	}

	@RequestMapping(value = "/sfdorder/loadbanksum/{bankPerch}")
	@ResponseBody
	public String getBankSumByBankPerch(@PathVariable String bankPerch, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		BigDecimal price = new BigDecimal(0.00);
		switch (bankPerch) {
		case "bcs":
			// 非测试时请放开
			BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
			if (prices != null && prices.length > 0) {
				price = prices[0];
			}
			break;
		case "bjBank":
			break;
		default:
			break;
		}
		return price.toString();
		// return "5123.23";
	}

	@RequestMapping(value = "/sfdorder/paycommit")
	@ResponseBody
	public String accountPaySub(HttpServletRequest request, HttpServletResponse response) {
		IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);

		String orderId = request.getParameter("orderId");
		String payType = request.getParameter("payType");
		String orderType = request.getParameter("orderType");
		String orderPayType = request.getParameter("orderPayType");
		// String perch = request.getParameter("perch");
		Boolean payStatus = false;

		// 余额支付
		switch (payType){
			case OrderPayLog.PAYTYPE_YE:
				String payPattern = request.getParameter("payPattern");
				String bankId = request.getParameter("bankId");
				String payPassword = request.getParameter("payPassword");
				MD5Util md5Util = new MD5Util();
				if ((!StringUtil.isEmpty(store.getPayPassword()) && StringUtil.isEmpty(payPassword))
						|| (!StringUtil.isEmpty(store.getPayPassword()) && !store.getPayPassword().equals(md5Util.getMD5ofStr(payPassword)))) {
					return "0";
				}
				if (OrderPayLog.ORDER_TYPE_WF.equals(orderType)) {
					IWfOrderService orderService = SpringContextHolder.getBean(IWfOrderService.class);
					payStatus = orderService.wfOrderBCSPay(orderId, store, orderType, orderPayType, payPattern, bankId, request);
				} else {
					IOrderService orderService = SpringContextHolder.getBean(IOrderService.class);
					payStatus = orderService.orderBCSPay(orderId, store, orderType, orderPayType, payPattern, bankId, request);
				}
				break;
			case OrderPayLog.PAYTYPE_UL:
				// 线下支付,上传凭证
				if (OrderPayLog.ORDER_TYPE_WF.equals(orderType)) {
					IWfOrderService orderService = SpringContextHolder.getBean(IWfOrderService.class);
					WfOrder order = orderService.getWfOrderById(orderId);
					// 设置图标保存路径
					String currentPath = "/customerfiles/store/" + store.getId() + "/order/" + DateUtil.getNoSpSysDateString();
					// 调用方法获取分类图标，返回图标路径
					String certPath = FileUploadHelper.uploadFile("payCert", currentPath, request, response);
					payStatus = orderService.wfOrderULPay(orderId, certPath, store, orderType, orderPayType, null, null, request);
				} else {
					IOrderService orderService = SpringContextHolder.getBean(IOrderService.class);
					Order order = orderService.getOrderByOrderId(orderId);
					order.setNickName(store.getStoreName());
					// 设置图标保存路径
					String currentPath = "/customerfiles/store/" + store.getId() + "/order/" + DateUtil.getNoSpSysDateString();
					// 调用方法获取分类图标，返回图标路径
					String certPath = FileUploadHelper.uploadFile("payCert", currentPath, request, response);
					order.setPayCert(certPath);
					order.setPayType(OrderConst.ORDER_PAY_TYPE_UNLINE);
					payStatus = orderService.orderULPay(order, store, orderType, orderPayType, null, null, request);
				}
				break;
			case OrderPayLog.PAYTYPE_OL:
				break;
		}

		if (payStatus) {
			return "1";
		} else {
			return "2";
		}
	}

	/**
	 * 跳转到登录界面
	 *
	 * @param orderId
	 * @param orderType
	 * @param payType
	 * @param price
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sfdorder/paywait/{orderId}/{orderType}/{payType}/{price}")
	public String orderPayWait(@PathVariable String orderId, @PathVariable String orderType, @PathVariable String payType,@PathVariable String price, HttpServletRequest request) {
		request.setAttribute("orderId",orderId);
		request.setAttribute("price",price);
		request.setAttribute("payType",payType);
		request.setAttribute("orderType",orderType);
		return "/sfdPay/orderPayWait";
	}

}
