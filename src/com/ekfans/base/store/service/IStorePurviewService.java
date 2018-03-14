package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.StorePurview;

/**
 * 企业权限功能菜单Service接口
 * 
 * @ClassName: IStorePurviewService
 * @author Lgy
 * @date 2014-01-06 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStorePurviewService {
	/**
	 * 根据角色ID获取店铺角色所对应的权限集合
	 * 
	 * @param roleId
	 * @return
	 */
	public List<StorePurview> getStorePurviewByRoleId(String roleId);

	/**
	 * 得到所有的店铺角色权限
	 * 
	 * @Title: storePurviewList
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<StorePurview> 返回类型
	 * @throws
	 */
	public List<StorePurview> storePurviewList();

	/**
	 * 
	 * @Title: getSPurviewByRoleId
	 * @Description: TODO(用于修改的根据roleId得到这个权限集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param roleId
	 * @return List<StorePurview> 返回类型
	 * @throws
	 */
	public List<StorePurview> getSPurviewByRoleId(String roleId);

	/**
	 * 
	 * @Title: sPurviewList
	 * @Description: TODO(用于修改的得到整个权限的页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param roleId
	 * @return List<StorePurview> 返回类型
	 * @throws
	 */
	public List<StorePurview> sPurviewList(String roleId);

	/**
	 * 
	 * @param roleId
	 * @param purviewId
	 * @return
	 */
	public StorePurview getPurviewById(String roleId, String purviewId);
	
	public StorePurview getPurviewById(String purviewId);

}