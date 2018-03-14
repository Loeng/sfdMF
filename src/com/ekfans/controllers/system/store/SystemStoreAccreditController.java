package com.ekfans.controllers.system.store;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.Wfpys;
import com.ekfans.base.store.service.IAccreditService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.store.service.IWfpysService;
import com.ekfans.base.store.util.AccreditHelper;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemStoreAccreditController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SystemStoreAccreditController.class);
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IAccreditService accreditService;
	@Autowired
	private ISystemAreaService areaService;
	@Autowired
	private IWfpysService wfpysService;

	/**
	 * 跳转到（处置，产生，流通）资质认证列表页面
	 */
	@RequestMapping(value = "/system/store/zizhi/jumplist/{rzType}")
	public String jumpStoreBasisPage(@PathVariable String rzType) {
		String storeName = getRequest().getParameter("storeName");
		String email = getRequest().getParameter("email");
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

		getRequest().setAttribute("slist", storeService.getStoreUnCheckList(rzType, storeName, email, pager));
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("storeName", storeName);
		getRequest().setAttribute("email", email);
		getRequest().setAttribute("rzType", rzType);

		return "/system/store/auth/storeZizhiList";
	}

	/**
	 * 根据资质类型和企业id获取未认证的资质和对应的证明文件，跳转到资质认证页面
	 * 
	 * @param type
	 *            0:采购资质,1:销售资质,2:运输资质
	 * @param id
	 *            企业id
	 */
	@RequestMapping(value = "/system/store/zizhi/jumpZiZhiPage/{type}/{id}")
	public String jumpZiZhiPage(@PathVariable String type, @PathVariable String id) {
		// getRequest().setAttribute("silist", storeIntelService.getUnZiZhi(id,
		// type));
		Store store = storeService.getStore(id);
		getRequest().setAttribute("Store", store);
		getRequest().setAttribute("rzType", type);
		return "/system/store/auth/storeZizhiCheck";
	}

	/**
	 * load得出危险废物经营许可资质
	 */
	@RequestMapping(value = "/system/auth/jumpIntelPage/wfzz/load/{storeId}")
	public String wfzzLoad(@PathVariable String storeId) {
		Accredit accredit = accreditService.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_WF);
		if (accredit != null && !StringUtil.isEmpty(accredit.getAreaId())) {
			accredit.setAreaFullName(areaService.getAreaFullNameByAreaIdAndDelimiter(accredit.getAreaId(), ""));
		}
		getRequest().setAttribute("wfAccredit", accredit);
		return "/system/store/auth/zizhiWFload";
	}

	/**
	 * load得出排放污染物许可资质
	 */
	@RequestMapping(value = "/system/auth/jumpIntelPage/pwzz/load/{storeId}")
	public String pwzzLoad(@PathVariable String storeId) {
		Accredit accredit = accreditService.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_PW);
		if (accredit != null && !StringUtil.isEmpty(accredit.getAreaId())) {
			accredit.setAreaFullName(areaService.getAreaFullNameByAreaIdAndDelimiter(accredit.getAreaId(), ""));
		}
		getRequest().setAttribute("pwAccredit", accredit);
		return "/system/store/auth/zizhiPWload";
	}

	/**
	 * load得出所有资质
	 */
	@RequestMapping(value = "/system/auth/jumpIntelPage/yszz/load/{storeId}")
	public String yszzLoad(@PathVariable String storeId) {
		Accredit accredit = accreditService.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_YS);
		if (accredit != null && !StringUtil.isEmpty(accredit.getAreaId())) {
			accredit.setAreaFullName(areaService.getAreaFullNameByAreaIdAndDelimiter(accredit.getAreaId(), ""));
		}
		getRequest().setAttribute("ysAccredit", accredit);
		return "/system/store/auth/zizhiYSload";
	}

	/**
	 * 认证资质
	 */
	@RequestMapping(value = "/system/store/zizhi/checkZiZhi")
	@ResponseBody
	public boolean checkZiZhi() {
		Manager manager = (Manager) getSession().getAttribute(SystemConst.MANAGER);
		String rzType = getRequest().getParameter("rzType");
		String storeId = getRequest().getParameter("storeId");
		return accreditService.checkAccs(rzType, storeId, manager);
	}
	
	/**
	 * 危废品运输界定标准展示
	 */
	@RequestMapping(value = "/system/store/accredit/wfpysShow")
	public String wfpysShow(HttpServletRequest request, HttpServletResponse response) {
		String mlIds = request.getParameter("mlIds");
		List<Wfpys> mlList = null;
		if (!StringUtil.isEmpty(mlIds)) {
			Set<String> set=new HashSet<>();
			String[] ids = mlIds.split(";");
			for(String idStr:ids){
				set.add(idStr);
			}
			mlList = wfpysService.getList(set);
		}
		request.setAttribute("mlList", mlList);
		return "/userCenter/store/authenticate/load/wfpysShow";
	}
}
