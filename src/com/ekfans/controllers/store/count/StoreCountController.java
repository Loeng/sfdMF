package com.ekfans.controllers.store.count;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ekfans.base.product.service.IProductService;
import com.ekfans.basic.controller.BasicController;

/**
 * 
 * 店铺商品Controller
 * 
 * @Title: StoreProductController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-23 下午05:23:47 
 * @version V1.0
 */
@Controller
public class StoreCountController extends BasicController {
	@Autowired
	IProductService productService;

	
	
	/**
	 * 出售中的商品Controller方法
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/count/security")
	public ModelAndView saleProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		return new ModelAndView("/store/product/productList", null);
	}
}
