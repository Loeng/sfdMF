package com.ekfans.base.finance.service;

import java.util.List;

import com.ekfans.base.finance.model.BankPurview;

/**
 * 企业权限功能菜单Service接口
 * 
 * @ClassName: IStorePurviewService
 * @author Lgy
 * @date 2014-01-06 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IBankPurviewService {
	/**
	 * 根据角色ID获取店铺角色所对应的权限集合
	 * 
	 * @param roleId
	 * @return
	 */
	public List<BankPurview> getBankPurviewByRoleIdAndType(String roleId, Boolean type);

	/**
	 * 得到所有的店铺角色权限
	 * 
	 * @Title: storePurviewList
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<StorePurview> 返回类型
	 * @throws
	 */
	public List<BankPurview> bankPurviewList();

	/**
	 * 
	 * @Title: sPurviewList
	 * @Description: TODO(用于修改的得到整个权限的页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param roleId
	 * @return List<StorePurview> 返回类型
	 * @throws
	 */
	public List<BankPurview> bPurviewList(String roleId, Boolean type,String parentRoleId,Boolean parentType);

	/**
	 * 
	 * @param roleId
	 * @param purviewId
	 * @return
	 */
	public BankPurview getPurviewById(String roleId, String purviewId);

	public BankPurview getPurviewById(String purviewId);

}