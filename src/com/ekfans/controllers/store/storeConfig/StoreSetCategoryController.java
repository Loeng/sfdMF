package com.ekfans.controllers.store.storeConfig;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
/**
 * 
* @ClassName: StoreIndexCategoryController
* @Description: TODO(跳转到分类设置页面)
* @author ekfans
* @date 2014-4-14 下午12:00:01
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreSetCategoryController extends BasicController{
    @Autowired
    private IProductCategoryService productCategoryService;
    
    /**
     * 
    * @Title: list
    * @Description: TODO 跳转到分类设置页面
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param response
    * @param session
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/set/category")
    public String list(HttpServletRequest request,
            HttpServletResponse response, HttpSession session){
        // 从session中得到店铺对象
        Store store = (Store)session.getAttribute(SystemConst.STORE);
        //如果store对象为空则返回登陆页面
        if(store == null){
            return "/store/login"; 
        }
        
        // 查找店铺所有的分类
        List<ProductCategory> categories = productCategoryService.getStoreCategories(store.getId(), "STORE", true, 0);
        
        request.setAttribute("categories", categories);
        return "store/config/storeSetCategory";
    }
    
    /**
     * 
    * @Title: save
    * @Description: TODO 保存分类
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param response
    * @param session
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/set/category/save")
    @ResponseBody
    public String save(HttpServletRequest request,
            HttpServletResponse response, HttpSession session){
        // 从session中得到店铺对象
        Store store = (Store)session.getAttribute(SystemConst.STORE);
        //如果store对象为空则返回登陆页面
        if(store == null){
            return "/store/login"; 
        }
        // 得到一级分类的id
        String[] ids = request.getParameterValues("id");
        // 得到一级分类名
        String[] names = request.getParameterValues("name");
        // 得到二级分类id
        String[] childIds = request.getParameterValues("childId");
        // 得到二级分类名
        String[] childNames = request.getParameterValues("childName");
        // 得到二级分类的父分类id
        String[] parentIds = request.getParameterValues("parentId");
        // 定义一个存放一级分类的list
        List<ProductCategory> parentList = new ArrayList<ProductCategory>();
        // 定义一个存放二级分类的list
        List<ProductCategory> childList = new ArrayList<ProductCategory>();
        
        if(ids!=null && ids.length>0){
            // 遍历一级分类
            for(int i=0;i<ids.length;i++){
                ProductCategory pc = new ProductCategory();
                // 设置id
                pc.setId(ids[i]);
                // 设置分类名
                pc.setName(names[i]);
                // 设置状态
                pc.setStatus(true);
                // 设置审核状态
                pc.setCheckStatus(1);      //添加审核操作后去除
                // 设置店铺id
                pc.setStoreId(store.getId());
                // 放入list
                parentList.add(pc);
            }
        }
       
        if(childIds!=null && childIds.length>0){
            // 遍历二级分类
            for(int i=0;i<childIds.length;i++){
                ProductCategory pc = new ProductCategory();
                // 设置id
                pc.setId(childIds[i]);
                // 设置分类名
                pc.setName(childNames[i]);
                // 设置状态
                pc.setStatus(true);
                // 设置审核状态
                pc.setCheckStatus(1);   //添加审核操作后去除
                // 设置店铺id
                pc.setStoreId(store.getId());
                // 设置父分类名
                pc.setParentId(parentIds[i]);
                // 放入list
                childList.add(pc);
            }
        }
        
        // 保存成功则返回成功
        if(productCategoryService.saveStoreCategory(parentList, childList)){
            return "success";
        }
        return "fail";
    }
    
    @RequestMapping(value = "/store/set/category/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable String id,HttpServletRequest request,
            HttpServletResponse response, HttpSession session){
        productCategoryService.deleteCategory(id);
        return null;
    }
}
