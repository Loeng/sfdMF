package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.StoreCollect;
import com.ekfans.pub.util.Pager;

/**
 * 商户收藏管理实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IStoreCollectService {
    /**
     * 
    * @Title: getList
    * @Description: TODO(根据用户id得到对应的商品收藏列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return List<ProductCollect> 返回类型
    * @throws
     */
    public List<StoreCollect>  getList(Pager pager,String userId);
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
}