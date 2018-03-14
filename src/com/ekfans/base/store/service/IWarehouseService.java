package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.Warehouse;
import com.ekfans.pub.util.Pager;


/**
 * 
 * 仓库角色Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IWarehouseService {
    
    /**
     * 添加仓库
     * 
     * @param store
     * @return
     */
    public Boolean add(Warehouse wh);
    
    /**
     * 
    * @Title: list
    * @Description: TODO(仓库列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param name
    * @param status
    * @return Warehouse 返回类型
    * @throws
     */
    public List<Warehouse> list(Pager pager,String name,String status);
    
    /**
     * 
    * @Title: delete
    * @Description: TODO(删除)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return boolean 返回类型
    * @throws
     */
    public boolean delete(String id);
    
    /**
     * 
    * @Title: getWarehouseById
    * @Description: TODO(根据id获得仓库)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return Warehouse 返回类型
    * @throws
     */
    public Warehouse getWarehouseById(String id);
    
    /**
     * 
    * @Title: update
    * @Description: TODO(修改)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param wh
    * @return boolean 返回类型
    * @throws
     */
    public boolean update(Warehouse wh);
}