package com.ekfans.base.product.service;

import com.ekfans.base.product.model.ProductCategoryRel;

/**
 * 商品与分类关系实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductCategoryRelService {
    /**
     * 
    * @Title: add
    * @Description: TODO(添加)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pcRl
    * @return boolean 返回类型
    * @throws
     */
    public boolean add(ProductCategoryRel pcRl);
    /**
     * 
    * @Title: getPcRlBy
    * @Description: TODO(根据商品id得到分类与商品关系信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param productId
    * @return ProductCategoryRel 返回类型
    * @throws
     */
    public ProductCategoryRel getPcRlBy(String productId);
    /**
     * 
    * @Title: modify
    * @Description: TODO(修改)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pcRl
    * @return boolean 返回类型
    * @throws
     */
    public boolean modify(ProductCategoryRel pcRl);
    
    /**
     * 
    * @Title: remove
    * @Description: 根据分类ID,商品ID删除商品分类关联
    * @param pcRl
    * @return boolean 返回类型
     */
    public boolean remove(ProductCategoryRel pcRl);
}
