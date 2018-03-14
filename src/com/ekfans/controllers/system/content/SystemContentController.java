package com.ekfans.controllers.system.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.base.content.model.ContentModel;
import com.ekfans.base.content.model.ContentRel;
import com.ekfans.base.content.service.ContentCategoryService;
import com.ekfans.base.content.service.IContentCategoryRelService;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.base.content.service.IContentModelService;
import com.ekfans.base.content.service.IContentRelService;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.number.NoManager;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemContentController extends BasicController {
	@Autowired
	private IContentService contentService;

	@Autowired
	private IContentModelService contentModelService;

	@Autowired
	private IContentCategoryService contentCategoryService;

	@Autowired
	private IContentCategoryRelService contentCategoryRelService;
	@Autowired
	private IContentRelService relService;

	/**
	 * 跳转到新增页面 查询出咨询的分类信息,这里第一次默认查询第一级
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/add")
	public String add(HttpServletRequest request) {
		// 查询出一级咨询
		List<ContentCategory> categories = contentCategoryService.getCategories();
		request.setAttribute("categories", categories);

		String cTypes = Cache.getSystemParamConfig("资讯标签");
		if(!StringUtil.isEmpty(cTypes)){
			String[] types = cTypes.split(";");
			request.setAttribute("cTypes",types);
		}

		return "/system/content/contentAdd";
	}

	/**
	 * @Title channelList
	 * @Description TODO(关联频道列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午1:58:01
	 */
	@RequestMapping("/system/content/channelList")
	public String channelList(HttpServletRequest request){
		String id = request.getParameter("id");
		// 获取频道分类
		List<ContentCategory> categories = contentCategoryService.getCategories();
		// 获取用户选中频道
		List<ContentRel> contentRels = relService.getList(id);
		// 判断分类集合是否为空
		if(categories != null && categories.size() > 0){
			// 判断资讯关联频道分类集合是否为空
			if(contentRels != null && contentRels.size() > 0){
//						// 遍历资讯关联频道
					for (int j = 0; j < contentRels.size(); j++) {
						setChilds(categories,contentRels.get(j).getChannelId());
					}
//				}
			}else{
				request.setAttribute("categories", categories);
			}
			
		}
		request.setAttribute("categories", categories);
		return "/system/content/channelList";
	}
	
	
	// 递归
	public void setChilds(List<ContentCategory> categories,String channelId) {
		// 遍历分类列表
		for (int i = 0; i < categories.size(); i++) {
			// 获取分类对象
			ContentCategory category = (ContentCategory) categories.get(i);
			// 如果获取的对象为空，则跳过
			if (category == null) {
				continue;
			}
			// 当前分类存在关联,页面显示选中
			if(category.getId().equals(channelId)){
				category.setChecked(true);
			}
			// 递归调用设置子列表的子列表
			setChilds(category.getChildList(),channelId);
		}
	}
	
	/**
	 * 
	 * @Title: getContentChildCategory
	 * @Description: TODO(新增咨询关联分类-查询子分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/content/add/categoryChild", method = RequestMethod.POST)
	public void getContentChildCategory(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前的分类Id
		String id = request.getParameter("id");
		// 查询出当前咨询的子分类,并将其转为JSON字符串返回
		List<ContentCategory> categories = contentCategoryService.getChildCategories(id);
		String JsonStr = JsonUtil.listToJson(categories);
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新增资讯
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/save")
	public String save(Content content, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 调用工具类,手动生成一个contentIds
			NoManager numM = new NoManager();
			String contentId = numM.createContentId();
			content.setId(contentId);
			// 获取改咨询关联的分类ID
			String contentCategoryId = request.getParameter("categoryId");
			// 频道分类ID
			String channelRelId = request.getParameter("channelRelId");
			if (StringUtil.isEmpty(content.getId()) && StringUtil.isEmpty(content.getContentName())) {
				// 如果为空，返回添加失败
				request.setAttribute("addOk", false);
				return "/system/content/contentAdd";
			}
			// 设置上传图片的保存路径
			String currentPath = "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/contentIco/";
			// 调用方法保存上传的图片,返回图片的路径
			String contentPicPath = FileUploadHelper.uploadFile("icon", currentPath, request, response);
			// 设置上传图片的路径到实体
			content.setNavigationImage(contentPicPath);
			if(!StringUtil.isEmpty(channelRelId)){
				content.setIds(channelRelId);
			}
			// 获得咨询内容集合
			String[] contentDetails = request.getParameterValues("contentDetail");
			// 利用Service的方法添加资讯 和 咨询的详情信息 和 咨询关联的分类
			if (contentService.addContent(content, null, null) && contentModelService.addContentModel(contentId, contentDetails,request) && contentService.addContentCategory(contentId, contentCategoryId)) {
				request.setAttribute("addOk", true);
				// 添加成功，返回
				return "/system/content/contentAdd";
			}
		} catch (Exception e) {
			// 添加失败，返回false
			request.setAttribute("addOk", false);
		}
		return "error";
	}

	/**
	 * 删除资讯 和 咨询详情信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id删除资讯
			if (contentService.deleteContent(id) && contentModelService.deleteContentModel(id)) {
				// 删除成功，返回true
				return true;
			}
		} catch (Exception e) {
			// 删除失败，返回false
			return false;
		}
		return "error";
	}

	/**
	 * 修改资讯
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/modify")
	public String modify(Content content, HttpServletRequest request, HttpServletResponse response) {
		String contentCategoryId = request.getParameter("categoryId");
		String status = request.getParameter("status"); // 获取是否显示
		String[] contentModerDetails = request.getParameterValues("contentDetail"); // 获得修改后的contentModerDetail
		String contentId = content.getId();
		String url = "";
		// 频道分类ID
		String channelRelId = request.getParameter("channelRelId");
		if(!StringUtil.isEmpty(channelRelId)){
			content.setIds(channelRelId);
		}
		if (StringUtil.isEmpty(contentId)) {
			request.setAttribute("addOk", false);
			url = "/system/content/contentAdd";
		} else {
			String currentPath = "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/contentIco/";
			currentPath = FileUploadHelper.uploadFile("icon", currentPath, request, response);
			if (!StringUtil.isEmpty(currentPath)) {
				content.setNavigationImage(currentPath);
			}

			// 查询出旧的content,并为其设置新值
			Content oldContent = contentService.getContent(contentId);
			if (contentCategoryId == null || contentCategoryId == "" || contentCategoryId == " ") {
				ContentCategoryRel ccr = contentService.getContentCategoryRelByContentId(contentId);
				contentCategoryId = ccr.getCategoryId();
			}
			oldContent.setContentName(content.getContentName());
			oldContent.setNavigationText(content.getNavigationText());
			oldContent.setNavigationImage(content.getNavigationImage());
			oldContent.setStatus(Boolean.valueOf(status));
			oldContent.setKeyWords(content.getKeyWords());
			oldContent.setDescription(content.getDescription());
			oldContent.setPosition(content.getPosition());
			oldContent.setAuthor(content.getAuthor());
			oldContent.setIds(content.getIds());
			oldContent.setAppPic1(content.getAppPic1());
			oldContent.setAppPic2(content.getAppPic2());
			oldContent.setAppPic3(content.getAppPic3());
			oldContent.setContentType(content.getContentType());
			oldContent.setContentLabel(content.getContentLabel());
			// 利用Service的方法修改资讯
			if (contentService.modifyContent(oldContent, null, null) && contentModelService.updateContentModel(contentId, contentModerDetails) && contentService.updateContentCategory(contentId, contentCategoryId)) {
				// 获取所有分类
				// ContentCategory categoryxz =
				// Cache.getContentsByCategoryName("优选帮助中心");
				ContentCategory categorydz = Cache.getContentsByCategoryName("大宗帮助中心");
				IContentCategoryService categoryService = SpringContextHolder.getBean(ContentCategoryService.class);
				// 根据咨询ID查询咨询所属分类
				List<ContentCategoryRel> listRel = contentCategoryRelService.getContentCategoryRelByContentId(oldContent.getId());
				if (listRel != null && listRel.size() > 0) {
					for (int j = 0; j < listRel.size(); j++) {
						ContentCategoryRel rel = listRel.get(j);
						if (rel.getContentId().equals(oldContent.getId())) {
							oldContent.setCategoryId(rel.getCategoryId());
						}
					}
				}
				String isCov = "";
				if (!StringUtil.isEmpty(oldContent.getCategoryId())) {
					// 根据咨询的分类ID获取分类模板
					ContentCategory cc = categoryService.getCategoryById(oldContent.getCategoryId());
					// if(isCov!="1"){
					// List<ContentCategory> childList =
					// categoryxz.getChildList();
					// for (int i = 0; i < childList.size(); i++) {
					// ContentCategory c = childList.get(i);
					// System.out.println(c.getParentId());
					// if(c.getId().equals(cc.getId())){
					// c.setContentName(oldContent.getContentName());
					// isCov ="1";
					// }
					// }
					//
					// Cache.engine.remove("优选帮助中心");
					// categoryxz.setChildList(childList);
					// // Cache.engine.add("优选帮助中心", categoryxz);
					// }
					if (!"1".equals(isCov) && categorydz != null) {
						List<ContentCategory> childListdz = categorydz.getChildList();
						for (int i = 0; i < childListdz.size(); i++) {
							ContentCategory c = childListdz.get(i);
							System.out.println("parentId:" + c.getParentId());
							if (c.getId().equals(cc.getId())) {
								c.setContentName(oldContent.getContentName());
								isCov = "1";
							}
						}
						categorydz.setChildList(childListdz);
						Cache.engine.remove("大宗帮助中心");
						// Cache.engine.add("大宗帮助中心", categorydz);
					}
				}
				request.setAttribute("modifyOk", true);
				url = "/system/content/contentModify";
			} else {
				request.setAttribute("modifyOk", false);
				url = "/system/content/contentModify";
			}
			request.setAttribute("content", content);
		}

		// 查询出一级咨询分类
		List<ContentCategory> categories = contentCategoryService.getCategories();
		// 根据咨询id获得咨询的详情信息
		List<ContentModel> contentModels = contentModelService.getContentModel2ByContentId(contentId, null);
		List<List<ContentCategory>> list= contentCategoryService.getContentCategoryBycontentId(contentId);
//		// 获取二级菜单
//		List<ContentCategory> secondCategories = new ArrayList<>();
//		if(listMap.get("fristId") != null){
//			secondCategories = contentCategoryService.getChildCategories(listMap.get("fristId").toString());
//		}
//		
//		// 获取三级菜单
//		List<ContentCategory> thirdCategories = new ArrayList<>();
//		if(listMap.get("secondId") != null){
//			thirdCategories = contentCategoryService.getChildCategories(listMap.get("secondId").toString());
//		}

		request.setAttribute("categories", categories);
		request.setAttribute("contentCategorys", list);
		request.setAttribute("contentModels", contentModels);
		request.setAttribute("nowIndex", contentModels == null ? "1" : contentModels.size() + "");

		return url;
	}
	
	
	/**
	 * 根据id查询资讯信息 跳转到修改咨询页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/detail/{contentId}")
	public String detail(@PathVariable String contentId, HttpServletRequest request) {
		try {
			// 查询出一级咨询分类
			List<ContentCategory> categories = contentCategoryService.getCategories();

			// 利用Service的方法根据id查找资讯
			Content content = contentService.getContent(contentId);
			// 第一次进入页面查询资讯关联分类名称
			List<ContentRel> contentRels = relService.getList(contentId);
			String cnames = "";
			if(contentRels != null && contentRels.size() > 0){
				for (ContentRel contentRel : contentRels) {
					cnames+= contentRel.getCname() + " ";
				}
			}

			String cTypes = Cache.getSystemParamConfig("资讯标签");
			if(!StringUtil.isEmpty(cTypes)){
				String[] types = cTypes.split(";");
				request.setAttribute("cTypes",types);
			}

			List<List<ContentCategory>> list = contentCategoryService.getContentCategoryBycontentId(contentId);
			// 根据咨询id获得咨询的详情信息
			List<ContentModel> contentModels = contentModelService.getContentModel2ByContentId(contentId, null);

			// 为了方便操作文本编辑器
			String nowIndex = contentModels.size() + "";
//			// 获取二级菜单
//			List<ContentCategory> secondCategories = new ArrayList<>();
//			if(listMap.get("thirdId") != null){
//				secondCategories = contentCategoryService.getChildCategories(listMap.get("thirdId").toString());
//			}
//			
//			// 获取三级菜单
//			List<ContentCategory> thirdCategories = new ArrayList<>();
//			if(listMap.get("secondId") != null){
//				thirdCategories = contentCategoryService.getChildCategories(listMap.get("secondId").toString());
//			}

//			request.setAttribute("secondCategories", secondCategories);
//			request.setAttribute("thirdCategories", thirdCategories);

			request.setAttribute("contentCategorys", list);
			request.setAttribute("contentModels", contentModels);
			request.setAttribute("content", content);
			request.setAttribute("categories", categories);
			request.setAttribute("nowIndex", nowIndex);
			request.setAttribute("cnames", cnames);
			return "/system/content/contentModify";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 
	 * @Title: showDetail
	 * @Description: TODO(查询出咨询的详情的信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param id
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/content/contentDetail/{id}")
	public String showDetail(@PathVariable String id, HttpServletRequest request) {
		// 获得咨询信息
		Content content = contentService.getContent(id);
		// 根据咨询id获得咨询的详情信息
		List<ContentModel> contentModels = contentModelService.getContentModelByContentId(id);
		if (content != null) {
			request.setAttribute("content", content);
			request.setAttribute("contentModels", contentModels);
			return "/system/content/contentDetail";
		}
		return "error";
	}

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(查询出咨询列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/content/list")
	public String list(HttpServletRequest request) {
		try {
			// 定义分页
			Pager pager = new Pager();
			// 获取页面搜索参数
			// 从页面获取资讯名称
			String name = request.getParameter("name");
			// 获取开始时间
			String bigenDate = request.getParameter("bigenDate");
			// 获取结束时间
			String endDate = request.getParameter("endDate");
			// 从页面获取审核状态
			String checkStatus = request.getParameter("checkStatus");
			// 从页面得到分类
			String categoryId = request.getParameter("categoryId");
			// 从页面获取页码
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

			// 利用Service的方法查找资讯
			List<Content> contents = contentService.listContent(pager, name, bigenDate, endDate, checkStatus, categoryId);
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

			if (!StringUtil.isEmpty(categoryId)) {
				// 回现关联分类
				ContentCategory cc = this.contentCategoryService.getCategoryById(categoryId);
				String[] ccIds = cc.getFullPath().split("_");
				for (int i = 0; i < ccIds.length; i++) {
					if (i == 0 && ccIds.length >= 2)
						request.setAttribute("twoContentCategory", contentCategoryService.getChildCategories(ccIds[i]));
					if (i == 1 && ccIds.length >= 3)
						request.setAttribute("threeContentCategory", contentCategoryService.getChildCategories(ccIds[i]));
				}
				request.setAttribute("ccIds", ccIds);
			}

			// 一级咨询
			List<ContentCategory> oneContentCategory = contentCategoryService.getCategories();
			request.setAttribute("oneContentCategory", oneContentCategory);
			request.setAttribute("categoryId", categoryId);

			request.setAttribute("bigenDate", bigenDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("checkStatus", checkStatus);
			request.setAttribute("contents", contents);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("pager", pager);
			request.setAttribute("name", name);
			return "/system/content/contentList";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 检查资讯id是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/content/checkId/{id}")
	@ResponseBody
	public Object checkId(@PathVariable String id) {
		try {
			if (!contentService.checkId(id)) {
				// 如果id不存在，返回true
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
