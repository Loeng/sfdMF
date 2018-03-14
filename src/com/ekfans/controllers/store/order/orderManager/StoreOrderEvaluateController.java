package com.ekfans.controllers.store.order.orderManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.vo.OderDetailProduct;
import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: StoreOrderListController
* @Description: TODO(商户中心-待评价订单页面)
* @author 成都易科远见科技有限公司
* @date 2014-4-14 下午02:53:40
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreOrderEvaluateController extends BasicController{
    // 注入service
    @Autowired
    private IStoreOrderService storeOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;
    
    /**
     * 
    * @Title: list
    * @Description: TODO(已发货-用户已评-待评价订单集合)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/order/appraise")
    public String list(HttpServletRequest request, HttpSession session){
	//  获取当前的店铺Id
	Store store = (Store) session.getAttribute(SystemConst.STORE);
	String storeId = store.getId();
	String purviewId = "ORDER";
	StorePurview purview = Cache.getStorePurview(store.getRoleId(),purviewId,true);
	
	 // 定义分页
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
	
	// 获得相关参数
	String orderId = request.getParameter("orderId");
	String userName = request.getParameter("userName");
	String beginDate = request.getParameter("beginDate");
	String endDate = request.getParameter("endDate");
	
	List<Order> orders = 
	    storeOrderService.getStoreOrderEvaluateByConditions(storeId,orderId,userName,beginDate,endDate,pager);
	request.setAttribute("orders",orders);
	request.setAttribute("orderId",orderId);
	request.setAttribute("userName",userName);
	request.setAttribute("beginDate",beginDate);
	request.setAttribute("endDate",endDate);
	
	request.setAttribute("pager",pager);
	request.setAttribute("currentpageStr",currentPage);
	
	// 把权限对应的权限放入session
	session.setAttribute("storePurview",purview);
	session.setAttribute("parentChose",purviewId);    // 商户中心顶部的默认选中
	session.setAttribute("storeChose","ORDER_APPRAISE");    //  商户中心左侧菜单的默认
        return "/userCenter/store/order/orderManager/orderEvaluate";
    }  
    
    /**
     * 
    * @Title: list2
    * @Description: TODO(待评价订单集合的重写)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/order/appraise2/{beginDate}/{endDate}/{status}")
    public String list2(@PathVariable String beginDate,@PathVariable String endDate,
                        @PathVariable String status,HttpServletRequest request, HttpSession session){
	// 从Session中获得store对象
	Store store = ((Store) session.getAttribute(SystemConst.STORE));
	// 获取当前的店铺Id
	String storeId = store.getId();
	
	// 调用缓存 获取店铺会员对应的权限集合   这里获取的是交易管理页面的权限集合
	String purviewId = "ORDER";
	StorePurview purview = Cache.getStorePurview(store.getRoleId(),purviewId,true);
	
	 // 定义分页
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
	
	// 获得相关参数
	String orderId = request.getParameter("orderId");
	String userName = request.getParameter("userName");
	
	List<Order> orders = 
	    storeOrderService.getStoreOrderEvaluateByConditions(storeId,orderId,userName,beginDate,endDate,pager);
	request.setAttribute("orders",orders);
	request.setAttribute("orderId",orderId);
	request.setAttribute("userName",userName);
	request.setAttribute("beginDate",beginDate);
	request.setAttribute("endDate",endDate);
	
	request.setAttribute("pager",pager);
	request.setAttribute("currentpageStr",currentPage);
	
	// 把权限对应的权限放入session
	session.setAttribute("storePurview",purview);
	session.setAttribute("parentChose",purviewId);    // 商户中心顶部的默认选中
	session.setAttribute("storeChose","ORDER_APPRAISE");    //  商户中心左侧菜单的默认选中
	
        return "/userCenter/store/order/orderManager/orderEvaluate";
    }  
    
    
    /**
     * 
     * @Title: storeOrderHandel
     * @Description: TODO(查询出待评价订单详情  跳往待评价订单处理页面)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param @param orderId
     * @param @param request
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    @RequestMapping(value = "/store/order/orderEvaluateHandel/{orderId}")
    public String storeOrderHandel(@PathVariable String orderId, HttpServletRequest request, HttpServletResponse response){
        Store store = ((Store) request.getSession().getAttribute(SystemConst.STORE));
        //获取订单id
        Order order = this.orderService.getOrderInfoAndOrderAddressInfo(orderId);
        //查询订单对应的订单详情，商品信息，评价信息
        List<OderDetailProduct> list = this.orderDetailService.getOrderDetailAndProductAndApprise(orderId, store.getId());
        
        request.setAttribute("order", order);
        request.setAttribute("entityList", list);
        
	/*// 查询出当前订单号所对应的商品信息  对于查询商品涉及分页
	List<Product> products = 
	    storeOrderService.getProductByOrderId2(orderId);
	// 查询出订单的跟踪信息
	List<OrderTreatDetail> treatDetails = 
	    storeOrderService.findOrderTreatDetailByOrderId(orderId);
	// 查询出订单的详情信息
	OrderAddress address = null;
	Order order = null;
	Object[] objects = 
	    storeOrderService.getStoreOrderDetailById(orderId);
	// 对于已付款的订单查询出付款时间
	OrderTreatDetail treatDetail = null;
	// 预Java内防空指针
	if(objects != null){
	    // 从订单详情取出OrderAddress
	    address = (OrderAddress)objects[0];
	    // 从订单详情取出Order
	    order = (Order)objects[1];
	    // 已付款的订单查询出付款时间
	    if("3".equals(order.getStatus())){
		treatDetail = new OrderTreatDetail();
		treatDetail.setCreateTime(storeOrderService.getOrderPayTimeOrderId(orderId));
	    }
	}
	// 将订单的信息绑定到request对象上
	request.setAttribute("products",products);
	request.setAttribute("address",address);
	request.setAttribute("order",order);
	request.setAttribute("treatDetail",treatDetail);
	request.setAttribute("treatDetails",treatDetails);
	// 返回视图
*/	 
        return "/userCenter/store/order/orderManager/orderHandle/evaluateToUser";
    }
    
    /**
     * 
    * @Title: evaluateStoreOrder
    * @Description: TODO(商户中心-交易管理-待评价订单提交评价)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param appraise
    * @param @param request
    * @param @return    设定文件
    * @return Object    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/order/evaluate")
    public String evaluateStoreOrder(HttpServletRequest request,HttpSession session){
        //获取当前的店铺Id
        String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
        //这里店铺的的id与店主的userId相对应
        User user = storeOrderService.getUserById(storeId);
        //获取订单id
        String orderId = request.getParameter("orderId");
        //获取被评价人id
        String[] targetId = request.getParameterValues("targetId");
        //获取评价类型【好，中，差】
        String type = request.getParameter("etype");
        //获取评价类型
        String[] note = request.getParameterValues("note");
        //获取商品id
        String[] productId = request.getParameterValues("productId");
        //获取订单详情id
        String[] orderDetailId = request.getParameterValues("orderDetailId");
        
        boolean judgment = this.orderService.appraiseProductInfo(orderId, targetId[0], productId, type, note, user, orderDetailId, 2);
        
        
    	/*String[] noteArray = request.getParameterValues("note");
    	String[] typeArray = request.getParameterValues("etype");
    	String[] productIdArray = request.getParameterValues("productId");
    	String orderId = request.getParameter("orderId");
    	String userId = request.getParameter("userId");
    	//  获取当前的店铺Id
    	String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
    	//  这里店铺的的id与店主的userId相对应
    	User user = storeOrderService.getUserById(storeId);
    	//  评论者名字 为当前店主的名字
    	String observer = user.getId();
    	
    	boolean addOk = 
    	    storeOrderService.evaluateStoreOrder(noteArray,typeArray,productIdArray,orderId,observer,userId);
    	 */
    	if(judgment){
    	    return "redirect:/store/order/list";
    	}else{
    	    return "redirect:/store/order/orderEvaluateHandel/"+orderId+"/1";
    	}
    }
}
