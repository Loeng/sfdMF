package com.ekfans.base.user.service;

import java.util.List;

import com.ekfans.base.user.model.UserLevel;
import com.ekfans.pub.util.Pager;

/**
 * 会员等级实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IUserLevelService {
	/**
	 * 查询等级列表
	 * @param page
	 * @return
	 */
	public List<UserLevel> getLevels(Pager page);
	
	/**
	 * 新增会员等级
	 * @param level
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addLevel(UserLevel level,String uploadFileName);
	
	/**
	 * 根据id删除会员等级
	 * @param id
	 * @return
	 */
	public boolean deleteLevel(String id);
	
	/**
	 * 修改会员等级
	 * @param level
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyLevel(UserLevel level,String uploadFileName);
	
	/**
	 * 根据id查找会员等级
	 * @param id
	 * @return
	 */
	public UserLevel getLevel(String id);
	
	/**
	 * 
	* @Title: getUserLevelByName
	* @Description: TODO 根据会员等级名取得会员等级
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param @param name
	* @param @return    设定文件
	* @return UserLevel    返回类型
	* @throws
	 */
	public UserLevel getUserLevelByName(String name);
	
}