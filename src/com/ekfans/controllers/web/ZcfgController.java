package com.ekfans.controllers.web;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.ResourceBundleUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class ZcfgController extends BasicController{
	
	private Logger log = LoggerFactory.getLogger(ZcfgController.class);
	@Resource
	private IContentCategoryService contentCategoryService;
	@Resource
	private IContentService contentService;

	@RequestMapping(value = "/web/wfcz/zcfgOne")
	public String getOneLoad(){
		String keyId = (new ResourceBundleUtil()).getProperty("zcfg.content.category");
		
		List<ContentCategory> cclist = contentCategoryService.getChildCategories(keyId);
		
		getRequest().setAttribute("cclist", cclist);
		getRequest().setAttribute("firstId", cclist != null && cclist.size() > 0 ? cclist.get(0).getId() : "");
		getRequest().setAttribute("firstName", cclist != null && cclist.size() > 0 ? cclist.get(0).getName() : "");
		getRequest().setAttribute("cc", contentCategoryService.getCategoryById(keyId));
		
		return "/web/channel/zcfg/category";
	}
	
	@RequestMapping(value = "/web/wfcz/zcfgTwo")
	public String getTwoLoad(){
		String id = getRequest().getParameter("id");
		String name = getRequest().getParameter("name");
		String currentpageStr = getRequest().getParameter("pageNum"); // 页码
		
		// 定义分页
		Pager pager = new Pager();
		pager.setRowsPerPage(10);
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);
		
		getRequest().setAttribute("clist", contentService.getContentByFullPath(pager, id));
		getRequest().setAttribute("name", name);
		getRequest().setAttribute("id", id);
		getRequest().setAttribute("pager", pager);
		
		return "/web/channel/zcfg/content_list";
	}
}
