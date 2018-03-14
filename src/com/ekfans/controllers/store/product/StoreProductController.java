package com.ekfans.controllers.store.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.ValuationCategory;
import com.ekfans.base.order.service.IValuationCategoryService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.ProductInfo;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.base.product.model.ProductPrice;
import com.ekfans.base.product.model.ProductTemplate;
import com.ekfans.base.product.model.ProductValuation;
import com.ekfans.base.product.model.TemplateFields;
import com.ekfans.base.product.service.IProductBrandService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductInfoDetailService;
import com.ekfans.base.product.service.IProductInfoService;
import com.ekfans.base.product.service.IProductPictureService;
import com.ekfans.base.product.service.IProductPriceService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.service.IProductTemplateService;
import com.ekfans.base.product.service.IProductValuationService;
import com.ekfans.base.product.service.ITemplateFieldsService;
import com.ekfans.base.product.util.ProductHelper;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.model.Area;
import com.ekfans.base.system.service.IAreaService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 店铺商品Controller
 * 
 * @Title: StoreProductController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-23 下午05:23:47 
 * @version V1.0
 */
@Controller
public class StoreProductController extends BasicController {

	@Autowired
	private IProductService productService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IProductBrandService productBrandService;
	@Autowired
	private IProductCategoryService productCategoryService;
	@Autowired
	private IProductTemplateService productTemplateService;
	@Autowired
	private ITemplateFieldsService templateFieldsService;
	@Autowired
	private IProductInfoService productInfoService;
	@Autowired
	private IProductInfoDetailService productInfoDetailService;
	@Autowired
	private IProductPictureService productPictureService;
	@Autowired
	private IProductPriceService productPriceService;
	@Autowired
	private IValuationCategoryService valuationCategory;
	@Autowired
	private IProductValuationService productValuationService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ISystemAreaService systemAreaService;

	/**
	 * 跳转到选择分类页面
	 */
	@RequestMapping(value = "/store/product/storeProductAddClassify")
	public String addClassify(HttpServletRequest request) {
		String type = request.getParameter("jugg");
		// 获取页面的name属性
		String name = request.getParameter("name");
		String productType = "";
		if ("0".equals(type)) {
			productType = Cache.getResource("cengpinProduct");
		} else if ("1".equals(type)) {
			productType = Cache.getResource("yuanlianProduct");
		} else {
			productType = Cache.getResource("lvseshangcheng");
		}
		// 查询出分类列表返回到页面
		List<ProductCategory> pcs = productCategoryService.getCategories(name, productType);
		request.setAttribute("pcs", pcs);
		request.setAttribute("type", type);
		return "/userCenter/store/product/storeProductAddClassify";
	}

	/**
	 * 点击菜单选项得到对应的下级菜单
	 */
	@RequestMapping(value = "/store/productCategory/plist/{id}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Object plist(@PathVariable String id) {
		// 利用Service的方法查找品牌
		List<ProductCategory> productCategorys = productCategoryService.getChildCategories(id);
		// 将品牌列表转换为json字符串
		String jsonResult = JsonUtil.listToJson(productCategorys);

		System.out.println("数据:" + jsonResult);
		return jsonResult;
	}

	/**
	 * 新增商品时搜索商品分类
	 */
	@RequestMapping(value = "/store/product/productCategory/search/{name}/{type}")
	public String search(@PathVariable String name, @PathVariable String type, HttpServletRequest request) {
		String productType = "";
		if (StringUtil.isEmpty(name)) {
			request.setAttribute("categories", null);
		} else {
			// name = StringUtil.ISO88591ToUTF8(name).trim();
			if ("0".equals(type)) {
				productType = Cache.getResource("cengpinProduct");
			} else {
				productType = Cache.getResource("yuanlianProduct");
			}
			// 利用Service的方法根据id查找分类
			Map<String, String> categories = productCategoryService.searchCategoriesByName(name, productType);
			request.setAttribute("categories", categories);
		}
		return "/userCenter/store/product/productCategorySearch";
	}

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/store/product/add/{type}")
	public String add(@PathVariable String type, HttpServletRequest request) {
		// 得到页面的商品分类Id
		String id = request.getParameter("pcId");
		// 从ProductCategory取得模板Id
		String templateId = productCategoryService.getTemplateByCategoryId(id);
		String pcName = productCategoryService.getCategoryFullNameByCategoryId(id, " > ");
		// 根据模板Id取得模板对象
		ProductTemplate productTemplate = productTemplateService.getProductTemplateByTempLateId(templateId);
		// 根据模板Id取得模板扩展字段集合
		List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(templateId);
		List<ValuationCategory> valuationCategotyList = valuationCategory.getValuationCategory();
		// 得到大区
		List<Area> areaList = areaService.getList(null);
		request.setAttribute("areaList", areaList);
		request.setAttribute("valuationCategotyList", valuationCategotyList);
		request.setAttribute("pcName", pcName);
		request.setAttribute("pcId", id);
		request.setAttribute("type", type);
		request.setAttribute("productTemplate", productTemplate);
		request.setAttribute("templateFields", templateFields);
		return "/userCenter/store/product/storeProductAdd";
	}

	/**
	 * 删除商品
	 */
	@RequestMapping(value = "/store/product/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		// 利用Service的方法根据id删除商品s
		productService.deleteProduct(id);
		// 删除成功，返回true
		return true;
	}

	/**
	 * 查询商品信息（根据商品Id取得所有信息放到修改页面）
	 */
	@RequestMapping(value = "/store/product/edit/{id}/{returnType}/{type}")
	public String edit(@PathVariable String id, @PathVariable String returnType, @PathVariable String type) {
		try {
			// 利用Service的方法根据id查找商品
			Product product = productService.getProductById(id);
			// 如果product不为空
			if (product == null) {
				return "redirect:/store/product/sales/" + type;
			}

			// 获取商品所关联的店铺对象
			Store store = storeService.getStore(product.getStoreId());
			String storeName = "";
			// 如果店铺存在，则取店铺名
			if (store != null) {
				storeName = store.getStoreName();
			}

			// 获取商品所关联的品牌
			String brandName = "";
			if (!StringUtil.isEmpty(product.getBrand())) {
				ProductBrand brand = productBrandService.getBrand(product.getBrand());
				// 得到productBrand的name值
				if (brand != null) {
					brandName = brand.getName();
				}
			}

			// 根据商品的主分类获取该商品所需要关联的模板ID
			String templateId = productCategoryService.getTemplateByCategoryId(product.getMainCategory());
			// 得到分类名
			String categoryName = productCategoryService.getCategoryFullNameByCategoryId(product.getMainCategory(), " > ");
			// 根据模板Id取得模板对象
			ProductTemplate productTemplate = productTemplateService.getProductTemplateByTempLateId(templateId);
			// 根据模板Id取得模板扩展字段集合
			List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(templateId);
			// 获取商品多角度视图集合
			List<ProductPicture> pictureList = productPictureService.getList(id);
			// 获取商品的价格区间集合
			List<ProductPrice> priceList = productPriceService.getProductPriceByProductId(id);

			// 获取一级分类
			List<ProductCategory> pcs = productCategoryService.getCategories(null, null);

			List<ValuationCategory> valuationCategotyList = valuationCategory.getValuationCategory();
			getRequest().setAttribute("valuationCategotyList", valuationCategotyList);
			List<ProductValuation> pvList = productValuationService.getListByProduct(id);
			// 得到大区
			List<Area> areaList = areaService.getList(null);
			getRequest().setAttribute("areaList", areaList);
			getRequest().setAttribute("pvList", pvList);
			getRequest().setAttribute("returnType", returnType);
			getRequest().setAttribute("pType", type);
			getRequest().setAttribute("pcs", pcs);
			getRequest().setAttribute("product", product);
			getRequest().setAttribute("storeName", storeName);
			getRequest().setAttribute("categoryName", categoryName);
			getRequest().setAttribute("brandName", brandName);
			getRequest().setAttribute("productTemplate", productTemplate);
			getRequest().setAttribute("templateFields", templateFields);
			getRequest().setAttribute("pictureList", pictureList);
			getRequest().setAttribute("priceList", priceList);
			return "/userCenter/store/product/productModify";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * load商品信息（根据商品Id取得所有信息放到修改页面）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/product/editLoad/{id}/{categoryId}")
	public String editLoad(@PathVariable String id, @PathVariable String categoryId, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找商品
			Product product = productService.getProductById(id);
			// 如果product不为空
			if (product == null) {
				return "redirect:/store/product/sales";
			}

			// 获取商品所关联的店铺对象
			Store store = storeService.getStore(product.getStoreId());
			String storeName = "";
			// 如果店铺存在，则取店铺名
			if (store != null) {
				storeName = store.getStoreName();
			}

			// 获取商品所关联的品牌
			String brandName = "";
			if (!StringUtil.isEmpty(product.getBrand())) {
				ProductBrand brand = productBrandService.getBrand(product.getBrand());
				// 得到productBrand的name值
				if (brand != null) {
					brandName = brand.getName();
				}
			}

			// 从ProductCategory取得模板Id
			String templateId = productCategoryService.getTemplateByCategoryId(categoryId);
			// 根据模板Id取得模板对象
			ProductTemplate productTemplate = productTemplateService.getProductTemplateByTempLateId(templateId);
			// 根据模板Id取得模板扩展字段集合
			List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(templateId);
			// 获取商品多角度视图集合
			List<ProductPicture> pictureList = productPictureService.getList(id);
			request.setAttribute("product", product);
			request.setAttribute("storeName", storeName);
			request.setAttribute("brandName", brandName);
			request.setAttribute("productTemplate", productTemplate);
			request.setAttribute("templateFields", templateFields);
			request.setAttribute("pictureList", pictureList);
			return "/userCenter/store/product/productModifyLoad";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * load出模板扩展字段的对象
	 */
	@RequestMapping(value = "/store/product/tempfieldsLoad/{id}/{productId}")
	public String temfieldsLoad(@PathVariable String id, @PathVariable String productId, HttpServletRequest request) {
		// 根据模板Id取得模板扩展字段集合
		List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(id);
		request.setAttribute("templateFields", templateFields);

		// 取商品Info表的集合
		Map<String, ProductInfo> infoMap = new HashMap<String, ProductInfo>();
		List<ProductInfo> infoList = null;
		// 定义一个MAP<模板ID，模板值<值，图片>>
		Map<String, Map<String, String>> detailMap = new HashMap<String, Map<String, String>>();
		try {
			// 如果商品编号不为空，则将商品的模板取出来，放入Attribute中
			if (!StringUtil.isEmpty(productId)) {
				infoList = productInfoService.getProductInfoByProductId(productId);

				if (infoList != null && infoList.size() > 0) {
					for (int i = 0; i < infoList.size(); i++) {
						ProductInfo info = infoList.get(i);
						if (info != null && !StringUtil.isEmpty(info.getTemplateFieldId())) {
							infoMap.put(info.getTemplateFieldId(), info);
						}
					}
				}

				// 去商品模板明细表的集合
				List<ProductInfoDetail> infoDetailList = productInfoDetailService.getProductInfoDetailByProductId(productId);

				// 如果查出的模板明细表集合不为空并且长度大于0，则便利
				if (infoDetailList != null && infoDetailList.size() > 0) {
					for (int i = 0; i < infoDetailList.size(); i++) {
						// 取出模板明细
						ProductInfoDetail infoDetail = infoDetailList.get(i);
						if (infoDetail == null) {
							continue;
						}

						// 如果infoId1不为空 -- 将值放入Map
						if (!StringUtil.isEmpty(infoDetail.getInfoId1()) && !StringUtil.isEmpty(infoDetail.getInfoValue1())) {
							ProductHelper.configTempDetailInfoMap(detailMap, infoDetail.getInfoId1(), infoDetail.getInfoValue1(), infoDetail.getInfoPic1());
						}

						// 如果infoId2不为空 -- 将值放入Map
						if (!StringUtil.isEmpty(infoDetail.getInfoId2()) && !StringUtil.isEmpty(infoDetail.getInfoValue2())) {
							ProductHelper.configTempDetailInfoMap(detailMap, infoDetail.getInfoId2(), infoDetail.getInfoValue2(), infoDetail.getInfoPic2());
						}

						// 如果infoId3不为空 -- 将值放入Map
						if (!StringUtil.isEmpty(infoDetail.getInfoId3()) && !StringUtil.isEmpty(infoDetail.getInfoValue3())) {
							ProductHelper.configTempDetailInfoMap(detailMap, infoDetail.getInfoId3(), infoDetail.getInfoValue3(), infoDetail.getInfoPic3());
						}

						// 如果infoId4不为空 -- 将值放入Map
						if (!StringUtil.isEmpty(infoDetail.getInfoId4()) && !StringUtil.isEmpty(infoDetail.getInfoValue4())) {
							ProductHelper.configTempDetailInfoMap(detailMap, infoDetail.getInfoId4(), infoDetail.getInfoValue4(), infoDetail.getInfoPic4());
						}

					}
				}
				request.setAttribute("infoDetailList", infoDetailList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 将值放入request中
		request.setAttribute("detailMap", detailMap);
		request.setAttribute("infoMap", infoMap);

		return "/userCenter/store/product/tempfieldsLoad";
	}

	/**
	 * load出模板扩展字段的对象
	 */
	@RequestMapping(value = "/store/product/tempfieldsCheck/{productId}/{fieldId1}/{tempVal1}/{fieldId2}/{tempVal2}/{fieldId3}/{tempVal3}/{fieldId4}/{tempVal4}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String temfieldsCheck(@PathVariable String productId, @PathVariable String fieldId1, @PathVariable String tempVal1, @PathVariable String fieldId2,
			@PathVariable String tempVal2, @PathVariable String fieldId3, @PathVariable String tempVal3, @PathVariable String fieldId4, @PathVariable String tempVal4,
			HttpServletRequest request) {
		// 将传过来的Id中的横线截取出来
		if (!StringUtil.isEmpty(fieldId1) && fieldId1.indexOf("-") != -1) {
			fieldId1 = fieldId1.substring(fieldId1.indexOf("-") + 1, fieldId1.length());
			tempVal1 = StringUtil.ISO88591ToUTF8(tempVal1);
		}
		// 将传过来的Id中的横线截取出来
		if (!StringUtil.isEmpty(fieldId2) && fieldId2.indexOf("-") != -1) {
			fieldId2 = fieldId2.substring(fieldId2.indexOf("-") + 1, fieldId2.length());
			tempVal2 = StringUtil.ISO88591ToUTF8(tempVal2);
		}
		// 将传过来的Id中的横线截取出来
		if (!StringUtil.isEmpty(fieldId3) && fieldId3.indexOf("-") != -1) {
			fieldId3 = fieldId3.substring(fieldId3.indexOf("-") + 1, fieldId3.length());
			tempVal3 = StringUtil.ISO88591ToUTF8(tempVal3);
		}
		// 将传过来的Id中的横线截取出来
		if (!StringUtil.isEmpty(fieldId4) && fieldId4.indexOf("-") != -1) {
			fieldId4 = fieldId4.substring(fieldId4.indexOf("-") + 1, fieldId4.length());
			tempVal3 = StringUtil.ISO88591ToUTF8(tempVal3);
		}

		// 调用Service方法获取商品模板明细值
		ProductInfoDetail infoDetail = productInfoDetailService.getInfoDetailByProductIdAndTemps(productId, fieldId1, tempVal1, fieldId2, tempVal2, fieldId3, tempVal3, fieldId4,
				tempVal4);
		// 定义返回的JsonMap
		Map<String, Object> valueMap = new HashMap<String, Object>();
		if (infoDetail != null) {
			valueMap.put("check", true);
			valueMap.put("price", infoDetail.getPrice());
			valueMap.put("quantity", infoDetail.getQuantity());
			valueMap.put("quantityWarning", infoDetail.getQuantityWarning());
			valueMap.put("detailId", infoDetail.getId());
		} else {
			valueMap.put("check", false);
		}
		String jsonResult = JsonUtil.convertToJsonString(valueMap);

		return jsonResult;
	}

	/**
	 * 新增商品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/product/save")
	@ResponseBody
	public Object save(Product product, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Store store = (Store) session.getAttribute(SystemConst.STORE);
		if (store == null) {
			return "redirect:/store/";
		}
		// 验证商品编号和商品名是否为空
		if (StringUtil.isEmpty(product.getName()) || StringUtil.isEmpty(product.getProductNumber())) {
			// 如果为空，返回添加失败
			return "false";
		}

		// 检测商品编号是否存在，如果存在，则返回失败
		if (!productService.checkProductId(product.getProductNumber(), product.getId())) {
			// 返回添加失败
			return false;
		}

		// 设置详细地址
//		if (!StringUtil.isEmpty(product.getHabitat())) {
//			String habitatAddress = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(product.getHabitat(), "");
//			product.setHabitatAddress(habitatAddress);
//		}

		// 设置交货地址ID
		if (StringUtil.isEmpty(product.getDeliceAddressId())) {
			String deliceAddressId = product.getDeliceAddress();
			product.setDeliceAddressId(deliceAddressId);
		}		
		
		// 设置交货地址
		if (!StringUtil.isEmpty(product.getDeliceAddress())) {
			String deliceAddress = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(product.getDeliceAddress(), "");
			product.setDeliceAddress(deliceAddress);
		}
		
				
		// 设置店铺
		product.setStoreId(store.getId());

		// 设置模板ID
		String templateId = request.getParameter("proTemplate");
		product.setTemplateId(templateId);

		// 设置描述
		String description = request.getParameter("description");
		product.setDescription(description);
		String systemCheckstatus = Cache.getSystemParamConfig("是否需要审核");
		if ("0".equals(systemCheckstatus)) {
			product.setCheckStatus(1);
		} else {
			product.setCheckStatus(0);
		}

		// 利用Service的方法添加商品
		if (productService.saveProduct(product, request, response)) {
			return true;
		}
		return false;
	}

	/**
	 * 修改商品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/product/modify")
	@ResponseBody
	public Object modify(Product product, HttpServletRequest request, HttpServletResponse response) {
		try {

			// 验证商品编号和商品名是否为空
			if (StringUtil.isEmpty(product.getName()) || StringUtil.isEmpty(product.getProductNumber()) || StringUtil.isEmpty(product.getStoreId())) {
				// 如果为空，返回添加失败
				return "false";
			}

			// 检测商品编号是否存在，如果存在，则返回失败
			if (!productService.checkProductId(product.getProductNumber(), product.getId())) {
				// 返回添加失败
				return false;
			}

			// 设置模板ID
			String templateId = request.getParameter("proTemplate");
			product.setTemplateId(templateId);

			// 设置描述
			String description = request.getParameter("description");
			product.setDescription(description);

			// 设置详细地址
//			if (!StringUtil.isEmpty(product.getHabitat())) {
//				String habitatAddress = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(product.getHabitat(), "");
//				product.setHabitatAddress(habitatAddress);
//			}
			
			
			// 设置交货地址ID
			if (StringUtil.isEmpty(product.getDeliceAddressId())) {
				String deliceAddressId = product.getDeliceAddress();
				product.setDeliceAddressId(deliceAddressId);
			}		
			
			// 设置交货地址
			if (!StringUtil.isEmpty(product.getDeliceAddress())) {
				String deliceAddress = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(product.getDeliceAddress(), "");
				product.setDeliceAddress(deliceAddress);
			}

			// 后台默认审核通过
			String systemCheckstatus = Cache.getSystemParamConfig("是否需要审核");
			if ("0".equals(systemCheckstatus)) {
				product.setCheckStatus(1);
			} else {
				product.setCheckStatus(0);
			}

			// 利用Service的方法更新商品
			if (productService.updateProduct(product, "", request, response)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 添加失败，设置ok值为false返回
			request.setAttribute("addOk", false);
		}
		return false;
	}

	/**
	 * 检查商品编号是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/product/checkId/{productNumber}/{productId}")
	@ResponseBody
	public Object checkId(@PathVariable String productNumber, @PathVariable String productId) {
		if (productService.checkProductId(productNumber, productId)) {
			// 如果id不存在，返回true
			return true;
		}
		return false;
	}

	/**
	 * 新增商品时查询品牌列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/product/brand/plist")
	public String plist(HttpServletRequest request) {
		try {
			// 定义分页
			Pager pager = new Pager();
			// 从页面获取品牌名称
			String name = request.getParameter("brandName");
			// 从页面获取页码
			String currentpageStr = request.getParameter("pageNum");
			// 从页面获取品牌状态
			String status = request.getParameter("status");
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
			// 利用Service的方法查找品牌
			List<ProductBrand> productBrand = productBrandService.getBrands(pager, name, status);
			request.setAttribute("productBrand", productBrand);
			request.setAttribute("pager", pager);
			request.setAttribute("name", name);
			request.setAttribute("status", status);
			return "/userCenter/store/product/productAndBrand";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 品牌弹出层的搜索
	 */
	@RequestMapping(value = "/store/product/brand/plistSearch/{name}/{page}")
	public String plistSearch(@PathVariable String name, @PathVariable String page, HttpServletRequest request) {
		try {
			name = StringUtil.ISO88591ToUTF8(name);
			// 定义分页
			Pager pager = new Pager();
			// 将从页面获取的分页数据转化成int型
			int currentPage = 1;
			if (!StringUtil.isEmpty(page)) {
				try {
					currentPage = Integer.parseInt(page);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 设置要查询的页码
			pager.setCurrentPage(currentPage);
			// 利用Service的方法查找品牌
			List<ProductBrand> productBrand = productBrandService.getBrands(pager, name, null);
			request.setAttribute("productBrand", productBrand);
			request.setAttribute("pager", pager);
			if (!StringUtil.isEmpty(name)) {
				request.setAttribute("brandName", name);
			} else {
				request.setAttribute("brandName", "");
			}

			return "/userCenter/store/product/productAndBrand";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 出售中的商品
	 */
	@RequestMapping(value = "/store/product/sales/{type}")
	public String sales(@PathVariable String type, HttpServletRequest request) {
		// 从缓存中取得storeId
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		String storeId = store.getId();
		// 定义分页
		Pager pager = new Pager();
		String name = request.getParameter("name");
		// String brand = request.getParameter("brand");
		String minUnitPrice = request.getParameter("minUnitPrice");
		String maxUnitPrice = request.getParameter("maxUnitPrice");
		String productNumber = request.getParameter("productNumber");
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
		pager.setRowsPerPage(10);
		// List<Product> products = null;
		// // 利用Service的方法查找商品
		// if(StringUtil.isEmpty(brand)){
		// products = productService.getProductsWithoutBrand(pager,
		// name, "true", "", storeId, minUnitPrice, maxUnitPrice);
		// }else{
		// products = productService.listProduct(pager, name, "true",
		// brand, "", storeId, minUnitPrice, maxUnitPrice);
		// }
		String productType = "";
		if ("0".equals(type)) {
			productType = Cache.getResource("cengpinProduct");
		} else if ("1".equals(type)) {
			productType = Cache.getResource("yuanlianProduct");
		} else {
			productType = Cache.getResource("lvseshangcheng");
		}
		List<Product> products = productService.getProductsWithoutBrand(pager, name, "true", "1", "", storeId, minUnitPrice, maxUnitPrice, null, productNumber, null, productType);

		// 定义返回的参数Map
		request.setAttribute("pager", pager);
		request.setAttribute("type", type);
		request.setAttribute("products", products);
		request.setAttribute("name", name);
		// request.setAttribute("brand", brand);
		request.setAttribute("minUnitPrice", minUnitPrice);
		request.setAttribute("maxUnitPrice", maxUnitPrice);
		request.setAttribute("productNumber", productNumber);
		return "/userCenter/store/product/productList";
	}

	/**
	 * 
	 * @Title: stocks
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/product/stocks/{type}")
	public String stocks(@PathVariable String type, HttpServletRequest request) {
		// 从缓存中取得userId
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		String storeId = store.getId();
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取商品名称
		String name = request.getParameter("name");
		String min = request.getParameter("minUnitPrice");
		// String brand = request.getParameter("brand");
		String max = request.getParameter("maxUnitPrice");
		String productNumber = request.getParameter("productNumber");
		// 从页面获取分页数据
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(10);
		// List<Product> products = null;
		// // 利用Service的方法查找商品
		// if(StringUtil.isEmpty(brand)){
		// products = productService.getProductsWithoutBrand(pager,
		// name, "false", "", storeId, min, max);
		// }else{
		// products = productService.listProduct(pager, name, "false",
		// brand, "", storeId, min, max);
		// }
		String productType = "";
		if ("0".equals(type)) {
			productType = Cache.getResource("cengpinProduct");
		} else if ("1".equals(type)) {
			productType = Cache.getResource("yuanlianProduct");
		} else {
			productType = Cache.getResource("lvseshangcheng");
		}
		List<Product> products = productService.getProductsWithoutBrand2(pager, name, "", storeId, min, max, productNumber, null, productType);
		request.setAttribute("type", type);
		request.setAttribute("pager", pager);
		request.setAttribute("products", products);
		request.setAttribute("name", name);
		request.setAttribute("minUnitPrice", min);
		request.setAttribute("maxUnitPrice", max);
		request.setAttribute("productNumber", productNumber);
		return "/userCenter/store/product/productFactory";
	}

	/**
	 * 
	 * @Title: getProductList
	 * @Description: TODO(销售挂牌商品列表展示) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "/web/channel/xsgp/{pageNum}")
	public String getProductList(@PathVariable String pageNum, HttpServletRequest request) {
		// 获取页面搜索项
		// 分类名
		String categoryName = request.getParameter("categoryName");
		// 品名
		String name = request.getParameter("name");
		// 交易地址
		String address = request.getParameter("address");

		// 主分类的分类id
		String mainCatgId = request.getParameter("mainCatgId");

		// 定义分页
		Pager pager = new Pager();
		if (!StringUtil.isEmpty(pageNum)) {
			pager.setCurrentPage(Integer.parseInt(pageNum));
		}
		pager.setRowsPerPage(20);
		// 查询商品列表
		List<Product> products = productService.getListByParams(pager, categoryName, address, name, mainCatgId);

		// 返回页面的值
		request.setAttribute("products", products);
		request.setAttribute("pager", pager);
		request.setAttribute("name", name);
		request.setAttribute("categoryName", categoryName);
		return "/web/channel/xsgp/xsgpList";
	}

	/**
	 * 
	 * @Title: getProductList
	 * @Description: TODO(销售挂牌商品列表展示) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "/web/channel/xsgpList")
	public String xsgpList(String num, HttpServletRequest request) {

		// 定义分页
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setRowsPerPage(Integer.parseInt(num));
		pager.setRowsPerPage(7);
		// 查询商品列表
		List<Product> products = productService.getListByParams(pager, null, null, null, Cache.getResource("cengpinProduct"));

		// 返回页面的值
		request.setAttribute("products", products);
		request.setAttribute("pager", pager);
		return "/web/channel/xsgp/xhjy_xhgp";
	}

	@RequestMapping(value = "/web/channel/xsgp/categoryNameList")
	public String getCategoryNameList(HttpServletRequest request) {
		List<String> categories = productCategoryService.getCategoryNames();
		request.setAttribute("categories", categories);
		return "/web/channel/xsgp/categoryNameList";
	}
}
