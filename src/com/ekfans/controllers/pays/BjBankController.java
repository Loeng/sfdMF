package com.ekfans.controllers.pays;

import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.basic.controller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuguoyu on 2016/11/14.
 */
@Controller
public class BjBankController extends BasicController {
	@Autowired
	IStoreService storeService;
	@Autowired
	IOrderService orderService;
	@Autowired
	IWfOrderService wfOrderService;

	@RequestMapping(value = "/sfdpay/bjbank/companypay/{orderId}/{orderType}/{bankId}/{payType}")
	public String goBjBankCompanyPay(@PathVariable String orderId, @PathVariable String orderType, @PathVariable String bankId, @PathVariable String payType,
			HttpServletRequest request) {
//		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
//		BigDecimal payPrice = new BigDecimal(0.00);
//		String orderNum = "";
//		if (OrderPayLog.ORDER_TYPE_WF.equals(orderType)) {
//
//			WfOrder order = wfOrderService.getWfOrderById(orderId);
//			if (order != null) {
//				if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
//					payPrice = order.getYfPrice();
//				} else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
//					payPrice = order.getTotalPrice().subtract(order.getPayPrice());
//					if (payPrice.doubleValue() <= 0) {
//						payPrice = new BigDecimal(0.00).subtract(payPrice);
//					}
//				} else if (OrderPayLog.PAY_TYPE_JSTK.equals(payType)) {
//					payPrice = order.getPayPrice().subtract(order.getTotalPrice());
//					if (payPrice.doubleValue() <= 0) {
//						payPrice = new BigDecimal(0.00).subtract(payPrice);
//					}
//				}
//				orderNum = order.getBankOrderId();
//			}
//		} else {
//			IOrderService orderService = SpringContextHolder.getBean(IOrderService.class);
//			Order order = orderService.getOrderByOrderId(orderId);
//			payPrice = order != null ? order.getTotalPrice() : new BigDecimal("0.00");
//			orderNum = order.getBankOrderId();
//		}
//
//		BasicRequest br = new BasicRequest(request);
//
//		// 2.获取订单金额(整型字符串，后两位表示小数)
//		String orderAmt = (payPrice.multiply(new BigDecimal(100))).intValue() + "";
//
//		// 3.商户接收支付信息URL 商户接收jsp
//		String notifyUrl = br.getWebFullUrlPrex() + "/sfdpay/bjbank/company/notify/" + orderId + "/" + orderType + "/" + bankId + "/" + payType + "/" + store.getId();
//		String responseUrl = br.getWebFullUrlPrex() + "/sfdpay/bjbank/company/response/" + orderId + "/" + orderType + "/" + bankId + "/" + payType + "/" + store.getId();
//
//		// 4.商户备注信息 l
//		String merRemarks = "";
//
//		// // 5.商户ID
//		// String merchantID = Cache.getResource("bjbank.merchant.id");
//		//
//		// // 6.商户证书号
//		// String certNumber = Cache.getResource("bjbank.cert.number");
//		//
//		// // 7.商户接收通知消息方式 通知类型 0 http;)
//		// String notifyType = "0";
//
//		// 8.是否在线取货 !--是否在线取货 0 是;1 否-->
//		String goodsType = "1";
//
//		// 9.交易类型 交易类型 0 支付;1 查询对帐单
//		String actionType = "0";
//		// 10.对帐日期
//		String merCheckDate = "";
//
//		/* 生产商户信息对象 */
//		MerPaymentInfo merPayInfo = new MerPaymentInfo();
//		// 1.设置订单号
//		merPayInfo.setOrderNum(orderNum);
//		// 2.设置订单金额
//		merPayInfo.setOrderAmt(orderAmt);
//		// 3.设置通知URL
//		merPayInfo.setNotifyUrl(notifyUrl);
//		// 4.设置商户备注
//		merPayInfo.setMerRemarks(merRemarks);
//		// 5.设置商户接收通知方式
//		merPayInfo.setActionType(actionType);
//		merPayInfo.setResponseUrl(responseUrl);
//		// add by lixke 设置商品类型
//		merPayInfo.setGoodsType(goodsType);
//		// 设置对账日期
//		merPayInfo.setMerCheckDate(merCheckDate);
//		/* 生成支付请求对象 */
//		MerPaymentRequest merReq = new MerPaymentRequest(merPayInfo);
//		// 银行请求URL
//		String bankUrl = "";
//		// 银行应用标识
//		String netType = "";
//		// 请求数据包
//		String merReqData = "";
//		System.out.println(merPayInfo.getXMLStr());
//		if (!merReq.isReadyNow()) {
//			// 失败后的商户特殊处理
//			System.out.println(merReq.getErrMsg());
//		} else {
//			bankUrl = (String) merReq.getBankURLB2B();
//			netType = (String) merReq.getNetType();
//			merReqData = (String) merReq.getReqData();
//			System.out.println("merReqData = " + merReqData);
//			System.out.println("bankUrl = " + bankUrl);
//		}
//		request.setAttribute("bankUrl", bankUrl);
//		request.setAttribute("netType", netType);
//		request.setAttribute("merReqData", merReqData);
		return "/sfdPay/bjBankPay/commonPay";
	}

	@RequestMapping(value = "/sfdpay/bjbank/company/notify/{orderId}/{orderType}/{bankId}/{payType}/{storeId}")
	@ResponseBody
	public String bjBankCompanyNotify(@PathVariable String orderId, @PathVariable String orderType, @PathVariable String bankId, @PathVariable String payType,
			@PathVariable String storeId, HttpServletRequest request) {
//		String bankSignData = request.getParameter("bankSignData");
//		System.out.println("bankSignData of merResponse.jsp = " + bankSignData);
//
//		MerPaymentResponse merRep = new MerPaymentResponse(bankSignData);
//		String orderNum = "";
//		// String orderAmt = "";
//		// String orderStatus = "";
//		// String retCode = "";
//		// String accDate = "";
//		if (merRep.isReadyNow()) {
//			orderNum = (String) merRep.getOrderNum();
//			// orderAmt = (String) merRep.getOrderAmt();
//			// orderStatus = (String) merRep.getOrderStatus();
//			// retCode = (String) merRep.getRetCode();
//			// accDate = (String) merRep.getaccDate();
//			if (orderId.equals(orderNum)) {
//				if (OrderPayLog.ORDER_TYPE_WF.equals(orderType)) {
//					IWfOrderService orderService = SpringContextHolder.getBean(IWfOrderService.class);
//					orderService.wfOrderOLPay(orderId, storeId, orderType, payType, OrderPayLog.PAYTYPE_OL, bankId, request);
//				} else {
//					IOrderService orderService = SpringContextHolder.getBean(IOrderService.class);
//					orderService.orderOLPay(orderId, storeId, orderType, payType, OrderPayLog.PAYTYPE_OL, bankId, request);
//				}
//			}
//		}

		return "/sfdPay/bjBankPay/commonPay";
	}

	@RequestMapping(value = "/sfdpay/bjbank/company/response/{orderId}/{orderType}/{bankId}/{payType}/{storeId}")
	public String bjBankCompanyResponse(@PathVariable String orderId, @PathVariable String orderType, @PathVariable String bankId, @PathVariable String payType,
			@PathVariable String storeId, HttpServletRequest request) {
//		String bankSignData = request.getParameter("bankSignData");
//		System.out.println("bankSignData of merResponse.jsp = " + bankSignData);
//		MerPaymentResponse merRep = new MerPaymentResponse(bankSignData);
//		String orderNum = "";
//		String orderAmt = "";
//		// String orderStatus = "";
//		// String retCode = "";
//		// String accDate = "";
//		if (merRep.isReadyNow()) {
//			orderNum = (String) merRep.getOrderNum();
//			orderAmt = (String) merRep.getOrderAmt();
//			// orderStatus = (String) merRep.getOrderStatus();
//			// retCode = (String) merRep.getRetCode();
//			// accDate = (String) merRep.getaccDate();
//			if (OrderPayLog.ORDER_TYPE_WF.equals(orderType)) {
//					IWfOrderService orderService = SpringContextHolder.getBean(IWfOrderService.class);
//					orderService.wfOrderOLPay(orderId, storeId, orderType, payType, OrderPayLog.PAYTYPE_OL, bankId, request);
//			} else {
//				IOrderService orderService = SpringContextHolder.getBean(IOrderService.class);
//				orderService.orderOLPay(orderId, storeId, orderType, payType, OrderPayLog.PAYTYPE_OL, bankId, request);
//			}
//		}
//		BigDecimal price = new BigDecimal(0.00);
//		if(!StringUtil.isEmpty(orderAmt)){
//			price = new BigDecimal(orderAmt).divide(new BigDecimal(100));
//		}
//		IBankService bankService = SpringContextHolder.getBean(IBankService.class);
//		Bank bank = bankService.getBankById(bankId);
//		String bankImg = null;
//		if(bank != null){
//			bankImg = bank.getLogo();
//		}
//		request.setAttribute("orderId",orderId);
//		request.setAttribute("price",price);
//		request.setAttribute("orderType",orderType);
//		request.setAttribute("storeId",storeId);
//		request.setAttribute("bankImg",bankImg);
		return "/sfdPay/paySuccess";
	}

	@RequestMapping(value = "/sfdpay/bjbank/commonpay/{orderId}/{orderType}/{bankId}/{payType}")
	public String goBjBankCommonPay(@PathVariable String orderId, @PathVariable String orderType, @PathVariable String bankId, @PathVariable String payType,
			HttpServletRequest request) {
//		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
//		BigDecimal payPrice = new BigDecimal(0.00);
//		String orderNum = "";
//		if (OrderPayLog.ORDER_TYPE_WF.equals(orderType)) {
//
//			WfOrder order = wfOrderService.getWfOrderById(orderId);
//			if (order != null) {
//				if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
//					payPrice = order.getYfPrice();
//				} else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
//					payPrice = order.getTotalPrice().subtract(order.getPayPrice());
//					if (payPrice.doubleValue() <= 0) {
//						payPrice = new BigDecimal(0.00).subtract(payPrice);
//					}
//				} else if (OrderPayLog.PAY_TYPE_JSTK.equals(payType)) {
//					payPrice = order.getPayPrice().subtract(order.getTotalPrice());
//					if (payPrice.doubleValue() <= 0) {
//						payPrice = new BigDecimal(0.00).subtract(payPrice);
//					}
//				}
//				orderNum = order.getBankOrderId();
//			}
//		} else {
//			IOrderService orderService = SpringContextHolder.getBean(IOrderService.class);
//			Order order = orderService.getOrderByOrderId(orderId);
//			payPrice = order != null ? order.getPaid() : new BigDecimal("0.00");
//			orderNum = order.getBankOrderId();
//		}
//
//		BasicRequest br = new BasicRequest(request);
//
//		// 2.获取订单金额(整型字符串，后两位表示小数)
//		String orderAmt = (payPrice.multiply(new BigDecimal(100))).intValue() + "";
//
//		// 3.商户接收支付信息URL 商户接收jsp
//		String notifyUrl = br.getWebFullUrlPrex() + "/sfdpay/bjbank/company/notify/" + orderId + "/" + orderType + "/" + bankId + "/" + payType + "/" + store.getId();
//		String responseUrl = br.getWebFullUrlPrex() + "/sfdpay/bjbank/company/response/" + orderId + "/" + orderType + "/" + bankId + "/" + payType + "/" + store.getId();
//
//		// 4.商户备注信息 l
//		String merRemarks = "";
//
//		// 8.是否在线取货 !--是否在线取货 0 是;1 否-->
//		String goodsType = "1";
//
//		// 9.交易类型 交易类型 0 支付;1 查询对帐单
//		String actionType = "0";
//		// 10.对帐日期
//		String merCheckDate = "";
//
//		/* 生产商户信息对象 */
//		com.ekfans.plugin.webService.bjBank.B2C.client.MerPaymentInfo merPayInfo = new com.ekfans.plugin.webService.bjBank.B2C.client.MerPaymentInfo();
//		// 1.设置订单号
//		merPayInfo.setOrderNum(orderNum);
//		// 2.设置订单金额
//		merPayInfo.setOrderAmt(orderAmt);
//		// 3.设置通知URL
//		merPayInfo.setNotifyUrl(notifyUrl);
//		// 4.设置商户备注
//		merPayInfo.setMerRemarks(merRemarks);
//		// 5.设置商户接收通知方式
//		merPayInfo.setActionType(actionType);
//		merPayInfo.setResponseUrl(responseUrl);
//		// add by lixke 设置商品类型
//		merPayInfo.setGoodsType(goodsType);
//		// 设置对账日期
//		merPayInfo.setMerCheckDate(merCheckDate);
//		/* 生成支付请求对象 */
//		com.ekfans.plugin.webService.bjBank.B2C.client.MerPaymentRequest merReq = new com.ekfans.plugin.webService.bjBank.B2C.client.MerPaymentRequest(merPayInfo);
//		// 银行请求URL
//		String bankUrl = "";
//		// 银行应用标识
//		String netType = "";
//		// 请求数据包
//		String merReqData = "";
//		System.out.println(merPayInfo.getXMLStr());
//		if (!merReq.isReadyNow()) {
//			// 失败后的商户特殊处理
//			System.out.println(merReq.getErrMsg());
//		} else {
//			bankUrl = (String) merReq.getBankURL();
//			netType = (String) merReq.getNetType();
//			merReqData = (String) merReq.getReqData();
//			System.out.println("merReqData = " + merReqData);
//			System.out.println("bankUrl = " + bankUrl);
//		}
//		request.setAttribute("bankUrl", bankUrl);
//		request.setAttribute("netType", netType);
//		request.setAttribute("merReqData", merReqData);
		return "/sfdPay/bjBankPay/payReq";
	}
}
