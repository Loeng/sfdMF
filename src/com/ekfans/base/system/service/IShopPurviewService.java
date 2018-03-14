package com.ekfans.base.system.service;

import java.util.List;

import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.model.ShopPurview;

/**
 * 系统权限菜单Service接口
 * 
 * @ClassName: IShopPurviewService
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IShopPurviewService {
	
	/**
	 * 得到所有系统菜单列表
	 */
	public List<ShopPurview> getAllShopPurview();
	/**
	 * 得到管理员所有系统菜单列表
	 * @throws Exception 
	 */
	public List<ShopPurview> getManagerAllShopPurview(Manager manager) throws Exception;
	/**
	 * 得到所有系统菜单列表,并标记用户选中菜单
	 */
	public List<ShopPurview> getPurviewsByRoleIdOrMark(String roleId);

	/**
	 * 根据管理员获取管理员权限
	 * 
	 * @param manager
	 * @return
	 */
	public List<ShopPurview> getPurviewsByManager(Manager manager, String topId);

	/**
	 * 根据管理员获取管理员权限
	 * 
	 * @param manager
	 * @return
	 */
	public List<ShopPurview> getTopPurviewsByManager(Manager manager);

	/**
	 * 根据管理员和上级菜单获取管理员权限的下级菜单
	 * 
	 * @param manager
	 * @return
	 */
	public List getChildPurViewsByManager(Manager manager, String parentId);

	/**
	 * 
	 * @Title: getFristPurViews
	 * @Description: TODO(查询出所有的一级菜单) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<ShopPurview> 返回类型
	 * @throws
	 */
	public List<ShopPurview> getFristPurViews();

	/**
	 * 
	 * @Title: getChildPurViews
	 * @Description: TODO(查询出对应的子集菜单) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<ShopPurview> 返回类型
	 * @throws
	 */
	public List<ShopPurview> getChildPurViews(String id);


	/**
	 * 
	 * @Title: getShopPurviewbyRoleId
	 * @Description: TODO(用于修改的根据roleId得到对应的权限集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param roleId
	 * @return List<ShopPurview> 返回类型
	 * @throws
	 */
	public List<ShopPurview> getShopPurviewbyRoleId(String roleId);
}
