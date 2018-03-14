package com.ekfans.controllers.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.channel.model.Channel;
import com.ekfans.base.channel.service.IChannelService;
import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.service.InquiryService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.SupplyProduct;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.service.ISupplyProductService;
import com.ekfans.base.product.util.ProductConst;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.Warehouse;
import com.ekfans.base.store.service.IWarehouseService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.plugin.page.util.BasicConst;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: hjcProductListController
 * @Description: TODO(前台-搜索店铺、搜索商品)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-19 下午02:32:49
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class ProductListController {

	private Logger log = LoggerFactory.getLogger(ProductListController.class);
	@Autowired
	private IProductService productService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private InquiryService inquiryService;
	@Autowired
	private ISupplyProductService supplyProductService;
	@Autowired
    private IProductCategoryService productCategoryService;
	@Autowired
	private ISystemAreaService systemAreaService;
	@Autowired
	private IChannelService channelService;

	/**
	 * 
	 * @Title: goToProductList
	 * @Description: TODO(前台搜索时匹配出满足条件的商品) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/pro/list")
	public String goToProductList(HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取商品名称
		String pName = request.getParameter("pName");
		String wName = request.getParameter("wName");
		// 从页面获取分页数据
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
		// 得到所有的商品
		List<ProductCategory> pList = productCategoryService.getChildCategories(Cache.getResource("productCategory.dazhong"));
		List<Product> products = productService.getList(pager, null, wName, "2",pName);
		List<Warehouse> whs = warehouseService.list(null, null, "true");
		request.setAttribute("products", products);
		request.setAttribute("pList", pList);
		request.setAttribute("whs", whs);
		request.setAttribute("pager", pager);
		request.setAttribute("pName", pName);
		request.setAttribute("wName", wName);
		return "/web/purchase/channel/shopping/loadProList";
	}
	
	@RequestMapping(value="/web/storeweb/buttomLoad")
	public String getButtomXz(HttpServletRequest request){
	    // 从缓存获取优选商城帮助中心
	    request.setAttribute("yxHelp", Cache.getContentsByCategoryName("优选帮助中心"));
	    return "/web/shop/channel/commons/buttom";
	}
	@RequestMapping(value="/web/store/buttomLoad")
    public String getButtomDz(HttpServletRequest request){
        // 从缓存获取优选商城帮助中心
        request.setAttribute("dzHelp", Cache.getContentsByCategoryName("大宗帮助中心"));
        return "/web/purchase/channel/commons/buttom";
    }
	@RequestMapping(value = "/web/storeweb/channelConfigByLoad/{channelId}")
	public String getProduceDetailsByChannelId(@PathVariable String channelId, HttpServletRequest request) {
		// IProductCategoryService categoryService =
		// SpringContextHolder.getBean(IProductCategoryService.class);
		BasicRequest brequest = new BasicRequest(request);
		Map<ProductCategory, List<Product>> details = new LinkedHashMap<ProductCategory, List<Product>>();
		ProductCategory parentCategory = Cache.getProductCategory(channelId);
		if (parentCategory != null) {
			// 调用方法查询商品集合
			// List<Product> productList =
			// productService.getProductsByCategoryId(channelId,
			// ProductConst.PRODUCT_TYPE_XIAOZONG, 0);
			List<String> productIdList = Cache.getProductsByCategory(channelId, ProductConst.PRODUCT_TYPE_XIAOZONG);
			// 如果查出的商品集合不为空并且长度大于0，则放入Map中，key值为： 配置类型+配置位置
			List<Product> plist = new ArrayList<Product>();
			if (productIdList != null && productIdList.size() > 0) {
				for (int i = 0; i < productIdList.size(); i++) {
					// 根据ID去查询商品
					Product p = Cache.getProductById(productIdList.get(i));
					if (p != null) {
						p.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_PRODUCT, p.getId()));
						plist.add(p);
					}

				}
			}
			parentCategory.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_PRODUCT_CATEGORY, parentCategory.getId()));
			details.put(parentCategory, plist);
		}
		// 如果该条频道配置类型为商品分类
		List<String> categoryIdList = Cache.getChildProductCategorys(channelId);
		// 如果查出的商品分类集合不为空并且长度大于0，则放入Map中，key值为： 配置类型+配置位置
		if (categoryIdList != null && categoryIdList.size() > 0) {
			for (int j = 0; j < categoryIdList.size() && j < 3; j++) {
				String categoryId = categoryIdList.get(j);
				ProductCategory category = Cache.getProductCategory(categoryId);
				if (category != null) {
					// 获取分类ID从缓存中获取商品ID
					List<String> listProductId = Cache.getProductsByCategory(category.getId(), ProductConst.PRODUCT_TYPE_XIAOZONG);
					List<Product> plist = new ArrayList<Product>();
					if (listProductId != null && listProductId.size() > 0) {
						// 根据ID获取商品信息
						for (int i = 0; i < listProductId.size(); i++) {
							Product p = Cache.getProductById(listProductId.get(i));
							if (p != null) {
								p.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_PRODUCT, p.getId()));
								plist.add(p);
							}
						}
					}
					category.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_PRODUCT_CATEGORY, category.getId()));
					details.put(category, plist);
					// // 调用方法查询商品集合,查询优选商城的商品
					// List<Product> productList =
					// productService.getProductsByCategoryId(category.getId(),"1",
					// 0);
					// // 如果查出的商品集合不为空并且长度大于0，则放入Map中，key值为： 配置类型+配置位置
					// List<Product> plist = new ArrayList<Product>();
					// if (productList != null && productList.size() > 0) {
					// for (int m = 0; m < productList.size(); m++) {
					// Product p = productList.get(m);
					// if (p != null) {
					// p.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_PRODUCT,
					// p.getId()));
					// plist.add(p);
					// }
					// }
					// }
					// category.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_PRODUCT_CATEGORY,
					// category.getId()));
					// details.put(category, plist);
				}
			}
		}
		request.setAttribute("configMap", details);
		return "/web/shop/channel/index/indexPList";
	}

	// ***************************核心企业中心****************************************//
	@RequestMapping(value = "/store/storeweb/productByGysList")
	public String getProductListByGys(HttpServletRequest request) {
		String productName = request.getParameter("productName"); // 商品名称
		String storeName = request.getParameter("storeName"); // 供应商名称
		String currentpageStr = request.getParameter("pageNum"); // 页码

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

		List<SupplyProduct> supplyProduct = supplyProductService.getList(productName, storeName, "", pager);

		request.setAttribute("supplyProduct", supplyProduct);
		request.setAttribute("pager", pager);
		request.setAttribute("currentpageStr", currentPage);
		request.setAttribute("productName", productName);
		request.setAttribute("storeName", storeName);

		return "/userCenter/store/coreCompany/coreProductListByGys";
	}

	/**
	 * 
	 * @Title: loadById
	 * @Description: TODO(load出核心企业查看供应商议价详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/productList/Supple/load/{id}")
	public String loadById(@PathVariable String id, HttpServletRequest request) {
		// 根据ID获取商品详情信息
		SupplyProduct pro = supplyProductService.getSupplyProductById(id);
		if (pro.getStatus() != 0) {
			// 根据商品ID查询议价信息
			Inquiry inquiry = inquiryService.getInquiryByProductId(pro.getId());
			request.setAttribute("inquiry", inquiry);
			request.setAttribute("st", 0);
		}
		request.setAttribute("st", 1);
		request.setAttribute("product", pro);
		return "/userCenter/store/coreCompany/loadSupply";
	}

	/**
	 * @Title: InquiryAdd
	 * @Description: TODO(保存核心企业发起议价) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param session
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/storeInquiry/Add")
	@ResponseBody
	public String InquiryAdd(HttpServletRequest request, HttpSession session) {
		Inquiry in = new Inquiry();
		// 根据ID获取商品详情信息
		String userId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
		in.setBuyersId(userId);
		in.setLinkPerson(request.getParameter("linkPerson"));
		in.setNumber(Integer.parseInt(request.getParameter("number")));
		in.setLinkTel(request.getParameter("linkTel"));
		in.setPrice(new BigDecimal(request.getParameter("price")));
		in.setCreateTime(DateUtil.getSysDateTimeString());
		in.setNote(request.getParameter("note"));
		// 获取商品ID
		String id = request.getParameter("productid");
		SupplyProduct pro = supplyProductService.getSupplyProductById(id);
		in.setSellersId(pro.getUserId());
		in.setProductId(id);
		in.setUpdateTime(DateUtil.getSysDateTimeString());
		in.setStatus("0");
		if (!inquiryService.saveInq(in, pro)) {
			return "0";
		}
		return "1";
	}

	/**
	 * @Title: InquiryAdd
	 * @Description: TODO(保存采购商发起议价) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param session
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/storeInquiry/supplyAdd")
	@ResponseBody
	public String InquirySupplyAdd(HttpServletRequest request, HttpSession session) {
		Inquiry in = new Inquiry();
		// 根据ID获取商品详情信息
		String userId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
		in.setBuyersId(userId);
		in.setLinkPerson(request.getParameter("linkPerson"));
		in.setNumber(Integer.parseInt(request.getParameter("number")));
		in.setLinkTel(request.getParameter("linkTel"));
		in.setPrice(new BigDecimal(request.getParameter("price")));
		in.setCreateTime(DateUtil.getSysDateTimeString());
		in.setUpdateTime(DateUtil.getSysDateTimeString());
		in.setNote(request.getParameter("note"));
		// 获取商品ID
		String id = request.getParameter("productId");
		in.setSellersId(request.getParameter("sellersId"));
		in.setProductId(id);
		in.setStatus("0");
		if (!inquiryService.saveSupplyInq(in)) {
			return "0";
		}
		return "1";
	}
	
	@RequestMapping(value = "/web/wjbcProduct/jumpToListPage")
	public String jumpToListPage(HttpServletRequest request) {
		String channelId = request.getParameter("channelId");
		String curProCatgId = request.getParameter("curProCatgId");
		String childId = request.getParameter("childId");
		// 根据传进来的频道主键获取频道对象
		Channel channel = channelService.getChannelById(channelId);
		
		request.setAttribute("curProCatgId", curProCatgId);
		request.setAttribute("childId", childId);
		request.setAttribute("channel", channel);
		
		return "/web/channel/wjbctest";
	}
	
	
	@RequestMapping(value = "/web/wjbcProduct/list")
    public String wjbcProductList(HttpServletRequest request) {
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取商品根类
        String categoryRootId = request.getParameter("categoryRootId");
        // 从页面获取商品分类
        String categoryId = request.getParameter("categoryId");
        // 从页面获取分页数据
        String currentpageStr = request.getParameter("pageNum");
        String name = request.getParameter("pname");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");

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
        pager.setRowsPerPage(5);
        // 得到所有的商品
        List<Product> products = productService.getListShowByPcId(pager, categoryId,categoryRootId, name, minPrice, maxPrice);
        
        request.setAttribute("products", products);
        request.setAttribute("pager", pager);
        request.setAttribute("pageNum", currentPage);
        return "/web/channel/wjbc/loadProList1";
    }
	
	/**
	 * 查询页头部
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/wjbcProduct/listHeader")
	public String wjbcProductListHeader(HttpServletRequest request) {
		// 从页面获取商品根类
		String childId = request.getParameter("childId");
		String categoryRootId = request.getParameter("categoryRootId");
		List<ProductCategory> pcList = productCategoryService.getChildCategories(categoryRootId);
		request.setAttribute("pcList", pcList);
		request.setAttribute("childId", childId);
		return "/web/channel/wjbc/loadProListHeader";
	}
	
	/**
	 * 右侧展示商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/wjbcProduct/shows")
	public String wjbcProductShowByProCatgRootId(HttpServletRequest request) {
		// 从页面获取商品根类
		String categoryRootId = request.getParameter("categoryRootId");
		Pager pager = new Pager();
		pager.setRowsPerPage(3);
		List<Product> productList = productService.getListShowByPcId(pager, null,categoryRootId, null,null,null);
		request.setAttribute("productList", productList);
		return "/web/channel/wjbc/product_one";
	}
	

	/**
	 * 跳转到列表页
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/wjbcProduct/proShowIndex")
	public String jumpProductList(HttpServletRequest request) {

		String channelId = request.getParameter("channelId");
		String catgNum = request.getParameter("catg");
		
		// 根据传进来的频道主键获取频道对象
		Channel channel = channelService.getChannelById(channelId);
		// 获取产品分类
		List<ProductCategory> categories = Cache.getProductCategorys();
		String categoryRootId = categories.get(Integer.parseInt(catgNum)).getId();
		
		Pager pager = new Pager();
		pager.setRowsPerPage(4);

		ProductCategory pcatgRoot = productCategoryService.getCategoryById(categoryRootId);
		// 得到所有的商品
		List<ProductCategory> pcatg = productCategoryService.getChildCategories(categoryRootId);
		List<Product> products = productService.getListShowByPcId(pager, null,categoryRootId, null,null,null);
		request.setAttribute("pcatgRoot", pcatgRoot);
		request.setAttribute("pcatg", pcatg);
		request.setAttribute("products", products);
		request.setAttribute("channel", channel);

		return "/web/channel/lssc/proShowIndex";
	}
	
	@RequestMapping(value = "/web/indexProduct/list")
	public String gjsList(HttpServletRequest request){
	   // 定义分页
        Pager pager = new Pager();
        // 从页面获取分页数据
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
        pager.setRowsPerPage(4);
        // 得到所有的商品
        List<Product> gjsProducts = productService.getListShowByPcId(pager, Cache.getResource("gjsProduct"),null,null,null,null);
        request.setAttribute("gjsProducts", gjsProducts);
        request.setAttribute("gjsPc", Cache.getResource("gjsProduct"));
        request.setAttribute("pager", pager);
        request.setAttribute("nowYear", DateUtil.getSysDateString().substring(0,4));
        request.setAttribute("nowMeah", DateUtil.getSysDateString().substring(5,7));
        request.setAttribute("nowDate", DateUtil.getSysDateString().substring(8,10));
        return "/web/channel/index/loadList";
	}
	
	/**
	 * 
	* @Title: wjbcProductList
	* @Description: TODO(贵金属频道的商品信息展现)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param request
	* @return String 返回类型
	* @throws
	 */
	@RequestMapping(value = "/web/gjsProduct/list")
    public String gjsProductList(HttpServletRequest request) {
        // 定义分页
        Pager pager = new Pager();
        pager.setRowsPerPage(8);
        // 从页面获取分页数据
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
        pager.setRowsPerPage(8);
        // 得到所有的商品
        List<Product> gjsProducts = productService.getListShowByPcId(pager, Cache.getResource("gjsProduct"),null,null,null,null);
        List<Product> ysjsProducts = productService.getListShowByPcId(pager, Cache.getResource("ysjsProduct"),null,null,null,null);
        List<Product> xgjsProducts = productService.getListShowByPcId(pager, Cache.getResource("xgjsProduct"),null,null,null,null);
        List<Product> xtjsProducts = productService.getListShowByPcId(pager, Cache.getResource("xtjsProduct"),null,null,null,null);        
        request.setAttribute("gjsProducts", gjsProducts);
        request.setAttribute("ysjsProducts", ysjsProducts);
        request.setAttribute("xgjsProducts", xgjsProducts);
        request.setAttribute("xtjsProducts", xtjsProducts);
        request.setAttribute("gjsPc", Cache.getResource("gjsProduct"));
        request.setAttribute("ysjsPc", Cache.getResource("ysjsProduct"));
        request.setAttribute("xgjsPc", Cache.getResource("xgjsProduct"));
        request.setAttribute("xtjsPc", Cache.getResource("xtjsProduct"));
        request.setAttribute("nowDate", DateUtil.getSysDateString());
        request.setAttribute("pager", pager);
        
        return "/web/channel/gjs/loadProList";
    }
	
	/**
	 * 贵金属更多
	 */
	@RequestMapping(value = "/web/gjsGdProduct/list/{pcId}/{currentpageStr}")
	public String gjsGdProductList(@PathVariable String pcId,@PathVariable String currentpageStr,HttpServletRequest request) {
        // 定义分页
        Pager pager = new Pager();
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
        pager.setRowsPerPage(10);
        // 得到所有的商品
        List<Product> products = productService.getListShowByPcId(pager, pcId,null,null,null,null);
        // 得到分类名称
        String pcName = productCategoryService.getCategoryById(pcId).getName();
        request.setAttribute("products", products);
        request.setAttribute("nowDate", DateUtil.getSysDateString());
        request.setAttribute("pager", pager);
        request.setAttribute("pcId", pcId);
        request.setAttribute("pcName", pcName);
        return "/web/channel/gjs/gdProList";
    }
	
	@RequestMapping(value = "/web/gjsGdProduct/getProductList/{quantity}")
	public String getProductList(@PathVariable int quantity,String productName,HttpServletRequest request){
		Pager page = new Pager();
		// 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            currentPage = Integer.parseInt(currentpageStr);
        }
        // 设置要查询的页码
        page.setCurrentPage(currentPage);
        page.setRowsPerPage(10);
        List<Product> list=new ArrayList<Product>();
        List<Product> pList=productService.getListByQuantity(page, productName, quantity);
		if(pList != null && pList.size()>0){
			for (Product p : pList) {
				if(p == null){
					continue;
				}
				if(!StringUtil.isEmpty(p.getHabitat())){
				p.setHabitat(systemAreaService.getAreaFullNameByAreaIdAndDelimiter(p.getHabitat(), ""));
				}
				list.add(p);
			}
		}
        request.setAttribute("productList", list);
		request.setAttribute("pager", page);
        request.setAttribute("currentpageStr", currentpageStr);
        request.setAttribute("productName", productName);
		
		return "/web/channel/gqxx/loadPL";
	}
}
