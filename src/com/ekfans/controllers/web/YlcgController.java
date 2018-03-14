package com.ekfans.controllers.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.system.service.IAreaService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.ResourceBundleUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class YlcgController extends BasicController {
	
	private Logger log = LoggerFactory.getLogger(YlcgController.class);
	@Resource
	private IAreaService areaService;
	@Resource
	private IProductService productService;

	@RequestMapping(value = "/web/xhjy/ylcgone")
	public String getGAreaLoad(){
		getRequest().setAttribute("alist", areaService.getAllArea());
		
		return "/web/channel/ylcg/area";
	}
	
	@RequestMapping(value = "/web/xhjy/ylcgtwo")
	public String getAreaByStoreLoad(){
		String areaId = getRequest().getParameter("areaId");
		
		getRequest().setAttribute("slist", areaService.getStoreByAreaId(areaId));
		
		return "/web/channel/ylcg/store";
	}
	
	@RequestMapping(value = "/web/xhjy/ylcgthree")
	public String getStoreByProjectLoad(){
		String categoryId = (new ResourceBundleUtil()).getProperty("yuanlianProduct");
		String currentpageStr = getRequest().getParameter("pageNum"); // 页码
		String storeId = getRequest().getParameter("storeId"); // 企业id
		String areaId = getRequest().getParameter("areaId"); // 区域id
		
		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("plist", productService.getProductByCategoryId(pager, storeId, categoryId, areaId));
		getRequest().setAttribute("storeId", storeId);
		getRequest().setAttribute("areaId", areaId);
		
		return "/web/channel/ylcg/project";
	}
}
