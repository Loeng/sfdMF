package com.ekfans.controllers.store.contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccreditService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ScrapWfp;
import com.ekfans.base.wfOrder.model.ScrapWfpTrans;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.base.wfOrder.service.IScrapWfpService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
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
public class ScrapWfpController extends BasicController {
	@Autowired
	private IContractService contractService;
	@Autowired
	private IScrapWfpService wfpService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IAccreditService accreditService;

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/skipAdd/{viewType}/{contractId}")
	public String addScrapWfp(@PathVariable String viewType, @PathVariable String contractId, HttpServletRequest request, HttpSession session) {
		String wfpId = request.getParameter("wfpId");
		if (!StringUtil.isEmpty(wfpId)) {
			ScrapWfp wfp = wfpService.getScrapWfpById(wfpId);
			request.setAttribute("scrapWfp", wfp);
		}
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		User user = (User) session.getAttribute(SystemConst.USER);
		if (!StringUtil.isEmpty(contractId)) {
			Contract contract = contractService.getContractById(contractId);
			request.setAttribute("contract", contract);
		}

		request.setAttribute("store", store);
		request.setAttribute("user", user);
		request.setAttribute("viewType", viewType);
		request.setAttribute("orgId", Cache.getResource("orgId"));
		request.setAttribute("sign", Cache.getResource("key"));
		request.setAttribute("lineUrl", Cache.getResource("lineUrl"));
		return "/userCenter/store/contract/scrapWfpAdd";
	}
	

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/skipEdit/{viewType}/{contractId}/{wfpId}")
	public String editScrapWfp(@PathVariable String viewType, @PathVariable String contractId, @PathVariable String wfpId, HttpServletRequest request, HttpSession session) {
		if (!StringUtil.isEmpty(wfpId)) {
			ScrapWfp wfp = wfpService.getScrapWfpById(wfpId);
			request.setAttribute("scrapWfp", wfp);
		}
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		User user = (User) session.getAttribute(SystemConst.USER);
		if (!StringUtil.isEmpty(contractId)) {
			Contract contract = contractService.getContractById(contractId);
			request.setAttribute("contract", contract);
		}
		request.setAttribute("pageType", "edit");
		request.setAttribute("store", store);
		request.setAttribute("user", user);
		request.setAttribute("viewType", viewType);
		return "/userCenter/store/contract/scrapWfpAdd";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/skipReload/{viewType}/{contractId}/{wfpId}")
	public String reloadScrapWfp(@PathVariable String viewType, @PathVariable String contractId, @PathVariable String wfpId, HttpServletRequest request, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		if (!StringUtil.isEmpty(wfpId)) {
			ScrapWfp wfp = wfpService.getScrapWfpById(wfpId);
			contractId = wfp.getContractId();
			wfp.setParentId(wfp.getId());
			wfp.setCreator(store.getId());
			wfp.setCheckStatus("0");
			wfp.setCheckNote("");
			wfp.setId("");
			request.setAttribute("scrapWfp", wfp);
		}
		User user = (User) session.getAttribute(SystemConst.USER);
		if (!StringUtil.isEmpty(contractId)) {
			Contract contract = contractService.getContractById(contractId);
			request.setAttribute("contract", contract);
		}
		request.setAttribute("store", store);
		request.setAttribute("user", user);
		request.setAttribute("viewType", viewType);
		return "/userCenter/store/contract/scrapWfpAdd";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/skipView/{viewType}/{wfpId}")
	public String viewScrapWfp(@PathVariable String viewType, @PathVariable String wfpId, HttpServletRequest request, HttpSession session) {
		ScrapWfp wfp = wfpService.getScrapWfpById(wfpId);
		List<ScrapWfpTrans> childs = wfpService.getTransByWfpId(wfp.getId());
		List<ScrapWfpTrans> trans = new ArrayList<ScrapWfpTrans>();
		for (int i = 0; i < childs.size(); i++) {
			ScrapWfpTrans scratTrans = childs.get(i);
			if (scratTrans != null) {
				Store transStore = storeService.getStore(scratTrans.getTransId());
				Accredit transAcc = accreditService.getAccreditByStoreAndType(transStore.getId(), "2");
				transStore.setTransAccredit(transAcc);
				scratTrans.setTransStore(transStore);
				trans.add(scratTrans);
			}
		}

		Store buyStore = storeService.getStore(wfp.getBuyId());
		Accredit buyAcc = accreditService.getAccreditByStoreAndType(buyStore.getId(), "0");
		buyStore.setBuyerAccredit(buyAcc);
		Store salStore = storeService.getStore(wfp.getSalId());
		wfp.setChildList(trans);
		request.setAttribute("salStore", salStore);
		request.setAttribute("buyStore", buyStore);
		request.setAttribute("scrapWfp", wfp);
		request.setAttribute("viewType", viewType);
		return "/userCenter/store/contract/scrapWfpView";
	}

	@RequestMapping(value = "/store/wfpscrap/skipSave")
	@ResponseBody
	public Boolean saveScrapWfp(ScrapWfp wfpScrap, HttpServletRequest request, HttpServletResponse response) {
		return wfpService.SaveOrUpdate(wfpScrap, request, response);
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/skipDel/{viewType}/{scrapWfpId}")
	public String removeScrapWfp(@PathVariable String viewType, @PathVariable String scrapWfpId, HttpServletRequest request, HttpSession session) {
		String contractId = request.getParameter("contractId");
		Boolean delStatus = wfpService.deleteScrapWfp(scrapWfpId, request);

		request.setAttribute("delStatus", delStatus + "");
		Pager pager = new Pager(1);
		pager.setRowsPerPage(20);
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		List<ScrapWfp> wfps = wfpService.getWfpList(contractId, null, null, null, null, store.getId(), viewType, pager);
		request.setAttribute("scrapWfpList", wfps);
		request.setAttribute("contractId", contractId);
		request.setAttribute("viewType", viewType);
		request.setAttribute("pager", pager);
		request.setAttribute("pageNum", 1);
		return "/userCenter/store/contract/scrapWfpList";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/loadTransContracts/{contractId}")
	public String scrapWfpTransContracts(@PathVariable String contractId, HttpServletRequest request, HttpSession session) {
		Contract contract = contractService.getContractById(contractId);
		Store buyStore = storeService.getStore(contract.getSecondId());
		Store salStore = storeService.getStore(contract.getFirstId());

		String scrapWfpId = request.getParameter("scrapWfpId");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (!StringUtil.isEmpty(scrapWfpId)) {
			List<ScrapWfpTrans> transList = wfpService.getTransByWfpId(scrapWfpId);
			if (transList != null && transList.size() > 0) {

				for (int i = 0; i < transList.size(); i++) {
					ScrapWfpTrans trans = transList.get(i);
					if (trans == null) {
						continue;
					}
					if (!StringUtil.isEmpty(trans.getTransContractId())) {
						map.put(trans.getTransContractId(), true);
					} else {
						map.put(trans.getTransId(), true);
					}
				}
			}
		}
		request.setAttribute("transMap", map);
		List<Contract> contracts = contractService.getYsContractsByContractId(contractId);
		request.setAttribute("transCons", contracts);
		request.setAttribute("buyStore", buyStore);
		request.setAttribute("salStore", salStore);
		return "/userCenter/store/contract/scrapTransContracts";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/loadSalStore/{storeId}")
	public String scrapWfpSalStore(@PathVariable String storeId, HttpServletRequest request, HttpSession session) {
		String wfpId = request.getParameter("wfpId");
		if (!StringUtil.isEmpty(wfpId)) {
			ScrapWfp wfp = wfpService.getScrapWfpById(wfpId);
			request.setAttribute("salWfp", wfp);
		}
		Store store = storeService.getStore(storeId);
		Accredit acc = accreditService.getAccreditByStoreAndType(storeId, "1");
		store.setSalerAccredit(acc);
		request.setAttribute("salStore", store);
		return "/userCenter/store/contract/scrapSalInfo";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/loadBuyStore/{storeId}")
	public String scrapWfpBuyStore(@PathVariable String storeId, HttpServletRequest request, HttpSession session) {
		String wfpId = request.getParameter("wfpId");
		if (!StringUtil.isEmpty(wfpId)) {
			ScrapWfp wfp = wfpService.getScrapWfpById(wfpId);
			request.setAttribute("buyWfp", wfp);
		}

		Store store = storeService.getStore(storeId);
		Accredit acc = accreditService.getAccreditByStoreAndType(storeId, "0");
		store.setBuyerAccredit(acc);
		request.setAttribute("buyStore", store);
		return "/userCenter/store/contract/scrapBuyInfo";
	}

	/**
	 * @Title: skipAddContract
	 * @Description: TODO(跳转到新增申报页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/wfpscrap/loadTransStores")
	public String scrapWfpTransStores(HttpServletRequest request, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		String wfpId = request.getParameter("wfpId");
		String storeIds = request.getParameter("transStoreIds");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		List<Store> stores = new ArrayList<Store>();
		if (!StringUtil.isEmpty(storeIds)) {
			if (!StringUtil.isEmpty(storeIds)) {
				String[] ids = storeIds.split(";");
				for (int i = 0; i < ids.length; i++) {
					String storeId = ids[i];
					if (!StringUtil.isEmpty(storeId) && !map.containsKey(storeId)) {
						Store transStore = storeService.getStore(storeId);
						Accredit acc = accreditService.getAccreditByStoreAndType(transStore.getId(), "2");
						transStore.setTransAccredit(acc);
						stores.add(transStore);
						if (transStore.getId().equals(store.getId())) {
							session.setAttribute(SystemConst.STORE, transStore);
						}
						map.put(storeId, true);
					}
				}
			}
		}

		Map<String, ScrapWfpTrans> transMap = new HashMap<String, ScrapWfpTrans>();
		if (!StringUtil.isEmpty(wfpId)) {
			List<ScrapWfpTrans> transList = wfpService.getTransByWfpId(wfpId);
			if (transList != null && transList.size() > 0) {

				for (int i = 0; i < transList.size(); i++) {
					ScrapWfpTrans trans = transList.get(i);
					if (trans == null) {
						continue;
					}
					transMap.put(trans.getTransId(), trans);
				}
			}
		}
		request.setAttribute("transStoreMap", transMap);

		// if (!map.containsKey(store.getId()) &&
		// "3".equals(store.getTransStatus())) {
		// Store transStore = storeService.getStore(store.getId());
		// Accredit acc =
		// accreditService.getAccreditByStoreAndType(transStore.getId(), "2");
		// transStore.setTransAccredit(acc);
		// stores.add(transStore);
		// session.setAttribute(SystemConst.STORE, transStore);
		// }

		request.setAttribute("transStores", stores);
		return "/userCenter/store/contract/scrapTransInfo";
	}

	/**
	 * 在线申报列表
	 * 
	 * @param viewType
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/store/wfpscrap/skipScrapList/{viewType}")
	public String scrapWfpList(@PathVariable String viewType, HttpServletRequest request, HttpServletResponse response) {
		String contractId = request.getParameter("contractId");
		String contractName = request.getParameter("contractName");
		String wfName = request.getParameter("wfName");
		String checkStatus = request.getParameter("checkStatus");
		String contractNo = request.getParameter("contractNo");
		String pageNum = request.getParameter("pageNum");
		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(pageNum);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Pager pager = new Pager(pageNo);
		pager.setRowsPerPage(20);
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		request.getSession().setAttribute(SystemConst.STORE, store);
		List<ScrapWfp> wfps = wfpService.getWfpList(contractId, contractName, contractNo, wfName, checkStatus, store.getId(), viewType, pager);

		request.setAttribute("scrapWfpList", wfps);

		request.setAttribute("contractId", contractId);
		request.setAttribute("contractName", contractName);
		request.setAttribute("wfName", wfName);
		request.setAttribute("checkStatus", checkStatus);
		request.setAttribute("contractNo", contractNo);
		request.setAttribute("viewType", viewType);
		request.setAttribute("pager", pager);
		request.setAttribute("pageNum", pageNum);
		return "/userCenter/store/contract/scrapWfpList";
	}

	@RequestMapping(value = "/store/wfpscrap/changeContract/{viewType}/{scrapId}")
	public String changeContract(@PathVariable String viewType, @PathVariable String scrapId, HttpServletRequest request, HttpServletResponse response) {
		ScrapWfp wfp = wfpService.getScrapWfpById(scrapId);
		List<Contract> transContracts = contractService.getYsContractsByContractId(wfp.getContractId());
		request.setAttribute("scrapWfp", wfp);
		request.setAttribute("transContracts", transContracts);
		request.setAttribute("viewType", viewType);
		return "/userCenter/store/contract/changeContract";
	}

	@RequestMapping(value = "/store/wfpscrap/changeSave")
	@ResponseBody
	public Boolean changeContractSave(HttpServletRequest request, HttpServletResponse response) {
		return wfpService.changeTrans(request, response);
	}

	@RequestMapping(value = "/store/wfpscrap/loadWfps")
	public String loadScrapWfps(HttpServletRequest request) {
		String loadContractId = request.getParameter("loadContractId");
		String loadContractName = request.getParameter("loadContractName");
		String loadWfName = request.getParameter("loadWfName");
		String loadContractNo = request.getParameter("loadContractNo");
		String loadPageNum = request.getParameter("loadPageNum");
		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(loadPageNum);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Pager pager = new Pager(pageNo);
		pager.setRowsPerPage(20);
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		List<ScrapWfp> wfps = wfpService.getWfpListLoad(loadContractId, loadContractName, loadContractNo, loadWfName, "1", store.getId(), null, pager);
		request.setAttribute("scrapWfpList", wfps);
		request.setAttribute("loadContractId", loadContractId);
		request.setAttribute("loadContractName", loadContractName);
		request.setAttribute("loadWfName", loadWfName);
		request.setAttribute("loadContractNo", loadContractNo);
		request.setAttribute("pager", pager);
		request.setAttribute("loadPageNum", loadPageNum);
		return "/userCenter/store/contract/scrapWfpLoads";
	}
}
