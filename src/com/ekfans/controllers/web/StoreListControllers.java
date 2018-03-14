package com.ekfans.controllers.web;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.service.web.storeList.IStoreListService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreLevel;
import com.ekfans.base.store.service.IStoreLevelService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class StoreListControllers extends BasicController {

	@Autowired
	private IStoreListService storeListService;
	@Autowired
	private IProductCategoryService productCategoryService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IStoreLevelService storeLevelService;
	@Autowired
	private IOrderDetailService orderDetailService;
	@Autowired
	private IStoreService storeService;

	@RequestMapping(value = "/web/store/logoList")
	public String getStoreLogoList(HttpServletRequest request){
		// 从页面获取分页数据
		String currentpageStr = request.getParameter("pageNum");
		// 定义分页
        Pager pager = new Pager();
        pager.setRowsPerPage(6);

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
        pager.setRowsPerPage(8);
		
        // 得到已认证企业列表
        List<Store> stores = storeService.getStoreByHx(null);
		
		// 得到所有的店铺等级
		List<StoreLevel> storeLevels = storeLevelService.getLevels(null);
		
		request.setAttribute("stores", stores);
		request.setAttribute("storeLevels", storeLevels);
		request.setAttribute("pager", pager);
		
		return "/web/channel/lssc/logoList";
	}
	/**
	 * 搜索出店铺
	 */
	@RequestMapping(value = "/web/store/getMateStore")
	public String getStoresByFuzzyName() {
		String currentpageStr = getRequest().getParameter("pageNum"); // 页码
		String storeName = getRequest().getParameter("storeName");
		
		// 分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		pager.setCurrentPage(currentPage);
		
		
		// 得到所有的店铺等级
		List<StoreLevel> storeLevels = storeLevelService.getLevels(null);
		
		List<Store> stores = storeListService.getStoresByFuzzyName(storeName, pager);
		if (stores != null) {
			for (Store s : stores) {
				s.setProductCategoryList(productCategoryService.getProductCategoryByStoreId(s.getId()));
				s.setProductPictureList(productService.listProduct(s.getId()));
				s.setOrderDetailList(orderDetailService.getOrderDetailByStoreId(s.getId()));
			}
		}
		
		Collections.reverse(stores);
		// 得到所有商品分类
		List<ProductCategory> productAllCategories = productCategoryService.getCategories(null, null);
		
		// 先决条件
		getRequest().setAttribute("productAllCategories", productAllCategories);
		getRequest().setAttribute("storeName", storeName);
		getRequest().setAttribute("storeLevels", storeLevels);
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("stores", stores);
		getRequest().setAttribute("size", stores.size());
		
		return "/web/webSearch/storeList";
	}

	/**
	 * 商品列表页用户按条件搜索出满足条件的商品, 商品列表项采用局部刷新
	 */
	@RequestMapping(value = "/web/store/getStoreByCondition")
	public String getProductByCondition() {
		String storeName = getRequest().getParameter("storeName");
		String sortNameAndType = getRequest().getParameter("sortNameAndType"); // 排序
		String storeLevelId = getRequest().getParameter("storeLevelId"); // 店铺等级
		String currentpageStr = getRequest().getParameter("pageNum"); // 页码
		
		// 分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		pager.setCurrentPage(currentPage);
		
		List<Store> stores = storeListService.getStoresByCondition(storeLevelId, pager, storeName);
		if (stores != null) {
			for (Store s : stores) {
				s.setProductCategoryList(productCategoryService.getProductCategoryByStoreId(s.getId()));
				s.setProductPictureList(productService.listProduct(s.getId()));
				s.setOrderDetailList(orderDetailService.getOrderDetailByStoreId(s.getId()));
			}
		}
		if ("desc".equals(sortNameAndType)) {
			Collections.reverse(stores);
		}
		
		getRequest().setAttribute("storeName", storeName);
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("stores", stores);
		getRequest().setAttribute("storeNum", stores.size());
		
		return "/web/webSearch/storeListModel";
	}

	/**
	 * 危废处置--危险品处置--回收专区（load处置企业）
	 */
	@RequestMapping(value = "/web/store/getStoreByGroup")
	public String getStoreByStoreGroup() {
		Pager pager = new Pager();
		pager.setRowsPerPage(6);
		pager.setCurrentPage(1);
		
		getRequest().setAttribute("store", storeService.getStoreByGroup(pager, "3"));
		
		return "/web/channel/wxpcz/loadGroup";
	}
	
	@RequestMapping(value = "/web/wfcz/wfpcz/getStoreByCS")
	public String getStoreByCS() {
		Pager pager = new Pager();
		pager.setRowsPerPage(6);
		pager.setCurrentPage(1);
		
		getRequest().setAttribute("store", storeService.getStoreByGroup(pager, "1"));
		
		return "/web/channel/wxpcs/cs_store";
	}
}
