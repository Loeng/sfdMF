package com.ekfans.base.store.service;

import com.ekfans.base.store.model.WarehouseOrder;



/**
 * 
 * 提货单Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IWarehouseOrderService {
    
    /**
     * 用于load出的出库单信息
     * 
     */
    public WarehouseOrder getWarehouseOrderById(String id);
    
    /**
     * 根据id得到对象
     */
    public WarehouseOrder getById(String id);
    
    /**
     * 修改
     */
    public boolean update(WarehouseOrder who);
}