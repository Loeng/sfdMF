package com.ekfans.controllers.store.order.bargain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.service.IOrderDetailService;
import com.ekfans.base.order.service.IOrderTreatDetailService;
import com.ekfans.base.order.service.StoreOrder.OrderManage.IStoreOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StorePurview;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: StoreOrderListController
* @Description: TODO(跳转到待付款的订单的页面)
* @author 成都易科远见科技有限公司
* @date 2014-4-14 下午02:53:40
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreGylOrderWaitPayController extends BasicController{
    // 注入service
    @Autowired
    private IStoreOrderService storeOrderService;
    @Autowired
    private IOrderTreatDetailService orderTreatDetailService;
    @Autowired
    private IOrderDetailService orderDetailService;
   
    /**
     * 
    * @Title: storeOrderHandel
    * @Description: TODO(查询出代付款订单详情  并跳往处理界面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param orderId
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/order/gylOrderWaitPayHandel/{orderId}/{actType}/{orderType}")
    public String storeOrderHandel(@PathVariable String orderId,@PathVariable String actType,@PathVariable String orderType,HttpServletRequest request){
        // 查询出当前订单号所对应的商品信息
        // 查询出当前订单号所对应的商品信息
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetail(orderId);
        // 查询出订单的跟踪信息
        //List<OrderTreatDetail> treatDetails = storeOrderService.findOrderTreatDetailByOrderId(orderId);
        List<OrderTreatDetail> orderTreatDetailList = orderTreatDetailService.getOrderTreatDetailListByOrderId(orderId);
	
        OrderAddress address = null;
        Order order = null;
        OrderTreatDetail treatDetail = null;
        // 查询出订单的详情信息
        Object[] objects = storeOrderService.getStoreOrderDetailById(orderId);
        if(objects != null){
            // 从订单详情取出OrderAddress
            address = (OrderAddress)objects[0];
            // 从订单详情取出Order
            order = (Order)objects[1];
        }
        request.setAttribute("actType",actType);
        request.setAttribute("orderType",orderType);
        // 将订单的信息绑定到request对象上
        request.setAttribute("orderDetails", orderDetails);
        request.setAttribute("address",address);
        request.setAttribute("order",order);
        request.setAttribute("treatDetail",treatDetail);
        request.setAttribute("treatDetails",orderTreatDetailList);
        request.setAttribute("ordersTotalPrice", order.getFare().add(order.getTotalPrice()));
        // 返回视图
        return "/userCenter/store/order/gyl/orderHandle/orderWaitPayListHandle";
    }
    
    
}
