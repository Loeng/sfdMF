package com.ekfans.base.system.service;

import com.ekfans.base.system.model.RolePurviewRel;

/**
 * 角色权限关系表实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IRolePurviewRelService {
    /**
     * 
    * @Title: addRolePurviewRel
    * @Description: TODO(添加)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param rpr
    * @return boolean 返回类型
    * @throws
     */
    public boolean addRolePurviewRel(RolePurviewRel rpr);
    
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
    * @Title: modify
    * @Description: TODO(修改)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param spr
    * @return boolean 返回类型
    * @throws
     */
    public boolean modify(RolePurviewRel rpr);
    /**
     *     
    * @Title: modify
    * @Description: TODO(根据roleId删除关联权限表的对应数据)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param roleId
    * @return boolean 返回类型
    * @throws
     */
    public boolean getRoleIdDelete(String roleId);    
}
