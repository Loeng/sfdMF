package com.ekfans.controllers.system.store;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.ICreditEstimatesService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.service.IAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.system.store.VO.AttributeVO;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemStoreController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SystemStoreController.class);
	@Resource
	private IStoreService storeService;
	@Resource
	private ICreditEstimatesService creditEstimatesService;
	@Resource
	private IAreaService areaService;

	/**
	 * 跳转到企业入驻页面
	 */
	@RequestMapping(value = "/system/store/jumpAddPage/{type}")
	public String jumpAddPage(@PathVariable String type) {
		getRequest().setAttribute("arealist", areaService.getAllArea());

		return "/system/store/store_add";
	}

	/**
	 * 保存企业信息（采购商，供应商，处置企业，运输企业）
	 * 
	 * @return 1:成功，2:失败，3:企业logo为空，4:营业执照为空
	 */
	@RequestMapping(value = "/system/store/saveStore")
	@ResponseBody
	public int saveStore(@ModelAttribute AttributeVO vo, HttpServletRequest req, HttpServletResponse resp) {
		// 企业logo
		String logoPath = "/customerfiles/store/storeLogo/";
		String logo = FileUploadHelper.uploadFile("storeLogo", logoPath, req, resp);
		// 营业执照
		String businessLicensePath = "/customerfiles/store/businessLicenseFile";
		String businessLicense = FileUploadHelper.uploadFile("businessLicense", businessLicensePath, req, resp);

		if (StringUtil.isEmpty(logo)) {
			return 3;
		}
		if (StringUtil.isEmpty(businessLicense)) {
			return 4;
		}

		// 设置：企业logo,营业执照
		vo.getStore().setStoreLogo(logo);
		vo.getStore().setBusinessLicense(businessLicense);
		vo.getStore().setStoreLogo(logo);
		vo.getStore().setBusinessLicense(businessLicense);

		boolean flag = storeService.saveStoreInfo(vo.getUser(), vo.getStore(), req);
		return flag ? 1 : 2;
	}

	/**
	 * 分页查询企业列表页面
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/system/store/newlist/{type}")
	public String jumpListPage(@PathVariable String type, HttpServletRequest req) {
		storeListPage(type, req, 1);

		return "/system/store/store_list";
	}

	/**
	 * 删除企业操作
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/system/store/newdelete", method = RequestMethod.POST)
	@ResponseBody
	public Boolean deleteStore(HttpServletRequest req) {
		String ids = req.getParameter("storeIds");
		boolean flag = this.storeService.deleteById(ids);
		return flag;
	}

	/**
	 * 根据企业id查询企业信息，跳转到修改页面
	 * 
	 * @param id 企业id
	 * @param type 1：显示提交按钮，2：不显示
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/system/store/jumpUpdatePage/{id}/{type}")
	public String jumpUpdatePage(@PathVariable String id, @PathVariable String type) {

		// 绑定页面显示需要的数据
		getRequest().setAttribute("flag", type);
		getRequest().setAttribute("arealist", areaService.getAllArea());
		getRequest().setAttribute("store", this.storeService.getStoreById(id));

		return "/system/store/store_update";
	}

	/**
	 * 修改企业信息
	 * 
	 * @return 1:成功，2:失败，3:企业logo为空，4:营业执照为空
	 */
	@RequestMapping(value = "/system/store/updateStore")
	@ResponseBody
	public int updateStore(@ModelAttribute AttributeVO vo, HttpServletRequest req, HttpServletResponse resp) {
		// 企业logo
		String logoPath = "/customerfiles/store/" + DateUtil.getNoSpSysDateString() + "/storeLogo/";
		String logo = FileUploadHelper.uploadFile("storeLogo", logoPath, req, resp);
		// 营业执照
		String businessLicensePath = "/customerfiles/store/alepercent";
		String businessLicense = FileUploadHelper.uploadFile("businessLicense", businessLicensePath, req, resp);
		// 信用代码证
		String creditCodeCard = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		creditCodeCard = FileUploadHelper.uploadFile("creditCodeCard", creditCodeCard, req, resp);
		// 公司章程
		String articles = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		articles = FileUploadHelper.uploadFile("articles", articles, req, resp);
		// 公司简介(WORD版)
		String synopsis = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		synopsis = FileUploadHelper.uploadFile("synopsis", synopsis, req, resp);
		if (StringUtil.isEmpty(logo)) {
			return 3;
		}
		if (StringUtil.isEmpty(businessLicense)) {
			return 4;
		}

		vo.getStore().setArticles(articles);
		vo.getStore().setSynopsis(synopsis);
		vo.getStore().setCreditCodeCard(creditCodeCard);
		vo.getStore().setStoreLogo(logo);
		vo.getStore().setBusinessLicense(businessLicense);
		boolean flag = storeService.updateStoreInfo(vo.getUser(), vo.getStore());
		// 修改成功同步更新到监控平台
		// 同步要求企业认证“基础认证”通过
		if (flag) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 分页查询待认证企业
	 */
	@RequestMapping(value = "/system/store/jumpCheckListPage/{type}")
	public String jumpCheckListPage(@PathVariable String type) {
		String storeName = getRequest().getParameter("storeName");
		String currentPageNum = getRequest().getParameter("pageNum");

		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentPageNum)) {
			try {
				currentPage = Integer.parseInt(currentPageNum);
			} catch (Exception e) {
				log.error("error：" + e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);

		return "/system/store/store_check_list";
	}

	/**
	 * 跳转到企业认证页面
	 * 
	 * @param id
	 *            企业id
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/system/store/jumpAuStPage/{id}")
	public String jumpAuStPage(@PathVariable String id, HttpServletRequest req) {
		String flay = req.getParameter("flay");

		// 绑定页面显示需要的数据
		req.setAttribute("store", this.storeService.getStoreById(id));

		if ("1".equals(flay)) {
			return "/system/store/store_check_one";
		} else if ("2".equals(flay)) {
			return "/system/store/store_check_two";
		} else if ("3".equals(flay)) {
			return "/system/store/store_check_three";
		}
		return "error";
	}

	/**
	 * 企业认证操作
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/system/store/checkPassOrRefus", method = RequestMethod.POST)
	@ResponseBody
	public Boolean authStoreInfo(HttpServletRequest req) {
		Manager ma = (Manager) req.getSession().getAttribute(SystemConst.MANAGER);
		String id = req.getParameter("storeId");
		String status = req.getParameter("status");
		String mark = req.getParameter("mark");
		String checknote = req.getParameter("checknote");

		return this.storeService.authStoreInfo(id, status, mark, checknote, ma.getId());
	}

	/**
	 * 分页查询信用测算信息，跳转到列表页
	 * 
	 * @param type
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/system/store/jumpCreditPage/{type}")
	public String jumpCreditPage(@PathVariable String type, HttpServletRequest req) {
		int status;
		try {
			status = Integer.valueOf(req.getParameter("status"));
		} catch (Exception e) {
			status = -1;
		}
		String storeName = req.getParameter("storeName");
		String currentPageNum = req.getParameter("pageNum");

		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentPageNum)) {
			try {
				currentPage = Integer.parseInt(currentPageNum);
			} catch (Exception e) {
				log.error("error：" + e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);

		// 绑定页面显示需要的数据
		req.setAttribute("tempType", type);
		req.setAttribute("pager", pager);
		req.setAttribute("celist", this.creditEstimatesService.getCeList(pager, status, storeName, type));
		req.setAttribute("status", status);
		req.setAttribute("storeName", storeName);

		return "/system/store/credit_list";
	}

	/**
	 * 查询企业信用测算信息
	 * 
	 * @param id
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/system/store/queryCreditInfo/{id}/{type}")
	public String getCreditInfo(@PathVariable String id, @PathVariable String type, HttpServletRequest req) {

		// 绑定页面显示需要的数据
		req.setAttribute("tempType", type);
		req.setAttribute("c", this.creditEstimatesService.getCreditEstimates(id));

		return "/system/store/credit_detail";
	}

	/**
	 * 测算企业信用等级
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/system/store/auCreditInfo", method = RequestMethod.POST)
	@ResponseBody
	public Boolean auCreditInfo(HttpServletRequest req) {
		String id = req.getParameter("ceId");
		String note = req.getParameter("note");

		return this.creditEstimatesService.auCreditInfo(id, note);
	}

	/**
	 * 验证企业名称是否重复
	 * 
	 * @param name
	 *            用户名
	 * @return true：有该用户，false：没有
	 */
	@RequestMapping(value = "/system/store/checkStoreName", method = RequestMethod.POST)
	@ResponseBody
	public Boolean checkStoreName(HttpServletRequest req) {
		String storeName = req.getParameter("storeName");

		return this.storeService.checkStoreName(storeName);
	}

	/**
	 * 修改时验证企业名称是否重复
	 * 
	 * @param req
	 * @return true：有该用户，false：没有
	 */
	@RequestMapping(value = "/system/store/checkStoreNameUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Boolean checkStoreNameUpdate(HttpServletRequest req) {
		String oldStoreName = req.getParameter("oldStoreName");
		String newStoreName = req.getParameter("newStoreName");

		return this.storeService.checkStoreNameUpdate(oldStoreName, newStoreName);
	}

	/**
	 * 查询企业列表
	 * 
	 * @param type
	 *            0：个人会员，1：供应商，2：采购商，3：核心企业
	 * @param req
	 */
	private void storeListPage(String type, HttpServletRequest req, Integer mark) {
		String currentPageNum = req.getParameter("pageNum");
		int status;
		try {
			status = Integer.valueOf(req.getParameter("status"));
		} catch (Exception e) {
			status = -1;
		}
		String storeName = req.getParameter("storeName");
		String legalMobile = req.getParameter("legalMobile");
		String mail = req.getParameter("email");
		String userName = req.getParameter("userName");

		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentPageNum)) {
			try {
				currentPage = Integer.parseInt(currentPageNum);
			} catch (Exception e) {
				log.error("error：" + e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);

		List<Store> list = this.storeService.getStoreList(pager, status, storeName, legalMobile, mail, userName, type, mark);

		// 绑定页面显示需要的数据
		req.setAttribute("stores", list);
		req.setAttribute("pager", pager);
		req.setAttribute("status", status);
		req.setAttribute("storeName", storeName);
		req.setAttribute("legalMobile", legalMobile);
		req.setAttribute("email", mail);
		req.setAttribute("userName", userName);
		req.setAttribute("tempType", type);
	}

	/**
	 * 新增商品时查询店铺列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/store/plist")
	public String plist(HttpServletRequest request) {
		try {
			// 定义分页
			Pager pager = new Pager();
			// 从页面获取店铺名称
			String name = request.getParameter("storeName");
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
			// 利用Service的方法查找店铺(仅查询核心企业)
			List<Store> stores = storeService.listStore(pager, null, true, name, null, null, null);
			request.setAttribute("stores", stores);
			request.setAttribute("pager", pager);
			request.setAttribute("pageNum", currentpageStr);
			request.setAttribute("name", name);
			return "/system/product/productAndStore";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: 发布时选择企业的
	 * @Description: TODO()
	 * 详细业务流程:
	 * (详细描述此方法相关的业务处理流程)
	 * @param 
	 * @return 
	 * @throws
	 */
	@RequestMapping(value = "/system/store/plistSearch/{pageNum}")
	public String pStoreList(@PathVariable String pageNum, HttpServletRequest request) {
		String name = request.getParameter("name");
		// 定义分页
		Pager pager = new Pager();
		pager.setCurrentPage(Integer.parseInt(pageNum));

		// 利用Service的方法查找店铺(仅查询核心企业)
		List<Store> stores = storeService.listStore(pager, null, true, name, null, null, null);
		request.setAttribute("stores", stores);
		request.setAttribute("pager", pager);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("name", name);
		return "/system/product/productAndStore";
	}
}
