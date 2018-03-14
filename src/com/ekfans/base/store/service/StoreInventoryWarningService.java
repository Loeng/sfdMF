package com.ekfans.base.store.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.Store;

@Service
public class StoreInventoryWarningService implements IStoreInventoryWarningService {

    @Resource
    private IStoreDao storeDao;
    // 定日错误日志
    public static Logger log = LoggerFactory.getLogger(StoreInventoryWarningService.class);
    
    /**
    *　获取用户的商店信息　
    * @param user
    * @return storeID 返回类型
    * @throws
     */
    public Store getInventoryWarningInfo(String storeID){
      try {
          
        Store store=  (Store) storeDao.get(storeID);
        /*
        if(StringUtil.isEmpty(store.getWarningEmail())&&StringUtil.isEmpty(store.getWarningMobile())){
            //这里是否是这个样子的逻辑？？？
            User user=  storeDao.get(User.class,storeID);
            store.setWarningEmail(user.getEmail());
            store.setWarningMobile(user.getMobile());
            
        }*/
        
        return store;
      } catch (Exception e) {
        log.error(e.getMessage());
      }
        
        return null;
    }
    
    /**
    * 跟新用户的的　商店　预警信息
    * @param id
    * @return Store 返回类型
    * @throws
     */
    public Store updateInventoryWarning(String id,String warnEmail,String warnMobile){
        try {
            
            Store store=  (Store) storeDao.get(id);
//            store.setWarningEmail(warnEmail);
//            store.setWarningMobile(warnMobile);
            storeDao.updateBean(store);
            return store;
          } catch (Exception e) {
            log.error(e.getMessage());
          }
        return null;
    }
}
