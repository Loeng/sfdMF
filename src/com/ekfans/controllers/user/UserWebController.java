package com.ekfans.controllers.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.product.model.ProductCollect;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductCollectService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.model.ShopCart;
import com.ekfans.base.system.service.IShopCartService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.EncryptionAndDecryption;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 会员信息Controller
 * 
 * @Title: UserWebController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liuguoyu 
 * @date 2014-3-25 上午10:57:30 
 * @version V1.0
 */
@Controller
@RequestMapping("/user/center")
public class UserWebController extends BasicController {
	@Autowired
	private IUserService userService;

	@Autowired
	private IShopCartService shopCartService;
	 
	@Autowired
	private IOrderService orderService;
	 
	@Autowired
	private IProductCollectService productCollectService;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private IProductCategoryService productCategoryService;
	
	@Autowired
	private IUserDao userDao;
	/**
	 * 会员首页跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    // 获取登陆用户的信息,完整的对象
        User u = (User)session.getAttribute(SystemConst.USER);
        if(u == null) {
            return "redirect:/web/login";
        }
        User user = userService.getUser(u.getId());
        String userId = user.getId();
        //查询出未支付的订单信息
        List<Order> notPayOrderList = orderService.getMyOrders(null,userId,"2",null,null);
        //查询出未收货的订单
        List<Order> notShouHuoOrderList = orderService.getMyOrders(null,userId,"3",null,null);
        //查询出购物车的商品
        List<ShopCart> shopCartList = shopCartService.getListByUserId(userId);
        //查询出收藏夹中的商品
        List<ProductCollect> pcs = productCollectService.getList(null, userId);
		int size = shopCartList == null ? 0 : shopCartList.size();
        //绑定需要的参数回到页面
        request.setAttribute("notPayOrderList", notPayOrderList);
        request.setAttribute("notShouHuoOrderList", notShouHuoOrderList);
        request.setAttribute("shopCartList", shopCartList);
        request.setAttribute("shopCartSum", size);
        request.setAttribute("pcs", pcs);
    	return "/userCenter/customer/index";
	}
	
	
	/**
	 * AJAX获取头页面收藏夹数量
	 * @param session HttpSession
	 * @return String "num1,num2"
	 */
	@RequestMapping("/top")
	@ResponseBody
	public String top(HttpSession session){
		 User u = (User)session.getAttribute(SystemConst.USER);//获取session中有效用户对象
		 if(null == u){//如果用户不存在(未登录),则将返回字符串构建为"0,0"
			return "0";
		 }else{//如果用户存在(已登录),则获取该用户收藏的店铺和商品数量信息
			 String shopCartSum = shopCartService.getShopCartSum(u.getId());//获取收藏的店铺数量
			 return shopCartSum;
		 }
	}
	
	/**
	 * 头页面通过AJAX获取session用户对象
	 * @param session HttpSession
	 * @return
	 */
	@RequestMapping(value="/getSessionUser",produces="application/json; charset=utf-8")
	@ResponseBody
	public String getUser(HttpSession session){
		 User user = (User)session.getAttribute(SystemConst.USER);//获取session中有效用户对象
		 if(null != user){//如果用户存在(已登录),则将用户对象转换成JSON数组后返回给前台
			 return JsonUtil.objectToJson(user);
		 }
		 return "";
	}
	
	/**
	 * 构建收藏夹数量字符串
	 * js通过","拆分返回字符串后设置给相应的页面元素
	 * @param num1 收藏的店铺数量
	 * @param num2 收藏的商品数量
	 * @return String "num1,num2"
	 */
	private String buildTopCollectResult(String num1,String num2){
		StringBuffer sb = new StringBuffer();
		sb.append(num1);
		sb.append(",");
		sb.append(num2);
		return sb.toString();
	}
	
}
