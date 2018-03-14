package com.ekfans.controllers.store.appraisal;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderWfp;
import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.order.service.IOrderWfpService;
import com.ekfans.base.store.model.MortgageApplication;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IMortgageApplicationService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.BankBinding;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IBankBindingService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class MortgageApplicationController extends BasicController {

	private Logger log = LoggerFactory
			.getLogger(MortgageApplicationController.class);
	@Resource
	private IMortgageApplicationService mortgageApplicationService;
	@Resource
	private IOrderDetailService orderDetailService;
	@Resource
	private IBankBindingService bankBindingService;
	@Resource
	private IContractService contractService;
	@Resource
	private IOrderWfpService orderWfpService;
	@Resource
	private IUserService userService;

	/**
	 * 根据订单编号查询订单详情
	 */
	@RequestMapping(value = "/store/finance/getOrderDetailByOrderId")
	public String getOrderDetailByOrderId() {
		String orderId = getRequest().getParameter("orderId");

		// 根据订单号查询订单信息
		List<OrderDetail> odlist = null;
		if (!StringUtil.isEmpty(orderId)) {
			odlist = this.orderDetailService.getOrderDetail(orderId);
		}
		BigDecimal countsum = new BigDecimal(0);
		if (odlist != null) {
			for (OrderDetail od : odlist) {
				countsum = countsum.add(od.getTotalPrice());
			}
		}

		// 绑定页面显示需要的数据
		getRequest().setAttribute("odlist", odlist);
		getRequest().setAttribute("orderId", orderId);
		getRequest().setAttribute("countsum", countsum);

		return "/userCenter/store/finance_manage/orderDetail_load";
	}

	/**
	 * 根据订单编号查询危废品订单详情
	 */
	@RequestMapping(value = "/store/finance/getOrderWfpByOrderId")
	public String getOrderWfpByOrderId() {
		Store store = (Store) getRequest().getSession().getAttribute(
				SystemConst.STORE);
		String orderId = getRequest().getParameter("orderId");

		OrderWfp odw = orderWfpService.getOrderDtailByOrderId(orderId, store);

		BigDecimal countsum = new BigDecimal(0);
		if (odw != null) {
			countsum = countsum.add(new BigDecimal(odw.getTotalPrice()));
		}

		getRequest().setAttribute("odw", odw);
		getRequest().setAttribute("countsum", countsum);

		return "/userCenter/store/finance_manage/orderWfpDetail_load";
	}

	/**
	 * 根据合同编号查询合同信息，并且Load出合同信息页面
	 */
	@RequestMapping(value = "/store/finance/getContractLoad")
	public String getContractLoad() {
		String storeId = ((Store) getSession().getAttribute(SystemConst.STORE))
				.getId();
		String contractNo = getRequest().getParameter("contractNo");

		getRequest().setAttribute("contract",
				contractService.getContractByContractNo(storeId, contractNo));

		return "/userCenter/store/finance_manage/contractDetail_load";
	}

	/**
	 * 跳转到（订单，信用，合同）借贷申请列表页面
	 * 
	 * @param type
	 *            1:订单申请，2:信用申请，3:合同借贷，4:危废品借贷
	 */
	@RequestMapping(value = "/store/finance/jumpOrdersLoanListPage/{type}")
	public String jumpOrdersLoanListPage(@PathVariable Integer type) {
		String storeId = ((Store) getRequest().getSession().getAttribute(
				SystemConst.STORE)).getId(); // 企业id
		String startTime = getRequest().getParameter("startTime"); // 开始时间
		String endTime = getRequest().getParameter("endTime"); // 结束时间
		String orderId = getRequest().getParameter("orderId"); // 订单号
		String pageNum = getRequest().getParameter("pageNum"); // 页码
		String minMoney = getRequest().getParameter("minMoney"); // 借款金额（小）
		String maxMoney = getRequest().getParameter("maxMoney"); // 借款金额（大）
		String contractNo = getRequest().getParameter("contractNo"); // 合同编号
		String status = getRequest().getParameter("status");

		// 分页
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
		pager.setRowsPerPage(10);

		// 绑定页面显示需要的数据
		getRequest().setAttribute(
				"malist",
				this.mortgageApplicationService.getMAList(pager, type, storeId,
						startTime, endTime, orderId, minMoney, maxMoney,
						contractNo, status));
		getRequest().setAttribute("startTime", startTime);
		getRequest().setAttribute("endTime", endTime);
		getRequest().setAttribute("orderId", orderId);
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("minMoney", minMoney);
		getRequest().setAttribute("maxMoney", maxMoney);
		getRequest().setAttribute("contractNo", contractNo);
		getRequest().setAttribute("status", status);

		if (type == 1) {
			return "/userCenter/store/finance_manage/orderLoanList";
		} else if (type == 2) {
			return "/userCenter/store/finance_manage/creditLoanList";
		} else if (type == 3) {
			return "/userCenter/store/finance_manage/contractLoanList";
		}
		return "/userCenter/store/finance_manage/orderWfpLoanList";
	}

	/**
	 * 跳转到（订单，信用，合同）借贷申请页面
	 * 
	 * @param type
	 *            1:订单借贷，2:信用借贷，3:合同借贷，4:危废品借贷
	 */
	@RequestMapping(value = "/store/finance/jumpDaiPape/{type}")
	public String jumpDaiPape(@PathVariable Integer type) {
		Store store = (Store) getRequest().getSession().getAttribute(
				SystemConst.STORE);
		String orderId = getRequest().getParameter("orderId"); // 订单号

		List<OrderDetail> odlist = null;
		BigDecimal countsum = new BigDecimal(0);
		OrderWfp odw = null;
		if (type == 1) {
			if (!StringUtil.isEmpty(orderId)) {
				odlist = orderDetailService.getOrderDetail(orderId);
			}
			if (odlist != null) {
				for (OrderDetail od : odlist) {
					countsum = countsum.add(od.getTotalPrice());
				}
			}
		} else if (type == 4) {
			odw = orderWfpService.getOrderDtailByOrderId(orderId, store);
			if (odw != null) {
				countsum = countsum.add(new BigDecimal(odw.getTotalPrice()));
			}
		}

		// 查询用户信息
		User user = userService.getUser(store.getId());
		List<BankBinding> bblist = null;
		if (user.getIsAssociatedBank()) {
			bblist = bankBindingService.getBankBindingByStoreId(user.getId(),
					"true");
		}

		// 绑定页面显示需要的数据
		getRequest().setAttribute("orderId",
				StringUtil.isEmpty(orderId) ? "" : orderId);
		getRequest().setAttribute("countsum", countsum);
		getRequest().setAttribute("odlist", odlist);
		getRequest().setAttribute("bblist", bblist);
		getRequest().setAttribute("odw", odw);
		getRequest().setAttribute("mark", user.getIsAssociatedBank());

		if (type == 1) {
			return "/userCenter/store/finance_manage/orderLoanApply";
		} else if (type == 2) {
			return "/userCenter/store/finance_manage/creditLoanApply";
		} else if (type == 3) {
			return "/userCenter/store/finance_manage/contractLoanApply";
		}
		return "/userCenter/store/finance_manage/orderWfpLoanApply";
	}

	/**
	 * 保存（订单，信用，合同）借款申请
	 */
	@RequestMapping(value = "/store/finance/saveOrderShen", method = RequestMethod.POST)
	@ResponseBody
	public int saveShen(@ModelAttribute MortgageApplication ma,
			HttpServletResponse response) {
		if (ma.getType() == 1) {
			// 获取订单合同
			String orderPDF = "/customerfiles/company/order/"
					+ DateUtil.getNoSpSysDateString() + "/";
			orderPDF = FileUploadHelper.uploadFile("orderpdf", orderPDF,
					getRequest(), response);
			ma.setOrderPdfFile(orderPDF);
		} else if (ma.getType() == 2) {
			// 获取抵押物(文件)
			String pawnFile = "/customerfiles/company/pawnFile/"
					+ DateUtil.getNoSpSysDateString() + "/";
			pawnFile = FileUploadHelper.uploadFile("pawnFile", pawnFile,
					getRequest(), response);
			ma.setPawnFile(pawnFile);
		}

		return mortgageApplicationService.saveMa(ma);
	}

	/**
	 * 跳转到（订单，信用，合同）借贷详情页面或借贷编辑页面
	 * 
	 * @param jum
	 *            2:查看，3:编辑
	 */
	@RequestMapping(value = "/store/finance/jumpDaiDetailPape/{jum}")
	public String jumpDaiDetailPape(@PathVariable int jum) {
		Store store = (Store) getRequest().getSession().getAttribute(
				SystemConst.STORE);
		String id = getRequest().getParameter("id");

		MortgageApplication ma = mortgageApplicationService.getMaById(id);

		// 查询用户信息
		User user = userService.getUser(store.getId());
		List<BankBinding> bblist = null;
		if (user.getIsAssociatedBank()) {
			bblist = bankBindingService.getBankBindingByStoreId(user.getId(),
					"true");
		}
		List<OrderDetail> odlist = null;
		BigDecimal countsum = new BigDecimal(0);
		OrderWfp odw = null;
		if (ma.getType() == 1) {
			if (!StringUtil.isEmpty(ma.getOrderId())) {
				odlist = orderDetailService.getOrderDetail(ma.getOrderId());
			}
			if (odlist != null) {
				for (OrderDetail od : odlist) {
					countsum = countsum.add(od.getTotalPrice());
				}
			}
		} else if (ma.getType() == 4) {
			odw = orderWfpService
					.getOrderDtailByOrderId(ma.getOrderId(), store);
			if (odw != null) {
				countsum = countsum.add(new BigDecimal(odw.getTotalPrice()));
			}
		}

		// 绑定页面显示需要的数据
		getRequest().setAttribute("odlist", odlist);
		getRequest().setAttribute("bblist", bblist);
		getRequest().setAttribute("countsum", countsum);
		getRequest().setAttribute("ma", ma);
		getRequest().setAttribute("odw", odw);
		getRequest().setAttribute("mark", user.getIsAssociatedBank());

		if (ma.getType() == 1) {
			return "/userCenter/store/finance_manage/orderLoanEdit";
		} else if (ma.getType() == 2) {
			return "/userCenter/store/finance_manage/creditLoanEdit";
		} else if (ma.getType() == 3) {
			return "/userCenter/store/finance_manage/contractLoanEdit";
		}
		return "/userCenter/store/finance_manage/orderWfpLoanEdit";
	}

	/**
	 * 更新（订单，信用，合同）借款申请
	 */
	@RequestMapping(value = "/store/finance/updateMaDetail")
	@ResponseBody
	public int updateMaDetail(@ModelAttribute MortgageApplication ma,
			HttpServletResponse response) {
		if (ma.getType() == 1) {
			// 获取订单合同
			String orderPDF = "/customerfiles/company/order/"
					+ DateUtil.getNoSpSysDateString() + "/";
			orderPDF = FileUploadHelper.uploadFile("orderpdf", orderPDF,
					getRequest(), response);
			ma.setOrderPdfFile(orderPDF);
		} else if (ma.getType() == 2) {
			// 获取抵押物(文件)
			String pawnFile = "/customerfiles/company/pawnFile/"
					+ DateUtil.getNoSpSysDateString() + "/";
			pawnFile = FileUploadHelper.uploadFile("pawnFile", pawnFile,
					getRequest(), response);
			ma.setPawnFile(pawnFile);
		}

		return mortgageApplicationService.updateMa(ma);
	}

}
