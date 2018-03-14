package com.ekfans.controllers.system.product;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.ProductTemplate;
import com.ekfans.base.product.model.TemplateFields;
import com.ekfans.base.product.model.TemplateFieldsCategory;
import com.ekfans.base.product.service.IProductTemplateService;
import com.ekfans.base.product.service.ITemplateFieldsCategoryService;
import com.ekfans.base.product.service.ITemplateFieldsService;
import com.ekfans.base.product.util.ProductConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: SystemProductTemplateController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 成都易科远见科技有限公司
 * @date Apr 22, 2014 10:10:26 AM
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */

@Controller
public class SystemProductTemplateController extends BasicController {
	// 定义SERVICE
	@Autowired
	private IProductTemplateService productTemplateService;

	@Autowired
	private ITemplateFieldsService templateFieldsService;

	@Autowired
	private ITemplateFieldsCategoryService templateFieldsCategoryService;

	/**
	 * 
	 * @Title: list
	 * @Description: TODO查找商品列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/product/template")
	public String list(HttpServletRequest request) {

		// 定义分页
		Pager pager = new Pager();
		// 从页面获取商品名称
		String name = request.getParameter("name");

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
		// 利用Service的方法查找商品
		try {
			List<ProductTemplate> productTemplate = productTemplateService.listTemplate(pager, name);
			request.setAttribute("productTemplate", productTemplate);
			request.setAttribute("name", name);
			request.setAttribute("pager", pager);
			return "system/product/config/productTemplate/productTemplate";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/productTemplate/add")
	public String add(HttpServletRequest request) {
		Map<String, String> fieldMap = ProductConst.tempFieldTypeMap;
		request.setAttribute("productTempFieldsMap", fieldMap);
		return "system/product/config/productTemplate/productTemplateAdd";
	}

	/**
	 * 
	 * @Title: listCategory
	 * @Description: TODO(新增模板时load模板分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productTemplate/listCategory")
	public String listCategory(HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取分类名称
		// String name = request.getParameter("name");

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
		List<TemplateFieldsCategory> categories = templateFieldsCategoryService.listCategory(pager, null);
		request.setAttribute("categories", categories);
		request.setAttribute("pager", pager);
		return "system/product/config/productTemplate/templateCategory";
	}

	/**
	 * 
	 * @Title: listCategory
	 * @Description: TODO(新增模板时选择模板分类后load子分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productTemplate/listChildCategory")
	public String listChildCategory(HttpServletRequest request) {

		// 从页面获取商品名称
		String pId = request.getParameter("pId");
		Map<String, String> fieldMap = ProductConst.tempFieldTypeMap;
        request.setAttribute("productTempFieldsMap", fieldMap);
		List<TemplateFieldsCategory> childCategories = templateFieldsCategoryService.listChildCategories(pId);
		request.setAttribute("childCategories", childCategories);
		return "system/product/config/productTemplate/templateChildCategory";
	}

	/**
	 * 
	 * @Title: save
	 * @Description: TODO 保存操作 详细业务流程: 获得页面上添加的信息放到数组中，在for循环每次新增就new一个对象去保存。
	 * @param template
	 * @param templateFields
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productTemplate/save")
	public String save(HttpServletRequest request) {
		// 得到模板名
		String templateName = request.getParameter("name");

		ProductTemplate template = new ProductTemplate();
		// 设置模板名
		template.setName(templateName);
		// 设置创建时间
		template.setCreateTime(DateUtil.getSysDateTimeString());
		// 保存模板
		productTemplateService.saveTempalte(template);
		// 从数据库得到保存的模板
		template = productTemplateService.getTemplateByName(templateName);

		// 得到排序列表
		String[] position = request.getParameterValues("position");
		// 得到扩展字段名
		String[] key = request.getParameterValues("key");
		// 得到扩展字段是否为可选字段
		String[] common = request.getParameterValues("common");
		// 得到扩展字段值
		String[] value = request.getParameterValues("value");
		// 得到扩展字段值的类型
		String[] valueType = request.getParameterValues("valueType");
		// 得到扩展字段是否可搜索
		String[] search = request.getParameterValues("isSearch");
		// 得到扩展字段所属分类
		String[] cId = request.getParameterValues("categoryId");

		if (position != null && position.length > 0) {
			// 循环保存模板扩展字段
			for (int i = 0; i < position.length; i++) {
				TemplateFields field = new TemplateFields();
				field.setTempId(template.getId());
				if (!StringUtil.isEmpty(position[i])) {
					field.setPosition(Integer.parseInt(position[i]));
				}
				field.setFieldName(key[i]);
				field.setCommons(Boolean.parseBoolean(common[i]));
				field.setSearch(Boolean.parseBoolean(search[i]));
				field.setFieldValue(value[i]);
				if ("请选择".equals(valueType[i])) {
					field.setFieldType("");
				} else {
					field.setFieldType(valueType[i]);
				}
				field.setFieldCategory(cId[i]);
				templateFieldsService.saveTempalteFields(field);
			}
		}
		request.setAttribute("addOk", true);
		Map<String, String> fieldMap = ProductConst.tempFieldTypeMap;
        request.setAttribute("productTempFieldsMap", fieldMap);
		return "system/product/config/productTemplate/productTemplateAdd";
	}

	/**
	 * 
	 * @Title: delete
	 * @Description: TODO 删除操作 详细业务流程: 根据Id删除
	 * @param id
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productTemplate/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		try {
			if (productTemplateService.deleteTemplate(id) && templateFieldsService.deleteTempalteFields(id)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return null;
	}

	/**
	 * 
	 * @Title: detail
	 * @Description: TODO 根据id查询 详细业务流程: 根据id查询
	 * @param id
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productTemplate/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		// 得到模板
		ProductTemplate template = productTemplateService.getProductTemplateById(id);

		// 得到模板分类
		List<TemplateFieldsCategory> categories = templateFieldsCategoryService.listCategoryByTemplateId(id);
		// 循环得到扩展字段放入分类中
		for (int i = 0; i < categories.size(); i++) {
			String cId = categories.get(i).getId();
			String tId = template.getId();
			List<TemplateFields> temps = templateFieldsService.listFieldsByTidAndCid(tId, cId);
			categories.get(i).setFields(temps);
		}

		Map<String, String> fieldMap = ProductConst.tempFieldTypeMap;
		request.setAttribute("productTempFieldsMap", fieldMap);
		// 返回值
		request.setAttribute("template", template);
		request.setAttribute("categories", categories);
		return "system/product/config/productTemplate/productTemplateModify";
	}

	/**
	 * 删除扩展字段
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/productTemplate/deleteField/{id}")
	@ResponseBody
	public Object deleteField(@PathVariable String id, HttpServletRequest request) {
		// 利用Service的方法根据id删除分类
		if (templateFieldsService.deleteField(id)) {
			// 删除成功，返回true
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: modify
	 * @Description: TODO修改模板 详细业务流程: 修改模板(每次修改扩展类的时候都先将删除一次，在执行保存操作。)
	 * @param temps
	 * @param template
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productTemplate/modify")
	public String modify(ProductTemplate template, HttpServletRequest request) {
		// 修改模板
		productTemplateService.modifyTemplate(template);
		// 从页面得到扩展字段id
		String[] ids = request.getParameterValues("fieldId");
		// 得到排序列表
		String[] position = request.getParameterValues("position");
		// 得到扩展字段名
		String[] key = request.getParameterValues("key");
		// 得到扩展字段是否为可选字段
		String[] common = request.getParameterValues("common");
		// 得到扩展字段值
		String[] value = request.getParameterValues("value");
		// 得到扩展字段值的类型
		String[] valueType = request.getParameterValues("valueType");
		// 得到扩展字段是否可搜索
		String[] search = request.getParameterValues("isSearch");
		// 得到扩展字段所属分类
		String[] cId = request.getParameterValues("categoryId");
		// 循环保存模板扩展字段
		for (int i = 0; i < position.length; i++) {
			// 如果id不为空，则为已存在的扩展字段，用修改方法
			if (!StringUtil.isEmpty(ids[i])) {
				TemplateFields field = new TemplateFields();
				field.setId(ids[i]);
				field.setTempId(template.getId());
				field.setFieldName(key[i]);
				field.setCommons(Boolean.parseBoolean(common[i]));
				field.setSearch(Boolean.parseBoolean(search[i]));
				field.setFieldValue(value[i]);
				if ("请选择".equals(valueType[i])) {
					field.setFieldType("");
				} else {
					field.setFieldType(valueType[i]);
				}
				field.setFieldCategory(cId[i]);
				if (!StringUtil.isEmpty(position[i])) {
					field.setPosition(Integer.parseInt(position[i]));
				}
				templateFieldsService.modifyTempalteFields(field);
			} else {
				TemplateFields field = new TemplateFields();
				field.setTempId(template.getId());
				field.setFieldName(key[i]);
				field.setCommons(Boolean.parseBoolean(common[i]));
				field.setSearch(Boolean.parseBoolean(search[i]));
				field.setFieldValue(value[i]);
				if ("请选择".equals(valueType[i])) {
					field.setFieldType("");
				} else {
					field.setFieldType(valueType[i]);
				}
				field.setFieldCategory(cId[i]);
				if (!StringUtil.isEmpty(position[i])) {
					field.setPosition(Integer.parseInt(position[i]));
				}
				templateFieldsService.saveTempalteFields(field);
			}
		}
		request.setAttribute("modifyOk", true);

		// 得到模板分类
		List<TemplateFieldsCategory> categories = templateFieldsCategoryService.listCategoryByTemplateId(template.getId());
		// 循环得到扩展字段放入分类中
		for (int i = 0; i < categories.size(); i++) {
			String categoryId = categories.get(i).getId();
			String tId = template.getId();
			List<TemplateFields> temps = templateFieldsService.listFieldsByTidAndCid(tId, categoryId);
			categories.get(i).setFields(temps);
		}
		// 返回值
		request.setAttribute("template", template);
		request.setAttribute("categories", categories);
		return "system/product/config/productTemplate/productTemplateModify";
	}

	/**
	 * 新增商品时查询模板列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/template/plist")
	public String plist(HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取商品名称
		String name = request.getParameter("name");

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
		try {
			// 利用Service的方法查找品牌
			List<ProductTemplate> temp = productTemplateService.listTemplate(pager, name);
			request.setAttribute("temp", temp);
			request.setAttribute("name", name);
			request.setAttribute("pager", pager);
			return "/system/product/productAndTempLate";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: plist
	 * @Description: TODO 模板弹出层的搜索 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/template/plistSearch/{name}")
	public String plistSearch(@PathVariable String name, HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
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
		try {
			// 利用Service的方法查找品牌
			List<ProductTemplate> temp = productTemplateService.listTemplate(pager, name);
			request.setAttribute("temp", temp);
			request.setAttribute("name", name);
			request.setAttribute("pager", pager);
			return "/system/product/productAndTempLate";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: detailId
	 * @Description: TODO 模板管理的查看详情 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/productTemplateDetails/{id}")
	public String detailId(@PathVariable String id, HttpServletRequest request) {
		ProductTemplate productTemplate = productTemplateService.getProductTemplateById(id);
		String productTemplateId = productTemplate.getId();
		// List<TemplateFields> tempList =
		// templateFieldsService.getProductTempalteFieldsById(productTemplateId);
		List<TemplateFields> tempList = templateFieldsService.getProductTempalteFieldsByTemplateId(productTemplateId);
		request.setAttribute("productTemplate", productTemplate);
		request.setAttribute("tempList", tempList);
		return "/system/product/config/productTemplate/productTempLateDetails";
	}
}
