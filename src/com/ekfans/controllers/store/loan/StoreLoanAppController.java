package com.ekfans.controllers.store.loan;

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

import com.ekfans.base.loan.model.LoanApp;
import com.ekfans.base.loan.model.LoanAppDetail;
import com.ekfans.base.loan.model.LoanType;
import com.ekfans.base.loan.model.LoanTypeDetail;
import com.ekfans.base.loan.service.ILoanAppService;
import com.ekfans.base.loan.service.ILoanTypeDetailService;
import com.ekfans.base.loan.service.ILoanTypeService;
import com.ekfans.base.loan.util.LoanAppUtil;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.service.IBankService;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 企业中旬融资申请的Controller
 * 
 * @author liuguoyu
 *
 */
@Controller
public class StoreLoanAppController extends BasicController {
	@Autowired
	private IStoreService storeService;

	@Autowired
	private ILoanTypeService loanTypeService;

	@Autowired
	private ILoanTypeDetailService loanTypeDetailService;

	@Autowired
	private IBankService bankService;

	@Autowired
	private ILoanAppService loanAppService;

	/**
	 * 跳转到融资申请页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/apply")
	public String goApply(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		List<Bank> banks = bankService.getBank();
		request.setAttribute("bankList", banks);
		request.setAttribute("regCapital", store.getRegCapital());
		return "/userCenter/store/loan/loanApp";
	}

	/**
	 * 保理融资申请、订单融资申请
	 * @param typeId 订单类型id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/applyLoan/{typeId}")
	public String goToApply(@PathVariable String typeId, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		List<Bank> banks = bankService.getBank();
		// 长沙银行
		String bankId = "402880fa543808d9015438359b590121";
		// 查找loanType
		LoanType loanType = loanTypeService.getLoanTypeById(typeId);

		// 返回数据至页面
		request.setAttribute("bankId", bankId);
		request.setAttribute("typeId", typeId);
		request.setAttribute("bankList", banks);
		request.setAttribute("loanTypeName", loanType.getLoanName());
		request.setAttribute("loanTypeNote", loanType.getNote());
		return "/userCenter/store/loan/loanApp";
	}

	/**
	 * 保存融资申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/store/loan/applysave")
	@ResponseBody
	public String applySaveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
		Boolean addStatus = loanAppService.saveOrUpdateLoanApp(request, response);
		if (addStatus) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 根据银行ID获取可贷款的种类JSON串
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/loadtypes/{bankId}")
	@ResponseBody
	public String loadLoanTypes(@PathVariable String bankId, HttpServletRequest request, HttpServletResponse response) {
		List<LoanType> loanTypeList = loanTypeService.getLoanTypeByBankId(null, bankId, "true", null);
		backWriteStr(response, JsonUtil.listToJson(loanTypeList));
		return null;
	}

	/**
	 * 加载贷款种类下的数据提交要求
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/loaddetails/{typeId}")
	public String loanLoanDetails(@PathVariable String typeId, HttpServletRequest request, HttpServletResponse response) {
		List<LoanTypeDetail> detailList = loanTypeDetailService.getDetailsByTypeId(typeId);
		request.setAttribute("detailList", detailList);
		return "/userCenter/store/loan/appLoadDetailList";
	}

	/**
	 * 加载订单选择
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/loadorder")
	public String loanLoadOrders(HttpServletRequest request, HttpServletResponse response) {

		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}

		String orderId = request.getParameter("orderId");
		String orderType = request.getParameter("orderType");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pageNo = request.getParameter("pageNo");

		if (StringUtil.isEmpty(orderType)) {
			orderType = "0";
		}

		int currentPageNo = 1;
		if (!StringUtil.isEmpty(pageNo)) {
			try {
				currentPageNo = Integer.parseInt(pageNo);
			} catch (Exception e) {
				currentPageNo = 1;
			}
		}

		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(10);
		List<Object[]> orderList = loanAppService.getLoadOrders(store.getId(), orderId, orderType, startTime, endTime, pager);
		request.setAttribute("orderId", orderId);
		request.setAttribute("orderType", orderType);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("pager", pager);
		request.setAttribute("orderList", orderList);
		return "/userCenter/store/loan/loadOrderChose";
	}

	/**
	 * 加载合同选择
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/loadcontract")
	public String loanLoadContracts(HttpServletRequest request, HttpServletResponse response) {
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
		IContractService contractService = SpringContextHolder.getBean(IContractService.class);
		// 调用Service查询企业集合
		List<Contract> contractList = contractService.orderChoseContracts(pager, store.getId(), contractName, storeName, null, null, null, contractNo, null);

		request.setAttribute("storeName", storeName);
		request.setAttribute("contractName", contractName);
		request.setAttribute("contractNo", contractNo);
		request.setAttribute("contractList", contractList);
		request.setAttribute("pager", pager);
		return "/userCenter/store/loan/loadContractChose";
	}

	/**
	 * 跳转到融资申请页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/applist")
	public String appList(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}

		String bankId = request.getParameter("bankId");
		String loanTypeId = request.getParameter("loanTypeId");
		String appStatus = request.getParameter("appStatus");
		String pageNum = request.getParameter("pageNum");
		int pageNo = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			pageNo = Integer.parseInt(pageNum);
		}
		Pager pager = new Pager(pageNo);
		pager.setRowsPerPage(10);

		List<LoanApp> appList = loanAppService.listLoanApp(store.getId(), bankId, loanTypeId, appStatus, pager);
		List<Bank> banks = bankService.getBank();
		request.setAttribute("bankList", banks);
		request.setAttribute("appList", appList);
		request.setAttribute("bankId", bankId);
		request.setAttribute("loanTypeId", loanTypeId);
		request.setAttribute("appStatus", appStatus);
		request.setAttribute("pager", pager);
		request.setAttribute("bankName", request.getParameter("bankName"));
		request.setAttribute("loanTypeName", request.getParameter("loanTypeName"));
		return "/userCenter/store/loan/loanAppList";
	}

	/**
	 * 跳转到融资申请编辑页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/appedit/{appId}")
	public String loanAppEdit(@PathVariable String appId, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		LoanApp app = loanAppService.getAppById(appId, true);

		if (app != null) {
			Bank bank = bankService.getBankById(app.getBankId());
			if (bank != null) {
				app.setBankName(bank.getBankName());
			}

			LoanType loanType = loanTypeService.getLoanTypeById(app.getLoanTypeId());
			if (loanType != null) {
				app.setLoanTypeName(loanType.getLoanName());
			}

			Map<String, LoanAppDetail> appDetailMap = new HashMap<String, LoanAppDetail>();
			List<LoanTypeDetail> detailList = loanTypeDetailService.getDetailsByTypeId(app.getLoanTypeId());

			List<LoanAppDetail> appDetailList = app.getDetails();
			if (appDetailList != null && appDetailList.size() > 0) {
				for (LoanAppDetail detail : appDetailList) {
					if (detail == null) {
						continue;
					}
					appDetailMap.put(detail.getTypeDetailId(), detail);
				}
			}

			request.setAttribute("appDetailMap", appDetailMap);
			request.setAttribute("typeDetailList", detailList);
		}
		request.setAttribute("loanApp", app);
		return "/userCenter/store/loan/loanAppEdit";
	}

	/**
	 * 跳转到融资申请编辑页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/appview/{appId}")
	public String loanAppView(@PathVariable String appId, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		LoanApp app = loanAppService.getAppById(appId, true);

		if (app != null) {
			Bank bank = bankService.getBankById(app.getBankId());
			if (bank != null) {
				app.setBankName(bank.getBankName());
			}

			LoanType loanType = loanTypeService.getLoanTypeById(app.getLoanTypeId());
			if (loanType != null) {
				app.setLoanTypeName(loanType.getLoanName());
			}
		}
		request.setAttribute("loanApp", app);
		return "/userCenter/store/loan/loanAppView";
	}

	@RequestMapping(value = "/store/loan/appcancel/{appId}")
	@ResponseBody
	public String loanAppCancel(@PathVariable String appId, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}

		LoanApp app = loanAppService.getAppById(appId, false);

		if (app == null) {
			return "0";
		}
		if (!LoanAppUtil.APP_STATUS_APPLY.equals(app.getAppStatus()) && !LoanAppUtil.APP_STATUS_LX.equals(app.getAppStatus()) && !LoanAppUtil.APP_STATUS_SDKC.equals(app.getAppStatus())
				&& !LoanAppUtil.APP_STATUS_XSH.equals(app.getAppStatus())) {
			return "2";
		}

		Boolean updateStatus = loanAppService.changeAppStatus(app, LoanAppUtil.APP_STATUS_CANCEL, store.getStoreName(), store.getId(), "0");
		if (updateStatus) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 查看与下游企业的往来流水
	 * @param storeId 企业id
	 * @param wlqyId 往来企业id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/viewWlls")
	public String viewWlls(HttpServletRequest request) {
		// 获取当前登录企业用户
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}

		String storeId = request.getParameter("storeId"); // 企业id
		request.getSession().setAttribute("storeId", storeId);
		String wlqyId = request.getParameter("wlqyId"); // 往来企业id
		request.getSession().setAttribute("wlqyId", wlqyId);
		String orderId = request.getParameter("orderId"); // 订单号
		request.getSession().setAttribute("orderId", orderId);
		String orderType = request.getParameter("orderType"); // 订单类型
		request.getSession().setAttribute("orderType", orderType);
		String startTime = request.getParameter("startTime"); // 开始时间
		String endTime = request.getParameter("endTime"); // 结束时间
		String pageNo = request.getParameter("pageNo"); // 页码

		request.getSession().setAttribute("startTime", startTime);
		request.getSession().setAttribute("endTime", endTime);
		request.getSession().setAttribute("pageNo", pageNo);

		// 订单类型为空默认查询普通订单
		if (StringUtil.isEmpty(orderType)) {
			orderType = "0";
		}

		int currentPageNo = 1;
		if (!StringUtil.isEmpty(pageNo)) {
			try {
				currentPageNo = Integer.parseInt(pageNo);
			} catch (Exception e) {
				currentPageNo = 1;
			}
		}
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(10);

		List<Object[]> orderList = loanAppService.getWllsOrders(storeId, wlqyId, orderId, orderType, startTime, endTime, pager);

		// 返回数据至页面
		request.setAttribute("storeId", storeId);
		request.setAttribute("wlqyId", wlqyId);
		request.setAttribute("orderId", orderId);
		request.setAttribute("orderType", orderType);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("pager", pager);
		request.setAttribute("orderList", orderList);
		return "/userCenter/store/loan/wllsOrders";
	}

	/**
	 * 导出excel
	 * @param request
	 */
	@RequestMapping(value = "/store/loan/wllsOrder/exportExcel")
	public void exportWllsOrders(HttpServletRequest request, HttpServletResponse response) {
		String storeId = (String) request.getSession().getAttribute("storeId"); // 企业id

		String wlqyId = (String) request.getSession().getAttribute("wlqyId"); // 往来企业id

		String orderId = (String) request.getSession().getAttribute("orderId"); // 订单号
		String orderType = (String) request.getSession().getAttribute("orderType"); // 订单类型
		String startTime = (String) request.getSession().getAttribute("startTime"); // 开始时间
		String endTime = (String) request.getSession().getAttribute("endTime"); // 结束时间
		String pageNo = (String) request.getSession().getAttribute("pageNo"); // 页码

		// 订单类型为空默认查询普通订单
		if (StringUtil.isEmpty(orderType)) {
			orderType = "0";
		}

		int currentPageNo = 1;
		if (!StringUtil.isEmpty(pageNo)) {
			try {
				currentPageNo = Integer.parseInt(pageNo);
			} catch (Exception e) {
				currentPageNo = 1;
			}
		}
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(10000);

		// 查询需要导出的订单数据
		List<Object[]> orderList = loanAppService.getWllsOrders(storeId, wlqyId, orderId, orderType, startTime, endTime, null);

		try {
			// 调用service方法导出Excel
			if (!"3".equals(orderType)) {
				loanAppService.exportOrderExcel(orderType, orderList, response);
			} else {
				loanAppService.exportWfOrderExcel(orderList, response);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * 查看企业平台历史订单
	 * @param storeId 企业id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/loan/viewHistoreOrders")
	public String viewHistoreOrders(HttpServletRequest request) {
		// 获取当前登录企业用户
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/web/login";
		}

		String storeId = request.getParameter("storeId"); // 企业id

		String orderId = request.getParameter("orderId"); // 订单号
		String orderType = request.getParameter("orderType"); // 订单类型
		String startTime = request.getParameter("startTime"); // 开始时间
		String endTime = request.getParameter("endTime"); // 结束时间
		String pageNo = request.getParameter("pageNo"); // 页码

		int currentPageNo = 1;
		if (!StringUtil.isEmpty(pageNo)) {
			try {
				currentPageNo = Integer.parseInt(pageNo);
			} catch (Exception e) {
				currentPageNo = 1;
			}
		}
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(10);

		List<Object[]> orderList = loanAppService.getHistoreOrders(storeId, orderId, orderType, startTime, endTime, pager);

		// 返回数据至页面
		request.setAttribute("storeId", storeId);
		request.setAttribute("orderId", orderId);
		request.setAttribute("orderType", orderType);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("pager", pager);
		request.setAttribute("orderList", orderList);
		return "/userCenter/store/loan/historyOrderList";
	}

	/**
	 * 导出excel
	 * @param request
	 */
	@RequestMapping(value = "/store/loan/historyOrder/exportExcel")
	public void exportHistoryOrders(HttpServletRequest request, HttpServletResponse response) {
		String storeId = request.getParameter("storeId"); // 企业id
		String orderId = request.getParameter("orderId"); // 订单号
		String orderType = request.getParameter("orderType"); // 订单类型
		String startTime = request.getParameter("startTime"); // 开始时间
		String endTime = request.getParameter("endTime"); // 结束时间
		String pageNo = request.getParameter("pageNo"); // 页码

		int currentPageNo = 1;
		if (!StringUtil.isEmpty(pageNo)) {
			try {
				currentPageNo = Integer.parseInt(pageNo);
			} catch (Exception e) {
				currentPageNo = 1;
			}
		}
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(10000);

		// 查询需要导出的订单数据
		List<Object[]> orderList = loanAppService.getHistoreOrders(storeId, orderId, orderType, startTime, endTime, pager);

		try {
			// 调用service方法导出Excel
			loanAppService.exportHistoryOrderExcel(orderList, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
