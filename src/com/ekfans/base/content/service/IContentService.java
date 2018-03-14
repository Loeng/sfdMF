package com.ekfans.base.content.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.pub.util.Pager;

/**
 * 资讯实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IContentService {
    
   
    

	/**
	 * 根据id删除资讯
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteContent(String id);

	/**
	 * 添加资讯
	 * 
	 * @param content
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addContent(Content content, File uploadFile, String uploadFileContentType);

	/**
	 * 根据id查找资讯
	 * 
	 * @param id
	 * @return
	 */
	public Content getContent(String id);

	/**
	 * 修改资讯
	 * 
	 * @param content
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyContent(Content content, File uploadFile, String uploadFileContentType);

	/**
	 * 根据分类id查找资讯列表
	 * 
	 * @param pager 分页
	 * @param categoryId 分类id
	 * @param name 资讯名
	 * @return
	 */
	public List<Content> listContent(Pager pager, String categoryId, String name);

	/**
	 * 查找资讯列表
	 * 
	 * @param pager
	 *            分页
	 * @param name
	 *            资讯名
	 * @return
	 */
	public List<Content> listContent(Pager pager, String name,String bigenDate,String endDate,String checkStatus,String categoryId);

	/**
	 * 检查资讯id是否存在
	 * 
	 * @param id
	 * @return
	 */
	public boolean checkId(String id);

	/**
	 * 根据name查找资讯列表
	 */
	public Content getContentByName(String contentName);

	/**
	 * 
	 * @Title: getContentByFullPath
	 * @Description: TODO 根据全路径取得资讯列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param fullPath
	 * @param @return 设定文件
	 * @return Content 返回类型
	 * @throws
	 */
	public List<Content> getContentByFullPath(Pager pager, String fullPath);

	/**
	 * 
	 * @Title: addContentCategory
	 * @Description: TODO(新增咨询时,关联咨询的分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param contentId
	 * @param @param contentCategoryId
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean addContentCategory(String contentId, String contentCategoryId);

	/**
	 * 
	 * @Title: updateContentCategory
	 * @Description: TODO(跟新资讯详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param contentId
	 * @param @param contentCategoryId
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean updateContentCategory(String contentId, String contentCategoryId);

	/**
	 * 
	 * @Title: getContentCategoryRelByContentId
	 * @Description: TODO(修改咨询-获得当前咨询的分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param contentId
	 * @param @return 设定文件
	 * @return ContentCategoryRel 返回类型
	 * @throws
	 */
	public ContentCategoryRel getContentCategoryRelByContentId(String contentId);

	/**
	 * 根据资讯分类ID获取该分类所属的资讯
	 * 
	 * @Title: getContentsByCategory
	 * @param @param categoryId
	 * @param @return 设定文件
	 * @return List<Content> 返回类型
	 * @throws
	 */
	public List<Content> getContentsByCategory(String categoryId, int showNumber,Pager pager);

	public List<Content> listContent(String id);
	
	/**
	 * 
	* @Title: queryProductsByParams
	* @Description: TODO 批量移动时查找资讯集合
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param params
	* @return List<Product> 返回类型
	* @throws
	 */
	public List<Content> queryContentsByParams(Pager pager,Map<String,Object> params);

	public List<Content> getCCList(Pager pager, List<ContentCategory> cclist);
}
