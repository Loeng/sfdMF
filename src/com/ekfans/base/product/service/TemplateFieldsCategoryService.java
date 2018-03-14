package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.ITemplateFieldsCategoryDao;
import com.ekfans.base.product.model.TemplateFieldsCategory;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品模板扩展字段所属分类业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class TemplateFieldsCategoryService implements ITemplateFieldsCategoryService {
	// 定义DAO
	@Autowired
	private ITemplateFieldsCategoryDao templateFieldsCategoryDao;

	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(TemplateFieldsCategoryService.class);

	/**
	 * 添加分类
	 */
	public boolean addCategory(TemplateFieldsCategory category) {
		// 如果分类为空或分类名为空，返回false
		if (category == null || StringUtil.isEmpty(category.getCategoryName())) {
			return false;
		}
		try {
			// 模板分类为后台添加，默认启用
			category.setStatus(true);
			// 调用DAO方法添加分类
			templateFieldsCategoryDao.addBean(category);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据id查找分类
	 */
	public TemplateFieldsCategory getCategoryById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			// 调用DAO方法查找分类
			return (TemplateFieldsCategory) templateFieldsCategoryDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据id删除分类
	 */
	public boolean deleteCategory(String id) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		try {
			// 调用DAO方法查找分类
			templateFieldsCategoryDao.deleteById(id);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据id修改分类
	 */
	public boolean modifyCategory(TemplateFieldsCategory ad) {
		try {
			// 调用DAO方法修改分类
			templateFieldsCategoryDao.updateBean(ad);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;

	}

	/**
	 * 查找分类列表
	 */
	public List<TemplateFieldsCategory> listCategory(Pager pager, String name) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select category from TemplateFieldsCategory as category where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and category.categoryName  like :name");
			paramMap.put("name", "%" + name + "%");
		}

		// 查出一级分类
		sql.append(" and (category.parentId is null or category.parentId = '' or category.parentId = ' ')");
		try {
			// 调用DAO方法查找分类
			List<TemplateFieldsCategory> list = templateFieldsCategoryDao.list(pager, sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public TemplateFieldsCategory getCategoryByName(String name) {
		if (StringUtil.isEmpty(name)) {
			return null;
		}
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select category from TemplateFieldsCategory as category where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果查询条件输入了name，添加查询条件
		sql.append(" and category.categoryName = :name");
		paramMap.put("name", name);

		try {
			// 调用DAO方法查找分类
			List<TemplateFieldsCategory> list = templateFieldsCategoryDao.list(sql.toString(), paramMap);
			return list.get(0);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<TemplateFieldsCategory> listChildCategories(String parentId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select category from TemplateFieldsCategory as category where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果父分类id不为空，添加查询条件
		if (!StringUtil.isEmpty(parentId)) {
			sql.append(" and category.parentId = :parentId");
			paramMap.put("parentId", parentId);
		}
		try {
			// 调用DAO方法查找分类
			List<TemplateFieldsCategory> list = templateFieldsCategoryDao.list(sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean deleteChildCategory(String id) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("from TemplateFieldsCategory as category where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 添加查询条件
		sql.append(" and category.parentId = :parentId");
		paramMap.put("parentId", id);
		try {
			// 调用DAO方法查找分类
			templateFieldsCategoryDao.delete(sql.toString(), paramMap);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<TemplateFieldsCategory> listCategoryByTemplateId(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("select distinct(category) from TemplateFieldsCategory as category,TemplateFields as field where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 添加查询条件
		sql.append(" and category.id = field.fieldCategory");
		sql.append(" and field.tempId = :tempId");
		paramMap.put("tempId", id);
		try {
			// 调用DAO方法查找分类
			List<TemplateFieldsCategory> list = templateFieldsCategoryDao.list(sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
}