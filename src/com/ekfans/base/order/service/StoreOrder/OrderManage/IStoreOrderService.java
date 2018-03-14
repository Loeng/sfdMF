package com.ekfans.base.order.service.StoreOrder.OrderManage;

import java.util.List;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.Pager;

/**
 * 
 * @ClassName: IStoreOrderService
 * @Description: TODO(商户中心交易管理(订单)接口)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-7 上午11:13:21
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreOrderService {
	/**
	 * 
	 * @Title: getStoreOrderByConditions
	 * @Description: TODO(查询出满足条件的所有数据) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param userName
	 * @param @param beginDate
	 * @param @param endDate
	 * @param @return 设定文件
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getStoreOrderByConditions(String storeId,
			String orderId, String userName, String beginDate, String endDate,
			String status, String type, String actType,Pager pager);

	/**
	 * 
	 * @Title: getStoreOrderWaitPayByConditions
	 * @Description: TODO(查询出等待付款的订单) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param storeId
	 * @param @param orderId
	 * @param @param userName
	 * @param @param beginDate
	 * @param @param endDate
	 * @param @return 设定文件
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getStoreOrderWaitPayByConditions(String storeId,
			String orderId, String userName, String beginDate, String endDate,
			Pager pager);

	/**
	 * 
	 * @Title: getStoreOrderWaitAffirmByConditions
	 * @Description: TODO(查询出等待确认订单) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param storeId
	 * @param @param orderId
	 * @param @param userName
	 * @param @param beginDate
	 * @param @param endDate
	 * @param @return 设定文件
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getStoreOrderWaitShipmentsByConditions(String storeId,
			String orderId, String userName, String beginDate, String endDate,
			Pager pager);

	/**
	 * 
	 * @Title: getStoreOrderRefundByConditions
	 * @Description: TODO(退款中的订单) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param storeId
	 * @param @param orderId
	 * @param @param userName
	 * @param @param beginDate
	 * @param @param endDate
	 * @param @return 设定文件
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getStoreOrderRefundByConditions(String storeId,
			String orderId, String userName, String beginDate, String endDate,
			Pager pager);

	/**
	 * 
	 * @Title: getStoreOrderEvaluateByConditions
	 * @Description: TODO(待评价订单的) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param storeId
	 * @param @param orderId
	 * @param @param userName
	 * @param @param beginDate
	 * @param @param endDate
	 * @param @return 设定文件
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getStoreOrderEvaluateByConditions(String storeId,
			String orderId, String userName, String beginDate, String endDate,
			Pager pager);

	/**
	 * 
	 * @Title: getStoreOrderDetailById
	 * @Description: TODO(根据订单Id查询出订单详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return Object[] 返回类型
	 * @throws
	 */
	public Object[] getStoreOrderDetailById(String orderId);

	/**
	 * 
	 * @Title: getProductByOrderId
	 * @Description: TODO(根据订单号获得订单号所对应的商品) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return Product 返回类型
	 * @throws
	 */
	public List<Product> getProductByOrderId(String orderId);

	/**
	 * 
	 * @Title: getProductByOrderId
	 * @Description: TODO(商户中心-交易处理-待评价订单处理) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param pager
	 * @param @return 设定文件
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<Product> getProductByOrderId2(String orderId);

	/**
	 * 
	 * @Title: findOrderTreatDetailByOrderId
	 * @Description: TODO(订单跟踪-查询订单的操作信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return List<OrderTreatDetail> 返回类型
	 * @throws
	 */
	public List<OrderTreatDetail> findOrderTreatDetailByOrderId(String orderId);

	/**
	 * 
	 * @Title: modifyStoreOrderPrice
	 * @Description: TODO(交易管理-待付款订单管理-修改订单的实际收的金额) 详细业务流程: (修改订单金额后涉及订单操作的修改)
	 * @param @param paid
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean modifyStoreOrderPrice(String orderId, String paid,
			String createrId);

	/**
	 * 
	 * @Title: deliveryStoreOrder
	 * @Description: TODO(待发货订单发货) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param paid
	 * @param @param createrId
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean deliveryStoreOrder(String orderId, String storeName,
			String logisticsName, String logisticsNo, String actualShipping);

	/**
	 * 
	 * @Title: evaluateStoreOrder
	 * @Description: TODO(商户中心待评价订单-评价) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param noteArray
	 * @param @param typeArray
	 * @param @param observer
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean evaluateStoreOrder(String[] noteArray, String[] typeArray,
			String[] productIdArray, String orderId, String observer,
			String target);

	/**
	 * 
	 * @Title: getUserById
	 * @Description: TODO(根据userId得到user用户) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param userId
	 * @param @return 设定文件
	 * @return User 返回类型
	 * @throws
	 */
	public User getUserById(String userId);

	/**
	 * 
	 * @Title: getOrderPayTimeOrderId
	 * @Description: TODO(对于已付款的订单查询出付款时间) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getOrderPayTimeOrderId(String orderId);

	/**
	 * 
	 * @Title: getOrderById
	 * @Description: TODO(根据订单号,获取订单详情) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public Order getOrderById(String orderId);

	/**
	 * 
	 * @Title: getStoreOrderByConditions
	 * @Description: TODO(查询出满足条件的采购商订单) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @param userName
	 * @param @param beginDate
	 * @param @param endDate
	 * @param @return 设定文件
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getCaiStoreOrder(String userId, String orderId,
			String userName, String beginDate, String endDate, String status,
			String type, Pager pager);

	/**
	 * 
	 * @Title: getOrderSum
	 * @Description: TODO(带到各种状态下的订单数量) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param storeId
	 * @param type
	 * @param status
	 * @return String 返回类型
	 * @throws
	 */
	public String getOrderSum(String storeId, String userId, String type,
			String status);
}
