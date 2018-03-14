package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.StoreRole;
import com.ekfans.pub.util.Pager;


/**
 * 
 * 店铺角色Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IStoreRoleService {
    /**
     * 添加角色
     * 
     * @param role
     * @return
     */
    public boolean addRole(StoreRole role);

    /**
     * 根据id查找角色
     * 
     * @param id
     * @return
     */
    public StoreRole getRoleById(String id);

    /**
     * 根据id删除角色
     * 
     * @param id
     * @return
     */
    public boolean deleteRole(String id);

    /**
     * 修改角色
     * 
     * @param role
     */
    public boolean modifyRole(StoreRole role);

    /**
     * 查找角色列表
     * 
     * @param pager
     *            分页
     * @param name
     *            角色名
     * @return
     */
    public List<StoreRole> listRole(Pager pager, String name);

    /**
     * 根据角色ID获取角色名称
     * 
     * @param roleId
     * @return
     */
    public String getRoleNameById(String roleId);
    
    /**
     * 根据角色名称查询角色
     * 
     * @param name
     * @return
     */
    public List<StoreRole> getRoleByName(String name);
}