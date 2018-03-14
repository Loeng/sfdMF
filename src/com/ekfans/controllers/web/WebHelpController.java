package com.ekfans.controllers.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentModel;
import com.ekfans.base.content.service.ContentCategoryService;
import com.ekfans.base.content.service.IContentModelService;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 店铺登录Controller
 * 
 * @Title: StoreLoginController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author ll
 * @date 2014-5-15 上午9:18:54 
 * @version V1.0
 */
@Controller
public class WebHelpController extends BasicController {
	@Autowired
	private IContentService contentService;
	@Autowired
	private IContentModelService contentModelService;
	@Autowired
	private ContentCategoryService contentCategoryService;
	/**
	 * 
	 * @Title: getProduct
	 * @Description: TODO 帮助中心 详细业务流程: (详细描述此方法相关的业务处理流程) void 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/help/{id}")
	public String getProduct(@PathVariable String id) {
		// 从缓存获取四个分类
		String cid = getRequest().getParameter("cid");
		//根据获取的ID查询分类
		ContentCategory  c =contentCategoryService.getCategoryById(cid);
		ContentCategory category = null;
		if(c!=null){
		    category = Cache.getContentsByCategoryName(c.getName());
		}
		// 根据id和页码获取资讯和资讯内容
		Content content = contentService.getContent(id);
		List<ContentModel> contentModels =
		contentModelService.getContentModelByContentId(id);

		getRequest().setAttribute("content", content);
		getRequest().setAttribute("contentModels", contentModels);
		getRequest().setAttribute("category", category);

		return "/web/commons/help";
	}
	
	/**
	 * 
	* @Title: getProduct
	* @Description: TODO 大宗顶部直接跳转到帮助中心
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @return String 返回类型
	* @throws
	 */
	@RequestMapping(value = "/web/help")
    public String getProduct() {
        ContentCategory category = Cache.getContentsByCategoryName("大宗帮助中心");
        // 获取第一个子分类
        ContentCategory child = category.getChildList().get(0);
        
        Content content = new Content();
        List<ContentModel> contentModels = new ArrayList<ContentModel>();
        // 获取第一个子分类下的第一条资讯
        if(child.getContents()!=null && child.getContents().size()>0){
            content = child.getContents().get(0);
            contentModels = contentModelService.getContentModelByContentId(content.getId());
        }
       
        getRequest().setAttribute("content", content);
        getRequest().setAttribute("contentModels", contentModels);
        getRequest().setAttribute("category", category);

        return "/web/commons/help";
    }
}
