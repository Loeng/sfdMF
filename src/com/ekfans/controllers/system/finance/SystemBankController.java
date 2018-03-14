package com.ekfans.controllers.system.finance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.finance.model.BankAccount;
import com.ekfans.base.finance.model.BankPurview;
import com.ekfans.base.finance.service.IBankAccountService;
import com.ekfans.base.finance.service.IBankPurviewService;
import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.service.IBankService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemBankController extends BasicController {

	@Autowired
	private IBankService bankService;

	@Autowired
	private IBankAccountService accountService;

	@Autowired
	private IBankPurviewService purviewService;

	/**
	 * 跳转到新增合作机构页面
	 */
	@RequestMapping(value = "/system/finacne/bankAdd")
	public String add(HttpServletRequest request) {
		List<BankPurview> purviewList = purviewService.bankPurviewList();
		request.setAttribute("purviewList", purviewList);
		return "/system/finance/bank/bankAdd";
	}

	/**
	 * 新增保存操作
	 */
	@RequestMapping(value = "/system/bank/bankSave")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, Bank bank) {
		// 保存logo
		// 设置图标保存路径
		String currentPath = "/customerfiles/bank/logo/";
		// 调用方法获取分类图标，返回图标路径
		String picPath = FileUploadHelper.uploadFile("logo", currentPath, request, response);
		bank.setLogo(picPath);
		Boolean istrue = bankService.saveOrUpdateBank(bank, request);
		if (istrue) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 查找银行列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/bank/list")
	public String list(HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取银行名称
		String bankName = request.getParameter("bankName");
		// 从页面获取支行全称
		String fullName = request.getParameter("fullName");
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
		// 利用Service的方法查找频道
		List<Bank> banks = bankService.list(pager, bankName, fullName,null,null,null,null,null,null);
		request.setAttribute("banks", banks);
		request.setAttribute("pager", pager);
		request.setAttribute("bankName", bankName);
		request.setAttribute("fullName", fullName);
		return "/system/finance/bank/bankList";
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/bank/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		// 利用Service的方法根据id删除用户和积分日志
		if (bankService.delete(id)) {
			// 删除成功，返回true
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 跳转到修改或者详情页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/bank/detail/{id}/{type}")
	public String edit(@PathVariable String id, @PathVariable String type, HttpServletRequest request) {
		// 利用Service的方法根据id查找活动
		Bank bank = bankService.getBankById(id);
		// 如果activity不为空
		if (bank == null) {
			return "redirect:/system/bank/list";
		}

		BankAccount account = accountService.getMainAccountByBankId(bank.getId());
		List<BankPurview> purviewList = purviewService.bPurviewList(bank.getId(), false, null,false);
		// 放入request
		/*--- 省市区回显结束 ---*/
		request.setAttribute("bank", bank);
		request.setAttribute("type", type);
		request.setAttribute("bankAccount", account);
		request.setAttribute("purviewList", purviewList);
		return "system/finance/bank/bankDetail";
	}

	/**
	 * 修改合作银行
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/bank/modify")
	@ResponseBody
	public Object modify(Bank bank, HttpServletRequest request, HttpServletResponse response) {
		// 验证活动名是否为空
		if (StringUtil.isEmpty(bank.getBankName())) {
			// 如果为空，返回添加失败
			return "false";
		}
		// 保存logo
		// 设置图标保存路径
		String currentPath = "/customerfiles/bank/logo/";
		// 调用方法获取分类图标，返回图标路径
		String picPath = FileUploadHelper.uploadFile("logo", currentPath, request, response);
		bank.setLogo(picPath);

		if (bankService.saveOrUpdateBank(bank, request)) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 修改合作银行
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/bank/checkaccount")
	@ResponseBody
	public String checkAccount(HttpServletRequest request, HttpServletResponse response) {
		String accountName = request.getParameter("accountName");
		String accountId = request.getParameter("accountId");
		if (accountService.checkNameIsUsed(accountName, accountId)) {
			return "1";
		} else {
			return "0";
		}
	}
}
