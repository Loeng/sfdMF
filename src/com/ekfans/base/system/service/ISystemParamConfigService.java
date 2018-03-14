package com.ekfans.base.system.service;

import java.util.List;

import com.ekfans.base.system.model.SystemParamConfig;

/**
 * 系统参数配置实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2014-4-25
 * @version 1.0
 */
public interface ISystemParamConfigService {
    /**
     * 
    * @Title: getConfigList
    * @Description: TODO(根据参数类型得到系统参数配置列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return List<SystemParamConfig> 返回类型
    * @throws
     */
    public List<SystemParamConfig> getConfigList(String type);
    
    /**
     * 
    * @Title: getConfigById
    * @Description: TODO(根据id获取系统参数配置)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return SystemParamConfig 返回类型
    * @throws
     */
    public SystemParamConfig getConfigById(String id);
    
    /**
     * 
    * @Title: updateConfig
    * @Description: TODO(更新参数配置)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return boolean 返回类型
    * @throws
     */
    public boolean updateConfig(SystemParamConfig config);
}
