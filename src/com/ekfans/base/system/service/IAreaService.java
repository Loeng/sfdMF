package com.ekfans.base.system.service;

import java.util.List;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.model.Area;
import com.ekfans.pub.util.Pager;

/**
 * 区域Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IAreaService {
    
    /**
     * 列表方法
     */
    public List<Area> getList(Pager pager);
    
    /**
     * 获取全部区域
     */
    public List<Area> getAllArea();
    
    public List<Store> getStoreByAreaId(String areaId);
    
    
}
