package com.ekfans.controllers.system.finance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.finance.model.BankDepartment;
import com.ekfans.base.finance.model.BankPurview;
import com.ekfans.base.finance.service.IBankAccountService;
import com.ekfans.base.finance.service.IBankDepartmentService;
import com.ekfans.base.finance.service.IBankPurviewService;
import com.ekfans.base.user.service.IBankService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemBankDepartmentController extends BasicController {

	@Autowired
	private IBankService bankService;

	@Autowired
	private IBankAccountService accountService;

	@Autowired
	private IBankPurviewService purviewService;

	@Autowired
	private IBankDepartmentService departmentService;

	/**
	 * 跳转到部门列表页
	 */
	@RequestMapping(value = "/system/finacne/demarts/{bankId}")
	public String list(@PathVariable String bankId, HttpServletRequest request) {
		List<BankDepartment> departmentList = departmentService.getRootDepartmentsByBankId(bankId);
		request.setAttribute("departmentList", departmentList);
		request.setAttribute("bankId", bankId);
		return "/system/finance/bank/departmentList";
	}

	/**
	 * 跳转到添加部门页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/finacne/demarts/add")
	public String addRoot(HttpServletRequest request) {
		String bankId = request.getParameter("bankId");
		String parentId = request.getParameter("parentId");
		BankDepartment parentDepartment = null;
		List<BankPurview> purviews = null;
		if (!StringUtil.isEmpty(parentId)) {
			purviews = purviewService.getBankPurviewByRoleIdAndType(parentId, true);
			parentDepartment = departmentService.getDepartmentById(parentId);
		} else {
			purviews = purviewService.getBankPurviewByRoleIdAndType(bankId, false);
		}

		request.setAttribute("purviews", purviews);
		request.setAttribute("bankId", bankId);
		request.setAttribute("parentDepartment", parentDepartment);
		return "/system/finance/bank/departmentAdd";
	}

	/**
	 * 保存部门
	 */
	@RequestMapping(value = "/system/finacne/demarts/save")
	@ResponseBody
	public String save(BankDepartment department, HttpServletRequest request) {
		if (departmentService.saveOrUpdate(department, request)) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 校验部门是否可被删除
	 */
	@RequestMapping(value = "/system/finacne/demarts/checkCanRemove/{departmentId}")
	@ResponseBody
	public String checkCanRemove(@PathVariable String departmentId, HttpServletRequest request) {
		// 判断该账号下是否有子账户，如果有，则不让删除
		if (departmentService.checkDepartmentCanRemove(departmentId)) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 查询子部门信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/finacne/demarts/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		try {
			BankDepartment department = departmentService.getDepartmentById(id);
			if (department == null) {
				return "error";
			}
			request.setAttribute("department", department);
			String parentRoleId = "";
			Boolean parentType = false;
			if (!StringUtil.isEmpty(department.getParentId())) {
				parentRoleId = department.getParentId();
				parentType = true;
			} else {
				parentRoleId = department.getBankId();
				parentType = false;
			}

			List<BankPurview> purviews = purviewService.bPurviewList(id, true, parentRoleId, parentType);

			request.setAttribute("purviews", purviews);
			return "/system/finance/bank/departmentDetail";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 查找子分类列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/finacne/demarts/child/{id}")
	public String child(@PathVariable String id, HttpServletRequest request) {
		// 查询出分类列表返回到页面
		List<BankDepartment> departments = departmentService.getChildDepartments(id);
		request.setAttribute("departments", departments);
		return "/system/finance/bank/departmentTree";
	}

	/**
	 * 删除分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/finacne/demarts/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		try {
			// 利用Service的方法根据id删除分类
			if (departmentService.deleteDepartmentById(id)) {
				return true;
			}
		} catch (Exception e) {
			// 删除失败，返回false
			return false;
		}
		return "error";
	}

}
