package com.ekfans.controllers.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.ParseException;  
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.channel.model.ChannelConfig;
import com.ekfans.base.channel.service.IChannelConfigService;
import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.base.content.model.ContentModel;
import com.ekfans.base.content.service.IContentCategoryRelService;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.base.content.service.IContentModelService;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: InformationController
 * @Description: TODO 前台页的资讯列表 和 咨询列表详情,页面跳转
 * @author 成都易科远见科技有限公司
 * @date May 21, 2014 10:27:03 AM
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class InformationController extends BasicController {
	// 注入service
	@Autowired
	private IContentCategoryService contentCategoryService;

	@Autowired
	private IContentService contentService;

	@Autowired
	private IContentModelService contentModelService;
	@Autowired
	private IProductService productService;

	@Autowired
	private IProductCategoryService productCategoryService;
	
	@Autowired
    private IChannelConfigService channelConfigService;
	
	@Autowired
    private IContentCategoryRelService contentCategoryRelService;

	/**
	 * 
	 * @Title: view
	 * @Description: TODO 取得全部的二级分类 详细业务流程:wsj (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/information/{parentId}")
	public String view(@PathVariable String parentId, HttpServletRequest request) {
		// 分页bigen
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取分页数据
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		//
		pager.setRowsPerPage(8);
		// 分页end
		// 得到当前选中的分类
		String cId = request.getParameter("cId");
		// 取得分类
		List<ContentCategory> contentCategorys = contentCategoryService.getContentCategoryBypId(pager);
		// 取得该分类下的资讯
		List<Content> contents = null;
		if (cId != null && cId != "") {
			contents = contentService.getContentByFullPath(pager, cId);
		} else {
			contents = contentService.getContentByFullPath(pager, parentId);
		}
		// 取得推荐商品
		List<Product> products = productService.getVisitCountMaxProduct();
		// 获取资讯链接短路径
		List<Content> list = new ArrayList<Content>();
		if (contents != null && contents.size() > 0) {
			BasicRequest basicRequest = new BasicRequest(request);
			for (int i = 0; i < contents.size(); i++) {
				Content content = contents.get(i);
				if (content != null) {
					content.setLinkUrl(basicRequest.getContentWebUrl(content.getId(), null));
					list.add(content);
				}
			}
		}
		// 得到所有商品分类
		List<ProductCategory> productCategories = productCategoryService.getCategories(null,null);
		// 将分类集合绑定到页面上
		request.setAttribute("productCategories", productCategories);
		// 设置页面数据
		request.setAttribute("products", products);
		request.setAttribute("contentCategorys", contentCategorys);
		request.setAttribute("contents", contents);
		request.setAttribute("pager", pager);
		return "/web/contentInformation";
	}

	/**
	 * 
	 * @Title: viewDetails
	 * @Description: TODO 根据资讯列表 点击显示的该分类下的所有分类 详细业务流程:wsj (详细描述此方法相关的业务处理流程)
	 * @param @param parentId
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/information/detail/{fullPath}")
	public String viewDetails(@PathVariable String fullPath, HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();
		// 从页面获取分页数据
		String currentpageStr = request.getParameter("pageNum");

		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		List<ContentCategory> contentCategorys = contentCategoryService.getCategoryByPrarentId(pager, null, fullPath);
		List<Content> contents = contentService.getContentByFullPath(pager, fullPath);
		request.setAttribute("contentCategorys", contentCategorys);
		request.setAttribute("contents", contents);
		request.setAttribute("pager", pager);
		return "/web/informationCategoryDetail";
	}

	/**
	 * 
	 * @Title: viewDetail
	 * @Description: TODO 根据资讯列表页中的资讯ID跳转到详情页面 详细业务流程:wsj (详细描述此方法相关的业务处理流程)
	 * @param @param contentName
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/information/details/{id}/{pageNo}")
	public String viewDetail(@PathVariable String id, @PathVariable String pageNo, HttpServletRequest request) {
	    Map<String, ChannelConfig> map = channelConfigService.getChannelConfigsByChannelId(request, "information");
        request.setAttribute("configMap", map);
		Content ctent = contentService.getContent(id);
		ContentCategory category = null;
		if (ctent != null) {
			category = contentCategoryService.getCategoryById(ctent.getCategoryId());
		}
		List<ContentModel> contentModels = contentModelService.getContentModel2ByContentId(id, pageNo);
		List<Integer> pages = new ArrayList<Integer>();
		// 根据资讯id查询出对应的总页数
		List<ContentModel> contentModels2 = contentModelService.getContentModel2ByContentId(id, null);
		for (int i = 1; i <= contentModels2.size(); i++) {
		    pages.add(i);
		}
		// 定义分页
        Pager pager = new Pager();
        pager.setCurrentPage(Integer.valueOf(pageNo));
        Pager pager2 = new Pager();
        pager2.setRowsPerPage(10);
        //根据资讯id查询出对应的资讯分类
        ContentCategoryRel ccr = new ContentCategoryRel();
        if(!StringUtil.isEmpty(id)){
             ccr = contentCategoryRelService.getContentCategoryRelByContentId(id).get(0);
        }
        //查询该分类下的其他咨询
        List<Content> contentList = contentService.getContentByFullPath(pager2, ccr.getCategoryId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //若发布时间在 24 小时内则转为 x 小时前
        for (int i = 0; i < contentList.size(); i++){
        	Content content = contentList.get(i);
        	String nowTime = sdf.format(new Date());
        	try {
				Date date = sdf.parse(content.getCreateTime());
				Date now = sdf.parse(nowTime);
				long difference = now.getTime() - date.getTime();
				if (difference/1000/60/60 < 24){
					int diffHour = Integer.parseInt(String.valueOf(difference/1000/60/60));
					if (diffHour >= 1){
						content.setCreateTime(String.valueOf(diffHour) + " 小时前");
					}else{
						content.setCreateTime("刚刚");
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        }
		// 根据资讯id查询出对应的总页数
		// 绑定总页数到页面
		request.setAttribute("ctent", ctent);
		request.setAttribute("contentCategory", category);
		request.setAttribute("contentModels", contentModels);
		request.setAttribute("contentList", contentList);
		request.setAttribute("Pages", pages);
		request.setAttribute("pager", pager);
		return "/web/purchase/content/informationDetail";
	}
	
	/**
	 * 
	* @Title: viewDetail
	* @Description: TODO(分类得到资讯)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param id
	* @param pageNo
	* @param request
	* @return String 返回类型
	* @throws
	 */
    @RequestMapping(value = "/web/contentcategory/{objectId}")
    public String contentCartgory(@PathVariable String objectId, HttpServletRequest request) {
        
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取分页数据
        String currentpageStr = request.getParameter("pageNum");
        pager.setRowsPerPage(10);
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            try {
                currentPage = Integer.parseInt(currentpageStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        Map<String, ChannelConfig> map = channelConfigService.getChannelConfigsByChannelId(request, "information");
        request.setAttribute("configMap", map);
        
        List<Content> contents = contentService.listContent(pager, objectId, null);
        request.setAttribute("contents", contents);
        request.setAttribute("pager", pager);
        request.setAttribute("objectId", objectId);
        return "/web/purchase/content/informationList";
    }
    /**
     * 
     * @Title: getContents
     * @Description: TODO(根据咨询全路径获取全部咨讯)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param 
     * @return 
     * @throws
     */
    @RequestMapping(value = "/web/information/detail/{fullPath}/{pageNum}")
	public String getContents(@PathVariable String fullPath, @PathVariable String pageNum, HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();

		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPage = Integer.parseInt(pageNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String rows= request.getParameter("rows");
		//每页显示条
		pager.setRowsPerPage(Integer.parseInt(rows));
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		
		List<Content> contents = contentCategoryService.getWebContentByFullPath(pager,fullPath);
		request.setAttribute("contents", contents);
		request.setAttribute("pager", pager);
		return "/web/channel/hyzx/hyzxList";
	}
	// /**
	// *
	// * @Title: viewload
	// * @Description: TODO load其他分类的页面
	// * 详细业务流程:
	// * (详细描述此方法相关的业务处理流程)
	// * @param @param parentId
	// * @param @param request
	// * @param @return 设定文件
	// * @return String 返回类型
	// * @throws
	// */
	// @RequestMapping(value="/web/information/load/{id}")
	// public String viewload(@PathVariable String id,HttpServletRequest
	// request){
	// // 定义分页
	// Pager pager = new Pager();
	// // 从页面获取分页数据
	// String currentpageStr = request.getParameter("pageNum");
	//
	// // 将从页面获取的分页数据转化成int型
	// int currentPage = 1;
	// if (!StringUtil.isEmpty(currentpageStr)) {
	// try {
	// currentPage = Integer.parseInt(currentpageStr);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // 设置要查询的页码
	// pager.setCurrentPage(currentPage);
	// List<ContentCategory> contentCategorys =
	// contentCategoryService.getChildCategories(id);
	// List<ContentCategory> contents =
	// contentCategoryService.getContentCategoryByFullPath(pager,null,null,id);
	// request.setAttribute("contents",contents);
	// request.setAttribute("contentCategorys",contentCategorys);
	// request.setAttribute("pager",pager);
	// return "/web/contentInformationLoad";
	// }
}
