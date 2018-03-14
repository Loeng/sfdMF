package com.ekfans.base.store.service;

import java.io.File;
import java.util.List;

import com.ekfans.base.store.model.StoreLevel;
import com.ekfans.pub.util.Pager;

/**
 * 店铺等级实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IStoreLevelService {
	/**
	 * 查询等级列表
	 * @param page
	 * @return
	 */
	public List<StoreLevel> getLevels(Pager page);
	
	/**
	 * 新增店铺等级
	 * @param level
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addLevel(StoreLevel level,File uploadFile,String uploadFileContentType);
	
	/**
	 * 根据id删除店铺等级
	 * @param id
	 * @return
	 */
	public boolean deleteLevel(String id);
	
	/**
	 * 修改店铺等级
	 * @param level
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyLevel(StoreLevel level,File uploadFile,String uploadFileContentType);
	
	/**
	 * 根据id查找店铺等级
	 * @param id
	 * @return
	 */
	public StoreLevel getLevel(String id);
}