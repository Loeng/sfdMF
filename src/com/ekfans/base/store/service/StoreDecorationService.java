package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreDecorationDao;
import com.ekfans.base.store.model.StoreDecoration;
import com.ekfans.pub.util.StringUtil;

/**
 * 店铺装修业务实现Service
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
public class StoreDecorationService implements IStoreDecorationService {
	// 定义DAO
	@Autowired
	private IStoreDecorationDao storeDecorationDao;

    /**
     * 通过storeId得到StoreDecoration对象
     */
    public StoreDecoration getStoreDecorationByStoreId(String storeId) {
     // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sd from StoreDecoration as sd where 1=1");

        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果查询条件输入了storeId，添加查询条件
        if (!StringUtil.isEmpty(storeId)) {
            sql.append(" and sd.storeId = :storeId");
            paramMap.put("storeId", storeId);
        }
        try {
            // 调用DAO方法查找店铺
            List<StoreDecoration> list = storeDecorationDao.list(sql.toString(), paramMap);
            return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }

    /**
     * 对StoreDecoration进行修改
     */
    public boolean update(StoreDecoration sd) {
        try {
            // 调用DAO方法修改频道
            storeDecorationDao.updateBean(sd);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}