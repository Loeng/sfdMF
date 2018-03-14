package com.ekfans.controllers.system.content;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;

@Controller
public class SystemContentCategoryController extends BasicController {

	// 定义内容分类Service
	@Autowired
	private IContentCategoryService contentCategoryService;

	/**
	 * 跳转到分类页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/contentCategory/index")
	public String index(HttpServletRequest request) {
		// 查询出分类列表返回到页面
		List<ContentCategory> categories = contentCategoryService.getCategories();
		request.setAttribute("categories", categories);
		return "/system/content/categoryIndex";
	}

    /**
     * 查找子分类列表
     * 
     * @return
     */
    @RequestMapping(value = "/system/content/contentCategory/child/{id}")
    public String child(@PathVariable String id, HttpServletRequest request) {
        // 查询出分类列表返回到页面
        List<ContentCategory> categories = contentCategoryService.getAllChildCategories(id);
        request.setAttribute("categories", categories);
        return "/system/content/categoryTree";
    }
//	/**
//	 * 查找子分类列表
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/system/content/contentCategory/child/{id}")
//	public String child(@PathVariable String id, HttpServletRequest request) {
//		// 查询出分类列表返回到页面
//		List<ContentCategory> categories = contentCategoryService.getChildCategories(id);
//		request.setAttribute("categories", categories);
//		return "/system/content/categoryTree";
//	}


	/**
	 * 
	 * @Title: add
	 * @Description: TODO(跳转到添加分类页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/content/contentCategory/add")
	public String add(HttpServletRequest request) {
		return "/system/content/categoryAdd";
	}

	/**
	 * 添加分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/contentCategory/save")
	@ResponseBody
	public Object save(ContentCategory contentCategory, HttpServletRequest request, HttpServletResponse response) {
		// 设置图标保存路径
		String currentPath = "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/categoryIco/";
		// 调用方法获取分类图标，返回图标路径
		String categoryPicPath = FileUploadHelper.uploadFile("icon", currentPath, request, response);
		// 利用Service的方法添加分类
		if (contentCategoryService.addCategory(contentCategory, categoryPicPath)) {
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
	@RequestMapping(value = "/system/content/contentCategory/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		// 利用Service的方法根据id删除分类
		if (contentCategoryService.deleteCategory(id)) {
			// 删除成功，返回true
			return true;
		} else {
			// 删除失败，返回false
			return false;
		}
	}

	/**
	 * 修改分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/contentCategory/modify")
	@ResponseBody
	public Object modify(ContentCategory contentCategory, HttpServletRequest request, HttpServletResponse response) {
		// 设置图标保存路径
		String currentPath = "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/categoryIco/";
		// 调用方法获取分类图标，返回图标路径
		String categoryPicPath = FileUploadHelper.uploadFile("icon", currentPath, request, response);

		// 利用Service的方法修改分类
		if (contentCategoryService.modifyCategory(contentCategory, categoryPicPath)) {
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
	@RequestMapping(value = "/system/content/contentCategory/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找分类
			ContentCategory contentCategory = contentCategoryService.getCategoryById(id);
			request.setAttribute("category", contentCategory);
			return "/system/content/categoryInfo";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 检查分类id是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/contentCategory/checkId/{id}")
	@ResponseBody
	public Object checkId(@PathVariable String id) {
		// 如果id不存在，返回true
		if (!contentCategoryService.checkId(id)) {
			return true;
		}
		return false;
	}

	/**
	 * 频道配置选择资讯分类
	 * 
	 * @Title: channelConfigChoseAd
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/channel/config/ccChose")
	public String channelConfigChoseAd(HttpServletRequest request) {
		List<ContentCategory> categoryList = contentCategoryService.getContentCategorysByParentId("", true, 0);
		request.setAttribute("categoryList", categoryList);
		return "/web/channel/commons/config/channelConfigCCChose";
	}
}
