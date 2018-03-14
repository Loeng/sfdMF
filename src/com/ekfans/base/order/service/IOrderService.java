package com.ekfans.base.order.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderLog;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.model.OrderWfp;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.Pager;

/**
 * 订单Service接口
 * 
 * @ClassName: IOrderService
 * @Description:
 * @author lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IOrderService {
	/**
	 * @Title: listOrderWfp
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:后台查询wfp订单 (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param orderNumber
	 * @param startTime
	 * @param endTime
	 * @return List<OrderWfp> 返回类型
	 * @throws
	 */
	public List<OrderWfp> listOrderWfp(Pager pager, String orderNumber, String startTime, String endTime);

	/**
	 * @Title: getPayOrderList
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:后台查询wfp订单 (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param orderNumber
	 * @param startTime
	 * @param endTime
	 * @return List<OrderWfp> 返回类型
	 * @throws
	 */
	public List<Order> getPayOrderList(Pager pager, int orderType, String userId, String storeId, String buyName, String salName, String orderNumber, String startTime, String endTime);

	/**
	 * 按条件查询出所有的订单
	 * 
	 * @param pager
	 *            分页
	 * @param orderNum
	 *            订单号
	 * @param beginDate
	 *            订单开始日期
	 * @param endDate
	 *            订单结束日期
	 * @param beginPrice
	 *            订单小金额
	 * @param endPrice
	 *            订单大金额
	 * @param shippingStatus
	 *            订单状态
	 * @param type
	 *            订单类型（0：个人采购，1：大宗采购,2,供应商订单）
	 */
	public List<Order> listOrder(Pager pager, String orderNum, String beginDate, String endDate, String beginPrice, String endPrice, String shippingStatus, int type);

	/**
	 * 
	 * @Title: findOrderTotalPage
	 * @Description: TODO(查询出订单的记录显示的总页数) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param pager
	 * @param @param storeId
	 * @param @param userId
	 * @param @param status
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public int findOrderTotalPage(Pager pager, String storeId, String userId, String status);

	/**
	 * 
	 * @Title: add
	 * @Description: TODO(User中心新增订单、后台客服下单) 详细业务流程: (作为后台客服下单的一个功能模块涉及的操作有
	 *               第一部分: Order相应的信息 第二部分:
	 *               OrderDetail相应的信息(orderDetail部分由关联商品得到
	 *               ,通过关联商品的productId集合,完成订单) 第三部分: OrderAddress部分信息 第四部分:
	 *               OrderTreatDetail部分信息 )
	 * @param @param order
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean add(Order order, OrderAddress orderAddress, List<OrderDetail> detailList, OrderTreatDetail orderTreatDetail);

	/**
	 * 
	 * @Title: update
	 * @Description: TODO(User中心新增订单、后台客服下单) 详细业务流程: (作为后台客服下单的一个功能模块涉及的操作有
	 *               第一部分: Order相应的信息 第二部分:
	 *               OrderDetail相应的信息(orderDetail部分由关联商品得到
	 *               ,通过关联商品的productId集合,完成订单) 第三部分: OrderAddress部分信息 第四部分:
	 *               OrderTreatDetail部分信息 )
	 * @param @param order
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean update(Order order, OrderAddress orderAddress, List<OrderDetail> detailList, OrderTreatDetail orderTreatDetail);

	/**
	 * 
	 * @Title: changeOrderStatus
	 * @Description: TODO(User中心新增订单、后台客服下单) 详细业务流程: (作为后台客服下单的一个功能模块涉及的操作有
	 *               第一部分: Order相应的信息 第二部分:
	 *               OrderDetail相应的信息(orderDetail部分由关联商品得到
	 *               ,通过关联商品的productId集合,完成订单) 第三部分: OrderAddress部分信息 第四部分:
	 *               OrderTreatDetail部分信息 )
	 * @param @param order
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean changePayOrderStatus(Order order, String status);

	/**
	 * 
	 * @Title: getOrderDetailById
	 * @Description: TODO(根据Id查询出某个订单的详情信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param id
	 * @param @return 设定文件
	 * @return Object[] 返回类型
	 * @throws
	 */
	public Object[] getOrderDetailById(String id);

	/**
	 * 
	 * @Title: checkId
	 * @Description: TODO(检查Id是否存在) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param id
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean checkId(String id);

	/**
	 * 
	 * @Title: getStoreIdByName
	 * @Description: TODO(根据StoreName获得storeId) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param storeName
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getStoreIdByName(String storeName);

	/**
	 * 
	 * @Title: getUserIdByName
	 * @Description: TODO(根据userName获得userId) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param userName
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getUserIdByName(String userName);

	/**
	 * 
	 * @Title: getOrderDetail
	 * @Description: TODO(查询出订单详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param pager
	 * @param @param userId
	 * @param @param productId
	 * @param @param status
	 * @param @return 设定文件
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getMyOrders(Pager pager, String userId, String status, String shippingStatus, String userApp);

	/**
	 * 
	 * @Title: addOrder
	 * @Description: TODO(新增订单) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param order
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean addOrder(Order order);

	/**
	 * 
	 * @Title: getOrderById
	 * @Description: TODO 根据orderId取得order对象 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param id
	 * @param @return 设定文件
	 * @return Order 返回类型
	 * @throws
	 */
	public Order getOrderByOrderId(String id);

	/**
	 * 
	 * @Title: modifyOrde
	 * @Description: TODO 修改订单 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param order
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean modifyOrder(Order order);

	/**
	 * 
	 * @Title: getOrderByOrderIdRefund
	 * @Description: TODO 根据orderId取得Order对象 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return Order 返回类型
	 * @throws
	 */
	public Order getOrderId(String orderId);

	/**
	 * 
	 * @Title: evaluateOrder
	 * @Description: TODO(商户中心待评价订单-评价) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param noteArray
	 * @param @param typeArray
	 * @param @param observer
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean evaluateOrder(String[] noteArray, String[] typeArray, String[] productIdArray, String orderId, String observer, String storeId);

	/**
	 * 
	 * @Title: getOrderById
	 * @Description: TODO 根据传回来的Id取得order集合 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param id
	 * @param @return 设定文件
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getOrderById(String id);

	/**
	 * 
	 * @Title: getStoreOrderDetailById
	 * @Description: TODO(根据订单Id查询出订单详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return Object[] 返回类型
	 * @throws
	 */
	public Object[] getOrderDetailByOrderId(String orderId);

	/**
	 * 
	 * @Title: orderListByPayId
	 * @Description: TODO(根据总id得到账号) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param payId
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> orderListByPayId(String payId, String userId);

	/**
	 * 
	 * @Title: getAllOrderDetailByUserId
	 * @Description: TODO(根据userId查询出所有已买到宝贝) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param userId
	 * @param @return 设定文件
	 * @return List<OrderDetail> 返回类型
	 * @throws
	 */
	public List<OrderDetail> getAllOrderDetailByUserId(String userId);

	public List<Order> getAlreadyProductOrder(String id, Pager pager);

	public boolean appraiseProductInfo(String orderId, String storeId, String[] productId, String type, String[] note, User user, String[] orderDetailId, int judgment);

	public Order getOrderInfoAndOrderAddressInfo(String orderId);

	/**
	 * 大宗采购首页，订单展示
	 * 
	 * @param pager
	 * @return List<Order>
	 */
	public List<Order> getDaZongOrderList(Pager pager);

	/**
	 * 余额支付
	 * 
	 * @param orderId
	 *            订单ID
	 * @param store
	 *            企业
	 * @param payType
	 *            支付类型
	 * @param request
	 * @return
	 */
	public boolean orderBCSPay(String orderId, Store store,String orderType, String payType,String payPattern,String bankId, HttpServletRequest request);

	/**
	 * 线下支付订单
	 * @param store
	 * @param orderType
	 * @param payType
	 * @param payPattern
	 * @param bankId
	 * @param request
     * @return
     */
	public boolean orderULPay(Order order,Store store,String orderType, String payType,String payPattern,String bankId, HttpServletRequest request);

	/**
	 * 在线支付订单
	 * @param store
	 * @param orderType
	 * @param payType
	 * @param payPattern
	 * @param bankId
	 * @param request
	 * @return
	 */
	public boolean orderOLPay(String orderId,String userId,String orderType, String payType,String payPattern,String bankId, HttpServletRequest request);

	/**
	 * 建行支付
	 * @param orderId
	 * @param queryId
	 * @return
	 */
	public boolean orderOLCCBPay(String orderId, String queryId);

	/**
	 * 九鼎支付
	 * @param orderId
	 * @param queryId
	 * @param request
	 * @return
	 */
	public boolean orderOLJiudingPay(String orderId, String queryId, HttpServletRequest request);
}