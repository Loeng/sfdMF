package com.ekfans.controllers.store.contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.ICarInfoService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ContractCars;
import com.ekfans.base.wfOrder.model.ContractContent;
import com.ekfans.base.wfOrder.service.IContractContentService;
import com.ekfans.base.wfOrder.service.IContractDetailsService;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 合同处理Controller
 * 
 * @ClassName: ContractController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Ekfans_zhangJin
 * @date 2015-1-12 上午11:36:53
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class ContractController extends BasicController {
	@Autowired
	private IContractService contractService;
	@Autowired
	private IContractDetailsService cdService;
	@Autowired
	private IContractContentService ccService;
	@Autowired
	private IStoreService storeService;

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增合同页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/skipAddContract/{viewType}")
	public String skipAddContract(@PathVariable String viewType, HttpServletRequest request, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		User user = (User) session.getAttribute(SystemConst.USER);
		request.setAttribute("store", store);
		request.setAttribute("user", user);
		request.setAttribute("viewType", viewType);
		return "/userCenter/store/contract/qiyue";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增合同页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/contract/add/loadPw")
	public String contractLoadPw(HttpServletRequest request, HttpSession session) {
		String contractId = request.getParameter("contractId");
		String bjStatus = request.getParameter("bjStatus");
		List<ContractContent> contents = ccService.findContractContentWithChilds(contractId);
		Contract contract = contractService.getContractById(contractId);
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		request.setAttribute("contract", contract);
		request.setAttribute("contents", contents);
		request.setAttribute("bjStatus", bjStatus);
		request.setAttribute("store", store);
		return "/userCenter/store/contract/qiyueContentAddLoad";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增合同页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/contract/add/loadCars")
	public String contractLoadCars(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		String contractId = request.getParameter("contractId");
		String bjStatus = request.getParameter("bjStatus");
		String storeId = request.getParameter("storeId");
		ICarInfoService carInfoService = SpringContextHolder.getBean(ICarInfoService.class);
		List<CarInfo> carInfoList = carInfoService.getCheckCarsByStoreId(storeId);
		List<ContractCars> cars = contractService.getCarsByContract(contractId);
		Map<String, ContractCars> conCars = new HashMap<String, ContractCars>();
		if (cars != null && cars.size() > 0) {
			for (ContractCars car : cars) {
				if (car != null) {
					conCars.put(car.getCarInfoId(), car);
				}
			}
		}

		request.setAttribute("storeId", storeId);
		request.setAttribute("carInfoList", carInfoList);
		request.setAttribute("contractCars", conCars);
		request.setAttribute("bjStatus", bjStatus);
		return "/userCenter/store/contract/qiyueCarAddLoad";
	}

	/**
	 * @Title: findStoreListByStoreName
	 * @Description: TODO(根据企业名称查询企业) 详细业务流程:根据企业名称模糊匹配企业 (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/contract/findListByStoreName")
	public String findStoreListByStoreName(HttpServletRequest request) {
		// 获取企业名称
		String storeName = request.getParameter("storeName");
		// 获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 获取选择合同甲方还是 乙方
		String storeNum = request.getParameter("storeNumId");
		String htType = request.getParameter("htType");
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
		pager.setRowsPerPage(9);
		List<Store> storeList = storeService.getListStoreByStoreName(pager, storeName, storeNum, htType);
		request.setAttribute("storeList", storeList);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("pager", pager);
		request.setAttribute("storeName", storeName);
		request.setAttribute("storeNumId", storeNum);
		request.setAttribute("htType", htType);
		if (storeNum.equals("1")) {
			return "userCenter/store/contract/storeListFirst";
		} else {
			return "userCenter/store/contract/storeListSecond";
		}
	}

	/**
	 * @Title: addContract
	 * @Description: TODO(保存合同) 详细业务流程:用户添加好合同,提交保存 (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param response
	 * @param session
	 * @return String 返回结果页面
	 * @throws
	 */
	@RequestMapping(value = "/store/contract/contractAdd")
	public String addContract(@ModelAttribute Contract contract, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (contractService.saveContract(request, response, contract)) {
			request.setAttribute("addOk", "0");
		} else {
			request.setAttribute("addOk", "1");
		}
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		User user = (User) session.getAttribute(SystemConst.USER);
		String viewType = request.getParameter("viewType");
		request.setAttribute("store", store);
		request.setAttribute("user", user);
		request.setAttribute("viewType", viewType);
		return "/userCenter/store/contract/qiyue";
	}

	/**
	 * @Title: addContract
	 * @Description: TODO(更新合同) 详细业务流程:用户更新合同,提交保存 (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param response
	 * @param session
	 * @return String 返回结果页面
	 * @throws
	 */
	@RequestMapping(value = "/store/contract/contractUpdate")
	public String updateContract(Contract contract, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String typeId = request.getParameter("typeId");

		if (contractService.updateContract(request, response, contract)) {
			request.setAttribute("updateOk", "0");
		} else {
			request.setAttribute("updateOk", "1");
		}
		String viewType = request.getParameter("viewType");
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		request.setAttribute("store", store);
		request.setAttribute("viewType", viewType);
		request.setAttribute("typeId", typeId);
		request.setAttribute("contract", contract);
		return "/userCenter/store/contract/qiyueModify";
	}

	/**
	 * @Title: checkContract
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:发布合同对方审核合同 (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/contract/checkContract/{id}")
	@ResponseBody
	public String checkContract(@PathVariable String id) {
		if (!StringUtil.isEmpty(id)) {
			try {
				Contract con = contractService.getContractById(id);
				con.setContractCheckStatus("1");
				contractService.updateContract(con);
				return "0";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "1";
	}

	/**
	 * @Title: getContractById
	 * @Description: TODO(根据ID查询或编辑合同) 详细业务流程:支持前台合同列表的查看、编辑 (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */

	@RequestMapping(value = "/store/contract/updateContract/{contractId}")
	@ResponseBody
	public String updateYsContract(@PathVariable String contractId, HttpServletRequest request, HttpSession session) {
		if (contractService.updateYsContract(contractId, request)) {
			return "1";
		} else {
			return "0";
		}
	}

	@RequestMapping(value = "/store/jumdmentContractByStore/kong")
	@ResponseBody
	public int jumdmentContractByStore() {
		String mark = getRequest().getParameter("mark"); // 1:运输合同，2:销售合同
		String jStoreName = getRequest().getParameter("jStoreName"); // 甲方
		String yStoreName = getRequest().getParameter("yStoreName"); // 乙方

		return storeService.jumdmentContractByStore(mark, jStoreName, yStoreName) ? 1 : 2;
	}

	/**
	 * @Title: contractList
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:查询登陆用户合同 (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param session
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/contractList/{viewType}")
	public String contractList(@PathVariable String viewType, HttpServletRequest request, HttpSession session) {
		// 获取登录用户
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		// 获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 获取甲方名称
		String firstName = request.getParameter("firstName");
		// 获取乙方名称
		String secondName = request.getParameter("secondName");
		// 获取开始时间
		String startTime = request.getParameter("startTime");
		// 获取结束时间
		String endTime = request.getParameter("endTime");
		// 获取合同类型
		String type = request.getParameter("type");
		// 获取合同名称
		String contractName = request.getParameter("name");
		// 获取合同编号
		String contractNo = request.getParameter("contractNo");
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
		// 分页默认20条数据
		pager.setRowsPerPage(10);

		List<Contract> contractList = contractService.getContractByParams(pager, store.getId(), contractName, firstName, secondName, startTime, endTime, type, contractNo, viewType);
		request.setAttribute("contractList", contractList);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("firstName", firstName);
		request.setAttribute("secondName", secondName);
		request.setAttribute("contractName", contractName);
		request.setAttribute("type", type);
		request.setAttribute("endTime", endTime);
		request.setAttribute("contractNo", contractNo);
		request.setAttribute("startTime", startTime);
		request.setAttribute("storeId", store.getId());
		request.setAttribute("viewType", viewType);
		return "/userCenter/store/contract/qiyueList";
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/contract/ysContrants")
	public String getYsContractsById(HttpServletRequest request, HttpSession session) {

		Store store = (Store) session.getAttribute(SystemConst.STORE);

		// 获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 获取甲方名称
		String firstName = request.getParameter("firstName");
		// 获取乙方名称
		String secondName = request.getParameter("secondName");
		// 获取开始时间
		String startTime = request.getParameter("startTime");
		// 获取结束时间
		String endTime = request.getParameter("endTime");
		// 获取合同名称
		String contractName = request.getParameter("name");
		// 获取合同编号
		String contractNo = request.getParameter("contractNo");
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
		// 分页默认20条数据
		pager.setRowsPerPage(10);
		List<Contract> contractList = contractService.getContracts(pager, store.getId(), contractName, firstName, secondName, startTime, endTime, "1", "1", contractNo, null);
		request.setAttribute("contractList", contractList);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("firstName", firstName);
		request.setAttribute("secondName", secondName);
		request.setAttribute("contractName", contractName);
		request.setAttribute("endTime", endTime);
		request.setAttribute("contractNo", contractNo);
		request.setAttribute("startTime", startTime);
		request.setAttribute("storeId", store.getId());
		request.setAttribute("contractList", contractList);
		return "/userCenter/store/contract/ysContractList";
	}

	/**
	 * @Title: getContractById
	 * @Description: TODO(根据ID查询或编辑合同) 详细业务流程:支持前台合同列表的查看、编辑 (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */

	@RequestMapping(value = "/store/contract/contractById/{typeId}/{contractId}/{viewType}")
	public String getContractById(@PathVariable String typeId, @PathVariable String contractId, @PathVariable String viewType, HttpServletRequest request, HttpSession session) {
		// 得到合同对象
		Contract con = contractService.getContractById(contractId);
		// 得到登陆的企业
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		request.setAttribute("viewType", viewType);
		// 获取关联的运输合同集合
		List<Contract> ysContracts = contractService.getYsContractsByContractId(con.getId());

		// 传到页面的值
		request.setAttribute("contract", con);
		request.setAttribute("ysContracts", ysContracts);
		request.setAttribute("typeId", typeId);
		request.setAttribute("store", store);

		return "/userCenter/store/contract/qiyueModify";
	}

	@RequestMapping(value = "/system/contract/contractById/{typeId}/{contractId}")
	public String systemGetContractById(@PathVariable String typeId, @PathVariable String contractId, HttpServletRequest request, HttpSession session) {
		// 同上
		Contract con = contractService.getContractById(contractId);
		List<ContractContent> cclist = ccService.findContractContentWithChilds(contractId);
		request.setAttribute("contract", con);
		request.setAttribute("typeId", typeId);
		request.setAttribute("ccList", cclist);
		return "/system/contract/systemqiyueModify";
	}

	@RequestMapping(value = "/contract/deleteContractById/{contractId}")
	public String delContract(@PathVariable String contractId, HttpServletRequest request, HttpSession session) {
		boolean con = contractService.deleteContractById(contractId);
		if (con == true) {
			request.setAttribute("flag", "success");
		} else {
			request.setAttribute("flag", "fail");
		}
		return "redirect:/store/contractList";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增合同页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/contract/add/loadPw")
	public String systemContractLoadPw(HttpServletRequest request, HttpSession session) {
		String contractId = request.getParameter("contractId");
		String bjStatus = request.getParameter("bjStatus");
		List<ContractContent> contents = ccService.findContractContentWithChilds(contractId);
		Contract contract = contractService.getContractById(contractId);
		Store store = storeService.getStore(contract.getCreatorId());
		request.setAttribute("contract", contract);
		request.setAttribute("contents", contents);
		request.setAttribute("bjStatus", bjStatus);
		request.setAttribute("store", store);
		return "/userCenter/store/contract/qiyueContentAddLoad";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增合同页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/contract/add/loadCars")
	public String systemContractLoadCars(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		String contractId = request.getParameter("contractId");
		String bjStatus = request.getParameter("bjStatus");
		String storeId = request.getParameter("storeId");
		ICarInfoService carInfoService = SpringContextHolder.getBean(ICarInfoService.class);
		List<CarInfo> carInfoList = carInfoService.getCheckCarsByStoreId(storeId);
		List<ContractCars> cars = contractService.getCarsByContract(contractId);
		Map<String, ContractCars> conCars = new HashMap<String, ContractCars>();
		if (cars != null && cars.size() > 0) {
			for (ContractCars car : cars) {
				if (car != null) {
					conCars.put(car.getCarInfoId(), car);
				}
			}
		}

		request.setAttribute("storeId", storeId);
		request.setAttribute("carInfoList", carInfoList);
		request.setAttribute("contractCars", conCars);
		request.setAttribute("bjStatus", bjStatus);
		return "/userCenter/store/contract/qiyueCarAddLoad";
	}

	/**
	 * @Title: delContract
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据ID删除合同 (详细描述此方法相关的业务处理流程)
	 * @param contractId
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/contract/delContractById/{contractId}")
	@ResponseBody
	public String delContract(@PathVariable String contractId) {
		if (!StringUtil.isEmpty(contractId)) {
			if (contractService.deleteContractById(contractId)) {
				return "0";
			}
		}
		return "1";
	}

	/**
	 * @Title: delContract
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据ID删除合同 (详细描述此方法相关的业务处理流程)
	 * @param contractId
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/contract/ContractNo/{contractNo}")
	@ResponseBody
	public String ContractNo(@PathVariable String contractNo) {
		if (!StringUtil.isEmpty(contractNo)) {
			if (!contractService.isContractNo(contractNo)) {
				return "0";
			}
		}
		return "1";
	}

	/*************************** 后台合同管理 **************************************************************/
	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到就平台新增合同页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/skip/ContractAdd")
	public String systemSkipAddContract(HttpServletRequest request, HttpSession session) {
		Manager man = (Manager) session.getAttribute(SystemConst.MANAGER);
		request.setAttribute("storeId", man.getId());
		return "/system/contract/systemqiyue";
	}

	/**
	 * @Title: addContract
	 * @Description: TODO(保存合同) 详细业务流程:用户添加好合同,提交保存 (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param response
	 * @param session
	 * @return String 返回结果页面
	 * @throws
	 */
	@RequestMapping(value = "/system/contract/contractAdd")
	public String systemAddContract(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 获取页面信息
		Contract con = new Contract();
		// 设置合同名称
		con.setName(request.getParameter("name"));
		// 设置合同开始时间
		con.setStartTime(request.getParameter("startTime"));
		// 设置合同结束时间
		con.setEndTime(request.getParameter("endTime"));
		// 设置合同类型
		con.setType(request.getParameter("type"));
		// 设置合同甲方
		con.setFirstId(request.getParameter("storeIdfirstId"));
		// 设置合同乙方
		con.setSecondId(request.getParameter("storeIdSecondId"));
		// 设置合同附件
		con.setFile(request.getParameter("file"));
		// 设置合同备注
		con.setNote(request.getParameter("note"));
		// 获取登录用户名称
		Manager man = (Manager) session.getAttribute(SystemConst.MANAGER);
		// 设置创建人
		con.setCreatorId(man.getId());
		// 设置创建时间
		con.setCreateTime(DateUtil.getSysDateTimeString());
		// 设置更新时间
		con.setUpdateTime(DateUtil.getSysDateTimeString());
		// 设置组织结构ID
		String contractPDF = "/customerfiles/" + man.getId() + "/contract/" + DateUtil.getNoSpSysDateString() + "/";
		// 获取PDF文件路径转换为服务端路径
		contractPDF = FileUploadHelper.uploadFile("contractPDF", contractPDF, request, response);
		con.setFile(contractPDF);
		if (contractService.addContract(con)) {
			request.setAttribute("addOk", "0");
		} else {
			request.setAttribute("addOk", "1");
		}
		return "/system/contract/systemqiyue";
	}

	/**
	 * @Title: contractList
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:后台查询登陆用户合同 (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param session
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/contractList")
	public String systemContractList(HttpServletRequest request, HttpSession session) {
		// 获取登录用户
		Manager man = (Manager) session.getAttribute(SystemConst.MANAGER);
		// 获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 获取甲方名称
		String firstName = request.getParameter("firstName");
		// 获取乙方名称
		String secondName = request.getParameter("secondName");
		// 获取开始时间
		String startTime = request.getParameter("startTime");
		// 获取结束时间
		String endTime = request.getParameter("endTime");
		// 获取合同类型
		String type = request.getParameter("type");
		// 获取合同名称
		String contractName = request.getParameter("name");
		// 获取合同编号
		String contractNo = request.getParameter("contractNo");
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
		// 分页默认20条数据
		pager.setRowsPerPage(10);
		List<Contract> contractList = contractService.getContractByParams(pager, "", contractName, firstName, secondName, startTime, endTime, type, contractNo, null);
		request.setAttribute("contractList", contractList);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("firstName", firstName);
		request.setAttribute("secondName", secondName);
		request.setAttribute("contractName", contractName);
		request.setAttribute("type", type);
		request.setAttribute("contractNo", contractNo);
		request.setAttribute("endTime", endTime);
		request.setAttribute("startTime", startTime);
		request.setAttribute("storeId", man.getId());
		return "/system/contract/systemqiyueList";
	}

	/**
	 * @Title: findStoreListByStoreName
	 * @Description: TODO(根据企业名称查询企业) 详细业务流程:根据企业名称模糊匹配企业(后台) (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/contract/findListByStoreName")
	public String systemfindStoreListByStoreName(HttpServletRequest request) {
		// 获取企业名称
		String storeName = request.getParameter("storeName");
		// 获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 获取选择合同甲方还是 乙方
		String storeNum = request.getParameter("storeNumId");
		String htType = request.getParameter("htType");
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
		pager.setRowsPerPage(9);
		List<Store> storeList = storeService.getListStoreByStoreName(pager, storeName, "", htType);
		request.setAttribute("storeList", storeList);
		request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("pager", pager);
		request.setAttribute("storeName", storeName);
		request.setAttribute("storeNumId", storeNum);
		request.setAttribute("htType", htType);
		if (storeNum.equals("1")) {
			return "/system/contract/systemstoreListFirst";
		} else {
			return "/system/contract/systemstoreListSecond";
		}
	}

	public static void main(String[] args) {
		String str = "370784 20150100 31";
		str = str.replace(" ", "");
		System.out.println(str);
	}
}
