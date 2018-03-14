package com.ekfans.controllers.system.finance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.finance.model.BankAccount;
import com.ekfans.base.finance.model.BankDepartment;
import com.ekfans.base.finance.service.IBankAccountService;
import com.ekfans.base.finance.service.IBankDepartmentService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemBankAccountController extends BasicController {

	@Autowired
	private IBankAccountService accountService;

	@Autowired
	private IBankDepartmentService departmentService;

	/**
	 * 跳转到银行账户列表页
	 */
	@RequestMapping(value = "/system/bankaccount/list/{bankId}")
	public String list(@PathVariable String bankId, HttpServletRequest request) {
		// 从页面获取数据
		String name = request.getParameter("name");
		String departmentId = request.getParameter("departmentId");
		String realName = request.getParameter("realName");
		String phone = request.getParameter("phone");
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

		// 调用Service方法查询
		List<BankAccount> accountList = accountService.getAccountList(bankId, name, departmentId, realName, phone, pager);

		// 查询部门集合
		List<BankDepartment> departments = departmentService.getDepartmentsByBankId(bankId);

		// 将数据打回页面
		request.setAttribute("bankId", bankId);
		request.setAttribute("name", name);
		request.setAttribute("departmentId", departmentId);
		request.setAttribute("realName", realName);
		request.setAttribute("phone", phone);
		request.setAttribute("pager", pager);
		request.setAttribute("accountList", accountList);
		request.setAttribute("departments", departments);
		return "/system/finance/bank/bankAccountList";
	}

	/**
	 * 删除账户
	 */
	@RequestMapping(value = "/system/bankaccount/remove")
	@ResponseBody
	public String remove(HttpServletRequest request) {
		String accountId = request.getParameter("accountId");
		if (StringUtil.isEmpty(accountId)) {
			return "0";
		}
		String[] ids = accountId.split(";");

		Boolean status = accountService.removeByIds(ids);
		if (status) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 新增账户
	 */
	@RequestMapping(value = "/system/bankaccount/add/{bankId}")
	public String add(@PathVariable String bankId, HttpServletRequest request) {
		List<BankDepartment> departments = departmentService.getDepartmentsByBankId(bankId);
		request.setAttribute("departments", departments);
		request.setAttribute("bankId", bankId);
		return "/system/finance/bank/bankAccountAdd";
	}

	/**
	 * 编辑账户
	 */
	@RequestMapping(value = "/system/bankaccount/edit/{accountId}")
	public String edit(@PathVariable String accountId, HttpServletRequest request) {
		BankAccount account = accountService.getAccountById(accountId);
		List<BankDepartment> departments = null;
		if (account != null) {
			departments = departmentService.getDepartmentsByBankId(account.getBankId());

		}
		request.setAttribute("departments", departments);
		request.setAttribute("account", account);
		return "/system/finance/bank/bankAccountEdit";
	}

	/**
	 * 保存账户
	 */
	@RequestMapping(value = "/system/bankaccount/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(BankAccount account, HttpServletRequest request) {
		Boolean status = accountService.saveOrUpdate(account, request);
		if (status) {
			return "1";
		} else {
			return "0";
		}
	}

}
