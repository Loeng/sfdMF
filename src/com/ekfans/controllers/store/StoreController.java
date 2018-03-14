package com.ekfans.controllers.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.base.content.service.IContentCategoryRelService;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.IOrderWfpService;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.order.service.StoreOrder.IndexAccount.IStoreIndexAccountService;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.store.model.MortgageApplication;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.store.service.ICarInfoService;
import com.ekfans.base.store.service.IMortgageApplicationService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.webService.bcs.BCSClientService;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 店铺首页功能Controller
 * 
 * @Title: StoreController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司 
 * @date 2014-3-19 上午11:22:03 
 * @version V1.0
 */
@Controller
public class StoreController extends BasicController {

	@Autowired
	private IStoreService storeService;
	@Autowired
	private IStoreIndexAccountService storeIndexAccountService;
	@Autowired
	private IContentCategoryService categoryService;
	@Autowired
	private IContentCategoryRelService categoryRelService;
	@Autowired
	private IContentService contentService;
	@Autowired
	private IStoreOrderService storeOrderService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IMortgageApplicationService mortgageApplicationService;
	@Autowired
	private IOrderWfpService orderWfpService;
	@Autowired
	private ISupplyBuyService supplyBuyService;
	@Autowired
	private ICarInfoService carInfoService;

	/**
	 * 店铺会员首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/index")
	public String index(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 从Session中获取Store对象
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		User user = (User) session.getAttribute(SystemConst.USER);

		// 如果获取的店铺对象为空，则重定向到店铺登陆页面
		if (store == null) {
			return "redirect:/store/";
		}
		store = storeService.getStore(store.getId());
		String storeId = store.getId();
		session.setAttribute(SystemConst.STORE, store);
		
		if(store.getAccountStatus() && store.getAccountSuccess()){
			BigDecimal[] prices = BCSClientService.getAvlBal(storeId);
			request.setAttribute("accountPrices", prices);
		}
		
		if ("3".equals(user.getType())) {
			// 得到大宗待付款的订单
			String daDaiPaySum = storeOrderService.getOrderSum(storeId, null, "1", "2");
			// 得到大宗待发货的订单
			String daDaiFaSum = storeOrderService.getOrderSum(storeId, null, "1", "3");
			// 得到预订单待发货的订单
			String xiaoDaiFaSum = storeOrderService.getOrderSum(storeId, null, "0", "3");
			// 得到预订单待付款的订单
			String xiaoDaiPaySum = storeOrderService.getOrderSum(storeId, null, "0", "2");
			// 得到仓库商品
			String wareHouserProduct = productService.getProductSum(storeId, "false");
			// 得到出售的商品
			String chuShouProduct = productService.getProductSum(storeId, "true");
			// 得到申请借款的数量
			String applyJiedaiSum = mortgageApplicationService.getMASum(storeId);
			// 得到危废信息的列表
			List<SupplyBuy> supplyBuys = supplyBuyService.listSupplyBuy(null, null, null, null, null, null, "1", "2", "1", "1", null);
			session.setAttribute("parentChose", "index");
			// 绑定到页面
			request.setAttribute("daDaiPaySum", daDaiPaySum);
			request.setAttribute("daDaiFaSum", daDaiFaSum);
			request.setAttribute("xiaoDaiFaSum", xiaoDaiFaSum);
			request.setAttribute("xiaoDaiPaySum", xiaoDaiPaySum);
			request.setAttribute("chuShouProduct", chuShouProduct);
			request.setAttribute("wareHouserProduct", wareHouserProduct);
			request.setAttribute("applyJiedaiSum", applyJiedaiSum);
			request.setAttribute("supplyBuys", supplyBuys);
		} else if ("2".equals(user.getType())) {
			// 得到订单待付款的订单
			String daDaiPaySum = storeOrderService.getOrderSum(null, storeId, "1", "2");
			// 得到订单待收货的订单
			String daDaiFaSum = storeOrderService.getOrderSum(null, storeId, "1", "3");
			// 得到预订单待发货的订单
			String xiaoDaiFaSum = storeOrderService.getOrderSum(null, storeId, "0", "3");
			// 得到预订单待付款的订单
			String xiaoDaiPaySum = storeOrderService.getOrderSum(null, storeId, "0", "2");
			// 得到申请借款的数量
			String applyJiedaiSum = mortgageApplicationService.getMASum(storeId);
			// 得到申请的列表
			List<MortgageApplication> maList = mortgageApplicationService.getList(storeId);
			session.setAttribute("parentChose", "index");
			// 绑定到页面
			request.setAttribute("daDaiPaySum", daDaiPaySum);
			request.setAttribute("daDaiFaSum", daDaiFaSum);
			request.setAttribute("xiaoDaiFaSum", xiaoDaiFaSum);
			request.setAttribute("xiaoDaiPaySum", xiaoDaiPaySum);
			request.setAttribute("applyJiedaiSum", applyJiedaiSum);
			request.setAttribute("maList", maList);
		} else if ("1".equals(user.getType())) {
			// 得到订单待付款的订单
			String daDaiPaySum = storeOrderService.getOrderSum(storeId, null, "1", "2");
			// 得到订单待发货的订单
			String daDaiFaSum = storeOrderService.getOrderSum(storeId, null, "1", "3");
			// 得到预订单待发货的订单
			String xiaoDaiFaSum = storeOrderService.getOrderSum(storeId, null, "0", "3");
			// 得到预订单待付款的订单
			String xiaoDaiPaySum = storeOrderService.getOrderSum(storeId, null, "0", "2");
			// 得到仓库商品
			String wareHouserProduct = productService.getProductSum(storeId, "false");
			// 得到出售的商品
			String chuShouProduct = productService.getProductSum(storeId, "true");
			// 得到申请借款的数量
			String applyJiedaiSum = mortgageApplicationService.getMASum(storeId);
			// 得到危废信息的列表
			List<SupplyBuy> supplyBuys = supplyBuyService.listSupplyBuy(null, null, null, null, null, null, "1", "2", "1", "1", null);
			session.setAttribute("parentChose", "index");
			// 绑定到页面
			request.setAttribute("daDaiPaySum", daDaiPaySum);
			request.setAttribute("daDaiFaSum", daDaiFaSum);
			request.setAttribute("xiaoDaiFaSum", xiaoDaiFaSum);
			request.setAttribute("xiaoDaiPaySum", xiaoDaiPaySum);
			request.setAttribute("chuShouProduct", chuShouProduct);
			request.setAttribute("wareHouserProduct", wareHouserProduct);
			request.setAttribute("applyJiedaiSum", applyJiedaiSum);
			request.setAttribute("supplyBuys", supplyBuys);
		} else if ("4".equals(user.getType())) {
			// 得到申请借款的数量
			String applyJiedaiSum = mortgageApplicationService.getMASum(storeId);
			// 得到危废品未完成的订单
			String daDaiPaySum = orderWfpService.getOrderSum(storeId, "0");
			// 得到危废品完成的订单
			String daDaiFaSum = orderWfpService.getOrderSum(storeId, "1");
			// 得到申请的列表
			List<MortgageApplication> maList = mortgageApplicationService.getList(storeId);
			session.setAttribute("parentChose", "index");
			// 绑定到页面
			request.setAttribute("applyJiedaiSum", applyJiedaiSum);
			request.setAttribute("maList", maList);
			request.setAttribute("daDaiPaySum", daDaiPaySum);
			request.setAttribute("daDaiFaSum", daDaiFaSum);
		}
		return "/userCenter/store/index";
	}

	/**
	 * 
	 * @Title: view
	 * @Description: TODO 店铺培训资料的load 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/storeIndex/load")
	public String view(HttpServletRequest request) {
		String categoryName = "店铺培训资料";
		ContentCategory categorys = categoryService.getContentCategoryByName(categoryName,null);
		String categorysId = categorys.getId();

		List<ContentCategoryRel> ccrs = categoryRelService.getContentCategoryRelById(categorysId);
		List<Content> lc = new ArrayList<Content>();
		for (int i = 0; i < ccrs.size(); i++) {
			ContentCategoryRel ccgRel = ccrs.get(i);
			String contentid = ccgRel.getContentId();
			Content content = contentService.getContent(contentid);
			lc.add(content);
		}
		request.setAttribute("lc", lc);
		return "/store/indexDetails";
	}

	/**
	 * 取得店鋪公告的內容
	 */
	@RequestMapping(value = "/store/index/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		// Content content = contentService.getContent(id);
		// String contentId = content.getId();

		// try {
		// Pager pager = new Pager();
		// String currentpageStr = request.getParameter("pageNum");
		//
		// // 将从页面获取的分页数据转化成int型
		// int currentPage = 1;
		// if (!StringUtil.isEmpty(currentpageStr)) {
		// try {
		// currentPage = Integer.parseInt(currentpageStr);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// // 设置要查询的页码
		// pager.setCurrentPage(currentPage);
		// //获得content对象
		// Content content = contentService.getContent(id);
		// String contentId = content.getId();
		// //根据contentId取得
		// ContentCategoryRel contentCategoryRel =
		//
		//
		//
		// //取得content对象的ID
		// //将content对象的ID 传给ContentModel
		// List<ContentModel> cModels =
		// contentModelService.getContentModelByContentId(id);
		// //将content对象的ID 传给ContentCategory
		// ContentCategory category = categoryService.getCategoryById(id);
		// request.setAttribute("pager", pager);
		// request.setAttribute("category", category);
		// request.setAttribute("content", content);
		// request.setAttribute("contentModels", cModels);
		return "/store/shopNotice";
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return "error";
	}

	/**
	 * 获取店铺会员所对应的权限集合
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/purview/{id}")
	public String showPurview(@PathVariable String id, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 从Session中获取Store对象
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		Account account = (Account) session.getAttribute(SystemConst.ACCOUNT);
		// 如果获取的店铺对象为空，则重定向到店铺登陆页面
		if (store == null) {
			return "redirect:/store/";
		}

		String purvId = "";
		// 调用缓存获取权限对象
		StorePurview purview = null;
		String orgRoleId = "";
		if (account != null) {
			orgRoleId = account.getId();
		} else {
			orgRoleId = store.getRoleId();
		}
		purview = Cache.getStorePurview(orgRoleId, id, true);
		String redUrl = "";

		if (purview != null) {
			if (purview.getLevel() != 1 && !StringUtil.isEmpty(purview.getFullPath())) {
				purvId = purview.getId();
				redUrl = purview.getFullPath();
			} else {
				StorePurview defaultPurview = null;
				String defaultOpen = purview.getDefaultOpen();

				if (!StringUtil.isEmpty(defaultOpen)) {
					if (defaultOpen.indexOf(";") != -1) {
						String[] openIds = defaultOpen.split(";");
						for (String openId : openIds) {
							defaultPurview = Cache.getStorePurview(orgRoleId, openId, false);
							if (defaultPurview != null) {
								break;
							}
						}
					} else {
						defaultPurview = Cache.getStorePurview(orgRoleId, purview.getDefaultOpen(), true);
					}
				}
				if (defaultPurview != null) {
					redUrl = defaultPurview.getFullPath();
					purvId = defaultPurview.getId();
				}
			}
		}

		if (!StringUtil.isEmpty(redUrl)) {
			redUrl = redUrl.replaceAll("---", "/");
		}
		// 将获取到的集合放入request中
		session.setAttribute("storePurview", purview);
		session.setAttribute("purviewId", purvId);
		return "redirect:" + redUrl;
	}

	/**
	 * 会员权限跳转
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/manager/{id}")
	public String showUrl(@PathVariable String id, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		// // 如果传进来的ID为NULL，则根据路径获取ID
		// if (StringUtil.isEmpty(id)) {
		// IStorePurviewService purviewService =
		// SpringContextHolder.getBean(IStorePurviewService.class);
		// purviewService.get
		// }

		Store store = (Store) session.getAttribute(SystemConst.STORE);
		Account account = (Account) session.getAttribute(SystemConst.ACCOUNT);
		if (store == null) {
			return "redirect:/store/";
		}
		// 调用缓存获取权限对象
		// 获取当前选择的purview

		StorePurview purview = null;
		if (account != null) {
			purview = Cache.getStorePurview(account.getId(), id, true);
		} else {
			purview = Cache.getStorePurview(store.getRoleId(), id, true);
		}

		if (purview == null) {
			return "redirect:/store/";
		}
		// 获取最顶级的Purview
		StorePurview topPurview = null;
		if (account != null) {
			topPurview = Cache.getStorePurview(account.getId(), purview.getTopId(), true);
		} else {
			topPurview = Cache.getStorePurview(store.getRoleId(), purview.getTopId(), true);
		}

		session.setAttribute("storePurview", topPurview);
		session.setAttribute("purviewId", purview.getId());

		return "redirect:" + purview.getFullPath();
	}

	/**
	 * 
	 * @Title: partRefreshSellAccount 详细业务流程: (获取商店首页　销售结算状况的局部刷新
	 *         <根据选择时间查询出当前月的销售结算>)
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/partRefreshSellAccount", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String partRefreshSellAccount(String year, String month, HttpSession session, HttpServletRequest request) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/store/";
		}
		String YD = year + "-" + month;
		List<Order> orders = storeIndexAccountService.getOrderSellAccount(YD, store.getId());
		String jsonResult = JsonUtil.listToJson(orders);
		return jsonResult;
	}

	@RequestMapping(value = "/web/storeHome/{storeId}", produces = "text/plain;charset=UTF-8")
	public String storeHome(@PathVariable String storeId, HttpServletRequest request, HttpServletResponse response) {

		return "redirect:/storeIndex/shopIndex";
	}

}