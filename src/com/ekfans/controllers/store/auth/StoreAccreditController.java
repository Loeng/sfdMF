package com.ekfans.controllers.store.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Wfpml;
import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.TankMaterial;
import com.ekfans.base.store.model.Wfpys;
import com.ekfans.base.store.service.IAccreditService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.store.service.ITankMaterialService;
import com.ekfans.base.store.service.IWfpysService;
import com.ekfans.base.store.util.AccreditHelper;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class StoreAccreditController extends BasicController {

	@Autowired
	private IAccreditService accreditService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IWfpysService wfpysService;
	@Autowired 
	private ITankMaterialService tankMaterialService;

	/**
	 * 跳转到资质认证页面
	 */
	@RequestMapping(value = "/store/auth/jumpIntelPage")
	public String jumpIntelPage() {
		User user = (User) getSession().getAttribute(SystemConst.USER); // 在HttpSession中获取用户信息
		Store store = (Store) getSession().getAttribute(SystemConst.STORE); // 在HttpSession中获取企业信息
		store = storeService.getStoreById(store.getId());
		user = userService.getUser(user.getId());
		getSession().setAttribute(SystemConst.STORE, store);
		getSession().setAttribute(SystemConst.USER, user);
		return "/userCenter/store/authenticate/zizhi";
	}

	/**
	 * load得出危险废物经营许可资质
	 */
	@RequestMapping(value = "/store/auth/jumpIntelPage/wfzz/load")
	public String wfzzLoad() {
		Store store = (Store) getSession().getAttribute(SystemConst.STORE);
		Accredit accredit = accreditService.getAccreditByStoreAndType(store.getId(), AccreditHelper.ACC_RZTYPE_WF);
		getRequest().setAttribute("wfAccredit", accredit);
		return "/userCenter/store/authenticate/zizhiWFload";
	}

	/**
	 * load得出排放污染物许可资质
	 */
	@RequestMapping(value = "/store/auth/jumpIntelPage/pwzz/load")
	public String pwzzLoad() {
		Store store = (Store) getSession().getAttribute(SystemConst.STORE);
		Accredit accredit = accreditService.getAccreditByStoreAndType(store.getId(), AccreditHelper.ACC_RZTYPE_PW);
		getRequest().setAttribute("pwAccredit", accredit);
		return "/userCenter/store/authenticate/zizhiPWload";
	}

	/**
	 * load得出所有资质
	 */
	@RequestMapping(value = "/store/auth/jumpIntelPage/yszz/load")
	public String yszzLoad() {
		Store store = (Store) getSession().getAttribute(SystemConst.STORE);
		Accredit accredit = accreditService.getAccreditByStoreAndType(store.getId(), AccreditHelper.ACC_RZTYPE_YS);
		getRequest().setAttribute("ysAccredit", accredit);
		return "/userCenter/store/authenticate/zizhiYSload";
	}

	/**
	 * 编辑或保存认证的资质
	 */
	@RequestMapping(value = "/store/auth/accredit/save")
	@ResponseBody
	public boolean saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
		String rzType = request.getParameter("rzType");
		return accreditService.saveOrUpdate(request, response, rzType);
	}

	/**
	 * load出危废品运输界定标准的一级目录
	 */
	@RequestMapping(value = "/store/auth/accredit/wfpysLoad")
	public String wfpysLoad(HttpServletRequest request, HttpServletResponse response, String name) {
		List<Wfpys> wfpys = wfpysService.getList(true, null, name);
		request.setAttribute("wfpys", wfpys);
		request.setAttribute("name", name);
		return "/userCenter/store/authenticate/load/loadWfpys";
	}

	/**
	 * 根据id查出危废品运输界定标准的子级目录
	 */
	@RequestMapping(value = "/store/auth/accredit/wfpysChild")
	public String wfpysChild(HttpServletRequest request, HttpServletResponse response, String id) {
		List<Wfpys> wfpys = wfpysService.getList(false, id, null);
		request.setAttribute("wfpys", wfpys);
		return "/userCenter/store/authenticate/load/loadWfpysList";
	}

	/**
	 * 危废品运输界定标准展示
	 */
	@RequestMapping(value = "/store/auth/accredit/wfpysShow")
	public String wfpysShow(HttpServletRequest request, HttpServletResponse response) {
		String mlIds = request.getParameter("mlIds");
		List<Wfpys> mlList = null;
		if (!StringUtil.isEmpty(mlIds)) {
			Set<String> set = new HashSet<>();
			String[] ids = mlIds.split(";");
			for (String idStr : ids) {
				set.add(idStr);
			}
			mlList = wfpysService.getList(set);
		}
		request.setAttribute("mlList", mlList);
		return "/userCenter/store/authenticate/load/wfpysShow";
	}

	@RequestMapping(value = "/store/auth/accredit/getWfpys/{id}")
	public @ResponseBody
	List<Wfpys> getWfpys(@PathVariable String id) {
		List<Wfpys> wfpys = wfpysService.getList(false, id, null);
		return wfpys;
	}
    
	/**
	 * 根据车辆类型获取罐体材质
	 * @param id  车辆类型id
	 * @return
	 */
	@RequestMapping(value = "/store/auth/accredit/getTankMaterial/{id}")
	public @ResponseBody
	List<TankMaterial> getTankMaterial(@PathVariable String id) {
        List<TankMaterial> tank = tankMaterialService.getTankMaterialList(id);
		return tank;
	}
	
	@RequestMapping(value = "/web/auth/accredit/getWfpysToWeb/{id}")
	public @ResponseBody
	List<Wfpys> getWfpysToWeb(@PathVariable String id) {
		List<Wfpys> wfpys = wfpysService.getList(false, id, null);
		return wfpys;
	}
}
