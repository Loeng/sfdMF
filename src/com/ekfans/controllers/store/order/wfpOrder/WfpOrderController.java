package com.ekfans.controllers.store.order.wfpOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.CarUser;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccountLogService;
import com.ekfans.base.store.service.ICarInfoService;
import com.ekfans.base.store.service.ICarUserService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.BankBinding;
import com.ekfans.base.user.service.IBankBindingService;
import com.ekfans.base.wfOrder.dao.IWfOrderTransDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ContractContent;
import com.ekfans.base.wfOrder.model.OrderPayLog;
import com.ekfans.base.wfOrder.model.ScrapWfp;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderDetail;
import com.ekfans.base.wfOrder.model.WfOrderLog;
import com.ekfans.base.wfOrder.model.WfOrderPrice;
import com.ekfans.base.wfOrder.service.IContractContentService;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.base.wfOrder.service.IOrderPayLogService;
import com.ekfans.base.wfOrder.service.IScrapWfpService;
import com.ekfans.base.wfOrder.service.IWfOrderDetailService;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.base.wfOrder.util.WfOrderHelper;
import com.ekfans.base.wfOrder.util.WfOrderTransStore;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 危废品订单管理
 * 
 * @author Jin
 * @date 2015-1-15 下午5:27:07
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */

@Controller
@Scope("prototype")
public class WfpOrderController extends BasicController {

	@Autowired
	private IContractService contractService;
	@Autowired
	private IContractContentService contractContentService;
	@Autowired
	private IWfOrderService wfOrderService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private ISystemAreaService areaService;
	@Autowired
	private IScrapWfpService wfpService;
	@Autowired
	private IBankBindingService bankBindingService;
	@Autowired
	private IAccountLogService accountLogService;
	@Autowired
	private IWfOrderTransDao dao;

	/**
	 * 跳转到危废订单新增页面
	 * 
	 * @param scrapId
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/wfOrderAdd/{scrapId}/{viewType}")
	public String wfOrderAdd(@PathVariable String scrapId, @PathVariable String viewType, HttpServletRequest request, HttpSession session) {
		ScrapWfp wfp = wfpService.getScrapWfpById(scrapId);
		try {
			if (wfp != null) {
				Contract contract = contractService.getContractById(wfp.getContractId());
				List<ContractContent> contents = contractContentService.findContractContent(wfp.getContractId());
				if ("1".equals(contract.getPartyAORpartyB())) {
					List<Store> transStores = wfpService.getTransStoreByWfpId(wfp.getId());
					request.setAttribute("transStores", transStores);
				}
				double listQuantity = wfOrderService.getSurplusQuantityByScrapWfpId(wfp.getId(), null);

				boolean needMileage = contractService.checkContractNeedMileage(contract.getId());
				request.setAttribute("needMileage", needMileage);
				request.setAttribute("listQuantity", listQuantity);
				request.setAttribute("scrapWfp", wfp);
				request.setAttribute("contract", contract);
				request.setAttribute("contents", contents);
			}
			Store sotre = (Store) session.getAttribute(SystemConst.STORE);
			request.setAttribute("storeId", sotre.getId());
			request.setAttribute("viewType", viewType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "userCenter/store/wfpOrder/wfOrderAdd";
	}

	/**
	 * 跳转到危废订单编辑页面
	 * 
	 * @param
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/wfOrderEdit/{wfOrderId}/{viewType}")
	public String wfOrderEdit(@PathVariable String wfOrderId, @PathVariable String viewType, HttpServletRequest request, HttpSession session) {
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		try {
			if (wfOrder != null) {
				Contract contract = contractService.getContractById(wfOrder.getContractId());
				if ("1".equals(contract.getPartyAORpartyB())) {
					Map<String, Boolean> choseStores = wfOrderService.getWfOrderTrans(wfOrderId);
					List<Store> transStores = wfpService.getTransStoreByWfpId(wfOrder.getScrapId());
					request.setAttribute("transStores", transStores);
					request.setAttribute("storeMap", choseStores);
				}
				double listQuantity = wfOrderService.getSurplusQuantityByScrapWfpId(wfOrder.getScrapId(), wfOrderId);
				boolean needMileage = contractService.checkContractNeedMileage(contract.getId());
				request.setAttribute("needMileage", needMileage);
				request.setAttribute("listQuantity", listQuantity);
				request.setAttribute("contract", contract);
			}
			Store sotre = (Store) session.getAttribute(SystemConst.STORE);

			request.setAttribute("wfOrder", wfOrder);
			request.setAttribute("storeId", sotre.getId());
			request.setAttribute("viewType", viewType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "userCenter/store/wfpOrder/wfOrderEdit";
	}

	/**
	 * 跳转到危废订单编辑页面
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/wfOrderView/{wfOrderId}/{viewType}/{pageType}")
	public String wfOrderView(@PathVariable String wfOrderId, @PathVariable String viewType, @PathVariable String pageType, HttpServletRequest request, HttpSession session) {
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);

		// 开始测试 同步数据到监控平台,之后删除或者注释
		// WfOrderTrans orderTrans;
		// try {
		// orderTrans = (WfOrderTrans)
		// dao.get("402880fc4f63f176014f6407ec0f0015");
		// MonitorSyncService syncService = new MonitorSyncService(null, null,
		// "E:/workspace/.metadata/.me_tcat7/webapps/sfd", null, null, null,
		// null, null, orderTrans, "401",
		// null, "synchroOrderTransdata");
		// Thread thread = new Thread(syncService);
		// thread.start();
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// 结束

		Store sotre = (Store) session.getAttribute(SystemConst.STORE);
		try {
			sotre = storeService.getStore(sotre.getId());
			request.getSession().setAttribute(SystemConst.STORE, sotre);
			if (wfOrder != null) {
				Contract contract = contractService.getContractById(wfOrder.getContractId());
				request.setAttribute("contract", contract);
				if ("1".equals(pageType) && "0".equals(contract.getPartyAORpartyB())) {
					Map<String, Boolean> choseStores = wfOrderService.getWfOrderTrans(wfOrderId);
					List<Store> transStores = wfpService.getTransStoreByWfpId(wfOrder.getScrapId());
					request.setAttribute("transStores", transStores);
					request.setAttribute("storeMap", choseStores);
				} else {
					List<WfOrderTransStore> storeList = wfOrderService.getWfOrderTransList(wfOrderId);
					request.setAttribute("storeList", storeList);
				}
				double listQuantity = wfOrderService.getSurplusQuantityByScrapWfpId(wfOrder.getScrapId(), wfOrderId);
				request.setAttribute("listQuantity", listQuantity);

				if ("8".equals(pageType) && WfOrderHelper.WFORDER_STATUS_WAIT_PAY.equals(wfOrder.getStatus())) {
					if (wfOrder.getTotalPrice().doubleValue() < wfOrder.getPayPrice().doubleValue()) {
						// TODO: 1.还可以继续下单，需要转移至下批货款
						// TODO: 2.还可以继续下单，申请退款
						// TODO: 3.不可以继续下单，必须退款
						if (listQuantity > 0
								&& ((sotre.getId().equals(wfOrder.getBuyId()) && !wfOrder.isYfType()) || (sotre.getId().equals(wfOrder.getSaleId()) && wfOrder.isYfType()))) {
							// 还可以继续下单
							request.setAttribute("canContinue", true);
						} else if (listQuantity <= 0 && sotre.getId().equals(wfOrder.getSaleId())) {
							// 不可以继续下单
							request.setAttribute("canContinue", false);
						}
					}

				}

				/**
				 * 查询支付时收款账号
				 * 
				 * if (!StringUtil.isEmpty(wfOrder.getReturnPayId())) {
				 * BankBinding returnBinding =
				 * bankBindingService.getBankBindingById
				 * (wfOrder.getReturnPayId());
				 * request.setAttribute("returnBinding", returnBinding); }
				 */
				/**
				 * 查询支付时收款账号
				 * 
				 * if (!"2".equals(viewType) && (("1".equals(pageType) &&
				 * wfOrder.isYfType()) || !"1".equals(pageType))) { BankBinding
				 * bankBinding =
				 * bankBindingService.getBankBindingById(wfOrder.getPayBankId
				 * ()); request.setAttribute("bankBinding", bankBinding); }
				 */
			}
			List<WfOrderLog> orderLogs = wfOrderService.getOrderLog(wfOrderId);
			request.setAttribute("wfOrder", wfOrder);
			request.setAttribute("storeId", sotre.getId());
			request.setAttribute("viewType", viewType);
			request.setAttribute("pageType", pageType);
			request.setAttribute("orderLogs", orderLogs);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "userCenter/store/wfpOrder/wfOrderView";
	}

	/**
	 * 跳转到危废订单新增页面
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/contractOrder/orderList/{viewType}")
	public String wfOrderList(@PathVariable String viewType, HttpServletRequest request, HttpSession session) {
		String scrapId = request.getParameter("scrapId");
		String wfName = request.getParameter("wfName");
		String orderStatus = request.getParameter("orderStatus");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String orderId = request.getParameter("orderId");
		try {

			String pageNum = request.getParameter("pageNum");

			int currentPageNo = 0;
			try {
				currentPageNo = Integer.parseInt(pageNum);
			} catch (Exception e) {
				// TODO: handle exception
			}
			Pager pager = new Pager(currentPageNo);
			pager.setRowsPerPage(10);

			Store store = (Store) session.getAttribute(SystemConst.STORE);

			List<WfOrder> orderList = wfOrderService.getWfOrderList(scrapId, null, wfName, orderStatus, null, startTime, endTime, orderId, null, null, pager, viewType,
					store.getId());
			request.setAttribute("orderList", orderList);
			request.setAttribute("scrapId", scrapId);
			request.setAttribute("wfName", wfName);
			request.setAttribute("orderStatus", orderStatus);
			request.setAttribute("startTime", startTime);
			request.setAttribute("endTime", endTime);
			request.setAttribute("orderId", orderId);
			request.setAttribute("storeId", store.getId());
			request.setAttribute("viewType", viewType);
			request.setAttribute("pager", pager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "userCenter/store/wfpOrder/wfOrderList";
	}

	/**
	 * 保存新增的危废订单
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/wfOrderSave")
	@ResponseBody
	public Boolean wfOrderSave(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return wfOrderService.saveOrUpdateWfOrder(wfOrder, request, response);
	}

	@RequestMapping(value = "/store/order/loadBanks")
	public String loadBanks(HttpServletRequest request, HttpServletResponse response) {
		String payBankId = request.getParameter("payBankId");
		request.setAttribute("payBankId", payBankId);
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store != null) {
			List<BankBinding> banks = bankBindingService.getBankBindingByStoreId(store.getId(), "true");
			request.setAttribute("banks", banks);
		}

		return "userCenter/store/wfpOrder/bankLoad";
	}

	@RequestMapping(value = "/store/order/getOrderButton/{orderId}/{storeId}")
	@ResponseBody
	public int checkOrderButton(@PathVariable String orderId, @PathVariable String storeId, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(storeId)) {
			return 0;
		}
		WfOrder order = wfOrderService.getWfOrderById(orderId);
		if (order == null) {
			return 0;
		}
		if (order.getTotalPrice().doubleValue() > order.getPayPrice().doubleValue() && !order.getBuyId().equals(storeId)) {
			return 0;
		}

		if (order.getTotalPrice().doubleValue() < order.getPayPrice().doubleValue()) {
			double listQuantity = wfOrderService.getSurplusQuantityByScrapWfpId(order.getScrapId(), null);
			// TODO: 1.还可以继续下单，需要转移至下批货款
			// TODO: 2.还可以继续下单，申请退款
			// TODO: 3.不可以继续下单，必须退款

			if (listQuantity > 0 && storeId.equals(order.getBuyId())) {
				// 还可以继续下单
				return 1;
			} else if (listQuantity <= 0 && storeId.equals(order.getSaleId())) {
				// 不可以继续下单
				return 2;
			}
		}

		return 0;
	}

	/**
	 * 保存新增的危废订单
	 * 
	 * @param
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/order/getOrderTotalPrice")
	@ResponseBody
	public Double getOrderTotalPrice(HttpServletRequest request, HttpSession session) {
		String contractId = request.getParameter("contractId");
		String number = request.getParameter("number");
		String contents = request.getParameter("contents");

		if (!StringUtil.isEmpty(contents)) {
			String[] cons = contents.split(";");
			if (cons != null && cons.length > 0) {
				Map<String, Double> consMap = new HashMap<String, Double>();
				for (int i = 0; i < cons.length; i++) {
					String con = cons[i];
					String[] cs = con.split("-");
					if (cs == null || cs.length <= 0) {
						continue;
					}
					try {
						consMap.put(cs[0], Double.parseDouble(cs[1]));
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				return contractContentService.sumTotalPrice(contractId, number, consMap);
			}
		}
		return 0.00;
	}

	@RequestMapping(value = "/store/order/wfOrderCancel/{wfOrderId}")
	@ResponseBody
	public String wfOrderCancel(WfOrder wfOrder, @PathVariable String wfOrderId, HttpServletRequest request) {
		wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		if (wfOrderService.wfOrderCancel(wfOrder, request)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/order/wforderSure/{wfOrderId}/{pageType}")
	@ResponseBody
	public String orderSure(@PathVariable String wfOrderId, @PathVariable String pageType, String[] transportStoreId, HttpServletRequest request) {
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);

		if (wfOrderService.orderSure(wfOrder, pageType, transportStoreId, request)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/order/wfOrderTransLoad/{wfOrderId}/{pageType}")
	public String loadWfOrderTrans(@PathVariable String wfOrderId, @PathVariable String pageType, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		wfOrderService.getWfOrderTransLists(wfOrderId, store.getId(), request);
		return "userCenter/store/wfpOrder/wfOrderTransLoad";
	}

	@RequestMapping(value = "/store/order/wfOrderCarInfoLoad")
	public String loadWfOrderCars(HttpServletRequest request, HttpServletResponse response) {
		// String contractId = request.getParameter("contractId");
		// Store store = (Store)
		// request.getSession().getAttribute(SystemConst.STORE);
		ICarInfoService carInfoService = SpringContextHolder.getBean(ICarInfoService.class);
		// List<CarInfo> carInfoList =
		// contractService.getCarUsersByContract(contractId, store.getId());
		List<CarInfo> carInfoList = carInfoService.getCarInfosByStoreId(null, null, "1", request, response);
		request.setAttribute("showCarInfoList", carInfoList);
		return "userCenter/store/wfpOrder/wfOrderCarInfoLoad";
	}

	@RequestMapping(value = "/store/order/wfOrderCarUserLoad/{userType}/{carInfoId}")
	public String getCarUserList(@PathVariable String userType, @PathVariable String carInfoId, HttpServletRequest request, HttpServletResponse response) {
		ICarUserService userService = SpringContextHolder.getBean(ICarUserService.class);
		List<CarUser> list = userService.getCarUsersByStoreId(null, request, response, userType, null, null, "1");
		request.setAttribute("carInfoId", carInfoId);
		request.setAttribute("userList", list);
		if ("0".equals(userType)) {
			return "userCenter/store/wfpOrder/wfOrderDriverLoad";
		} else {
			return "userCenter/store/wfpOrder/wfOrderYYYLoad";
		}
	}

	@RequestMapping(value = "/store/order/wforderTransSure")
	@ResponseBody
	public String orderTransSure(HttpServletRequest request) {
		String wfOrderId = request.getParameter("wfOrderId");
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		if (wfOrderService.updateWfOrderTrans(wfOrderId, request)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/order/payYFPrice/{wfOrderId}/{viewType}/{pageType}")
	public String payYFPrice(@PathVariable String wfOrderId, @PathVariable String viewType, @PathVariable String pageType, HttpServletRequest request) {
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		if (wfOrder == null || "1".equals(wfOrder.getPayStatus()) || !WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF.equals(wfOrder.getStatus())) {
			request.setAttribute("payed", "true");
			return "/web/unionPay/unionPay";
		}

		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		IOrderPayLogService payLogService = SpringContextHolder.getBean(IOrderPayLogService.class);
		OrderPayLog payLog = payLogService.getLogByOrderIdAndPayType(wfOrderId, OrderPayLog.ORDER_TYPE_WF, OrderPayLog.PAY_TYPE_YF);
		if (payLog == null) {
			payLog = new OrderPayLog();
			payLog.setOrderId(wfOrder.getId());
			payLog.setPrice(wfOrder.getYfPrice());
			payLog.setUserId(store.getId());
			payLog.setStoreId(store.getId().equals(wfOrder.getSaleId()) ? wfOrder.getBuyId() : wfOrder.getSaleId());
			payLog.setOrderType(OrderPayLog.ORDER_TYPE_WF);
			payLog.setCreateTime(DateUtil.getSysDateTimeString());
			payLog.setPayType(OrderPayLog.PAY_TYPE_YF);
		}
		if (StringUtil.isEmpty(payLog.getId())) {
			payLogService.saveOrderPayLog(payLog);
		} else {
			payLog.setPrice(wfOrder.getYfPrice());
			payLog.setUserId(store.getId());
			payLog.setStoreId(store.getId().equals(wfOrder.getSaleId()) ? wfOrder.getBuyId() : wfOrder.getSaleId());
			payLogService.updateLogStatus("0", payLog);
		}

		String frontUrl = "/store/order/wfOrderView/" + wfOrderId + "/" + viewType + "/" + pageType;

		// String htmlStr = UnionPay.unionPay(request, payLog, frontUrl);
		// request.setAttribute("htmlStr", htmlStr);
		return "/web/unionPay/unionPay";
	}

	@RequestMapping(value = "/store/order/payWait")
	public String payWait(String wfOrderId, String type, HttpServletRequest request) {
		request.setAttribute("wfOrderId", wfOrderId);
		request.setAttribute("payType", type);
		return "userCenter/store/wfpOrder/wfOrderPayLoad";
	}

	@RequestMapping(value = "/store/order/bcsPayWait")
	public String bcsPayWait(String wfOrderId, String type, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		// 暂时将金额设置为100万
		BigDecimal price = accountLogService.getAvlBal(store.getId());
		// BigDecimal price = new BigDecimal("1000000");
		request.setAttribute("listPrice", price);
		request.setAttribute("wfOrderId", wfOrderId);
		WfOrder order = wfOrderService.getWfOrderById(wfOrderId);
		BigDecimal payPrice = null;
		if (OrderPayLog.PAY_TYPE_YF.equals(type)) {
			payPrice = order.getYfPrice();
		} else if (OrderPayLog.PAY_TYPE_JS.equals(type)) {
			payPrice = order.getTotalPrice().subtract(order.getPayPrice());
			if (payPrice.doubleValue() <= 0) {
				payPrice = new BigDecimal(0.00).subtract(payPrice);
			}
		} else if (OrderPayLog.PAY_TYPE_JSTK.equals(type)) {
			payPrice = order.getPayPrice().subtract(order.getTotalPrice());
			if (payPrice.doubleValue() <= 0) {
				payPrice = new BigDecimal(0.00).subtract(payPrice);
			}
		}
		request.setAttribute("payPrice", payPrice);
		request.setAttribute("payType", type);
		return "userCenter/store/wfpOrder/bcsPayWait";
	}

	@RequestMapping(value = "/store/order/bcsPay")
	@ResponseBody
	public String bcsPay(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);

		String wfOrderId = request.getParameter("wfOrderId");
		String type = request.getParameter("payType");
		String payPassword = request.getParameter("payPassword");
		MD5Util md5Util = new MD5Util();
		if ((!StringUtil.isEmpty(store.getPayPassword()) && StringUtil.isEmpty(payPassword))
				|| (!StringUtil.isEmpty(store.getPayPassword()) && !store.getPayPassword().equals(md5Util.getMD5ofStr(payPassword)))) {
			return "0";
		}

//		Boolean payStatus = wfOrderService.wfOrderBCSPay(wfOrderId, store, type, request);
		Boolean payStatus = false;
		if (payStatus) {
			return "1";
		} else {
			return "2";
		}
	}

	@RequestMapping(value = "/store/order/payYFFin/{wfOrderId}/{type}")
	@ResponseBody
	public String yfPayFin(@PathVariable String wfOrderId, @PathVariable String type, HttpServletRequest request) {

		IOrderPayLogService payLogService = SpringContextHolder.getBean(IOrderPayLogService.class);

		OrderPayLog payLog = payLogService.getLogByOrderIdAndPayType(wfOrderId, OrderPayLog.ORDER_TYPE_WF, OrderPayLog.PAY_TYPE_YF);
		if (payLog == null || "1".equals(payLog.getStatus())) {
			return "1";
		}

		if ("0".equals(type)) {
			payLogService.updateLogStatus("2", payLog);
			return "1";
		} else if ("1".equals(type)) {
			// UnionQuery.unionPayQuery(request, payLog);
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/order/wfOrderFh")
	@ResponseBody
	public String wfOrderFh(HttpServletRequest request, HttpServletResponse response) {
		String wfOrderId = request.getParameter("wfOrderId");
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);

		if (wfOrderService.queRenfh(wfOrder, request, response)) {

			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/order/wfOrderSh")
	@ResponseBody
	public String wfOrderSh(HttpServletRequest request, HttpServletResponse response) {
		String wfOrderId = request.getParameter("wfOrderId");
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);

		if (wfOrderService.queRensh(wfOrder, request, response)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/order/wfOrderPricesLoad")
	public String wfOrderPriceLoad(HttpServletRequest request) {
		String wfOrderId = request.getParameter("wfOrderId");
		String showType = request.getParameter("showType");
		if (!StringUtil.isEmpty(wfOrderId)) {
			WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
			request.setAttribute("showWfOrder", wfOrder);
			request.setAttribute("showType", showType);

			IWfOrderDetailService detailService = SpringContextHolder.getBean(IWfOrderDetailService.class);
			List<WfOrderDetail> details = detailService.getDetailsByWfOrderId(wfOrderId);
			if ("edit1".equals(showType) && !wfOrder.isCountType()) {
				if (details == null || details.size() <= 0) {
					WfOrderDetail detail = new WfOrderDetail();
					detail.setDryWeight(new BigDecimal(wfOrder.getWfpTotal()));
					detail.setWeight(new BigDecimal(wfOrder.getWfpTotal()));
					detail.setMoistureContent(new BigDecimal(0.00));
					detail.setPrice(wfOrder.getActualPrice());
					detail.setPrices(new ArrayList<WfOrderPrice>());
					IContractContentService contentService = SpringContextHolder.getBean(IContractContentService.class);
					List<ContractContent> contents = contentService.findContractContent(wfOrder.getContractId());
					if (contents != null && contents.size() > 0) {
						for (ContractContent content : contents) {
							if (content != null) {
								WfOrderPrice price = new WfOrderPrice();
								price.setContentName(content.getName());
								price.setContractContentId(content.getId());
								price.setChargingType(content.getChargingType());
								price.setChargingUnit(content.getChargingUnit());
								price.setContractContentId(content.getId());
								detail.getPrices().add(price);
							}
						}
					}
					details = new ArrayList<WfOrderDetail>();
					details.add(detail);
				}
				request.setAttribute("details", details);
				return "userCenter/store/wfpOrder/wfOrderPricesLoad";
			} else if ("edit2".equals(showType) && wfOrder.isCountType()) {
				if (details == null || details.size() <= 0) {
					WfOrderDetail detail = new WfOrderDetail();
					detail.setDryWeight(new BigDecimal(wfOrder.getWfpTotal()));
					detail.setWeight(new BigDecimal(wfOrder.getWfpTotal()));
					detail.setMoistureContent(new BigDecimal(0.00));
					detail.setPrice(wfOrder.getActualPrice());
					detail.setTotalPrice(detail.getDryWeight().multiply(detail.getPrice()));
					details = new ArrayList<WfOrderDetail>();
					details.add(detail);
				}
				request.setAttribute("details", details);
				return "userCenter/store/wfpOrder/wfOrderPricesLoad2";
			} else {
				request.setAttribute("details", details);
				return "userCenter/store/wfpOrder/wfOrderPricesLoadView";
			}
		}
		return "userCenter/store/wfpOrder/wfOrderPricesLoadView";
	}

	@RequestMapping(value = "/store/order/wfOrderPricesReLoad")
	public String wfOrderPriceReLoad(HttpServletRequest request, HttpServletResponse response) {
		String wfOrderId = request.getParameter("wfOrderId");
		String showType = request.getParameter("showType");
		if (!StringUtil.isEmpty(wfOrderId)) {
			WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
			request.setAttribute("showWfOrder", wfOrder);
			request.setAttribute("showType", showType);

			try {
				List<WfOrderDetail> details = WfOrderHelper.computation(wfOrder, request, response);
				System.out.println(details.size());
				if ("edit1".equals(showType) && !wfOrder.isCountType()) {
					if (details == null || details.size() <= 0) {
						WfOrderDetail detail = new WfOrderDetail();
						detail.setDryWeight(new BigDecimal(wfOrder.getWfpTotal()));
						detail.setWeight(new BigDecimal(wfOrder.getWfpTotal()));
						detail.setMoistureContent(new BigDecimal(0.00));
						detail.setPrice(wfOrder.getActualPrice());
						detail.setPrices(new ArrayList<WfOrderPrice>());
						IContractContentService contentService = SpringContextHolder.getBean(IContractContentService.class);
						List<ContractContent> contents = contentService.findContractContent(wfOrder.getContractId());
						if (contents != null && contents.size() > 0) {
							for (ContractContent content : contents) {
								if (content != null) {
									WfOrderPrice price = new WfOrderPrice();
									price.setContentName(content.getName());
									price.setContractContentId(content.getId());
									price.setChargingType(content.getChargingType());
									price.setChargingUnit(content.getChargingUnit());
									price.setContractContentId(content.getId());
									detail.getPrices().add(price);
								}
							}
						}
						details = new ArrayList<WfOrderDetail>();
						details.add(detail);
					}
					request.setAttribute("details", details);
					return "userCenter/store/wfpOrder/wfOrderPricesLoad";
				} else {
					if (details == null || details.size() <= 0) {
						WfOrderDetail detail = new WfOrderDetail();
						detail.setDryWeight(new BigDecimal(wfOrder.getWfpTotal()));
						detail.setWeight(new BigDecimal(wfOrder.getWfpTotal()));
						detail.setMoistureContent(new BigDecimal(0.00));
						detail.setPrice(wfOrder.getActualPrice());
						detail.setTotalPrice(detail.getDryWeight().multiply(detail.getPrice()));
						details = new ArrayList<WfOrderDetail>();
						details.add(detail);
					}
					request.setAttribute("details", details);
					return "userCenter/store/wfpOrder/wfOrderPricesLoad2";
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return "null";
	}

	@RequestMapping(value = "/store/order/wfOrderPricesSub")
	@ResponseBody
	public String wfOrderPriceSub(HttpServletRequest request, HttpServletResponse response) {
		String wfOrderId = request.getParameter("wfOrderId");
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		if (wfOrderService.uploadPW(wfOrder, request, response)) {
			return "1";
		} else {
			return "0";
		}
	}

	@RequestMapping(value = "/store/order/queRenPrice")
	@ResponseBody
	public String queRenPrice(HttpServletRequest request) {
		String wfOrderId = request.getParameter("wfOrderId");
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);

		if (wfOrderService.queRenPrice(wfOrder, request)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/order/jieSuan/{wfOrderId}/{viewType}/{pageType}")
	public String queRenPrice(@PathVariable String wfOrderId, @PathVariable String viewType, @PathVariable String pageType, HttpServletRequest request) {
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		if (wfOrder == null || !WfOrderHelper.WFORDER_STATUS_WAIT_PAY.equals(wfOrder.getStatus())) {
			request.setAttribute("payed", "true");
			return "/web/unionPay/unionPay";
		}
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		IOrderPayLogService payLogService = SpringContextHolder.getBean(IOrderPayLogService.class);
		OrderPayLog payLog = payLogService.getLogByOrderIdAndPayType(wfOrderId, OrderPayLog.ORDER_TYPE_WF, OrderPayLog.PAY_TYPE_JS);
		if (payLog == null) {
			payLog = new OrderPayLog();
			payLog.setOrderId(wfOrder.getId());
			payLog.setPrice(wfOrder.getTotalPrice().subtract(wfOrder.getYfPrice()));
			payLog.setUserId(store.getId());
			payLog.setStoreId(store.getId().equals(wfOrder.getSaleId()) ? wfOrder.getBuyId() : wfOrder.getSaleId());
			payLog.setOrderType(OrderPayLog.ORDER_TYPE_WF);
			payLog.setCreateTime(DateUtil.getSysDateTimeString());
			payLog.setPayType(OrderPayLog.PAY_TYPE_JS);
		}
		if (StringUtil.isEmpty(payLog.getId())) {
			payLogService.saveOrderPayLog(payLog);
		} else {
			payLog.setPrice(wfOrder.getTotalPrice().subtract(wfOrder.getYfPrice()));
			payLog.setUserId(store.getId());
			payLog.setStoreId(store.getId().equals(wfOrder.getSaleId()) ? wfOrder.getBuyId() : wfOrder.getSaleId());
			payLogService.updateLogStatus("0", payLog);
		}

		String frontUrl = "/store/order/wfOrderView/" + wfOrderId + "/" + viewType + "/" + pageType;

		// String htmlStr = UnionPay.unionPay(request, payLog, frontUrl);
		// request.setAttribute("htmlStr", htmlStr);
		return "/web/unionPay/unionPay";
	}

	@RequestMapping(value = "/store/order/tuikuan/{wfOrderId}/{viewType}/{pageType}")
	public String tuikuan(@PathVariable String wfOrderId, @PathVariable String viewType, @PathVariable String pageType, HttpServletRequest request) {
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);
		if (wfOrder == null || !WfOrderHelper.WFORDER_STATUS_WAIT_PAY.equals(wfOrder.getStatus())) {
			request.setAttribute("payed", "true");
			return "/web/unionPay/unionPay";
		}
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		IOrderPayLogService payLogService = SpringContextHolder.getBean(IOrderPayLogService.class);
		OrderPayLog payLog = payLogService.getLogByOrderIdAndPayType(wfOrderId, OrderPayLog.ORDER_TYPE_WF, OrderPayLog.PAY_TYPE_JSTK);
		if (payLog == null) {
			payLog = new OrderPayLog();
			payLog.setOrderId(wfOrder.getId());
			payLog.setPrice(wfOrder.getYfPrice().subtract(wfOrder.getTotalPrice()));
			payLog.setUserId(store.getId());
			payLog.setStoreId(store.getId().equals(wfOrder.getSaleId()) ? wfOrder.getBuyId() : wfOrder.getSaleId());
			payLog.setOrderType(OrderPayLog.ORDER_TYPE_WF);
			payLog.setCreateTime(DateUtil.getSysDateTimeString());
			payLog.setPayType(OrderPayLog.PAY_TYPE_JSTK);
		}
		if (StringUtil.isEmpty(payLog.getId())) {
			payLogService.saveOrderPayLog(payLog);
		} else {
			payLog.setPrice(wfOrder.getYfPrice().subtract(wfOrder.getTotalPrice()));
			payLog.setUserId(store.getId());
			payLog.setStoreId(store.getId().equals(wfOrder.getSaleId()) ? wfOrder.getBuyId() : wfOrder.getSaleId());
			payLogService.updateLogStatus("0", payLog);
		}

		String frontUrl = "/store/order/wfOrderView/" + wfOrderId + "/" + viewType + "/" + pageType;

		// String htmlStr = UnionPay.unionPay(request, payLog, frontUrl);
		// request.setAttribute("htmlStr", htmlStr);
		return "/web/unionPay/unionPay";
	}

	@RequestMapping(value = "/store/order/jiesuanChose")
	@ResponseBody
	public String jiesuanChose(HttpServletRequest request) {
		String wfOrderId = request.getParameter("wfOrderId");
		String returnPayId = request.getParameter("returnPayId");
		String type = request.getParameter("type");
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);

		if (wfOrderService.changeReturnPayId(wfOrder, returnPayId, type, request)) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/store/order/jujuetk")
	@ResponseBody
	public String jujueTk(HttpServletRequest request) {
		String wfOrderId = request.getParameter("wfOrderId");
		if (StringUtil.isEmpty(wfOrderId)) {
			return "0";
		}
		WfOrder wfOrder = wfOrderService.getWfOrderById(wfOrderId);

		if (wfOrderService.jujueTk(wfOrder, request)) {
			return "1";
		}
		return "0";
	}
}
