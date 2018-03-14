package com.ekfans.base.store.service;

import com.ekfans.base.store.model.StoreDecoration;

/**
 * 店铺装修实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IStoreDecorationService {
    /**
     * 
    * @Title: getStoreDecorationByStoreId
    * @Description: 通过storeid得到StoreDecoration
    * 详细业务流程:
    * (通过storeId得到StoreDecoration对象)
    * @param storeId
    * @return StoreDecoration 返回类型
    * @throws
     */
    public StoreDecoration getStoreDecorationByStoreId(String storeId);
    /**
     * 
    * @Title: update
    * @Description: TODO(对StoreDecoration进行修改)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param sd
    * @return boolean 返回类型
    * @throws
     */
    public boolean update(StoreDecoration sd);
}