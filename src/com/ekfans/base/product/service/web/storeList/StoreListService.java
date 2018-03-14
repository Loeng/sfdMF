package com.ekfans.base.product.service.web.storeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.web.storeList.IStoreListDao;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.store.model.Store;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class StoreListService implements IStoreListService {
    
    private Logger log = LoggerFactory.getLogger(StoreListService.class);
    @Autowired 
    private IStoreListDao storeListDao;
    
    /**
     * 根据模糊的店铺名查询出满足条件的店铺
     */
    @Override
    public List<Store> getStoresByFuzzyName(String storeName,Pager pager) {
        
	try {
	    if(StringUtil.isEmpty(storeName)){
	        List<Store> stores = new ArrayList<Store>();
	        return stores;
	    }
	    StringBuffer hql = new StringBuffer("select s.id,s.storeName,s.storeLogo,s.province,u.name from Store as s,User as u where 1=1");
	    Map<String,Object> params = new HashMap<String,Object>();
	    hql.append(" and s.id = u.id");
	    hql.append(" and s.storeName like :storeName");
	    params.put("storeName","%"+storeName+"%");
	    
	    // 调用DAO方法查找商品
        List<Object[]> list = storeListDao.list(pager, hql.toString(), params);
        List<Store> stores = new ArrayList<Store>();
        for(Object[] object:list){
            Store store = new Store();
            store.setId((String)object[0]);
            store.setStoreName((String)object[1]);
            store.setStoreLogo((String)object[2]);
            //store.setProvince((String)object[3]);
            store.setStoreUserName((String)object[4]);
            stores.add(store);
        }
        return stores;
	}
	catch(Exception e) {
	    log.error(e.getLocalizedMessage());
	}
	return null;
    }

    /**
     * 根据店铺id得到分类的集合
     */
    public List<ProductCategory> getProductCategoryByStoreId(String storeId) {
        try {
            if(StringUtil.isEmpty(storeId)){
            return null;
            }
            StringBuffer hql = new StringBuffer("select pc.name from ProductCategory as pc,Product as p,ProductCategoryRel as pcr where 1=1");
            Map<String,Object> params = new HashMap<String,Object>();
            hql.append(" and p.id = pcr.productId");
            hql.append(" and pcr.categoryId = pc.id");
            hql.append(" and p.storeId = :storeId");
            params.put("storeId",storeId);
            
            // 调用DAO方法查找商品
            List<Object[]> list = storeListDao.list(hql.toString(), params);
            List<ProductCategory> pcs = new ArrayList<ProductCategory>();
            for(Object[] object:list){
                
                ProductCategory pc = new ProductCategory();
                pc.setName((String)object[0]);
                pcs.add(pc);
            }
            return pcs;
        }
        catch(Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
        }

    /**
     * 根据条件查询
     */
    public List<Store> getStoresByCondition(String storeLevelId,Pager pager,String storeName) {
        try {
            if(StringUtil.isEmpty(storeName)){
            return null;
            }
            StringBuffer hql = new StringBuffer("select s.id,s.storeName,s.storeLogo,s.province,u.name from Store as s,User as u where 1=1");
            Map<String,Object> params = new HashMap<String,Object>();
            hql.append(" and s.id = u.id");
            
            if(!StringUtil.isEmpty(storeLevelId)){
                hql.append(" and s.levelId = :levelId");
                params.put("levelId",storeLevelId);
                }
            hql.append(" and s.storeName like :storeName");
            params.put("storeName","%"+storeName+"%");
            
            
            // 调用DAO方法查找商品
            List<Object[]> list = storeListDao.list(pager, hql.toString(), params);
            List<Store> stores = new ArrayList<Store>();
            for(Object[] object:list){
                Store store = new Store();
                store.setId((String)object[0]);
                store.setStoreName((String)object[1]);
                store.setStoreLogo((String)object[2]);
                //store.setProvince((String)object[3]);
                store.setStoreUserName((String)object[4]);
                stores.add(store);
            }
            return stores;
        }
        catch(Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
   
}
