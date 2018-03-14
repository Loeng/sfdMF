package com.ekfans.controllers.system.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.base.content.service.IContentCategoryRelService;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemContentBatchController extends BasicController {
    @Autowired
    private IContentService contentService;

    @Autowired
    private IContentCategoryService contentCategoryService;
    
    @Autowired
    private IContentCategoryRelService contentCategoryRelService;
    
    /**
     * 
    * @Title: toView
    * @Description: TODO 跳转到批量移动页面
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/system/content/move")
    public String toView(HttpServletRequest request){
        // 得到通用分类的子分类
        List<ContentCategory> categories = contentCategoryService.getChildCategories("00000002");
        // 得到频道分类
        ContentCategory category = contentCategoryService.getCategoryById("00000002");
        
        List<ContentCategory> cates = new ArrayList<ContentCategory>();
        cates.add(category);
        //将商品分类数据集设置在requestScope
        request.setAttribute("categories", cates);
        request.setAttribute("generalCategoryChild", categories);
        return "/system/content/batch/move";    
    }
    
    /**
     * 
    * @Title: child
    * @Description: TODO 查找子分类
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/content/batch/child/{id}")
    public String child(@PathVariable String id, HttpServletRequest request) {
        // 查询出分类列表返回到页面
        List<ContentCategory> categories = contentCategoryService.getChildCategories(id);
        request.setAttribute("categories", categories);
        return "/system/content/batch/categoryTree";
    }
    
    /**
     * 
    * @Title: getProductsByCategoryId
    * @Description: TODO 根据频道分类id查找资讯
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/system/content/batch/getContentsByCategoryId/{id}/{pageNum}")
    public String getProductsByCategoryId(@PathVariable("id")String id,@PathVariable("pageNum")String pageNum,HttpServletRequest request){
        if(StringUtils.isEmpty(id)){
            return null;
        }
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
        pager.setCurrentPage(currentPage);
        
        //通过分类ID获取商品列表
        List<Content> list = contentService.listContent(pager, id, null);
        request.setAttribute("contents", list);
        request.setAttribute("pager", pager);
        request.setAttribute("totalRow", pager.getTotalRow());
        
        return "/system/content/batch/contentListTop";
    }
    
    /**
     * 
    * @Title: searchProducts
    * @Description: TODO 
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/system/content/batch/searchContents")
    public String searchProducts(HttpServletRequest request){
        // 定义分页
        Pager pager = new Pager();
        
        String pageNum = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(pageNum)) {
            try {
                currentPage = Integer.parseInt(pageNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pager.setCurrentPage(currentPage);
        Map<String,Object> map = new HashMap<String,Object>();
        //获取商品名称
        map.put("cName", "%"+request.getParameter("searchName")+"%");
        //得到所有子分类id
        List<String> ids = contentCategoryService.getAllCategoryIdsById(request.getParameter("categoryId"));
        //设置分类id
        map.put("ids", ids);
        //获取下移的频道分类编号
        map.put("categoryId", request.getParameter("downId"));
        //查询频道分类下的临时商品集合
        List<Content> contents = contentService.queryContentsByParams(pager,map);
        request.setAttribute("contents", contents);
        request.setAttribute("pager", pager);
        request.setAttribute("totalRow", pager.getTotalRow());
        
        return "/system/content/batch/contentListBot";
    }
    
    /**
     * 
    * @Title: getProductChildCategory
    * @Description: TODO 批量移动的分类选择得到子分类
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param response void 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/content/batch/categoryChild", method = RequestMethod.POST)
    public void getProductChildCategory(HttpServletRequest request, HttpServletResponse response) {
        // 获取当前的分类Id
        String id = request.getParameter("id");
        // 查询出子分类,并将其转为JSON字符串返回
        List<ContentCategory> categories = contentCategoryService.getChildCategories(id);
        String JsonStr = JsonUtil.listToJson(categories);
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
    * @Title: move
    * @Description: 将资讯移动至选择的分类中
    * @param ccid 分类ID
    * @param cid 资讯ID
    * @param isUp 是否为向上移动
    * @return String 返回类型
     */
    @RequestMapping(value="/system/content/batch/moveContent/{ccid}/{cid}/{isUp}",method=RequestMethod.POST)
    @ResponseBody
    public String move(@PathVariable("cid")String cId,@PathVariable("ccid")String ccId,@PathVariable("isUp")String isUp){
        //返回结果
        boolean result = false;
        //如果为向上移动，则删除资讯分类关联表中的关联
        if(isUp.equals("down")){
            result = contentCategoryRelService.remove(ccId, cId);    
        }else{
            ContentCategoryRel ccr = new ContentCategoryRel();
            ccr.setCategoryId(ccId);
            ccr.setContentId(cId);
            //否则新增关联至资讯关联表中
            result = contentCategoryRelService.add(ccr);
        }
        if(result){
            return "success";
        }else{
            return "fail";
        }
    }
}
