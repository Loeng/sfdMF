package com.ekfans.controllers.store.order.wfpOrder;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Wfpml;
import com.ekfans.base.order.service.IOrderValuationService;
import com.ekfans.base.order.service.IValuationCategoryService;
import com.ekfans.base.order.service.IWfpmlService;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class WfpScrapController {

	@Autowired
	private IWfpmlService wfpmlService;

	@Autowired
	private IValuationCategoryService valuationCategory;

	@Autowired
	private IOrderValuationService orderValuationService;

	@Autowired
	private IContractService contractService;

	/*
	 * ajaxChoseFile列表页添加文件
	 */
	@RequestMapping(value = "/wfpscrap/fileupload")
	@ResponseBody
	public boolean skipAjaxChoseFile(HttpServletRequest request, HttpServletResponse reponse, HttpSession session) {
		// Store store = (Store) session.getAttribute(SystemConst.STORE);
		// // 获取输入申报ID
		// String id = request.getParameter("updateid");
		// // 获取合同ID
		// String contractId = request.getParameter("contractId");
		// // 获取买家的ID
		// String buyerId = request.getParameter("buyerId");
		// // 获取供应商上传的联单
		// String Couplet = "/customerfiles/" + store.getId() + "/fileScrap/" +
		// DateUtil.getNoSpSysDateString() + "/";
		// // 获取审批资料PDF文件路径转换为服务端路径
		// String fileScrap = FileUploadHelper.uploadFile("scrapUpload",
		// Couplet, request, reponse);
		// boolean isUpdate = false;
		// // 根据ID更新数据
		// if (!StringUtil.isEmpty(id) && !StringUtil.isEmpty(fileScrap)) {
		// // 根据ID查询数据
		// WfpScrap ws = wfpScrapService.getWfpScrapById(id);
		// ws.setApproveInfo(fileScrap);
		// // 设置为未审核状态
		// ws.setStatus("1");
		// isUpdate = wfpScrapService.updateScrapApproveIngfo(ws, contractId,
		// buyerId, store.getId());
		// }
		// return isUpdate;
		return false;
	}

	/**
	 * 查询危废品名录
	 */
	@RequestMapping(value = "/wfpscrap/wfpml")
	public String wfpScrap(HttpServletRequest request) {
		// 获取parentId
		String parentId = request.getParameter("parentId");
		List<Wfpml> ml = wfpmlService.getWfpmlByParent(parentId);
		request.setAttribute("wfpml", ml);
		return "/userCenter/store/wfpOrder/wfml/loadDengji";
	}

	@RequestMapping(value = "/wfpscrap/wfpml/two")
	@ResponseBody
	public void wfpScraptwo(HttpServletRequest request, HttpServletResponse response) {
		// 获取parentId
		String parentId = request.getParameter("parentId");
		List<Wfpml> ml = wfpmlService.getWfpmlByParent(parentId);
		String m = JsonUtil.listToJson(ml);
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询危废品名录
	 */
	@RequestMapping(value = "/wfpscrap/wfpml/pager")
	public String wfpScrapMl(HttpServletRequest request, HttpServletResponse response) {
		// 获取parentId
		String parentId = request.getParameter("parentId");
		List<Wfpml> ml = wfpmlService.getWfpmlByParentId(null, parentId);
		request.setAttribute("wfpml", ml);
		// request.setAttribute("pager", pager);
		return "/userCenter/store/wfpOrder/wfml/loadThreeDengji";
	}

	/**
	 * 查询危废品名录
	 */
	@RequestMapping(value = "/wfpscrap/wfpmlonly")
	public String wfpScrapOnly(HttpServletRequest request) {
		// 获取parentId
		String parentId = request.getParameter("parentId");
		List<Wfpml> ml = wfpmlService.getWfpmlByParent(parentId);
		request.setAttribute("wfpml", ml);
		return "/userCenter/store/contract/wfml/loadDengji";
	}

	@RequestMapping(value = "/wfpscrap/wfpml/twoonly")
	@ResponseBody
	public void wfpScraptwoOnly(HttpServletRequest request, HttpServletResponse response) {
		// 获取parentId
		String parentId = request.getParameter("parentId");
		List<Wfpml> ml = wfpmlService.getWfpmlByParent(parentId);
		String m = JsonUtil.listToJson(ml);
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询危废品名录
	 */
	@RequestMapping(value = "/wfpscrap/wfpml/pageronly")
	public String wfpScrapMlOnly(HttpServletRequest request, HttpServletResponse response) {
		// 获取parentId
		String parentId = request.getParameter("parentId");
		List<Wfpml> ml = wfpmlService.getWfpmlByParentId(null, parentId);
		request.setAttribute("wfpml", ml);
		// request.setAttribute("pager", pager);
		return "/userCenter/store/contract/wfml/loadThreeDengji";
	}

	/**
	 * 危废品名录展示
	 */
	@RequestMapping(value = "/wfpscrap/wfpml/show")
	public String wfpScrapMlShow(HttpServletRequest request, HttpServletResponse response) {
		String mlIds = request.getParameter("mlIds");
		List<Wfpml> mlList = null;
		if (!StringUtil.isEmpty(mlIds)) {
			String[] ids = mlIds.split(";");
			mlList = wfpmlService.getWfpmlListByLastIds(ids);
		}
		request.setAttribute("mlList", mlList);
		return "/userCenter/store/wfpOrder/wfml/loadWfList";
	}

	@RequestMapping(value = "/wfpscrap/wfpml/dynaSearch1")
	@ResponseBody
	public List<Wfpml> dynaSearch1(String dynaName, HttpServletRequest request, HttpServletResponse response) {
		return wfpmlService.getWfpMlByName1(dynaName);
	}

	@RequestMapping(value = "/wfpscrap/wfpml/dynaSearch3")
	@ResponseBody
	public List<Wfpml> dynaSearch3(String dynaName, HttpServletRequest request, HttpServletResponse response) {
		return wfpmlService.getWfpMlByName3(dynaName);
	}

	@RequestMapping(value = "/wfpscrap/wfpml/dynaSearch2")
	@ResponseBody
	public List<Wfpml> dynaSearch2(String dynaName, HttpServletRequest request, HttpServletResponse response) {
		return wfpmlService.getWfpMlByName2(dynaName);
	}

	@RequestMapping(value = "/wfpscrap/wfpml/dynaSearch3ByPage")
	public String dynaSearch3ByPage(String dynaName, HttpServletRequest request, HttpServletResponse response) {
		String currentpageStr = request.getParameter("pageNum");
		Pager pager = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		// 设置查询的条数
		pager.setRowsPerPage(10);
		List<Wfpml> list = wfpmlService.getWfpMlByName3(pager, dynaName);
		request.setAttribute("wfpml", list);
		request.setAttribute("pager", pager);
		return "/userCenter/store/wfpOrder/DengjiLoad";
	}

	@RequestMapping(value = "/system/wfpScrap/getWfpmlList1")
	@ResponseBody
	public List<Wfpml> getWfpmlList1() {
		return wfpmlService.getWfpmlList(null);
	}

	@RequestMapping(value = "/system/wfpScrap/getWfpmlList2")
	@ResponseBody
	public List<Wfpml> getWfpmlList2(String firstId) {
		return wfpmlService.getWfpmlList(firstId);
	}

	@RequestMapping(value = "/system/wfpScrap/getWfpmlList3")
	@ResponseBody
	public List<Wfpml> getWfpmlList3(String secondId) {
		return wfpmlService.getWfpmlList(secondId);
	}

	@RequestMapping(value = "/system/wfpScrap/addWfpml1")
	@ResponseBody
	public String addWfpml1(String names) {
		if (!StringUtil.isEmpty(names)) {
			Wfpml m = new Wfpml();
			m.setName(names);
			wfpmlService.addWfpml(m, null);
			// 修改全路径
			Wfpml m2 = new Wfpml();
			m2.setId(m.getId());
			m2.setFullPath("|" + m.getId() + "|");
			m2.setIdentity(m.getIdentity());
			m2.setName(m.getName());
			m2.setNo(m.getNo());
			m2.setNote(m.getNote());
			m2.setParentId(m.getParentId());
			m2.setPosition(m.getPosition());
			m2.setShow_sub(m.getShow_sub());
			m2.setStatus(m.getStatus());
			wfpmlService.updateWfpml1(m2);
			return "success";
		}
		return null;
	}

	@RequestMapping(value = "/system/wfpScrap/addWfpml2")
	@ResponseBody
	public String addWfpml2(String names, String firstId) {
		if (!StringUtil.isEmpty(names)) {
			Wfpml m = new Wfpml();
			m.setName(names);
			m.setParentId(firstId);
			wfpmlService.addWfpml(m, firstId);
			// 修改全路径
			Wfpml m2 = new Wfpml();
			m2.setId(m.getId());
			m2.setFullPath("|" + firstId + "|" + m.getId() + "|");
			m2.setIdentity(m.getIdentity());
			m2.setName(m.getName());
			m2.setNo(m.getNo());
			m2.setNote(m.getNote());
			m2.setParentId(m.getParentId());
			m2.setPosition(m.getPosition());
			m2.setShow_sub(m.getShow_sub());
			m2.setStatus(m.getStatus());
			wfpmlService.updateWfpml1(m2);
			return "success";
		}
		return null;
	}

	@RequestMapping(value = "/system/wfpScrap/addWfpml3")
	@ResponseBody
	public String addWfpml3(String names, String statuss, String no, String identity, String firstId, String secondId) {
		if (!StringUtil.isEmpty(names)) {
			Wfpml m = new Wfpml();
			m.setName(names);
			m.setIdentity(identity);
			m.setStatus(statuss);
			m.setNo(no);
			m.setParentId(secondId);
			wfpmlService.addWfpml(m, secondId);
			// 修改全路径
			Wfpml m2 = new Wfpml();
			m2.setId(m.getId());
			m2.setFullPath("|" + firstId + "|" + secondId + "|" + m.getId() + "|");
			m2.setIdentity(m.getIdentity());
			m2.setName(m.getName());
			m2.setNo(m.getNo());
			m2.setNote(m.getNote());
			m2.setParentId(m.getParentId());
			m2.setPosition(m.getPosition());
			m2.setShow_sub(m.getShow_sub());
			m2.setStatus(m.getStatus());
			wfpmlService.updateWfpml1(m2);
			return "success";
		}
		return null;
	}

	@RequestMapping(value = "/system/wfpScrap/delWfpml3")
	@ResponseBody
	public String delWfpml3(String threeId) {
		if (!StringUtil.isEmpty(threeId)) {
			wfpmlService.delWfpml3(threeId);
			return "success";
		}
		return null;
	}

	@RequestMapping(value = "/system/wfpScrap/delWfpml2")
	@ResponseBody
	public String delWfpml2(String secondId) {
		if (!StringUtil.isEmpty(secondId)) {
			wfpmlService.delWfpml2(secondId);
			return "success";
		}
		return null;
	}

	@RequestMapping(value = "/system/wfpScrap/delWfpml1")
	@ResponseBody
	public String delWfpml1(String firstId) {
		if (!StringUtil.isEmpty(firstId)) {
			wfpmlService.delWfpml1(firstId);
			return "success";
		}
		return null;
	}

	/**
	 * 修改名录（三级通用）
	 *
	 * @param parentId
	 *            父级ID
	 * @param fullPath
	 *            全路径
	 * @param show_sub
	 * @param names
	 *            名称
	 * @param firstId
	 * @param no
	 * @param note
	 * @param statuss
	 * @param identity
	 * @param position
	 * @return
	 */
	@RequestMapping(value = "/system/wfpScrap/updateWfpml1")
	@ResponseBody
	public String updateWpml1(String parentId, String fullPath, String show_sub, String names, String myId, String no, String note, String statuss, String identity, int position) {
		//
		System.out.println("进入修改$$$$$$$$$$$$$$$$    " + myId + "  " + names + "  " + statuss);
		if (!StringUtil.isEmpty(myId)) {
			Wfpml m = new Wfpml();
			m.setName(names);
			m.setId(myId);
			m.setIdentity(identity);
			m.setStatus(statuss);
			m.setPosition(position);
			m.setNo(no);
			m.setNote(note);
			m.setShow_sub(show_sub);
			m.setFullPath(fullPath);
			m.setParentId(parentId);
			wfpmlService.updateWfpml1(m);

			return "success";
		}
		return null;
	}
}
