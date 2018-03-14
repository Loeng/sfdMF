package com.ekfans.controllers.system.order.orderVo;

import java.util.List;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderTreatDetail;
/**
 * 
* @ClassName: MyOrderVo
* @Description: TODO(后台-订单模块-客服下单所用实体类)
* @author 成都易科远见科技有限公司
* @date 2014-5-12 下午02:31:06
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class MyOrderVo{
        /**
         * Order部分
         */
        private Order order;
    
        /**
         * OrderDetail部分
         */
        private List<OrderDetail> orderDetails;
    
        /**
         * OrderTreatDetail部分
         */
        private OrderTreatDetail orderTreatDetail;
	
        /**
         * OrderAddress部分
         */
        private OrderAddress orderAddress;

	public Order getOrder() {
	    return order;
	}

	public void setOrder(Order order) {
	    this.order = order;
	}

	public List<OrderDetail> getOrderDetails() {
	    return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
	    this.orderDetails = orderDetails;
	}

	public OrderTreatDetail getOrderTreatDetail() {
	    return orderTreatDetail;
	}

	public void setOrderTreatDetail(OrderTreatDetail orderTreatDetail) {
	    this.orderTreatDetail = orderTreatDetail;
	}

	public OrderAddress getOrderAddress() {
	    return orderAddress;
	}

	public void setOrderAddress(OrderAddress orderAddress) {
	    this.orderAddress = orderAddress;
	}
        
}
