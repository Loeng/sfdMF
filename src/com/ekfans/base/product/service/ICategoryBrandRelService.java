package com.ekfans.base.product.service;

import com.ekfans.base.product.model.CategoryBrandRel;

/**
 * 商品分类与品牌关系实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface ICategoryBrandRelService {
    /**
     * 
    * @Title: addCategoryBrandRel
    * @Description: TODO(添加)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param cbr
    * @return boolean 返回类型
    * @throws
     */
    public boolean addCategoryBrandRel(CategoryBrandRel cbr);
    /**
     * 
    * @Title: delete
    * @Description: TODO(根据categoryId删除)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param categoryId
    * @return boolean 返回类型
    * @throws
     */
    public boolean delete(String categoryId);
}