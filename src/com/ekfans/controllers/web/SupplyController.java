package com.ekfans.controllers.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.IInquiryService;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreApplyService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: SupplyController
 * @Description: TODO(前台-供求中心页、供应信息页、求购信息页)
 * @author 成都易科远见科技有限公司
 * @date 2016年3月3日16:20:39
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@RequestMapping("/web/supply")
public class SupplyController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SupplyController.class);
	@Autowired
	private ISupplyBuyService supplyService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IProductCategoryService productService;
	@Autowired
	private IInquiryService inquiryService;

	/**
	 * 根据不同的供求类型和搜索条件获取供求列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/supplyShowList/{productType}/{type}")
	public String getShowTable(@PathVariable String productType, @PathVariable String type, HttpServletRequest request) {
		// 根据type获取分类
		List<ProductCategory> categorys = productService.getCategoriesByType(productType);

		// 从session中读取store
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		// 获取搜索区的值
		String categoryId = request.getParameter("categoryId");
		String destination = request.getParameter("destination");
		String topPrice = request.getParameter("topPrice");
		String lowPrice = request.getParameter("lowPrice");
		String title = request.getParameter("title");
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取页码
		String currentpageStr = getRequest().getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(20);
		// 查询6条供应信息
		List<SupplyBuy> supplyInfos = supplyService.listSupplyTable(pager, null, title, type, productType, destination, categoryId, lowPrice, topPrice);
		// 传递数据
		request.setAttribute("supplyInfos", supplyInfos);
		request.setAttribute("productType", productType);
		request.setAttribute("type", type);
		request.setAttribute("productType", productType);
		request.setAttribute("topPrice", topPrice);
		request.setAttribute("lowPrice", lowPrice);
		request.setAttribute("pager", pager);
		request.setAttribute("pageNum", currentPage);
		request.setAttribute("categorys", categorys);
		request.setAttribute("store", store);

		// 商品类型 0现货 1原料 类型0供应，1求购
		if ("1".equals(type) && "0".equals(productType)) {// 成品求购
			return "/web/channel/xsgp/qggpList";
		} else if ("1".equals(type) && "1".equals(productType)) {// 求购信息(原料)
			return "/web/channel/gqzx/qgxxListShow";
		} else if ("0".equals(type) && "1".equals(productType)) {// 供应信息(原料)
			return "/web/channel/gqzx/gyxxListShow";
		}
		return "";
	}

	/**
	 * 供求展示列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getSupplyShowList")
	public String supplyShowList(String type, Integer num) {
		if (StringUtil.isEmpty(type)) {
			type = "0";
		}
		if (num == null) {
			num = 10;
		}
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setRowsPerPage(num);

		// 查询10条供应信息
		List<SupplyBuy> supplyInfos = supplyService.webListSupplyShow(pager, type);
		// 查询10条求购信息
		List<SupplyBuy> demandInfos = supplyService.webListSupplyShow(pager, type);
		// 传递数据
		getRequest().setAttribute("supplyInfos", supplyInfos);
		getRequest().setAttribute("demandInfos", demandInfos);
		if ("0".equals(type)) {
			return "/web/channel/gqzx/gyxxListShow";
		} else {
			return "/web/channel/gqzx/qgxxListShow";
		}
	}

	/**
	 * 供求表格
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getSupplyShowTable/{productType}/{type}")
	public String supplyShowTable(@PathVariable String type, @PathVariable String productType, HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		pager.setRowsPerPage(6);
		// 查询6条供应信息
		List<SupplyBuy> supplyInfos = supplyService.listSupplyTable(pager, null, null, type, productType, null, null, null, null);
		// 传递数据
		request.setAttribute("supplyInfos", supplyInfos);
		request.setAttribute("productType", productType);
		request.setAttribute("type", type);
		request.setAttribute("productType", productType);
		// 商品类型 0现货 1原料 类型0供应，1求购
		if ("1".equals(type) && "1".equals(productType)) {// 求购信息(原料)
			return "/web/channel/gqzx/qgxxTable";
		} else if ("0".equals(type) && "1".equals(productType)) {// 供应信息(原料)
			return "/web/channel/gqzx/gyxxTable";
		}
		return "";
	}

	/**
	 * 求购表格
	 * 
	 * @return
	 */
	@RequestMapping(value = "/supplyShowQgTable")
	public String supplyShowQgTable(String type, String title, String pageNum) {
		if (StringUtil.isEmpty(type)) {
			type = "0";
		}
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取页码
		String currentpageStr = getRequest().getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(6);

		// 查询10条供应信息
		List<SupplyBuy> supplyInfos = supplyService.webListSupplyTable(pager, type, title);
		// 传递数据
		getRequest().setAttribute("supplyInfos", supplyInfos);
		getRequest().setAttribute("pageNum", currentPage);
		getRequest().setAttribute("pager", pager);
		if ("0".equals(type)) {
			return "/web/channel/xsgp/qggpList";
		} else {
			return "/web/channel/xsgp/qggpList";
		}
	}

	/**
	 * 供应信息页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listSupply")
	public String listSupply() {
		return "/url";
	}

	/**
	 * 求购信息页
	 * 
	 * @return
	 */
	@RequestMapping(value = "listDemand")
	public String listDemand() {

		return "/url";
	}

	/**
	 * 供求展示列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getSupplyToXhjy")
	public String getSupplyToXhjy(Integer num) {
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setRowsPerPage(num);
		// 查询10条求购信息
		List<SupplyBuy> demandInfos = supplyService.listSupplyTable(pager, null, null, "1", "0", null, null, null, null);
		// 传递数据
		getRequest().setAttribute("demandInfos", demandInfos);
		return "/web/channel/xsgp/xhjy_qggp";
	}

	/**
	 * 加载询价弹层
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loadXunJia")
	public String loadInquiry(HttpServletRequest request) {
		String id = request.getParameter("id");
		SupplyBuy sb = supplyService.getSupplyBuyById(id);
		// 供应信息浏览量加1
		sb.setViewCount(sb.getViewCount() + 1);
		supplyService.update(sb);
		if (!StringUtil.isEmpty(sb.getStoreId())) {
			Store s = storeService.getStoreById(sb.getStoreId());
			sb.setStoreName(s.getStoreName());
		}
		request.setAttribute("sb", sb);
		return "/web/channel/xsgp/loadXunJia";
	}

	/**
	 * 加载报价弹层
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loadBaojia")
	public String loadQuote(HttpServletRequest request) {
		String id = request.getParameter("id");
		SupplyBuy sb = supplyService.getSupplyBuyById(id);
		// 供应信息浏览量加1
		sb.setViewCount(sb.getViewCount() + 1);
		supplyService.update(sb);
		if (!StringUtil.isEmpty(sb.getStoreId())) {
			Store s = storeService.getStoreById(sb.getStoreId());
			sb.setStoreName(s.getStoreName());
		}
		request.setAttribute("sb", sb);
		return "/web/channel/xsgp/loadBaojia";
	}

	/**
	 * 添加询价信息
	 */
	@RequestMapping(value = "/saveInquiry")
	@ResponseBody
	public String saveInquiry(@ModelAttribute Inquiry i, HttpServletRequest request) {
		// 如果询价企业为登录状态则保存该企业信息
		Store s = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (s != null) {
			i.setBuyersId(s.getId());
			i.setStoreName(s.getStoreName());
			i.setBuyersId(s.getId());
		}
		i.setCreateTime(DateUtil.getSysDateTimeString());
		i.setStatus("0");
		// 保存询价信息
		if (inquiryService.saveBuyerInq(i)) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 加载最新行情
	 */
	@RequestMapping(value = "/newestSupplyList/{productType}/{type}")
	public String getNewList(@PathVariable String productType, @PathVariable String type, HttpServletRequest request) {
		List<SupplyBuy> sbs = supplyService.getNewSupplyList(productType, type);
		request.setAttribute("supplys", sbs);
		return "/web/channel/gqzx/newestSupplyList";
	}

}
