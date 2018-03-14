package com.ekfans.base.store.service;

import com.ekfans.base.store.model.Store;

public interface IStoreInventoryWarningService {

    
    /**
     *　获取用户的商店信息　
     * @param user
     * @return storeID 返回类型
     * @throws
      */
     public Store getInventoryWarningInfo(String storeID);
     
    /**
     * 跟新用户的的　商店　预警信息
     * @param id
     * @return Store 返回类型
     * @throws
      */
     public Store updateInventoryWarning(String id,String warnEmail,String warnMobile);
}
