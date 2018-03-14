package com.ekfans.controllers.user.appraisal;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.dto.AppraiseContDto;
import com.ekfans.base.store.dto.AppraiseDto;
import com.ekfans.base.store.service.IStorageAppraiseService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class UserAppraisalController extends BasicController {

	@Resource
	private IStorageAppraiseService storageAppraiseService;

	/**
	 * 
	 * @Title: 去往店铺评价页面
	 * @Description: TODO 去往店铺评价页面
	 * @param @param request
	 * @param @param session
	 * @param @return 设定文件
	 * @return ModelAndView 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/appraisal/list")
	public String appraisal(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		// 产品名称
		String productName = request.getParameter("productName");
		// 评价人
		String appraiser = request.getParameter("userName");
		// 评价开始时间
		String beginDate = request.getParameter("beginDate");
		// 评价结束时间
		String endDate = request.getParameter("endDate");
		// 得到已回复/
		String replyStatus = request.getParameter("replyStatus");

		// 分页
		Pager pager = new Pager();
		// 获取当前页码
		String currentpageStr = request.getParameter("pageNum");
		// 默认展示第一页
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			// 有值,替换掉默认页码
			currentPage = Integer.parseInt(currentpageStr);
		}
		pager.setCurrentPage(currentPage);

		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		String source = user.getId();
		// 评价下的内容
		List<AppraiseDto> lad = storageAppraiseService.getAppraiseBySource(source, pager);
		// 商家对客户的评价
		List<AppraiseDto> businessCustomerEvaluate = storageAppraiseService.getMyStoreAppraises(source, pager, productName, appraiser, beginDate, endDate, replyStatus);

		// 累计评价
		List<AppraiseContDto> lacd = storageAppraiseService.appraiseCount(user);

		request.setAttribute("appraiseContDtos", lacd);
		request.setAttribute("appraiseDtos", lad);

		// 将当前的搜索条件绑定到request对象上
		request.setAttribute("productName", productName);
		request.setAttribute("businessCustomerEvaluate", businessCustomerEvaluate);
		request.setAttribute("userName", appraiser);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("replyStatus", replyStatus);
		request.setAttribute("pager", pager);
		request.setAttribute("cur", "appraisal");
		return "/userCenter/customer/appraisal/userAppraisalList";
	}

	/**
	 * 
	 * @Title: appraisalParent
	 * @Description: TODO 根据传回来的被评价人取得商家返回给我的评价 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/user/target/list")
	public String appraisalParent(HttpServletRequest request, HttpSession session) {
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		List<AppraiseDto> lad = storageAppraiseService.getAppraiseByTarget(user.getId());
		request.setAttribute("appraiseDtos", lad);
		return "/userCenter/customer/appraisal/userAppraisalListDetail";
	}

	// /**
	// * d
	// * @Title: reply
	// * @Description: TODO 用户中心的商品评价
	// * 详细业务流程:
	// * (详细描述此方法相关的业务处理流程)
	// * @param @param content
	// * @param @param consultID
	// * @param @param request
	// * @param @return 设定文件
	// * @return String 返回类型
	// * @throws
	// */
	// @RequestMapping(value = "/user/order/evaluate")
	// public String evaluateStoreOrder(HttpServletRequest request,HttpSession
	// session){
	// String[] noteArray = request.getParameterValues("note");
	// String[] typeArray = request.getParameterValues("etype");
	// String[] productIdArray = request.getParameterValues("productId");
	// String orderId = request.getParameter("orderId");
	//
	// // 获取当前的店铺Id
	// String storeId = ((Store)
	// session.getAttribute(SystemConst.STORE)).getId();
	// // 这里店铺的的id与店主的userId相对应
	// User user = storeOrderService.getUserById(storeId);
	// // 评论者名字 为当前店主的名字
	// String observer = user.getName();
	//
	// boolean addOk =
	// storeOrderService.evaluateStoreOrder(noteArray,typeArray,productIdArray,orderId,observer);
	// if(addOk){
	// return "redirect:/store/order/appraise";
	// }else{
	// return "redirect:/store/order/orderEvaluateHandel/"+orderId+"/1";
	// }
	// }

	@RequestMapping(value = "/user/appraisal/appraisalListContentLoad")
	public String appraisalListContentLoad(HttpServletRequest request, HttpServletResponse response) {
		String judg = request.getParameter("judg");

		// 分页
		Pager pager = new Pager();
		// 获取当前页码
		String currentpageStr = request.getParameter("pageNum");
		// 默认展示第一页
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			// 有值,替换掉默认页码
			currentPage = Integer.parseInt(currentpageStr);
		}
		pager.setCurrentPage(currentPage);

		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		List<AppraiseDto> appraiseDLoadNew = null;
		if ("1".equals(judg)) {
			// 评价下的内容
			appraiseDLoadNew = storageAppraiseService.getAppraiseBySource(user.getId(), pager);
		} else if ("2".equals(judg)) {
			// 商家对客户的评价
			appraiseDLoadNew = storageAppraiseService.getMyStoreAppraises(user.getId(), pager, null, null, null, null, null);
		}

		request.setAttribute("appraiseDLoadNew", appraiseDLoadNew);
		request.setAttribute("pager", pager);
		return "/userCenter/customer/appraisal/userAppraisalListDetail";
	}

}
