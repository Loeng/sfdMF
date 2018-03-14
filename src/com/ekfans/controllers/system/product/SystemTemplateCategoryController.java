package com.ekfans.controllers.system.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.TemplateFieldsCategory;
import com.ekfans.base.product.service.ITemplateFieldsCategoryService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemTemplateCategoryController extends BasicController {
	// 定义Service
	@Autowired
	private ITemplateFieldsCategoryService templateFieldsCategoryService;

	/**
	 * 跳转到新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/templateCategory/add")
	public String add() {
		return "/system/product/templateCategory/templateCategoryAdd";
	}

	/**
	 * 新增分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/templateCategory/save")
	public String save(TemplateFieldsCategory templateCategory, HttpServletRequest request) {
			
		// 利用Service的方法添加分类
		templateFieldsCategoryService.addCategory(templateCategory);
		// 从数据库得到分类
		templateCategory = templateFieldsCategoryService
				.getCategoryByName(templateCategory.getCategoryName());
		// 从页面得到子分类名
		String[] names = request.getParameterValues("name");
		if(names!=null && names.length>0){
		 // 设置子分类
	        for (int i = 0; i < names.length; i++) {
	            TemplateFieldsCategory category = new TemplateFieldsCategory();
	            category.setCategoryName(names[i]);
	            category.setParentId(templateCategory.getId());
	            templateFieldsCategoryService.addCategory(category);
	        }
		}

		request.setAttribute("addOk", true);
		// 添加成功，返回
		return "/system/product/templateCategory/templateCategoryAdd";
	}

	/**
	 * 删除分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/templateCategory/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		// 利用Service的方法根据id删除分类
		if (templateFieldsCategoryService.deleteCategory(id) && 
                templateFieldsCategoryService.deleteChildCategory(id)) {
			// 删除成功，返回true
			return true;
		}
		return false;
	}
	
	/**
	 * 
	* @Title: deleteChild
	* @Description: TODO(修改分类时删除子分类)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param id
	* @return Object 返回类型
	* @throws
	 */
	@RequestMapping(value = "/system/templateCategory/deleteChild/{id}")
    @ResponseBody
    public Object deleteChild(@PathVariable String id) {
        // 利用Service的方法根据id删除分类
        if (templateFieldsCategoryService.deleteCategory(id)) {
            // 删除成功，返回true
            return true;
        }
        return false;
    }

	/**
	 * 修改分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/templateCategory/modify")
	public String modify(TemplateFieldsCategory templateCategory,
			HttpServletRequest request) {
	    // 修改父分类
	    templateFieldsCategoryService.modifyCategory(templateCategory);
	    // 从页面得到子分类名
        String[] ids = request.getParameterValues("childId");
        // 从页面得到子分类名
        String[] names = request.getParameterValues("childName");
        if(ids!=null && ids.length>0){
            for (int i = 0; i < names.length; i++) {
                // 如果id不为空，则为已存在的子分类，用修改方法
                if(!StringUtil.isEmpty(ids[i])){
                    TemplateFieldsCategory category = new TemplateFieldsCategory();
                    category.setId(ids[i]);
                    category.setCategoryName(names[i]);
                    category.setParentId(templateCategory.getId());
                    category.setStatus(true);
                    templateFieldsCategoryService.modifyCategory(category);
                }else{
                    // 如果id为空，则为新添加的分类
                    TemplateFieldsCategory category = new TemplateFieldsCategory();
                    category.setCategoryName(names[i]);
                    category.setParentId(templateCategory.getId());
                    templateFieldsCategoryService.addCategory(category);
                }
            }
        }
		
		// 修改成功，返回true
		request.setAttribute("modifyOk", true);
		// 查找子分类
        List<TemplateFieldsCategory> childs = templateFieldsCategoryService.listChildCategories(templateCategory.getId());
        // 把子分类列表放入childList
        templateCategory.setChildList(childs);
		request.setAttribute("category", templateCategory);
		return "system/product/templateCategory/templateCategoryModify";
		
	}

	/**
	 * 查询分类信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/templateCategory/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		// 利用Service的方法根据id查找分类
		TemplateFieldsCategory templateCategory = templateFieldsCategoryService
				.getCategoryById(id);
		// 查找子分类
        List<TemplateFieldsCategory> childs = templateFieldsCategoryService.listChildCategories(templateCategory.getId());
        // 把子分类列表放入childList
        templateCategory.setChildList(childs);
		request.setAttribute("category", templateCategory);
		return "system/product/templateCategory/templateCategoryModify";
	}

	/**
	 * 查询分类详情
	 * 
	 * @return
	 */
	
	@RequestMapping(value = "/system/templateCategory/information/{id}")
	public String information(@PathVariable String id, HttpServletRequest request) {
		// 利用Service的方法根据id查找分类
		TemplateFieldsCategory templateCategory = templateFieldsCategoryService
				.getCategoryById(id);
		// 查找子分类
		List<TemplateFieldsCategory> childs = templateFieldsCategoryService.listChildCategories(templateCategory.getId());
		// 把子分类列表放入childList
		templateCategory.setChildList(childs);
		
		request.setAttribute("category", templateCategory);
		return "system/product/templateCategory/templateCategoryQuery";
	}

	/**
	 * 查找分类列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/templateCategory/list")
	public String list(HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();

		// 从页面获取分类名称
		String name = request.getParameter("name");
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		// 利用Service的方法查找分类
		List<TemplateFieldsCategory> categories = templateFieldsCategoryService
				.listCategory(pager, name);
		request.setAttribute("categories", categories);
		request.setAttribute("pager", pager);
		request.setAttribute("name", name);
		return "/system/product/templateCategory/templateCategoryList";
	}
}
