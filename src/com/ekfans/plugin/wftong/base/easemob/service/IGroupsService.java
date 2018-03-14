package com.ekfans.plugin.wftong.base.easemob.service;

import com.ekfans.plugin.wftong.base.easemob.model.Groups;

/**
 * 
 * ClassName: IGroupsService 
 * @Description: TODO
 * @author chengm
 * @date 2015年7月28日
 */
public interface IGroupsService {

	/**
	 * @param groupsId
	 * @return
	 */
	public Groups findByGroupsId(String groupsId);

	/**
	 * 添加Groups信息
	 * @param groups
	 * @return
	 */
	public boolean addGroupsInfo(Groups groups);

}
