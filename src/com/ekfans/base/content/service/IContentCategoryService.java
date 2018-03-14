package com.ekfans.base.content.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.pub.util.Pager;

/**
 * 内容分类实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IContentCategoryService {
	/**
	 * 获取一级菜单
	 * 
	 * @return
	 */
	public List<ContentCategory> getCategories();

	/**
	 * 根据上级菜单获取下级菜单
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ContentCategory> getChildCategories(String parentId);

	/**
	 * 新增内容分类
	 * 
	 * @param category
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addCategory(ContentCategory category, String picturePath);

	/**
	 * 根据id删除内容分类
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteCategory(String id);

	/**
	 * 修改内容分类
	 * 
	 * @param category
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyCategory(ContentCategory category, String picturePath);

	/**
	 * 根据id查找内容分类
	 * 
	 * @param id
	 * @return
	 */
	public ContentCategory getCategoryById(String id);

	/**
	 * 
	 * @Title: checkId
	 * @Description: TODO(检查分类id是否存在) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean checkId(String id);

	/**
	 * 
	 * @Title: getContentCategoryByName
	 * @Description: TODO 根据分类名获得分类ID 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param name
	 * @param @return 设定文件
	 * @return ContentCategory 返回类型
	 * @throws
	 */

	public ContentCategory getContentCategoryByName(String name,String parentId);

	/**
	 * 
	 * @Title: getContentCategoryByFullPath
	 * @Description: TODO 根据全路径取得ContentCategory的二级菜单 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param fullPath
	 * @param @return 设定文件
	 * @return ContentCategory 返回类型
	 * @throws
	 */
	public List<ContentCategory> getContentCategoryByFullPath(Pager pager, String parentId, String fullPath);
	
	public List<Content> getWebContentByFullPath(Pager pager,String categoryId);

	/**
	 * 
	 * @Title: getCategoryByPrarentId
	 * @Description: TODO 根据获取的parentId获取ContentCategory集合 详细业务流程:
	 *               (详细描述此方法相关的业务处理流程)
	 * @param @param parentId
	 * @param @return 设定文件
	 * @return List<ContentCategory> 返回类型
	 * @throws
	 */
	public List<ContentCategory> getCategoryByPrarentId(Pager pager, String parentId, String id);

	/**
	 * 
	* @Title: getContentsByCategoryId
	* @Description: TODO 根据分类id获取资讯列表
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param categoryId
	* @return List<Content> 返回类型
	* @throws
	 */
	public List<Content> getContentsByCategoryId(String categoryId);

	/**
	 * 根据父ID查询子分类集合
	 * 
	 * @Title: getContentCategorysByParentId
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param parentId
	 * @param @param showNumber
	 * @param @return 设定文件
	 * @return List<ContentCategory> 返回类型
	 * @throws
	 */
	public List<ContentCategory> getContentCategorysByParentId(String parentId, boolean canParentNull, int showNumber);

	// 根据contentId获取该咨询分类名字集合
	public List<List<ContentCategory>> getContentCategoryBycontentId(String contentId);

	/**
	 * 
	* @Title: getAllChildCategories
	* @Description: TODO 根据父id获取子分类（包含status为0的分类）
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param parentId
	* @return List<ContentCategory> 返回类型
	* @throws
	 */
	public List<ContentCategory> getAllChildCategories(String parentId);


	/**
	 * 获取分类的全称
	 * 
	 * @Title: getCategoryFullName
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param categoryId 资讯分类ID
	 * @param @param splitStr 资讯分类名称每级之间的分隔符
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getCategoryFullNameByCategoryId(String categoryId, String splitStr);

	List<ContentCategory> getContentCategoryBypId(Pager pager);
	
	/**
	 * 
	* @Title: getAllCategoryIdsById
	* @Description: TODO 根据分类id得到所有子分类的id(包括自己)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param id
	* @return List<String> 返回类型
	* @throws
	 */
	public List<String> getAllCategoryIdsById(String id);
	
	public List<ContentCategory> getChlichAll(String id);
	
}