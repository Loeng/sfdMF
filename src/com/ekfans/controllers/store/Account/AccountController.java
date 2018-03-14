package com.ekfans.controllers.store.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.store.service.IStorePurviewService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IAccountService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.HttpUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * dController
 * 
 * @Title: StoreController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司 
 * @date 2014-3-19 上午11:22:03 
 * @version V1.0
 */
@Controller
public class AccountController extends BasicController {
	@Autowired
	private IAccountService accountService;

	@Autowired
	private IStorePurviewService purviewService;

	/**
	 * 子账号新增跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/account/add")
	public String accountAdd(HttpServletRequest request, HttpServletResponse response) {
		// 获取店铺对象
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			// 没有登陆
			return "redirect:/web/login/one";
		}

		// 获取用户对象
		User user = (User) request.getSession().getAttribute(SystemConst.USER);
		if (user == null) {
			IUserService userService = SpringContextHolder.getBean(IUserService.class);
			user = userService.getUser(store.getId());
		}
		if (user == null) {
			// 没有登陆
			return "redirect:/web/login/one";
		}

		Account account = new Account();
		account.setStoreId(store.getId());
		account.setType(user.getType());
		account.setOrgId("|" + store.getId() + "|" + DateUtil.getSysDateLong() + "|");
		request.setAttribute("account", account);
		request.setAttribute("pageType", "add");
		List<StorePurview> purviewList = purviewService.getStorePurviewByRoleId(store.getRoleId());
		request.setAttribute("purviewList", purviewList);
		return "/userCenter/customer/account/accountAddOrEdit";
	}

	/**
	 * 子账号新增保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/account/save")
	public String accountSaveOrUpdate(@ModelAttribute Account account, HttpServletRequest request, HttpServletResponse response) {
		// 获取店铺对象
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			// 没有登陆
			return "redirect:/web/login/one";
		}
		// 获取页面选择的权限集合
		String[] purviewIds = request.getParameterValues("purviewId");

		// 调用方法保存权限，如果返回的true，则将提示符设为true,否则，设为false
		boolean saveStatus = accountService.saveOrUpdateAccount(account, purviewIds, request);
		request.setAttribute("updateStatus", saveStatus);
		request.setAttribute("account", account);
		String pageType = request.getParameter("pageType");
		request.setAttribute("pageType", pageType);
		if ("add".equals(pageType)) {
			return "/userCenter/customer/account/accountAddOrEdit";
		} else {
			// 调用Service获取对象所对应的权限
			Map<String, Boolean> purviewMap = accountService.getAccountPurviews(account.getId());
			List<StorePurview> purviewList = purviewService.getStorePurviewByRoleId(store.getRoleId());
			request.setAttribute("account", account);
			request.setAttribute("purviewList", purviewList);
			request.setAttribute("purviewMap", purviewMap);
			return "/userCenter/customer/account/accountAddOrEdit";
		}
	}

	/**
	 * 子账号编辑
	 * 
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/store/account/edit/{accountId}")
	public String accountEdit(@PathVariable String accountId, HttpServletRequest request, HttpServletResponse response) {
		// 获取店铺对象
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			// 没有登陆
			return "redirect:/web/login/one";
		}
		// 调用Service获取ACCOUNT对象
		Account account = accountService.getAccountById(accountId);
		// 调用Service获取对象所对应的权限
		Map<String, Boolean> purviewMap = accountService.getAccountPurviews(accountId);
		List<StorePurview> purviewList = purviewService.getStorePurviewByRoleId(store.getRoleId());
		request.setAttribute("account", account);
		request.setAttribute("purviewList", purviewList);
		request.setAttribute("purviewMap", purviewMap);
		request.setAttribute("pageType", "edit");
		return "/userCenter/customer/account/accountAddOrEdit";
	}

	/**
	 * 子账号列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/account/list")
	public String accountList(HttpServletRequest request, HttpServletResponse response) {
		// 获取店铺对象
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			// 没有登陆
			return "redirect:/web/login/one";
		}
		String currentpageStr = request.getParameter("pageNum"); // 页码
		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		pager.setCurrentPage(currentPage);

		// 获取页面中搜索条件 - 用户名
		String name = request.getParameter("name");
		// 获取页面中搜索条件 - 部门名
		String department = request.getParameter("department");
		// 获取页面中搜索条件 - 联系人
		String contactName = request.getParameter("contactName");
		request.setAttribute("userName", name);
		request.setAttribute("userDepartment", department);
		request.setAttribute("userContactName", contactName);
		// 调用Service方法查询
		List<Account> accountList = accountService.getAccountsByStoreId(store.getId(), name, department, contactName, pager);
		request.setAttribute("accountList", accountList);
		request.setAttribute("pager", pager);

		return "/userCenter/customer/account/accountList";
	}

	/**
	 * 校验用户名是否重复
	 * 
	 * @param id
	 * @param name
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/store/account/checkName")
	@ResponseBody
	public boolean checkName(@RequestParam String id, @RequestParam String name, HttpServletRequest request, HttpServletResponse response) {
		return accountService.checkName(id, name);
	}

	/**
	 * 子账号列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/account/del")
	@ResponseBody
	public boolean accountDel(HttpServletRequest request, HttpServletResponse response) {
		// 从页面获取需要删除的ID；
		String id = request.getParameter("delId");

		if (StringUtil.isEmpty(id)) {
			return false;
		}
		String[] ids;
		if (id.indexOf("-") != -1) {
			ids = id.split("-");
		} else {
			ids = new String[1];
			ids[0] = id;
		}
		return accountService.deleteById(ids);
	}

}
