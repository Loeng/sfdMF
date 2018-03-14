package com.ekfans.controllers.system.finance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.AccountBank;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccountBankService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemAccountBankController extends BasicController {

	@Autowired
	private IAccountBankService accountBankService;

	@Autowired
	private IStoreService storeService;

	/**
	 * 跳转到新增合作机构页面
	 */
	@RequestMapping(value = "/system/account/checklist")
	public String checkList(HttpServletRequest request) {
		String storeName = request.getParameter("storeName");
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);

		List<AccountBank> banks = accountBankService.getCheckList(storeName, pager);
		request.setAttribute("banks", banks);
		request.setAttribute("storeName", storeName);
		request.setAttribute("pager", pager);
		return "/system/finance/account/checkList";
	}

	/**
	 * 跳转到新增合作机构页面
	 */
	@RequestMapping(value = "/system/account/gocheck/{id}")
	public String gocheck(@PathVariable String id, HttpServletRequest request) {
		AccountBank bank = accountBankService.getBnkById(id);
		Store store = storeService.getStore(bank.getUserId());
		request.setAttribute("bank", bank);
		request.setAttribute("store", store);
		return "/system/finance/account/check";
	}

	/**
	 * 跳转到新增合作机构页面
	 */
	@RequestMapping(value = "/system/account/check/{id}/{checkType}")
	public String check(@PathVariable String id, @PathVariable String checkType, HttpServletRequest request) {
		AccountBank bank = accountBankService.getBnkById(id);
		bank.setStatus(checkType);
		Boolean status = accountBankService.checkBank(bank, request);
		if (status) {
			request.setAttribute("checkSuccess", "true");
		} else {
			request.setAttribute("checkSuccess", "false");
		}
		request.setAttribute("bank", bank);
		Store store = storeService.getStore(bank.getUserId());
		request.setAttribute("store", store);
		return "/system/finance/account/check";
	}

}
