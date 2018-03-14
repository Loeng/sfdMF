package com.ekfans.controllers.system.product;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.service.IProductCacheService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ekfans.base.order.model.ValuationCategory;
import com.ekfans.base.order.service.IValuationCategoryService;
import com.ekfans.base.product.dao.ISupplyProductDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.ProductInfo;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.base.product.model.ProductPrice;
import com.ekfans.base.product.model.ProductPriceChange;
import com.ekfans.base.product.model.ProductTemplate;
import com.ekfans.base.product.model.ProductValuation;
import com.ekfans.base.product.model.SupplyProduct;
import com.ekfans.base.product.model.TemplateFields;
import com.ekfans.base.product.service.IProductBrandService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductInfoDetailService;
import com.ekfans.base.product.service.IProductInfoService;
import com.ekfans.base.product.service.IProductPictureService;
import com.ekfans.base.product.service.IProductPriceChangeService;
import com.ekfans.base.product.service.IProductPriceService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.service.IProductTemplateService;
import com.ekfans.base.product.service.IProductValuationService;
import com.ekfans.base.product.service.ISupplyProductService;
import com.ekfans.base.product.service.ITemplateFieldsService;
import com.ekfans.base.product.util.ProductHelper;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.model.Area;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.service.IAreaService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.ExcelUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.ZipUtil;

@Controller
public class SystemProductController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SystemProductController.class);

	// 定义SERVICE
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
	private ISystemAreaService systemAreaService;

	@Autowired
	private IProductPriceService productPriceService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ISupplyProductService supplyProductService;

	@Autowired
	private ISupplyProductDao supplyProductDao;
	@Autowired
    private IValuationCategoryService valuationCategory;
    @Autowired
    private IProductValuationService productValuationService;
    @Autowired
    private IAreaService areaService;
    
    @Autowired
    private IProductPriceChangeService changeService;
	/**
	 * 查找商品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/list/{type}")
	public String list(@PathVariable String type,HttpServletRequest request) {
		String status = request.getParameter("status"); // 商品状态
		String name = request.getParameter("name"); // 商品名称
		String storeName = request.getParameter("storeName"); // 店铺名
		String minUnitPrice = request.getParameter("minUnitPrice"); // 价格区间(小)
		String maxUnitPrice = request.getParameter("maxUnitPrice"); // 价格区间（大）
		String currentpageStr = request.getParameter("pageNum"); // 页码
		String productNumber = request.getParameter("productNumber"); // 商品编号
		
		 // 从页面得到分类
        String categoryId = request.getParameter("categoryId");
		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		pager.setCurrentPage(currentPage);
		String productType = "";
        if("0".equals(type)){
            productType = Cache.getResource("cengpinProduct");
        }else if("1".equals(type)){
            productType = Cache.getResource("yuanlianProduct");
        }else{
        	productType = Cache.getResource("lvseshangcheng");
        }
		List<Product> products = productService.getProductsWithoutBrand(pager, name, status, "1", storeName, "", minUnitPrice, maxUnitPrice, null, productNumber,categoryId,productType);
		if (!StringUtil.isEmpty(categoryId)) {
            // 回现关联分类
		    ProductCategory cc = this.productCategoryService
                    .getCategoryById(categoryId);
            String[] ccIds = cc.getFullPath().split("_");
            for (int i = 0; i < ccIds.length; i++) {
                if (i == 0 && ccIds.length >= 2)
                    request.setAttribute("twoContentCategory",
                            productCategoryService
                                    .getChildCategories(ccIds[i+1]));
                if (i == 1 && ccIds.length >= 3)
                    request.setAttribute("threeContentCategory",
                            productCategoryService
                                    .getChildCategories(ccIds[i+1]));
            }
            request.setAttribute("ccIds", ccIds);
        }

        // 一级咨询
        List<ProductCategory> oneContentCategory = productCategoryService
                .getCategories(null,null);
        request.setAttribute("oneContentCategory", oneContentCategory);
        request.setAttribute("categoryId", categoryId);
		request.setAttribute("pager", pager);
		request.setAttribute("products", products);
		request.setAttribute("status", status);
		request.setAttribute("type", type);
		request.setAttribute("name", name);
		request.setAttribute("storeName", storeName);
		request.setAttribute("minUnitPrice", minUnitPrice);
		request.setAttribute("maxUnitPrice", maxUnitPrice);
		request.setAttribute("productNumber", productNumber);
		return "/system/product/productList";
	}

	
	/**
     * 
     * @Title: getContentChildCategory
     * @Description: TODO(关联商品分类-查询子分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/system/product/add/categoryChild", method = RequestMethod.POST)
    public void getContentChildCategory(HttpServletRequest request, HttpServletResponse response) {
        // 获取当前的分类Id
        String id = request.getParameter("id");
        // 查询出当前咨询的子分类,并将其转为JSON字符串返回
        List<ProductCategory> categories = productCategoryService.getChildCategories(id);
        String JsonStr = JsonUtil.listToJson(categories);
        try {
        	response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	/**
	 * 商品审核
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/checkList/{type}")
	public ModelAndView check(@PathVariable String type,HttpServletRequest request) {

		// 定义分页
		Pager pager = new Pager();

		// 从页面获取商品名称
		String name = request.getParameter("name");
		// 从页面获取商品状态
		String status = request.getParameter("status");
		// 从页面获取商品品牌
		String brand = request.getParameter("brand");
		String storeId = request.getParameter("storeId");
		String productNumber = request.getParameter("productNumber");
		String minUnitPrice = request.getParameter("minUnitPrice");
		String maxUnitPrice = request.getParameter("maxUnitPrice");
		// 从页面获取分页数据
		String currentpageStr = request.getParameter("pageNum");

		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		String productType = "";
        if("0".equals(type)){
            productType = Cache.getResource("cengpinProduct");
        }else if("1".equals(type)){
            productType = Cache.getResource("yuanlianProduct");
        }else{
        	productType = Cache.getResource("lvseshangcheng");
        }
		// 利用Service的方法查找商品
		List<Product> products = productService.listUncheckProduct(pager, productNumber, name, status, brand, storeId, minUnitPrice, maxUnitPrice,productType);

		// 定义返回的参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("pager", pager);
		map.put("products", products);
		map.put("name", name);
		map.put("status", status);
		map.put("brand", brand);
		map.put("storeId", storeId);
		map.put("productNumber", productNumber);
		map.put("minUnitPrice", minUnitPrice);
		map.put("maxUnitPrice", maxUnitPrice);
		return new ModelAndView("system/product/productCheckList", map);

	}

	/**
	 * 删除商品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		// 利用Service的方法根据id删除商品s
		productService.deleteProduct(id);
		// 删除成功，返回true
		return true;
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/add")
	public String add(HttpServletRequest request) {
		// 得到页面的商品分类Id
		String id = request.getParameter("pcId");
		String pcName = request.getParameter("pcName");
		// 根据得到的商品分类Id取得ProductCategory
		ProductCategory productCategory = productCategoryService.getCategoryById(id);
		// 从ProductCategory取得模板Id
		String templateId = productCategoryService.getTemplateByCategoryId(productCategory.getId());
		String productCategoryName = productCategory.getName();
		// 根据模板Id取得模板对象
		ProductTemplate productTemplate = productTemplateService.getProductTemplateByTempLateId(templateId);
		// 根据模板Id取得模板扩展字段集合
		List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(templateId);

		request.setAttribute("pcName", pcName);
		request.setAttribute("productCategoryName", productCategoryName);
		request.setAttribute("pcId", id);
		request.setAttribute("productCategory", productCategory);
		request.setAttribute("productTemplate", productTemplate);
		request.setAttribute("templateFields", templateFields);
		return "system/product/systemProductAdd";
	}

	/**
	 * 查询商品信息（根据商品Id取得所有信息放到修改页面）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/edit/{id}/{type}")
	public String edit(@PathVariable String id,@PathVariable String type, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找商品
			Product product = productService.getProductById(id);
			// 如果product不为空
			if (product == null) {
				return "redirect:/system/product/list";
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
			String categoryName = productCategoryService.getCategoryFullNameByCategoryId(product.getMainCategory(), ">");
			// 根据模板Id取得模板对象
			ProductTemplate productTemplate = productTemplateService.getProductTemplateByTempLateId(templateId);
			// 根据模板Id取得模板扩展字段集合
			List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(templateId);
			// 获取商品多角度视图集合
			List<ProductPicture> pictureList = productPictureService.getList(id);
			// 获取商品的价格区间集合
			List<ProductPrice> priceList = productPriceService.getProductPriceByProductId(id);

			// 获取一级分类
			List<ProductCategory> pcs = productCategoryService.getCategories(null,null);
			List<ValuationCategory> valuationCategotyList = valuationCategory.getValuationCategory();
            getRequest().setAttribute("valuationCategotyList", valuationCategotyList);
            List<ProductValuation> pvList = productValuationService.getListByProduct(id);
            getRequest().setAttribute("pvList", pvList);
            //得到大区
            List<Area> areaList = areaService.getList(null);
            request.setAttribute("areaList", areaList);
			request.setAttribute("pcs", pcs);
			request.setAttribute("product", product);
			request.setAttribute("storeName", storeName);
			request.setAttribute("categoryName", categoryName);
			request.setAttribute("brandName", brandName);
			request.setAttribute("productTemplate", productTemplate);
			request.setAttribute("templateFields", templateFields);
			request.setAttribute("pictureList", pictureList);
			request.setAttribute("priceList", priceList);
			request.setAttribute("type", type);
			return "/system/product/productModify";
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
	@RequestMapping(value = "/system/product/editLoad/{id}/{categoryId}")
	public String editLoad(@PathVariable String id, @PathVariable String categoryId, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找商品
			Product product = productService.getProductById(id);
			// 如果product不为空
			if (product == null) {
				return "redirect:/system/product/list";
			}

			// 获取商品所关联的店铺对象
			Store store = storeService.getStore(product.getStoreId());
			String storeName = "";
			// 如果店铺存在，则取店铺名
			if (store != null) {
				storeName = store.getStoreName();
			}
			product.setMainCategory(categoryId);
			// 得到分类名
			String categoryName = productCategoryService.getCategoryFullNameByCategoryId(product.getMainCategory(), ">");

			// 获取商品所关联的品牌
			String brandName = "";
			if (!StringUtil.isEmpty(product.getBrand())) {
				ProductBrand brand = productBrandService.getBrand(product.getBrand());
				// 得到productBrand的name值
				if (brand != null) {
					brandName = brand.getName();
				}
			}

			// 获取商品多角度视图集合
			List<ProductPicture> pictureList = productPictureService.getList(id);
			// 获取商品的价格区间集合
			List<ProductPrice> priceList = productPriceService.getProductPriceByProductId(id);
			// 从ProductCategory取得模板Id
			String templateId = productCategoryService.getTemplateByCategoryId(categoryId);
			// 根据模板Id取得模板对象
			ProductTemplate productTemplate = productTemplateService.getProductTemplateByTempLateId(templateId);
			// 根据模板Id取得模板扩展字段集合
			List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(templateId);
			// 获取一级分类
			List<ProductCategory> pcs = productCategoryService.getCategories(null,null);
			request.setAttribute("pcs", pcs);
			request.setAttribute("product", product);
			request.setAttribute("storeName", storeName);
			request.setAttribute("brandName", brandName);
			request.setAttribute("productTemplate", productTemplate);
			request.setAttribute("templateFields", templateFields);
			request.setAttribute("pictureList", pictureList);
			request.setAttribute("priceList", priceList);
			request.setAttribute("categoryName", categoryName);
			return "/system/product/productModify";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: temfieldsLoad
	 * @Description: TODO(load出模板扩展字段的对象) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/product/tempfieldsLoad/{id}/{productId}")
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
			// TODO: handle exception
			e.printStackTrace();
		}

		// 将值放入request中
		request.setAttribute("detailMap", detailMap);
		request.setAttribute("infoMap", infoMap);

		return "system/product/tempfieldsLoad";
	}

	/**
	 * 
	 * @Title: temfieldsLoad
	 * @Description: TODO(load出模板扩展字段的对象) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/product/tempfieldsCheck/{productId}/{fieldId1}/{tempVal1}/{fieldId2}/{tempVal2}/{fieldId3}/{tempVal3}/{fieldId4}/{tempVal4}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String temfieldsCheck(@PathVariable String productId, @PathVariable String fieldId1, @PathVariable String tempVal1, @PathVariable String fieldId2, @PathVariable String tempVal2, @PathVariable String fieldId3, @PathVariable String tempVal3, @PathVariable String fieldId4, @PathVariable String tempVal4, HttpServletRequest request) {
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
		ProductInfoDetail infoDetail = productInfoDetailService.getInfoDetailByProductIdAndTemps(productId, fieldId1, tempVal1, fieldId2, tempVal2, fieldId3, tempVal3, fieldId4, tempVal4);
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
	@RequestMapping(value = "/system/product/save")
	@ResponseBody
	public Object save(Product product, HttpServletRequest request, HttpServletResponse response) {

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

			// 获取管理员对象
			Manager manager = (Manager) request.getSession().getAttribute(SystemConst.MANAGER);
			if (manager != null) {
				product.setCheckMan(manager.getName());
			}

			// 设置审核时间
			product.setCheckTime(DateUtil.getSysDateTimeString());
			// 后台默认审核通过
			product.setCheckStatus(1);

			// 利用Service的方法添加商品
			if (productService.saveProduct(product, request, response)) {
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
	 * 修改商品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/modify")
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
			
			
			
			
			// 获取管理员对象
			Manager manager = (Manager) request.getSession().getAttribute(SystemConst.MANAGER);
			if (manager != null) {
				product.setCheckMan(manager.getName());
			}

			// 设置审核时间
			product.setCheckTime(DateUtil.getSysDateTimeString());
			// 后台默认审核通过
			product.setCheckStatus(1);

			// 利用Service的方法更新商品
			if (productService.updateProduct(product, "system", request, response)) {
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
	 * 审核商品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/checkModify/{type}")
	public String checkModify(@PathVariable String type,HttpServletRequest request, HttpSession session) {
		try {
			// 从前台得到对象
			String id = request.getParameter("id");
			Product product = productService.getProductById(id);
			// 从前台得到对象的审核状态
			String check = request.getParameter("checkStatus");
			// 从前台得到对象的审核说明
			String checkNote = request.getParameter("checkNote");
			// 对审核状态的赋值
			product.setCheckStatus(Integer.valueOf(check));
			// 如果审核失败，需要说明原因
			product.setCheckNote(checkNote);
			// 设置审核时间
			product.setCheckTime(DateUtil.getSysDateTimeString());

			// 从session中获取管理员对象
			Manager manager = (Manager) session.getAttribute(SystemConst.MANAGER);
			// 设置审核人
			product.setCheckMan(manager.getRealName());
			product.setUpdateTime(DateUtil.getSysDateTimeString());

			// 利用Service的方法修改商品
			if (productService.productCheckModify(product)) {
				//商品审核通过且为上架状态
				if(product.isStatus()&&product.getCheckStatus()==1){
					//执行记录修改的价格
					ProductPriceChange change=new ProductPriceChange();
					change.setProductId(product.getId());
					change.setPrice(product.getUnitPrice());
					change.setCreateTime(DateUtil.getSysDateTimeString());
					changeService.addChange(change);

					// 审核通过时更新缓存
					Cache.refrefshIndexProductXsgp();
				}


				// 修改成功，返回true，返回修改页面
				request.setAttribute("modifyOk", true);
				request.setAttribute("type", type);
				request.setAttribute("product", product);
				return "system/product/productCheck";
			}

		} catch (Exception e) {
			// 修改失败，返回false
		    request.setAttribute("type", type);
			request.setAttribute("modifyOk", false);
		}
		return "error";
	}

	/**
	 * 跳转到审核页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/toCheck/{id}/{type}")
	public String toCheck(@PathVariable String id,@PathVariable String type, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找商品
			Product product = productService.getProductById(id);
			// 得到store对象
			Store store = storeService.getStore(product.getStoreId());
			// 得到store的storeName值
			String sName = "";
			if (store != null) {
				sName = store.getStoreName();
			}
			// 得到productbrand
			ProductBrand pb = productBrandService.getBrand(product.getBrand());
			// 得到productBrand的name值
			String pbName = "";
			if (pb != null) {
				pbName = pb.getName();
			}

			// 得到商品分类
			String pcName = productCategoryService.getCategoryFullNameByCategoryId(product.getMainCategory(), ">");
			request.setAttribute("pbName", pbName);
			request.setAttribute("pcName", pcName);
			request.setAttribute("sName", sName);
			request.setAttribute("product", product);
			request.setAttribute("type", type);
			return "system/product/productCheck";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 检查商品编号是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/checkId/{productNumber}")
	@ResponseBody
	public Object checkId(@PathVariable String productNumber) {
		try {
			if (productService.checkProductId(productNumber, "")) {
				// 如果id不存在，返回true
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @Title: productAddClassify
	 * @Description: TODO(跳转到分类设置界面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/product/productAddClassify")
	public String productAddClassify(HttpServletRequest request) {
		try {
			// 获取页面的name属性
			String name = request.getParameter("name");
			// 查询出分类列表返回到页面
			List<ProductCategory> pcs = productCategoryService.getCategories(name,null);
			request.setAttribute("pcs", pcs);
			return "system/product/productAddClassify";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: addClassifyByGys
	 * @Description: TODO(供应商发布商品,选择商品分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/product/supplyProductAddClassify")
	public String addClassifyByGys(HttpServletRequest request) {
		// 获取页面的name属性
		String name = request.getParameter("name");
		// 查询出分类列表返回到页面
		List<ProductCategory> pcs = productCategoryService.getCategories(name,null);
		request.setAttribute("pcs", pcs);
		return "/system/product/systemProductAddClassify";
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/systemAdd")
	public String systemAdd(HttpServletRequest request) {
		// 得到页面的商品分类Id
		String id = request.getParameter("pcId");
		// 从ProductCategory取得模板Id
		String templateId = productCategoryService.getTemplateByCategoryId(id);
		String pcName = productCategoryService.getCategoryFullNameByCategoryId(id, " > ");
		// 根据模板Id取得模板对象
		ProductTemplate productTemplate = productTemplateService.getProductTemplateByTempLateId(templateId);
		// 根据模板Id取得模板扩展字段集合
		List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(templateId);
		List<User> user = userService.findUserByType("1");
		request.setAttribute("user", user);
		request.setAttribute("pcName", pcName);
		request.setAttribute("pcId", id);
		request.setAttribute("productTemplate", productTemplate);
		request.setAttribute("templateFields", templateFields);
		return "/system/product/supplyProductAdd";
	}

	/**
	 * @Title: saveSupplyProduct
	 * @Description: TODO 保存供应商商品议价 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param supplyProduct
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/product/savesupply")
	@ResponseBody
	public Object saveSupplyProduct(HttpServletRequest request, SupplyProduct supplyProduct) {
		if (supplyProduct != null) {
			// 创建时间
			supplyProduct.setCreateTime(DateUtil.getSysDateTimeString());
			supplyProductService.saveSupplyProduct(supplyProduct);
			return true;
		}
		return false;
	}

	/**
	 * @Title: getSupplyList
	 * @Description: TODO(获取供应商后台商品列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/supply/supplyProductList")
	public String getSupplyList(HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// String status = request.getParameter("status");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		// 获取页面查询信息
		String productNum = request.getParameter("productNum");
		String supplyProductName = request.getParameter("supplyProductName");
		List<SupplyProduct> supply = supplyProductService.getList(supplyProductName, productNum, "", pager);
		request.setAttribute("supply", supply);
		request.setAttribute("pager", pager);
		request.setAttribute("productNum", productNum);
		request.setAttribute("supplyProductName", supplyProductName);
		return "/system/product/supplyList";
	}

	/**
	 * @Title: lookSupplyProductById
	 * @Description: TODO(后台查看商品的详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param type
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/supply/lookSupply/{id}/{type}")
	public String lookSupplyProductById(@PathVariable String id, @PathVariable String type, HttpServletRequest request) {
		// 根据ID查询商品列表
		try {
			SupplyProduct sp = (SupplyProduct) supplyProductDao.get(id);
			List<User> user = userService.findUserByType("1");
			request.setAttribute("user", user);
			request.setAttribute("sp", sp);
			request.setAttribute("type", type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/system/product/supplyListModefiy";
	}

	@RequestMapping(value = "/system/supply/supplyEdit")
	@ResponseBody
	public String editSupplyProduct(HttpServletRequest request, SupplyProduct sp) {
		try {
			supplyProductDao.updateBean(sp);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return "1";
	}

	/**
	 * @Title: checkSupplyNum
	 * @Description: TODO(页面搜索) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return int 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/supply/checkSupplyNum")
	@ResponseBody
	public int checkSupplyNum(HttpServletRequest request) {
		// 获取页面商品编号信息
		String productNum = request.getParameter("productNum");
		return supplyProductService.getCheckProductNum(productNum);
	}

	/**
	 * @Title: chooseClassify
	 * @Description: TODO(后台选择商品分类,模板分类下载excel) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productChooseClassify")
	public String chooseClassify(HttpServletRequest request) {
		// 获取页面的name属性
		String name = request.getParameter("name");
		// 查询出分类列表返回到页面
		List<ProductCategory> pcs = productCategoryService.getCategories(name,null);
		// 获取模板列表
		List<ProductTemplate> tem = productTemplateService.getProductTemplate();
		// 获取核心企业
		List<User> user = userService.getUserByType("3");
		request.setAttribute("user", user);
		request.setAttribute("pcs", pcs);
		request.setAttribute("tem", tem);
		return "/system/product/templateCategory/systemProductBatchAddClassify";
	}

	@RequestMapping(value = "/system/productCategory/plist/{id}")
	public void systemlist(@PathVariable String id, HttpServletResponse response) {
		// 利用Service的方法查找品牌
		List<ProductCategory> productCategorys = productCategoryService.getChildCategories(id);

		if (productCategorys != null && productCategorys.size() > 0) {
			ObjectMapper mapper = new ObjectMapper();
			// 将品牌列表转换为json字符串
			String jsonResult = null;
			try {
				jsonResult = mapper.writeValueAsString(productCategorys);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(jsonResult);
			this.backWriteStr(response, jsonResult);
		}
	}

	/**
	 * @Title: excelDownLoad
	 * @Description: TODO(商品模板下载) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param response
	 *            void 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/excel/download")
	public void excelDownLoad(HttpServletRequest request, HttpServletResponse response) {
		// 获取商品分类ID
		String productId = request.getParameter("productId");
		String[] products = productId.split(",");
		// 获取模板ID
		String categoryId = request.getParameter("categoryId");
		// 根据模板ID获取模板字段
		ProductTemplate pt = productTemplateService.getProductTemplateById(categoryId);
		// 商品字段
		String[] title = new String[] { "商品名称", "商品编号", "商品类型", "库存量", "数量单位", "商城价", "市场价", "批发参考价", "商品产地" };

		String name = products[1] + "-" + pt.getName() + "-商品上传模板-" + DateUtil.getSysDateString() + ".xls";
		// 调用excel生成类
		try {
			// 文件放入response中的输出流
			byte[] bytes = ExcelUtil.write_Excel(title, products[0], categoryId, "批量商品上传");
			response.setContentType("application/msexcel;charset=gbk");
			response.setHeader("Content-disposition", "attachment;filename= \"" + new String(name.getBytes(), "ISO-8859-1") + "\"");
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * @Title: excelAndZip
	 * @Description: TODO(跳转到批量上传商品页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/saveProduct/batch")
	public String excelAndZip(HttpServletRequest request) {
		// 获取核心企业
		List<User> user = userService.getUserByType("3");
		request.setAttribute("user", user);
		return "/system/product/systemProductBatchAdd";
	}

	/**
	 * @Title: saveBatchProduct
	 * @Description: TODO(批量保存商品的信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param response
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/saveProductBtach")
	public String saveBatchProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String result = "";
		String path = request.getSession().getServletContext().getRealPath("/");
		// 获取excel文件
		String productExcel = "/customerfiles/system/product/saveBatch/" + DateUtil.getNoSpSysDateString() + "/";
		productExcel = path + FileUploadHelper.uploadFile("productexcel", productExcel, request, response);
		// 获取zip包文件
		String picZipPath = "/customerfiles/system/product/saveBatch/zip/" + DateUtil.getNoSpSysDateString() + "/";
		picZipPath = path + FileUploadHelper.uploadFile("picpath", picZipPath, request, response);
		try {
			Map<String, List<String>> zipMap = null;
			List<String[]> readExcel = null;
			// 没传excel
			if (productExcel != null) {
				// 调用方法解析excel,得到excel第一个sheet的数据
				readExcel = ExcelUtil.readExcel(new FileInputStream(new java.io.File(productExcel)), 0);
			}
			// 从页面获取到发布商品的企业ID及名称
			String storeInfo = request.getParameter("storeId");
			// 设置减压路径
			String picOutPath = "/customerfiles/system/product/saveBatch/" + storeInfo + "/" + DateUtil.getSysDateString() + "/";
			if (picZipPath != null) {
				// 调用方法减压zip包,图片放入picOutPath
				zipMap = ZipUtil.unZip(picZipPath, request, picOutPath);
			}
			// 保存商品信息
			result = productService.saveBatchProduct(readExcel, storeInfo, zipMap, request, response);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		// 查询出分类列表返回到页面
		List<ProductCategory> pcs = productCategoryService.getCategories("",null);
		// 获取模板列表
		List<ProductTemplate> tem = productTemplateService.getProductTemplate();
		// 获取核心企业
		List<User> user = userService.getUserByType("3");
		request.setAttribute("user", user);
		request.setAttribute("pcs", pcs);
		request.setAttribute("tem", tem);
		request.setAttribute("result", result);
		request.setAttribute("uploadStatus", "ok");
		return "/system/product/templateCategory/systemProductBatchAddClassify";
	}
}
