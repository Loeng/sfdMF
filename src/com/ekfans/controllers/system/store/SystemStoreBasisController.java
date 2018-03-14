package com.ekfans.controllers.system.store;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.service.IStoreService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemStoreBasisController extends BasicController {
	
	private Logger log = LoggerFactory.getLogger(SystemStoreBasisController.class);
	@Resource
	private IStoreService storeService;

	/**
	 * 跳转到基础信息认证列表页面
	 */
	@RequestMapping(value = "/system/store/basic/jumplist")
	public String jumpStoreBasisPage(){
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
		
		getRequest().setAttribute("slist", storeService.getCheckStore(pager, storeName, 1));
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("storeName", storeName);
		
		return "/system/store/auth/store_basis_list";
	}
	
	/**
	 * 根据企业id查询企业信息，跳转到企业认证页面
	 * 
	 * @param id 企业id
	 */
	@RequestMapping(value = "/system/store/checkbasis/{id}")
	public String jumpCheckPage(@PathVariable String id){
		getRequest().setAttribute("store", storeService.getStore(id));
		
		return "/system/store/auth/store_basis_check";
	}
	
	/**
	 * 企业基础信息认证操作
	 */
	@RequestMapping(value = "/system/store/checkStoreBasisInfo")
	@ResponseBody
	public boolean checkStoreBasisInfo(){
		String storeId = getRequest().getParameter("storeId");
		String mark = getRequest().getParameter("mark");
		String status = getRequest().getParameter("status");
		
		return storeService.checkStoreInfo(storeId, mark, status);
	}
}
