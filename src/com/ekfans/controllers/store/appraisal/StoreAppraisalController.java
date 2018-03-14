package com.ekfans.controllers.store.appraisal;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.dto.AppraiseContDto;
import com.ekfans.base.store.dto.AppraiseDto;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStorageAppraiseService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class StoreAppraisalController extends BasicController {

	@Resource
	private IStorageAppraiseService storageAppraiseService;

	/**
	 * 去往店铺评价页面
	 */
	@RequestMapping(value = "/store/appraisal/list")
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

		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		List<AppraiseDto> lad = storageAppraiseService.getMyStoreAppraises(store, pager, productName, appraiser, beginDate, endDate, replyStatus);

		List<AppraiseContDto> lacd = storageAppraiseService.appraiseCount(store);

		request.setAttribute("appraiseContDtos", lacd);
		request.setAttribute("appraiseDtos", lad);

		// 将当前的搜索条件绑定到request对象上
		request.setAttribute("productName", productName);
		request.setAttribute("userName", appraiser);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("replyStatus", replyStatus);
		request.setAttribute("pager", pager);

		return "/userCenter/store/appraisal/appraisalList";
	}

	/**
	 * 评价的单条回复
	 */
	@RequestMapping(value = "/store/appraisal/replyAppraise")
	@ResponseBody
	public String reply(HttpServletRequest request) {
		String content = request.getParameter("content");
		String consultID = request.getParameter("consultID");
		// 验证穿过来的是不是空值
		if (StringUtil.isEmpty(content) || StringUtil.isEmpty(consultID)) {
			return "noNull";
		}
		HttpSession session = request.getSession();
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String returnString = storageAppraiseService.saveAppraise(content, consultID, store);
		return returnString;
	}

	/**
	 * 评价的批量回复
	 */
	@RequestMapping(value = "/store/appraisal/replyAppraiseAll")
	@ResponseBody
	public String replyAll(HttpServletRequest request) {
		String content = request.getParameter("content");
		String consultIDS = request.getParameter("consultIDS");

		// 验证穿过来的是不是空值
		if (StringUtil.isEmpty(content) || StringUtil.isEmpty(consultIDS)) {
			return "noNull";
		}
		HttpSession session = request.getSession();
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String returnString = storageAppraiseService.saveAppraises(content, consultIDS, store);
		return returnString;
	}

}
