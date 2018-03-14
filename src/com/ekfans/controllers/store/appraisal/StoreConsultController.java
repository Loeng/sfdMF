package com.ekfans.controllers.store.appraisal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Consult;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreConsultService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 店铺中心-评价/留言管理-咨询管理
 * 
 * @ClassName: StoreConsultController
 * @author 成都易科远见科技有限公司
 * @date 2014-5-4 上午10:32:25
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@Scope("prototype")
public class StoreConsultController {

	@Autowired
	private IStoreConsultService storeConsultService;

	/**
	 * 查询商户的留言和咨询
	 */
	@RequestMapping(value = "/store/consultMessage")
	public String findConsultByCondition(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		String userName = request.getParameter("userName"); // 得到咨询人
		String beginDate = request.getParameter("beginDate"); // 得到咨询的开始时间
		String endDate = request.getParameter("endDate"); // 得到咨询的结束时间
		String replyStatus = request.getParameter("replyStatus"); // 得到已回复
		String currentpageStr = request.getParameter("pageNum"); // 当前页码

		// 分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		pager.setCurrentPage(currentPage);

		// 执行查询
		List<Consult> consults = storeConsultService.getConsultByCondition(store, userName, beginDate, endDate, replyStatus, pager);

		request.setAttribute("consults", consults);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("replyStatus", replyStatus);
		request.setAttribute("pager", pager);
		request.setAttribute("currentPage", currentPage);

		return "/userCenter/store/appraisal/consultMessage";

	}

	/**
	 * 
	 * @Title: reply
	 * @Description: TODO(咨询的回复) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param content
	 * @param @param consultID
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/appraisal/replyConsult")
	@ResponseBody
	public String reply(String content, String consultID, HttpServletRequest request, HttpSession session) {
		// 验证穿过来的是不是空值
		if (StringUtil.isEmpty(content) || StringUtil.isEmpty(consultID)) {
			return "noNull";
		}
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String returnString = storeConsultService.saveConsult(content, consultID, store);
		return returnString;
	}

	@RequestMapping(value = "/store/appraisal/replyConsultAll")
	@ResponseBody
	public String replyAll(String content, String consultIDS, HttpServletRequest request) {
		// 验证穿过来的是不是空值
		if (StringUtil.isEmpty(content) || StringUtil.isEmpty(consultIDS)) {
			return "noNull";
		}
		HttpSession session = request.getSession();
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String returnString = storeConsultService.saveConsults(content, consultIDS, store);
		return returnString;
	}

}
