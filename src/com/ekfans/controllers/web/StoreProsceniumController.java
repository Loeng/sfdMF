package com.ekfans.controllers.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.model.Area;
import com.ekfans.base.system.service.IAreaService;
import com.ekfans.controllers.web.vo.ProListTemplateVO;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: StoreProsceniumController
 * @Description: TODO 店铺商品列表
 * @author wsj
 * @date May 8, 2014 5:00:22 PM
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@SuppressWarnings("unchecked")
public class StoreProsceniumController {

	@Autowired
	private IStoreService storeService;
	@Autowired
	private IAreaService areaService;

	/**
	 * 
	 * @Title: view
	 * @Description: TODO 根据storeId得到集合 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param storeId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("/web/proscenium/{storeId}")
	public String view(@PathVariable String storeId, HttpServletRequest request) {
		Pager pager = new Pager();
		String currentpageStr = request.getParameter("pageNum");

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

		Object[] objArray = storeService.getProductsById(storeId, pager);

		List<Product> products = null;
		String productNum = null;
		if (objArray != null) {
			products = (List<Product>) objArray[0];
			productNum = objArray[1].toString();
		}

		// 查询出商品主分类
		List<ProductCategory> categorys = storeService.findThisProductCatagoryByStore(storeId);
		// 查询出所有品牌
		List<ProductBrand> brands = storeService.findThisProductBrandByStore(storeId);
		// 查询出满足条件商品的所属模板
		List<ProListTemplateVO> tvos = storeService.findProductTemplateFieldsByStoreId(storeId);

		request.setAttribute("products", products);
		request.setAttribute("categorys", categorys);
		request.setAttribute("brands", brands);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("tvos", tvos);
		request.setAttribute("productNum", productNum);
		request.setAttribute("pager", pager);

		// 先决条件
		request.setAttribute("storeId", storeId);
		return "/web/prosceniumIndex";

	}

	/**
	 * 列表的查询操作
	 */
	@RequestMapping(value = "/web/proscenium/list")
	public String list(HttpServletRequest request) {
		// 先决条件
		String storeId = "1";

		String mainCategory = request.getParameter("mainCategory");
		String brand = request.getParameter("brand");
		// 模板名和值(这里默认处理两个模板 传过来的值为"key/value"对应格式的字符串,后台处理的时候需要拆分)
		String templateOne = request.getParameter("templateOne");
		String templateTwo = request.getParameter("templateTwo");
		// 排序
		String sortNameAndType = request.getParameter("sortNameAndType");

		// 分页
		Pager pager = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);

		Object[] objArray = storeService.getProductByConditionsByStore(storeId, mainCategory, brand, templateOne, templateTwo, sortNameAndType, pager);
		// 预防objArray的空指针
		List<Product> products = null;
		String productNum = null;
		if (objArray != null) {
			products = (List<Product>) objArray[0];
			productNum = objArray[1].toString();
		}
		request.setAttribute("products", products);
		request.setAttribute("productNum", productNum);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pager", pager);
		return "/web/prosceniumIndex";
		// return "/web/webSearch/prosceniumDetails";
		// return new ModelAndView("/web/prosceniumIndex");
	}

	/**
	 * 
	 * @Title: viewDetails
	 * @Description: TODO 根据查询到的全路径得到该分类下的所有商品分类 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param fullPath
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/proscenium/detail/{fullPath}")
	public String viewDetails(@PathVariable String fullPath, HttpServletRequest request) {
		// 查询出商品主分类
		List<ProductCategory> categorys = storeService.findThisProductCatagoryByFullPath(fullPath);
		request.setAttribute("categorys", categorys);
		return "/web/prosceniumDetails";
	}

	@RequestMapping(value = "/web/storeType/getHxInfo/{channel}/{type}")
	public String getStoreByHx(@PathVariable String channel, @PathVariable String type,HttpServletRequest request) {
		List<Store> storelist = storeService.getStoreByHx(type);
		String channelId = request.getParameter("channelId");
		
		// 最多取5条数据
		List<Store> newList = new ArrayList<>();
		int cutNum = 5;
		if (storelist != null) {
			for (int i = 0; i < storelist.size(); i++) {
				if (i == cutNum) {
					break;
				}
				newList.add(storelist.get(i));
			}
		}
		
		request.setAttribute("storelist", newList);
		request.setAttribute("channelId", channelId);
		request.setAttribute("type", type);
		if("index".equals(channel)){
			return "/web/channel/index/content_two";
		}else{
			return "/web/channel/qyrz/content_two";
		}
	}

	/**
	 * @Title: getStoreByType
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:前台获取区域表中运输企业的列表
	 *               (详细描述此方法相关的业务处理流程) void 返回类型
	 * @throws
	 */

	@RequestMapping(value = "/web/storeType/getYsList")
	public String getStoreByType(HttpServletRequest request) {
		// 分页
		Pager pager = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		String areaId = request.getParameter("areaId");
		if (areaId.equals("0")) {
			areaId = "";
		}
		// 获取某个区域下的企业数据,若区域ID为空，则获取所有运输企业的
		List<Store> s = storeService.getStoreByType(pager, areaId, "channel");
		List<Area> area = areaService.getAllArea();
		String areaName = "";
		for (int i = 0; i < area.size(); i++) {
			Area a = area.get(i);
			if (a.getId().equals(areaId)) {
				areaName = a.getName();
			}
		}
		request.setAttribute("areaName", areaName);
		request.setAttribute("areaList", area);
		request.setAttribute("storeList", s);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pager", pager);
		return "/web/channel/wxpcz/loadys";
	}
}