package com.ekfans.controllers.system.product;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.CategoryBrandRel;
import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.ProductTemplate;
import com.ekfans.base.product.service.ICategoryBrandRelService;
import com.ekfans.base.product.service.IProductBrandService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductTemplateService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemProductCategoryController extends BasicController {

	// 定义SERVICE
	@Autowired
	private IProductCategoryService productCategoryService;

	@Autowired
	private IProductBrandService productBrandService;

	@Autowired
	private ICategoryBrandRelService categoryBrandRelService;

	@Autowired
	private IProductTemplateService productTemplateService;

	/**
	 * 跳转到分类页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/productCategory/index")
	public String index(HttpServletRequest request) {
		// 查询出分类列表返回到页面
		List<ProductCategory> categories = productCategoryService.getCategories();
		request.setAttribute("categories", categories);
		return "/system/product/config/categoryIndex";
	}

	/**
	 * 查找子分类列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/productCategory/child/{id}")
	public String child(@PathVariable String id, HttpServletRequest request) {
		// 查询出分类列表返回到页面
		List<ProductCategory> categories = productCategoryService.getChildCategories(id);
		request.setAttribute("categories", categories);
		return "/system/product/config/categoryTree";
	}

	/**
	 * 跳转到添加分类页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/productCategory/add")
	public String add(HttpServletRequest request) {
		List<ProductBrand> brands = productBrandService.getBrands();
		List<ProductTemplate> pts = productTemplateService.listTemplate(null, null);
		request.setAttribute("pts", pts);
		request.setAttribute("brands", brands);
		return "/system/product/config/categoryAdd";
	}

	/**
	 * 添加分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/productCategory/save")
	@ResponseBody
	public Object save(ProductCategory productCategory, HttpServletRequest request, HttpServletResponse response) {
		// 设置图标保存路径
		String currentPath = "/customerfiles/product/" + DateUtil.getNoSpSysDateString() + "/categoryIco/";
		// 调用方法获取会员等级图标，返回图标路径
		String categoryPicPath = FileUploadHelper.uploadFile("icon", currentPath, request, response);
		// 得到对应的关联商品集合
		String[] brands = request.getParameterValues("brand");
		// 得到页面的Position
		String position = request.getParameter("position");
		// 判断必填项是否填写
		if (StringUtil.isEmpty(productCategory.getName()) || StringUtil.isEmpty(position)) {
			return false;
		}
		// 利用Service的方法添加分类
		if (productCategoryService.addCategory(productCategory, categoryPicPath)) {
			// 遍历数组进行关联表的添加
			if (brands != null && brands.length > 0) {
				for (int i = 0; i < brands.length; i++) {
					CategoryBrandRel cbr = new CategoryBrandRel();
					cbr.setBrandId(brands[i]);
					cbr.setCategoryId(productCategory.getId());
					categoryBrandRelService.addCategoryBrandRel(cbr);
				}
			}
			request.setAttribute("addOk", true);
			// 添加成功，返回true
			return true;
		} else {
			// 添加失败，返回false
			return false;
		}

	}

	/**
	 * 删除分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/productCategory/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		try {
			// 利用Service的方法根据id删除分类
			if (productCategoryService.deleteCategory(id)) {
				categoryBrandRelService.delete(id);
				// 删除成功，返回true
				return true;
			}
		} catch (Exception e) {
			// 删除失败，返回false
			return false;
		}
		return "error";
	}

	/**
	 * 修改分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/productCategory/modify")
	@ResponseBody
	public Object modify(ProductCategory productCategory, HttpServletRequest request, HttpServletResponse response) {
		// 得到对应的关联商品集合
		String[] brands = request.getParameterValues("brand");
		// 得到页面的Position
		String position = request.getParameter("position");
		// 设置图标保存路径
		String currentPath = "/customerfiles/product/" + DateUtil.getNoSpSysDateString() + "/categoryIco/";
		// 调用方法获取会员等级图标，返回图标路径
		String categoryPicPath = FileUploadHelper.uploadFile("icon", currentPath, request, response);
		// 判断必填项是否填写
		if (StringUtil.isEmpty(productCategory.getName()) || StringUtil.isEmpty(position)) {
			return false;
		}
		// 利用Service的方法修改分类
		if (productCategoryService.modifyCategory(productCategory, categoryPicPath)) {
			// 删除所有的分类重新添加
			categoryBrandRelService.delete(productCategory.getId());
			// 遍历数组进行关联表的添加
			if (brands != null && brands.length > 0) {
				for (int i = 0; i < brands.length; i++) {
					CategoryBrandRel cbr = new CategoryBrandRel();
					cbr.setBrandId(brands[i]);
					cbr.setCategoryId(productCategory.getId());
					categoryBrandRelService.addCategoryBrandRel(cbr);
				}
			}
			// 修改成功，返回true
			return true;
		} else {
			// 修改成功，返回true
			return false;
		}
	}

	/**
	 * 查询分类信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/productCategory/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找分类
			ProductCategory productCategory = productCategoryService.getCategoryById(id);
			request.setAttribute("category", productCategory);
			List<ProductBrand> brands = productBrandService.getBrands(id);
			List<ProductTemplate> pts = productTemplateService.listTemplate(null, null);
			request.setAttribute("pts", pts);
			request.setAttribute("brands", brands);
			return "/system/product/config/categoryDetail";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: plist
	 * @Description: TODO(点击菜单选项得到对应的下级菜单) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productCategory/plist/{id}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Object plist(@PathVariable String id) {
		try {
			// 利用Service的方法查找品牌
			List<ProductCategory> productCategorys = productCategoryService.getChildCategories(id);
			// 将品牌列表转换为json字符串
			String jsonResult = JsonUtil.listToJson(productCategorys);
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: search
	 * @Description: TODO(新增商品时搜索商品分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/product/productCategory/search/{name}")
	public String search(@PathVariable String name, HttpServletRequest request) {
		if (StringUtil.isEmpty(name)) {
			request.setAttribute("categories", null);
		} else {
			name = StringUtil.ISO88591ToUTF8(name);
			// 利用Service的方法根据id查找分类
			Map<String, String> categories = productCategoryService.searchCategoriesByName(name,null);
			request.setAttribute("categories", categories);
		}
		return "/system/product/productCategorySearch";
	}

	/**
	 * 跳转到添加根分类页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/productCategoryRoot/add")
	public String addRoot(HttpServletRequest request) {
		List<ProductBrand> brands = productBrandService.getBrands();
		request.setAttribute("brands", brands);
		return "/system/product/config/categoryRootAdd";
	}

	/**
	 * 频道配置选择商品分类
	 * 
	 * @Title: channelConfigChoseAd
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/channel/config/pcChose")
	public String channelConfigChoseAd(HttpServletRequest request) {
		List<ProductCategory> categoryList = productCategoryService.getChildCategories("", true, 0);
		request.setAttribute("categoryList", categoryList);
		return "/web/channel/commons/config/channelConfigPCChose";
	}

}
