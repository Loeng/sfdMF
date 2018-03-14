package com.ekfans.base.system.service;

import java.util.List;

import com.ekfans.base.system.model.ShopRole;
import com.ekfans.pub.util.Pager;

/**
 * 后台角色Service接口
 * 
 * @ClassName: IShopRoleService
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IShopRoleService {
	
	/**
	 * 添加后台角色
	 * 
	 * @param role 角色
	 * @param purviewIds 菜单集合
	 * @return 1:角色名称为空，2:角色名称6-20字符组成，3:保存失败，4:保存成功
	 */
	public int addShopRole(ShopRole role, String[] purviewIds);
	
	/**
	 * 查找角色列表
	 * 
	 * @param pager 分页
	 * @param name 角色名
	 */
	public List<ShopRole> listRole(Pager pager, String name);
	
	/**
	 * 根据id删除角色
	 */
	public boolean deleteRole(String id);
	
	
	/**
	 * 根据id查找角色
	 */
	public ShopRole getRoleById(String id);
	
	/**
	 * 修改角色和角色所属权限
	 * 
	 * @param role 角色
	 * @param purviewIds 菜单集合
	 * @return 1:角色名称为空，2:角色名称6-20字符组成，3:修改失败，4:修改成功
	 */
	public int updateRole(ShopRole role, String[] purviewIds);
	
	/**
	 * 根据角色ID获取角色名称
	 */
	public String getRoleNameById(String roleId);

}
