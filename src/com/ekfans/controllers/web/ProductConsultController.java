package com.ekfans.controllers.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.service.web.ProductConsult.IProductConsultService;
import com.ekfans.base.product.service.web.ProductDetail.IProductDetailService;
import com.ekfans.base.store.model.Consult;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: ProductConsultController
* @Description: TODO(前台-商品咨询)
* @author HJC
* @date 2014-6-11 上午10:23:04
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class ProductConsultController {
    @Autowired private IProductConsultService consultService;
    @Autowired private IProductService productService;
    @Autowired private IProductDetailService productDetailService;
    
    @RequestMapping(value = "/web/product/productConsult")
    public String proConsultPage(HttpServletRequest request,HttpSession session){
    	// 查看咨询或者新怎咨询前,用户必须得登陆,如果未登录,就跳转到登陆页面
    	User user = (User)session.getAttribute(SystemConst.USER);
    	if(user == null){
    	    return "redirect:/web/login/zero";
    	}
	    // 分页
        Pager pager = new Pager();
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
                currentPage = Integer.parseInt(currentpageStr);
           }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        // 获取搜索内容  商品咨询才有搜索咨询的功能
        String consultContent = request.getParameter("consultContent");
        // 获取商品Id
        String proId = request.getParameter("proId");
        // 获取咨询类型
        String type = request.getParameter("type");
        List<Consult> cs = 
            consultService.getProConsultByCondition(proId,consultContent,type,pager);
        
        // 获取Product
        Product p = productService.getProductById(proId);
        // 商品的累计评价
        String proAppraiseSum = productDetailService.getAppraiseSum(proId);
        
        request.setAttribute("currentPage",currentPage);
        request.setAttribute("pager",pager);
        request.setAttribute("proId",proId);
        request.setAttribute("cs",cs);
        request.setAttribute("p",p);
        request.setAttribute("proAppraiseSum",proAppraiseSum);
        request.setAttribute("type",type);
        request.setAttribute("consultContent",consultContent);
	return "/web/shop/product/productConsult";
    }
    
    /**
     * 
    * @Title: proConsultForLoad
    * @Description: TODO(load不同页面所用)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/web/product/productConsultForLoad")
    public String proConsultForLoad(HttpServletRequest request){
	// 分页
        Pager pager = new Pager();
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
                currentPage = Integer.parseInt(currentpageStr);
           }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        // 获取搜索内容  商品咨询才有搜索咨询的功能
        String consultContent = request.getParameter("content");
        // 获取商品Id
        String proId = request.getParameter("proId");
        // 获取咨询类型
        String type = request.getParameter("type");
        List<Consult> cs = 
            consultService.getProConsultByCondition(proId,consultContent,type,pager);
        
        request.setAttribute("currentPage",currentPage);
        request.setAttribute("pager",pager);
        request.setAttribute("proId",proId);
        request.setAttribute("cs",cs);
        request.setAttribute("type",type);
        request.setAttribute("consultContent",consultContent);
        
        // 根据load类型的不同,返回不同的值
        return "/web/shop/product/productConsultMode";
    }
    
    /**
     * 
    * @Title: addProductConsult
    * @Description: TODO(新增商品咨询)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/web/product/proConsultAdd")
    @ResponseBody
    public Object addProductConsult(HttpServletRequest request,HttpSession session){
    	String consultContent = request.getParameter("consultContent");
    	User user = (User)session.getAttribute(SystemConst.USER);
    	String proId = request.getParameter("proId");
    	boolean addOk = 
    	    consultService.addProductConsult("0",consultContent,proId,user.getId());
    	return addOk;
    }
}
