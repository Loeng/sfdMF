package com.ekfans.base.product.service.web.storeList;

import java.util.List;

import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.store.model.Store;
import com.ekfans.pub.util.Pager;

public interface IStoreListService {
    /**
     * 
    * @Title: getStoresByFuzzyName
    * @Description: TODO(前台-搜索-根据搜索条件为店铺,搜索出满足条件的店铺)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param storeName
    * @param @return    设定文件
    * @return List<Store>    返回类型
    * @throws
     */
    public List<Store> getStoresByFuzzyName(String storeName,Pager pager);
   
    /**
     * 
    * @Title: getProductCategoryByStoreId
    * @Description: TODO(根据店铺id得到商品分类的集合)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param StoreId
    * @return List<String> 返回类型
    * @throws
     */
    public List<ProductCategory> getProductCategoryByStoreId(String StoreId);
    
    /**
     * 
    * @Title: getStoresByCondition
    * @Description: TODO(根据条件查询)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param storeLevelId
    * @return List<Store> 返回类型
    * @throws
     */
    public List<Store> getStoresByCondition(String storeLevelId,Pager pager,String storeName);
    
}
