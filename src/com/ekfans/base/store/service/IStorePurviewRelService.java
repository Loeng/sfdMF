package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.StorePurviewRel;


/**
 * 店铺权限功能菜单与角色关联Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IStorePurviewRelService {
    /**
     * 
    * @Title: addStorePurviewRel
    * @Description: TODO(新增)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param spr
    * @return boolean 返回类型
    * @throws
     */
	public boolean addStorePurviewRel(StorePurviewRel spr);
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
	public boolean modify(StorePurviewRel spr);
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
	/**
	 * 
	* @Title: getStorePurviewRelByRoleId
	* @Description: TODO(根据roleId得到集合)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param roleId
	* @return List<StorePurviewRel> 返回类型
	* @throws
	 */
	public List<StorePurviewRel> getStorePurviewRelByRoleId(String roleId);
}