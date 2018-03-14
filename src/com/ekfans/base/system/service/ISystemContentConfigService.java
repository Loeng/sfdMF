package com.ekfans.base.system.service;

import java.util.List;

import com.ekfans.base.system.model.SystemContentConfig;

/**
 * 系统信息配置实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2014-4-25
 * @version 1.0
 */
public interface ISystemContentConfigService {
    /**
     * 
    * @Title: getgetConfigList
    * @Description: TODO(得到系统信息配置列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return List<SystemContentConfig> 返回类型
    * @throws
     */
    public List<SystemContentConfig> getConfigList();
    
    /**
     * 新增
     */
    public boolean addContentConfig(SystemContentConfig systemContentConfig);
    
    public SystemContentConfig getById(String id);
    
    public boolean updateContentConfig(SystemContentConfig systemContentConfig);
    
    public boolean delete(String id);
}
