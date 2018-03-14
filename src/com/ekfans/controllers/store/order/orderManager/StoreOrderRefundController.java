package com.ekfans.controllers.store.order.orderManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: StoreOrderListController
* @Description: TODO(跳转到退款中的订单的页面)
* @author ekfans
* @date 2014-4-14 下午02:53:40
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreOrderRefundController extends BasicController{
    //定义service
    @Autowired
    private IStoreOrderService storeOrderService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IOrderService orderService;
    
    @RequestMapping(value = "/store/order/refund")
    public String list(HttpServletRequest request,HttpSession session){
	//  获取当前的店铺Id
	String storeId = ((Store) session.getAttribute(SystemConst.STORE)).getId();
	
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
	    storeOrderService.getStoreOrderRefundByConditions(storeId,orderId,userName,beginDate,endDate,pager);
	
	request.setAttribute("orders",orders);
	request.setAttribute("orderId",orderId);
	request.setAttribute("userName",userName);
	request.setAttribute("beginDate",beginDate);
	request.setAttribute("endDate",endDate);
	request.setAttribute("currentpageStr",currentPage);
	request.setAttribute("pager",pager);
	
        return "/userCenter/store/order/orderManager/orderRefund";
    }  
    /**
     * 
     * @Title: orderView
     * @Description: TODO(申请融资查看订单详情)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param 
     * @return 
     * @throws
     */
    @RequestMapping(value="/store/order/orderById/{orderId}")
    public String orderView(@PathVariable String orderId, HttpServletRequest request){
    	//先用id查询普通订单若为空则查危废订单表
    	Order order = orderService.getOrderByOrderId(orderId);
    	//根据不同订单类型跳转订单详情页(0:普通订单，1:直付订单  2:绿色商城订单)
    	if(null!=order && order.getType()==0){
    		return "redirect:/store/order/orderRefundHandel/" + orderId;
    	}else if(null!=order && order.getType()==1){
    		return "redirect:/store/order/payorder/view/" + orderId+"/view";
    	}else if(null!=order && order.getType()==2){
    		return "redirect:/store/order/orderRefundHandel/" + orderId;
    	}else{
    		return "redirect:/store/order/wfOrderView/"+orderId+"/2/0";
    	}
    }
    /**
     * 
    * @Title: storeOrderHandel
    * @Description: TODO(退款中订单详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param orderId
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/order/orderRefundHandel/{orderId}")
    public String storeOrderHandel(@PathVariable String orderId,HttpServletRequest request){
        // 查询出当前订单号所对应的商品信息
        //List<Product> products = storeOrderService.getProductByOrderId(orderId);
        // 查询出当前订单号所对应的商品信息
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetail(orderId);
        // 查询出订单的跟踪信息
        List<OrderTreatDetail> treatDetails = storeOrderService.findOrderTreatDetailByOrderId(orderId);
        // 查询出订单的详情信息
        OrderAddress address = null;
        Order order = null;
        Object[] objects = orderService.getOrderDetailByOrderId(orderId);
        // 对于已付款的订单查询出付款时间
        OrderTreatDetail treatDetail = null;
        // 预Java内防空指针
        if(objects != null){
            // 从订单详情取出OrderAddress
            address = (OrderAddress)objects[0];
            // 从订单详情取出Order
            order = (Order)objects[1];
            // 已付款的订单查询出付款时间
            if(!"0".equals(order.getStatus())&&!"1".equals(order.getStatus())&&!"2".equals(order.getStatus())){
                treatDetail = new OrderTreatDetail();
                treatDetail.setCreateTime(storeOrderService.getOrderPayTimeOrderId(orderId));
            }
        }
    	// 将订单的信息绑定到request对象上
    	//request.setAttribute("products",products);
        request.setAttribute("orderDetails", orderDetails);
    	request.setAttribute("address",address);
    	request.setAttribute("order",order);
    	request.setAttribute("treatDetail",treatDetail);
    	request.setAttribute("treatDetails",treatDetails);
    	// 返回视图
    	return "/userCenter/store/order/orderManager/orderHandle/orderRefundListHandle";
    }
}
