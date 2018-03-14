package com.ekfans.controllers.store.Account;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.store.service.IStorePurviewService;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.service.IAccountService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemAccountController extends BasicController {
	
	private Logger log = LoggerFactory.getLogger(SystemAccountController.class);
	@Resource
	private IAccountService accountService;
	@Resource
	private IStorePurviewService purviewService;
	
	/**
	 * 跳转到子账号列表页面
	 */
	@RequestMapping(value = "/system/store/account/list")
	public String accountList() {
		String storeId = getRequest().getParameter("storeId"); // 企业id
		String type = getRequest().getParameter("type"); // 企业类型
		String currentpageStr = getRequest().getParameter("pageNum"); // 页码
		String name = getRequest().getParameter("name"); // 用户名
		String department = getRequest().getParameter("department"); // 部门名
		String contactName = getRequest().getParameter("contactName"); // 联系人
		
		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);
		
		// 调用Service方法查询
		List<Account> accountList = accountService.getAccountsByStoreId(storeId, name, department, contactName, pager);

		getRequest().setAttribute("storeId", storeId);
		getRequest().setAttribute("type", type);
		getRequest().setAttribute("name", name);
		getRequest().setAttribute("department", department);
		getRequest().setAttribute("contactName", contactName);
		getRequest().setAttribute("accountList", accountList);
		getRequest().setAttribute("pager", pager);
		
		return "/system/store/account/account_list";
	}

	/**
	 * 跳转到新增子账号页面
	 */
	@RequestMapping(value = "/system/store/account/add")
	public String accountAdd() {
		String storeId = getRequest().getParameter("storeId"); // 企业id
		String type = getRequest().getParameter("type"); // 企业类型
		
		getRequest().setAttribute("storeId", storeId);
		getRequest().setAttribute("type", type);
		getRequest().setAttribute("purviewList", purviewService.getStorePurviewByRoleId(type));
		
		return "/system/store/account/account_add";
	}
	
	/**
	 * 校验用户名是否重复
	 * 
	 * @return false:重复，true:不重复
	 */
	@RequestMapping(value = "/system/store/account/checkName")
	@ResponseBody
	public boolean checkName() {
		String id = getRequest().getParameter("id");
		String name = getRequest().getParameter("name");
		
		return accountService.checkName(id, name);
	}
	
	/**
	 * 子账号新增保存
	 */
	@RequestMapping(value = "/system/store/account/save")
	@ResponseBody
	public boolean accountSaveOrUpdate(@ModelAttribute Account account, HttpServletRequest request) {
		String[] purviewIds = getRequest().getParameterValues("purviewId"); // 权限集合
		
		// 调用方法保存权限，如果返回的true，则将提示符设为true,否则，设为false
		return accountService.saveOrUpdateAccount(account, purviewIds, request);
	}
	
	/**
	 * 子账号编辑
	 */
	@RequestMapping(value = "/system/store/account/edit/{id}/{roleId}/{mark}")
	public String accountEdit(@PathVariable String id, @PathVariable String roleId, @PathVariable int mark) {
		// 调用Service获取对象所对应的权限
		Account account = accountService.getAccountById(id);
		account.setUpdateTime(DateUtil.getSysDateTimeString());
		
		Map<String, Boolean> purviewMap = accountService.getAccountPurviews(id);
		List<StorePurview> purviewList = purviewService.getStorePurviewByRoleId(roleId);
		
		getRequest().setAttribute("account", account);
		getRequest().setAttribute("purviewList", purviewList);
		getRequest().setAttribute("purviewMap", purviewMap);
		
		return "/system/store/account/account_update";
	}
	
	/**
	 * 删除子账号
	 */
	@RequestMapping(value = "/system/store/account/del")
	@ResponseBody
	public boolean accountDel() {
		String id = getRequest().getParameter("delId"); // 从页面获取需要删除的ID

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
