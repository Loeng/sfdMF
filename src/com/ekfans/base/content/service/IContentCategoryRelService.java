package com.ekfans.base.content.service;

import java.util.List;

import com.ekfans.base.content.model.ContentCategoryRel;

/**
 * 内容和分类关系实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IContentCategoryRelService {
    
//    public List<ContentCategoryRel> contentCategory(Pager pager,String contentName,String content);
    //根据Id获得分类关系对象表
    public List<ContentCategoryRel> getContentCategoryRelById(String categoryId);
    
    //根据咨询ID获取分类关系
    public List<ContentCategoryRel> getContentCategoryRelByContentId(String contentId);

    /**
     * 
    * @Title: remove
    * @Description: TODO 去除资讯与分类的关联
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param ccId 分类id
    * @param cId 资讯id
    * @return boolean 返回类型
    * @throws
     */
    public boolean remove(String ccId, String cId);
    
    /**
     * 
    * @Title: add
    * @Description: TODO 添加资讯与分类的关联
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param ccr
    * @return boolean 返回类型
    * @throws
     */
    public boolean add(ContentCategoryRel ccr);
}