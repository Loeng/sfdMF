package com.ekfans.controllers.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class WebProductController extends BasicController {

	@Autowired
	IProductService productService;

	@Autowired
	IProductCategoryService categoryService;

	@Autowired
	private ISupplyBuyService supplyService;

	@Autowired
	private ISystemAreaService areaService;

	/**
	 * 
	 * @Title: getProductList
	 * @Description: TODO(销售挂牌商品列表展示) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "/web/channel/indexxsgpList")
	public String indexXsgpList(HttpServletRequest request) {
		// 查询商品列表
		List<List<Product>> products = Cache.getIndexProductXsgp();
		// 返回页面的值
		request.setAttribute("products", products);
		return "/web/channel/index/xhgp";
	}


	@RequestMapping(value = "/web/supply/indexShowList/{productType}/{type}")
	public String getShowTable(@PathVariable String productType, @PathVariable String type, HttpServletRequest request) {

		List<SupplyBuy> supplyInfos = Cache.getIndexSupplyBuyInfo(type, productType);
		// supplyInfos = supplyService.listSupplyTable(pager, null, null, type, productType, null, null, null, null);
		List<List<SupplyBuy>> rList = new ArrayList<List<SupplyBuy>>();
		List<SupplyBuy> childList = new ArrayList<SupplyBuy>();
		try{
			for (int i = 0; i < supplyInfos.size(); i++) {
				SupplyBuy supply = supplyInfos.get(i);
				if (supply == null) {
					continue;
				}
				if (i == 0 || (i + 1) % 2 == 1) {
					childList = new ArrayList<SupplyBuy>();
				}
				if (childList == null) {
					continue;
				}
				String address = areaService.getAreaFullNameByAreaIdAndDelimiter(supply.getAreaId(), ".");
				if (address.indexOf(".", address.indexOf(".")) != -1 && address.length()<= (address.indexOf(".", address.indexOf(".") + 1))) {
					address = address.substring(0, address.indexOf(".", address.indexOf(".") + 1));
				}
				supply.setAddress(address);

				childList.add(supply);
				if (i == 0 || (i + 1) % 2 == 1) {
					rList.add(childList);
				}

			}
		}catch (Exception e){
			e.printStackTrace();
		}
		// 传递数据
		request.setAttribute("supplyInfos", rList);
		request.setAttribute("productType", productType);
		request.setAttribute("type", type);
		return "/web/channel/index/gyxxListShow";
	}
	
	@RequestMapping(value = "/web/supply/qgindexShowList/{productType}/{type}")
	public String getQGShowTable(@PathVariable String productType, @PathVariable String type, HttpServletRequest request) {

		List<SupplyBuy> supplyInfos = Cache.getIndexSupplyBuyInfo(type, productType);
		//List<SupplyBuy> supplyInfos = supplyService.listSupplyTable(pager, null, null, type, productType, null, null, null, null);
		List<List<SupplyBuy>> rList = new ArrayList<List<SupplyBuy>>();
		List<SupplyBuy> childList = new ArrayList<SupplyBuy>();
		try{
			for (int i = 0; i < supplyInfos.size(); i++) {
				SupplyBuy supply = supplyInfos.get(i);
				if (supply == null) {
					continue;
				}
				if (i == 0 || (i + 1) % 2 == 1) {
					childList = new ArrayList<SupplyBuy>();
				}
				if (childList == null) {
					continue;
				}
				String address = areaService.getAreaFullNameByAreaIdAndDelimiter(supply.getAreaId(), ".");
				if (address.indexOf(".", address.indexOf(".")) != -1 && address.length()<= (address.indexOf(".", address.indexOf(".") + 1))) {
					address = address.substring(0, address.indexOf(".", address.indexOf(".") + 1));
				}
				supply.setAddress(address);

				childList.add(supply);
				if (i == 0 || (i + 1) % 2 == 1) {
					rList.add(childList);
				}

			}
		}catch (Exception e){
			e.printStackTrace();
		}
		// 传递数据
		request.setAttribute("supplyInfos", rList);
		request.setAttribute("productType", productType);
		request.setAttribute("type", type);
		return "/web/channel/index/qgxxListShow";
	}

	@RequestMapping(value = "/web/supply/indexShowListload/{productType}/{type}/{pageNo}", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getShowTable(@PathVariable String productType, @PathVariable String type, @PathVariable String pageNo, HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNo)) {
			try {
				currentPage = Integer.parseInt(pageNo) + 1;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(2);
		// 查询6条供应信息

//		List<SupplyBuy> supplyInfos = supplyService.listSupplyTable(pager, null, null, type, productType, null, null, null, null);
		List<SupplyBuy> supplyInfos = new ArrayList<>();
		for (SupplyBuy supply : supplyInfos) {
			if (supply == null) {
				continue;
			}
			String address = areaService.getAreaFullNameByAreaIdAndDelimiter(supply.getAreaId(), ".");
			if (address.indexOf(".", address.indexOf(".")) != -1) {
				address = address.substring(0, address.indexOf(".", address.indexOf(".") + 1));
			}
			supply.setAddress(address);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo", pager.getCurrentPage());
		map.put("totalPage", pager.getTotalPage());
		map.put("list", supplyInfos);
		return JsonUtil.convertToJsonString(map);
	}
}
