package com.ekfans.controllers.system.product;

/**
 * 品牌管理
 */
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.service.IProductBrandService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemBrandManagerController extends BasicController{
	// 定义SERVICE
	@Autowired private IProductBrandService productBrandService;
	
	@Autowired private IProductCategoryService productCategoryService;
	
	/**
	 * 品牌图标存放目录
	 */
	private static final String ICON_DIR = "/customerfiles/config/brandIcon/";
	/**
	 * 跳转到新增页面
	 * @return
	 */
	@RequestMapping(value = "/system/product/brand/add")
	public String add(HttpServletRequest request) {
		try {
		    return "/system/product/config/brandAdd";
		}
		catch(Exception e) {
		    e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 新增品牌
	 * @return
	 */
	@RequestMapping(value = "/system/product/brand/save")
	public String save(ProductBrand productBrand,HttpServletRequest request,HttpServletResponse response) {
		
		//将上传的图片设置到icon中
		setIconFromUpload(productBrand, request, response);
		try {
		    if(StringUtil.isEmpty(productBrand.getName())){
		        request.setAttribute("addOk", false);
                
                return "/system/product/config/brandAdd";
		    }
			// 利用Service的方法添加频道
			if (productBrandService.addBrand(productBrand,null,null)) {
				request.setAttribute("addOk", true);
				// 添加成功，返回
				return "/system/product/config/brandAdd";
			}
		} catch (Exception e) {
		    e.printStackTrace();
			// 添加失败，返回false
			request.setAttribute("addOk", false);
		}
		return "error";
	}
	
	/**
	 * 删除品牌
	 * @return
	 */
	@RequestMapping(value = "/system/product/brand/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		try {
			// 利用Service的方法根据id删除频道
			if (productBrandService.deleteBrand(id)) {
				// 删除成功，返回true
				return true;
			}
		} catch (Exception e) {
		    e.printStackTrace();
			// 删除失败，返回false
			return false;
		}
		return "error";
	}
		
	/**
	 * 修改品牌
	 * @return
	 */
	@RequestMapping(value = "/system/product/brand/modify")
	public String modify(ProductBrand productBrand,HttpServletRequest request,HttpServletResponse response) {
		//将上传的图片设置到icon中
		setIconFromUpload(productBrand, request, response);
		try {
			// 利用Service的方法修改频道
			if(productBrandService.modifyBrand(productBrand,null,null)){
				// 修改成功，返回true
				request.setAttribute("modifyOk", true);
				request.setAttribute("productBrand", productBrand);
				return "/system/product/config/brandModify";
			}
		} catch (Exception e) {
		    e.printStackTrace();
			// 修改失败，返回false
			request.setAttribute("modifyOk", false);
		}
		return "error";
	}
		
	/**
	 * 查询品牌信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/product/brand/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找频道
			ProductBrand productBrand = productBrandService.getBrand(id);
//			// 查询产品的分类信息
//			List<ProductCategory> productCategories = 
//			                      productCategoryService.getCategories();
//			request.setAttribute("productCategories",productCategories);
			request.setAttribute("productBrand", productBrand);
			return "system/product/config/brandModify";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	/**
	 * 查找品牌列表
	 * @return
	 */
	@RequestMapping(value = "/system/product/brand/list")
	public String list(HttpServletRequest request) {
		try {
			// 定义分页
			Pager pager = new Pager();
			
			// 从页面获取品牌名称
			String name = request.getParameter("name");
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
			// 利用Service的方法查找频道
			List<ProductBrand> productBrands = productBrandService.getBrands(pager, name,status);
			request.setAttribute("productBrands", productBrands);
			request.setAttribute("pager", pager);
			request.setAttribute("name", name);
			request.setAttribute("status", status);
			return "/system/product/config/brandList";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	/**
	 * 新增商品时查询品牌列表
	 * @return
	 */
	@RequestMapping(value = "/system/product/brand/plist")
	public String plist(HttpServletRequest request){
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
            List<ProductBrand> productBrand = productBrandService.getBrands(pager, name,status);
            request.setAttribute("productBrand", productBrand);
            request.setAttribute("pager", pager);
            request.setAttribute("name", name);
            request.setAttribute("status", status);
            return "/system/product/productAndBrand";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	/**
	 * 
	* @Title: plist
	* @Description: TODO 品牌弹出层的搜索
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param name
	* @param request
	* @return String 返回类型
	* @throws
	 */
	   @RequestMapping(value = "/system/product/brand/plistSearch/{name}/{page}")
	    public String plistSearch(@PathVariable String name, @PathVariable String page, HttpServletRequest request){
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
	            List<ProductBrand> productBrand = productBrandService.getBrands(pager, name,null);
	            request.setAttribute("productBrand", productBrand);
	            request.setAttribute("pager", pager);
	            if(!StringUtil.isEmpty(name)){
	                request.setAttribute("brandName", name);
	            }else{
	                request.setAttribute("brandName", "");
	            }
	           
	            return "/system/product/productAndBrand";
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "error";
	    }
	
	/**
	 * 品牌管理详情页
	 */
	@RequestMapping(value = "/system/product/brand/pdetail/{id}")
	public String pBrandDetail(@PathVariable String id,HttpServletRequest request){
	    ProductBrand productBrand = 
		productBrandService.getBrandDetailById(id);
	    if(productBrand != null){
		request.setAttribute("productBrand",productBrand);
		return "/system/product/config/brandDetail";
	    }
	    return "error";
	}
	
	/**
	 * 将上传的图片设置到icon中
	 * @param productBrand Form表单对象
	 * @param request
	 * @param response
	 */
	private void setIconFromUpload(ProductBrand productBrand,HttpServletRequest request,HttpServletResponse response){
		// 调用方法获取分类图标，返回图标路径
        String iconPath = FileUploadHelper.uploadFile("icon", ICON_DIR, request, response);
        productBrand.setIcon(iconPath);
	}
//	/**
//     * 品牌管理详情页
//     */
//    @RequestMapping(value = "/system/product/brand/pdetail/{id}")
//    public String pBrandDetail(@PathVariable String id,HttpServletRequest request){
//        ProductBrand productBrand = 
//        productBrandService.getBrandDetailById(id);
//        if(productBrand != null){
//        request.setAttribute("productBrand",productBrand);
//        return "/system/product/config/brandDetail";
//        }
//        return "error";
//    }
}
