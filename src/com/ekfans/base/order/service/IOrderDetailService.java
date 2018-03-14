package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.vo.OderDetailProduct;
import com.ekfans.pub.util.Pager;


/**
 * 订单明细实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IOrderDetailService {
      /**
       * 
      * @Title: getDetail
      * @Description: TODO 根据UserId取得订单的详细集合
      * 详细业务流程:
      * (详细描述此方法相关的业务处理流程)
      * @param @param pager
      * @param @param userId
      * @param @return    设定文件
      * @return List<OrderDetail>    返回类型
      * @throws
       */
      public List<OrderDetail> getDetail(Pager pager,String userId);
      
      /**
       * 
      * @Title: addDetail
      * @Description: TODO 添加订单详细信息
      * 详细业务流程:
      * (详细描述此方法相关的业务处理流程)
      * @param @param orderDetail
      * @param @return    设定文件
      * @return boolean    返回类型
      * @throws
       */
      public boolean addDetail(OrderDetail orderDetail);
      
      /**
       * 
      * @Title: getOrderDetail
      * @Description: TODO 根据传回来的orderId取得这个OrderDetail集合
      * 详细业务流程:
      * (详细描述此方法相关的业务处理流程)
      * @param @param orderId
      * @param @return    设定文件
      * @return List<OrderDetail>    返回类型
      * @throws
       */
      public List<OrderDetail> getOrderDetail(String orderId);
      
      /**
       * 根据订单id和商品id得到此商品的订单详情
       * 
       * @param productId 商品id
       * @param orderId 订单id
       * @return OrderDetail
       */
      public OrderDetail getOrderDetailByProductId(String productId, String orderId);
      
      /**
       * 
      * @Title: getOrderDetailByStoreId
      * @Description: TODO(根据storeId得到集合)
      * 详细业务流程:
      * (详细描述此方法相关的业务处理流程)
      * @param storeId
      * @return List<OrderDetail> 返回类型
      * @throws
       */
      public List<OrderDetail> getOrderDetailByStoreId(String storeId);
      
      public List<OderDetailProduct> getProductOrderInfo(String orderId);
      
      public List<OderDetailProduct> getOrderDetailAndProductAndApprise(String orderId, String storeId);
}