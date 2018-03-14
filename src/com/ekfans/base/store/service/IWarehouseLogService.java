package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.WarehouseLog;
import com.ekfans.pub.util.Pager;


/**
 * 
 * 仓库日志Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IWarehouseLogService {
    
    /**
     * 添加仓库
     * 
     * @param store
     * @return
     */
    public Boolean add(WarehouseLog whl);
    
    /**
     * 获取仓库日志集合
     */
    public List<WarehouseLog> list(Pager pager,String name,String status);
    
    /**
     * 根据id查询日志详情
     */
    public WarehouseLog getWarehouseLogById(String id);
    
   
}