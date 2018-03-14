package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.service.ProductService;
import com.ekfans.base.store.dao.IStoreCollectDao;
import com.ekfans.base.store.model.StoreCollect;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商户收藏管理业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class StoreCollectService implements IStoreCollectService {
	// 定义DAO
	@Autowired
	private IStoreCollectDao storeCollectDao;

	//定义错误日志
    public static Logger log = LoggerFactory.getLogger(ProductService.class);
    /**
     * 根据id删除
     */
    public boolean delete(String id) {
     // 如果传进来的id为空，则返回false
        if (StringUtil.isEmpty(id)) {
            return false;
        }

        try {
            // 调用SERVICE的方法删除店铺
            storeCollectDao.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据userID得到对应的收藏店铺
     */
    public List<StoreCollect> getList(Pager pager, String userId) {
     // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sc.id,s.id,s.storeLogo,s.storeName from Store as s ,StoreCollect as sc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //当品牌的id和product品跑对应
        sql.append(" and s.id = sc.storeId");
        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(userId)) {
            sql.append(" and sc.userId = :userId");
            paramMap.put("userId", userId);
        }
        try {
            // 调用DAO方法查找商品
            List<Object[]> list = storeCollectDao.list(pager, sql.toString(), paramMap);
            List<StoreCollect> storeCollects = new ArrayList<StoreCollect>();
            for(Object[] object:list){
                
                StoreCollect storeCollect = new StoreCollect();
                storeCollect.setId((String)object[0]);
                storeCollect.setStoreId((String)object[1]);
                storeCollect.setLogo((String)object[2]);
                storeCollect.setName((String)object[3]);
                storeCollects.add(storeCollect);
            }
            return storeCollects;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
    

}