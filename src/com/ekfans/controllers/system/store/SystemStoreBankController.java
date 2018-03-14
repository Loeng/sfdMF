package com.ekfans.controllers.system.store;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.StoreLegal;
import com.ekfans.base.store.service.IStoreFinancialDataService;
import com.ekfans.base.store.service.IStoreLegalResumeService;
import com.ekfans.base.store.service.IStoreLegalService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SystemStoreBankController extends BasicController {
	
	private Logger log = LoggerFactory.getLogger(SystemStoreBankController.class);
	@Resource
	private IStoreService storeService;
	@Resource
	private IStoreLegalService storeLegalService;
	@Resource
	private IStoreLegalResumeService storeLegalResumeService;
	@Resource
	private IStoreFinancialDataService storeFinancialDataService;

	/**
	 * 跳转到银行未认证的企业列表
	 */
	@RequestMapping(value = "/system/store/bank/jumplist")
	public String jumpStoreBasisPage(){
		String storeName = getRequest().getParameter("storeName"); // 企业名称
		String currentPageNum = getRequest().getParameter("pageNum"); // 当前页码
		
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
		
		getRequest().setAttribute("slist", storeService.getCheckStore(pager, storeName, 2));
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("storeName", storeName);
		
		return "/system/store/auth/store_bank_list";
	}
	
	/**
	 *  根据企业id获取企业法人信息和法人简历信息，跳转到银行认证页面
	 *  
	 * @param id 企业id
	 */
	@RequestMapping(value = "/system/store/bank/checkone/{id}")
	public String jumpBankOnePage(@PathVariable String id){
		StoreLegal sl = storeLegalService.getStoreLegalById(id);
		
		getRequest().setAttribute("store", storeService.getStore(id));
		getRequest().setAttribute("storelegal", sl == null ? new StoreLegal() : sl);
		getRequest().setAttribute("slrlist", storeLegalResumeService.getStoreLegalResumeByStoreId(id));
		
		return "/system/store/auth/store_bank_one";
	}
	
	/**
	 *  根据企业id获取企业近期财务信息，跳转到银行认证页面
	 *  
	 * @param id 企业id
	 */
	@RequestMapping(value = "/system/store/bank/checktwo/{id}")
	public String jumpBankTwoPage(@PathVariable String id){
		
		getRequest().setAttribute("store", storeService.getStore(id));
		getRequest().setAttribute("sfdlist", storeFinancialDataService.getStoreFinancialDataByStoreId(id));
		
		return "/system/store/auth/store_bank_two";
	}
}
