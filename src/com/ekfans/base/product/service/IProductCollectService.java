package com.ekfans.base.product.service;

import java.util.List;

import com.ekfans.base.product.model.ProductCollect;
import com.ekfans.pub.util.Pager;

/**
 * 商品收藏管理实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductCollectService {
    /**
     * 
    * @Title: getList
    * @Description: TODO(根据用户id得到对应的商品收藏列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return List<ProductCollect> 返回类型
    * @throws
     */
    public List<ProductCollect>  getList(Pager pager,String userId);
    /**
     * 
    * @Title: delete
    * @Description: TODO(根据id删除)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return boolean 返回类型
    * @throws
     */
    public boolean delete(String id);
    
    /**
     * 
    * @Title: getProductCollectByProductId
    * @Description: TODO(根据商品id查询)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param productId
    * @return ProductCollect 返回类型
    * @throws
     */
    public ProductCollect getProductCollectByProductId(String productId,String userId);
    
    /**
     * 
    * @Title: add
    * @Description: TODO(添加)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pc
    * @return boolean 返回类型
    * @throws
     */
    public boolean add(ProductCollect pc);
    
    /**
     * 根据id得到对象
     */
    public ProductCollect getProductCollectById(String id);
    
    /**
     * 
    * @Title: getShopCartSum
    * @Description: TODO(得到商品收藏总的商品数量)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return String 返回类型
    * @throws
     */
    public String getProductCollectSum(String userId);
}