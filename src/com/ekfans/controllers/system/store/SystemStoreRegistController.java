package com.ekfans.controllers.system.store;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.StoreApply;
import com.ekfans.base.store.service.IStoreApplyService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemStoreRegistController extends BasicController {

	// 定义SERVICE
	@Autowired
	private IStoreApplyService storeApplyService;

	/**
	 * 
	 * @Title: listAll
	 * @Description: TODO(后台商家入驻的集合显示页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/store/storeApplyList")
	public String listAll(HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();

		// 从页面获取店铺名称
		String operation = request.getParameter("operation");

		// 从页面获取状态
		String status = request.getParameter("status");

		// 从页面获取开始时间
		String beginDate = request.getParameter("beginDate");

		// 从页面获取结束时间
		String endDate = request.getParameter("endDate");
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
		// 得到所有的申请入驻店铺的集合
		List<StoreApply> sas = storeApplyService.listAll(pager, operation, status, beginDate, endDate);
		request.setAttribute("sas", sas);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("pager", pager);
		request.setAttribute("status", status);
		request.setAttribute("operation", operation);
		// 绑定数据到页面上
		return "/system/store/apply/storeApplyList";
	}

	/**
	 * 
	 * @Title: delete
	 * @Description: TODO(根据id删除店铺申请) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/storeApply/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		// 调用sevice删除成功返回true
		if (storeApplyService.deteteStoreApplyByID(id)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 跳转到详情页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/storeApply/query/{id}")
	public String toCheck(@PathVariable String id, HttpServletRequest request) {
		try {

			// 利用Service的方法根据id查找店铺
			StoreApply sa = storeApplyService.getStoreApplyById(id);
			request.setAttribute("sa", sa);
			return "/system/store/apply/storeApplyQuery";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: updateStruts
	 * @Description: TODO(店铺申请成功) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/storeApply/checkModify/{id}")
	public String updateStruts(@PathVariable String id, HttpSession session) {
		// 从session中获取管理员对象
		Manager manager = (Manager) session.getAttribute(SystemConst.MANAGER);

		// 根据id得到对应的店铺申请单
		StoreApply sa = storeApplyService.getStoreApplyById(id);
		// 申请通过
		sa.setStatus(1);
		// 设置审核人
		sa.setOperation(manager.getName());
		// 如果申请通过返回列表页面
		if (storeApplyService.updateStatus(sa)) {
			return "redirect:/system/store/storeApplyList";
		} else {
			return "error";
		}
	}
}
