package com.ekfans.base.content.service;

import java.util.List;

import com.ekfans.base.content.model.ContentModel;
import com.ekfans.pub.util.Pager;

import javax.servlet.http.HttpServletRequest;

/**
 * 资讯详细内容实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IContentModelService {
	/**
	 * 
	 * @Title: addContentModel
	 * @Description: TODO(新增咨询的时候 把咨询的详情信息添加到content_model表中) 详细业务流程:
	 *               (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean addContentModel(String contentId, String[] contentDetails, HttpServletRequest request);

	/**
	 * 
	 * @Title: getContentModelById
	 * @Description: TODO(根据咨询id获得咨询的详情信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return ContentModel 返回类型
	 * @throws
	 */
	public List<ContentModel> getContentModelByContentId(String contentId);

	/**
	 * 
	 * @Title: updateContentModel
	 * @Description: TODO(跟新咨询的详情信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean updateContentModel(String contentId, String[] contentDetails);

	/**
	 * 
	 * @Title: deleteContentModel
	 * @Description: TODO(根据id删除咨询的详情信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param id
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean deleteContentModel(String id);

	public List<ContentModel> getContentModel2ByContentId(String contentId, String page);
}
