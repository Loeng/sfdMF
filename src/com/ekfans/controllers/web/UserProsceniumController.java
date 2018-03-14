package com.ekfans.controllers.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.basic.controller.BasicController;

@Controller
@RequestMapping("/web")
public class UserProsceniumController extends BasicController{
    @Autowired
    IProductCategoryService productCategoryService;
    
    /**
     * 
    * @Title: index
    * @Description: TODO(进入首页)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param response
    * @param session
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        //得到所有商品分类
        List<ProductCategory> productCategories = productCategoryService.getCategories(null,null);
        //将分类集合绑定到页面上
        session.setAttribute("productCategories", productCategories);
        return "web/index";
    }
}
