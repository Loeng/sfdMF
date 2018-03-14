package com.ekfans.controllers.store;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.service.IAccreditService;
import com.ekfans.base.store.service.IIntelService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.store.util.AccreditHelper;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.ResourceBundleUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class WebStoreController extends BasicController {
	
	private Logger log = LoggerFactory.getLogger(WebStoreController.class);
	@Resource
	private IStoreService storeService;
	@Resource
	private IIntelService intelService;
	@Resource
	private IUserService userService;
	@Resource
	private IAccreditService accreditService;

	@RequestMapping(value = "/web/storedetail/{id}")
	public String getStoreInfo(@PathVariable String id){
		getRequest().setAttribute("store", storeService.getStore(id));
		getRequest().setAttribute("tgilist", intelService.getIntelByStoreIdTong(id, null));
		getRequest().setAttribute("accreditList", accreditService.getAccreditList(id, true));
		return "/web/commons/qyhy";
	}
	
	/**
	 * 网络融资--企业认证--认证企业（load）
	 */
	@RequestMapping(value = "/web/wlrz/qyrz/store")
	public String getCheckStore(){
		String pageNum = getRequest().getParameter("pageNum"); // 页码
		
		// 定义分页
		Pager pager = new Pager();
		pager.setRowsPerPage(5);
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPage = Integer.parseInt(pageNum);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);
		
		getRequest().setAttribute("slist", storeService.getBasisStore(pager));
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("pageNum", pageNum);
		
		return "/web/channel/qyrz/store";
	}
	
	@RequestMapping(value = "/web/wlrz/qyrz/islogin")
	public String getByIsLogin(){
		ResourceBundleUtil rbu = new ResourceBundleUtil();
		String income = rbu.getProperty("company.income"); // 成功资本对接--企业总数
		String reg = rbu.getProperty("company.reg"); // 成功入会--企业总数
		
		getRequest().setAttribute("income", income);
		getRequest().setAttribute("reg", Integer.valueOf(reg) + userService.getUserNumber());
		
		return "/web/channel/qyrz/is_login";
	}
	
	@RequestMapping(value = "/web/system/authentication")
	public String authentication(){
		
		return "redirect:/store/";
	}
	
	
	@RequestMapping(value = "/web/index/companyNum")
	public String companyNum(){
		ResourceBundleUtil rbu = new ResourceBundleUtil();
		String income = rbu.getProperty("company.income"); // 成功资本对接--企业总数
		String reg = rbu.getProperty("company.reg"); // 成功入会--企业总数
		
		getRequest().setAttribute("income", income);
		getRequest().setAttribute("reg", Integer.valueOf(reg) + userService.getUserNumber());
		
		return "/web/channel/index/companyData";
	}
}
