package com.ekfans.base.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.IWelfarePurchaseDao;
import com.ekfans.base.user.model.WelfarePurchase;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: WelfarePurchaseService
* @Description: TODO 福利采购service接口
* @author ekfans
* @date 2014-11-21 上午9:35:51
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class WelfarePurchaseService implements IWelfarePurchaseService {
	
	private Logger log = LoggerFactory.getLogger(WelfarePurchaseService.class);
	@Autowired
	private IWelfarePurchaseDao purchaseDao;
    @Override
    public boolean save(WelfarePurchase purchase) {
        // 对象为空返回false
        if(purchase == null){
            return false;
        }
        
        try {
            // 保存成功返回true
            purchaseDao.addBean(purchase);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<WelfarePurchase> list(Pager pager, String storeId, String productNo, String companyName,
            String beginTime, String endTime) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select w,p.unit from WelfarePurchase as w,Product as p where 1=1");
        // 表关联
        sql.append(" and w.productId = p.id");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(storeId)) {
            sql.append(" and w.storeId = :storeId");
            paramMap.put("storeId", storeId);
        }

        // 如果查询条件输入了status，添加查询条件
        if (!StringUtil.isEmpty(productNo)) {
            // 添加查询条件
            sql.append(" and w.productNo = :productNo");
            paramMap.put("productNo", productNo);
        }

        // 如果查询条件输入了mobile，添加查询条件
        if (!StringUtil.isEmpty(companyName)) {
            sql.append(" and w.companyName like :companyName");
            paramMap.put("companyName", "%" + companyName + "%");
        }
        
        // 价格区间的查找
        if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
            sql.append(" and w.createTime  BETWEEN :beginTime AND :endTime");
            paramMap.put("beginTime",beginTime);
            paramMap.put("endTime", endTime);
        } else if (!StringUtil.isEmpty(beginTime) && StringUtil.isEmpty(endTime)) {
            sql.append(" and w.createTime > :beginTime");
            paramMap.put("beginTime", beginTime);
        } else if (StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
            sql.append(" and w.createTime < :endTime");
            paramMap.put("endTime", endTime);
        }
        
        sql.append(" order by w.createTime desc");

        try {
            // 调用DAO方法查找用户
            List<Object[]> list = purchaseDao.list(pager, sql.toString(), paramMap);
            List<WelfarePurchase> purchases = new ArrayList<WelfarePurchase>();
            for(Object[] object:list){
                WelfarePurchase purchase = (WelfarePurchase)object[0];
                // 设置单价
                purchase.setUnit((String)object[1]);
                // 放入集合
                purchases.add(purchase);
            }
            return purchases;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public WelfarePurchase getPurchaseById(String id) {
        if(StringUtil.isEmpty(id)){
            return null;
        }
        
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select w,p.unit from WelfarePurchase as w,Product as p where 1=1");
        // 表关联
        sql.append(" and w.productId = p.id");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        sql.append(" and w.id = :id");
        paramMap.put("id", id);

        try {
            // 调用DAO方法查找用户
            List<Object[]> list = purchaseDao.list(sql.toString(), paramMap);
            WelfarePurchase purchase = new WelfarePurchase();
            for(Object[] object:list){
                purchase = (WelfarePurchase)object[0];
                // 设置单价
                purchase.setUnit((String)object[1]);
            }
            return purchase;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean update(WelfarePurchase purchase) {
        // 对象为空返回false
        if(purchase == null){
            return false;
        }
        
        try {
            // 保存成功返回true
            purchaseDao.updateBean(purchase);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    @Override
    public WelfarePurchase systemGetPurchaseById(String id) {
        if(StringUtil.isEmpty(id)){
            return null;
        }
        
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select w,p.unit,s.storeName from WelfarePurchase as w,Product as p,Store as s where 1=1");
        // 表关联
        sql.append(" and w.productId = p.id");
        // 表关联
        sql.append(" and w.storeId = s.id");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        sql.append(" and w.id = :id");
        paramMap.put("id", id);

        try {
            // 调用DAO方法查找用户
            List<Object[]> list = purchaseDao.list(sql.toString(), paramMap);
            WelfarePurchase purchase = new WelfarePurchase();
            for(Object[] object:list){
                purchase = (WelfarePurchase)object[0];
                // 设置单价
                purchase.setUnit((String)object[1]);
                // 设置核心企业名
                purchase.setStoreName((String)object[2]);
            }
            return purchase;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}