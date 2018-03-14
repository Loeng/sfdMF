package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IWarehouseDao;
import com.ekfans.base.store.model.Warehouse;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 仓库实现Service
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
public class WarehouseService implements IWarehouseService {

    // 定义DAO
    @Resource
    private IWarehouseDao warehouseDao;
    
    // 定义错误处理日志
    private Logger log = LoggerFactory.getLogger(WarehouseService.class);
    
    @Override
    public Boolean add(Warehouse wh) {
        try {
            warehouseDao.addBean(wh);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Warehouse> list(Pager pager,String name, String status) {

        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select wh from Warehouse as wh where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(name)) {
            sql.append(" and wh.name like :name");
            paramMap.put("name", "%" + name + "%");
        }
        // 如果查询条件输入了status，添加查询条件
        if (!StringUtil.isEmpty(status)) {
            sql.append(" and wh.status = :status");
            paramMap.put("status", Boolean.parseBoolean(status));
        }
        
        sql.append(" order by wh.createTime desc");
        try {
            // 调用DAO方法查找角色
            List<Warehouse> list = warehouseDao.list(pager, sql.toString(), paramMap);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        try {
            warehouseDao.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Warehouse getWarehouseById(String id) {
        try {
            return (Warehouse) warehouseDao.get(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Warehouse wh) {
       try {
           warehouseDao.updateBean(wh);
           return true;
       } catch (Exception e) {
        log.error(e.getMessage());
       }
       return false;
    }
    
    
	
}