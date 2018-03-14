package com.ekfans.base.product.service;

import java.util.List;

import com.ekfans.base.product.model.TemplateFieldsCategory;
import com.ekfans.pub.util.Pager;

/**
 * 商品模板扩展字段所属分类实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface ITemplateFieldsCategoryService {
    /**
     * 添加分类
     * @param category
     * @return
     */
    public boolean addCategory(TemplateFieldsCategory category);
    
    /**
     * 根据id查找分类
     * @param id
     * @return
     */
    public TemplateFieldsCategory getCategoryById(String id);
    
    /**
     * 根据分类名查找分类
     * @param name
     * @return
     */
    public TemplateFieldsCategory getCategoryByName(String name);
    
    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    public boolean deleteCategory(String id);
    
    /**
     * 根据父id删除子分类
     * @param id
     * @return
     */
    public boolean deleteChildCategory(String id);
    
    /**
     * 修改分类
     * @param category
     */
    public boolean modifyCategory(TemplateFieldsCategory category);
    
    /**
     * 查找一级分类列表
     * @param pager 分页
     * @param name 分类名
     * @return
     */
    public List<TemplateFieldsCategory> listCategory(Pager pager,String name);
    
    /**
     * 查找分类列表
     * @param pager 分页
     * @param name 分类名
     * @return
     */
    public List<TemplateFieldsCategory> listChildCategories(String parentId);
    
    /**
     * 
    * @Title: listCategoryByTemplateId
    * @Description: 根据模板id获取模板分类列表
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return List<TemplateFieldsCategory> 返回类型
    * @throws
     */
    public List<TemplateFieldsCategory> listCategoryByTemplateId(String id);
}