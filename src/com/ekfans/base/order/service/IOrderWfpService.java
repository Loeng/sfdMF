package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.OrderWfp;
import com.ekfans.base.store.model.Store;
import com.ekfans.pub.util.Pager;

/**
 * @ClassName: IOrderWfpService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 危废品逻辑处理接口
 * @date 2015-1-15 下午4:59:43
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IOrderWfpService {
	/**
	 * @Title: getAllOrderList
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:查询所有的订单列表 (详细描述此方法相关的业务处理流程)
	 * @param orderNumber
	 * @param orderStatus
	 * @param orderNmae
	 * @param startTime
	 * @param endTime
	 * @return List<OrderWfp> 返回类型
	 * @throws
	 */
	public List<OrderWfp> getAllOrderList(Pager pager, String orderNumber,
			String orderStatus, String orderNmae, String startTime,
			String endTime);

	/**
	 * @Title: updateOrder
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:更新order (详细描述此方法相关的业务处理流程)
	 * @param wfp
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean updateOrder(OrderWfp wfp);

	/**
	 * @Title: getOrderDtailById
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据orderid获取详情
	 *               (详细描述此方法相关的业务处理流程)
	 * @param orderId
	 * @return OrderWfp 返回类型
	 * @throws
	 */

	public OrderWfp getOrderDtailById(String orderId, Store s);

	/**
	 * @Title: addOrderWfp
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:保存危废品订单 (详细描述此方法相关的业务处理流程)
	 * @param wfp
	 * @param ov
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean addOrderWfp(OrderWfp wfp);

	/**
	 * @Title: getOrderWfpByParams
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据条件查询危废品订单 (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param storeid
	 * @param orderNumber
	 * @param orderStatus
	 * @param contractNumber
	 * @param startTime
	 * @param endTime
	 * @return List<OrderWfp> 返回类型
	 * @throws
	 */
	public List<OrderWfp> getOrderWfpByParams(Pager pager, Store store,
			String orgId, String orderNumber, String orderStatus,
			String startTime, String endTime, String wfpName, String isType,
			String type);

	/**
	 * 危废品订单的查询数量的方法
	 */
	public String getOrderSum(String storeId, String status);

	public OrderWfp getOrderDtailByOrderId(String orderId, Store s);

	public List<String> getAll();
}
