package com.ekfans.base.order.service.StoreOrder.IndexAccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IStoreOrderDao;
import com.ekfans.base.order.model.Order;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: StoreIndexAccount
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 成都易科远见科技有限公司
 * @date 2014-6-3 下午02:58:55
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("rawtypes")
public class StoreIndexAccountService implements IStoreIndexAccountService {

	private Logger log = LoggerFactory.getLogger(StoreIndexAccountService.class);
	@Autowired
	private IStoreOrderDao storeOrderDao;

	/**
	 * 
	 * @Title: getOrderSellAccount (查询出 商店首页的 根据选择的月查询出对应的销售结算信息-QXH)
	 * @param yd
	 * @param storeID
	 * @return List<Order> 返回类型
	 * @throws
	 */
	public List<Order> getOrderSellAccount(String yd, String storeID) {
		List<Order> lorder = new ArrayList<Order>();
		try {
			// String nowDate=DateUtil.getSysDateString().trim();
			String startDate = yd + "-01";
			String endDate = yd + "-31";
			StringBuffer sql = new StringBuffer("select o.status,count(o.status) as number ," + "(sum(o.totalPrice)-sum(paid))as yhPrice ,sum(paid) as paid " + " from Order as o where 1=1 ");
			// 不查出 订单状态 为 0：取消，1：关闭的订单
			sql.append(" and o.status>1 ");
			sql.append(" and o.storeId='").append(storeID).append("' ");
			sql.append(" and o.createTime >=:startDate and o.createTime <=:endDate ");
			// sql.append(" and o.createTime between  :startDate and :endDate ");

			sql.append(" group by o.status ");

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			List<?> list = storeOrderDao.list(sql.toString(), params);

			// 创建４个订单类型
			Order orderOne = new Order();
			orderOne.setStatus("代付款订单");
			orderOne.setUserId("0");
			lorder.add(orderOne);

			Order orderTwo = new Order();
			orderTwo.setStatus("待发货订单");
			orderTwo.setUserId("0");
			lorder.add(orderTwo);

			Order orderThree = new Order();
			orderThree.setStatus("已发货待评价订单");
			orderThree.setUserId("0");
			lorder.add(orderThree);

			Order orderFour = new Order();
			orderFour.setStatus("交易完成订单");
			orderFour.setUserId("0");
			lorder.add(orderFour);

			for (int i = 0; i < list.size(); i++) {
				Object[] object = (Object[]) list.get(i);
				Order order = new Order();
				// 将数据库的 储存字段 render为客户查看的信息
				String status = object[0].toString();
				if ("2".equals(status)) {
					order.setStatus("代付款订单");
				} else if ("3".equals(status)) {
					order.setStatus("待发货订单");
				} else if ("4".equals(status)) {
					order.setStatus("已发货待评价订单");
				} else if ("5".equals(status)) {
					order.setStatus("交易完成订单");
				}
				for (Order tempOrder : lorder) {
					// 如果　创建的类型和查出来的类型一样　则覆盖他的其他值　
					if (tempOrder.getStatus().equals(order.getStatus())) {
						tempOrder.setUserId(object[1].toString()); // 订单数量
						tempOrder.setTotalPrice((BigDecimal) object[2]); // 优惠金额
						tempOrder.setPaid((BigDecimal) object[3]); // 实际金额
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return lorder;
	}

	/**
	 * 各种类型订单的统计 注释:对于退款订单这块暂未处理
	 */
	@Override
	public String getOrderCount(String orderStatus, boolean shippingStatus, String userApp, String storeApp, String refundStatus, String storeId) {
		try {
			if (refundStatus != null) {
				String refundCount = StoreIndexAccountService.getOrderCountTYPETWO(refundStatus, storeId, storeOrderDao, log);
				return refundCount;
			} else {
				String orderCount = StoreIndexAccountService.getOrderCountTYPEONE(orderStatus, shippingStatus, userApp, storeApp, storeId, storeOrderDao, log);
				return orderCount;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// ==========================================订单统计的私有方法START====================================
	/**
	 * 非退换货处理-统计 注释:对于退款订单这块暂未处理
	 */
	private static String getOrderCountTYPEONE(String orderStatus, boolean shippingStatus, String userApp, String storeApp, String storeId, IStoreOrderDao myDao, Logger mylog) {
		try {
			StringBuffer hql = new StringBuffer(" select count(o.id) from Order as o where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			if (!StringUtil.isEmpty(orderStatus) && !StringUtil.isEmpty(userApp) && !StringUtil.isEmpty(storeApp)) {
				hql.append(" and o.status = :orderStatus and o.shippingStatus = :shippingStatus");
				hql.append(" and o.userApp = :userApp and o.storeApp = :storeApp");
				params.put("orderStatus", orderStatus);
				params.put("shippingStatus", shippingStatus);
				params.put("userApp", Boolean.parseBoolean(userApp));
				params.put("storeApp", Boolean.parseBoolean(storeApp));

				hql.append(" and o.storeId = :storeId");
				params.put("storeId", storeId);
			} else if (!StringUtil.isEmpty(orderStatus)) {
				hql.append(" and o.status = :orderStatus and o.shippingStatus = :shippingStatus");
				params.put("orderStatus", orderStatus);
				params.put("shippingStatus", shippingStatus);

				hql.append(" and o.storeId = :storeId");
				params.put("storeId", storeId);
			} else if (!StringUtil.isEmpty(orderStatus)) {
				hql.append(" and o.status = :orderStatus");
				params.put("orderStatus", orderStatus);

				hql.append(" and o.storeId = :storeId");
				params.put("storeId", storeId);
			} else if (shippingStatus == true) {
				hql.append(" and o.shippingStatus = :shippingStatus and o.status = 3");
				params.put("shippingStatus", shippingStatus);
			}

			List list = myDao.list(hql.toString(), params);
			if (list != null && list.size() == 1) {
				// 返回统计的数量
				return list.get(0) + "";
			}
		} catch (Exception e) {
			mylog.error(e.getMessage());
		}
		return "";
	}

	/**
	 * 退换货处理-统计
	 */
	private static String getOrderCountTYPETWO(String refundStatus, String storeId, IStoreOrderDao myDao, Logger mylog) {
		try {
			StringBuffer hql = new StringBuffer(" select count(r.id) from Refund as r,Order as o where 1=1");
			hql.append(" and r.orderId = o.id");
			Map<String, Object> params = new HashMap<String, Object>();
			if (!StringUtil.isEmpty(refundStatus)) {
				hql.append(" and r.status = :refundStatus");
				params.put("refundStatus", refundStatus);
			}
			if (!StringUtil.isEmpty(storeId)) {
				hql.append(" and o.storeId = :storeId");
				params.put("storeId", storeId);
			}

			List list = myDao.list(hql.toString(), params);
			if (list != null && list.size() == 1) {
				// 返回统计结果
				return list.get(0) + "";
			}
		} catch (Exception e) {
			mylog.error(e.getMessage());
		}
		return "";
	}
	// ==========================================订单统计私有方法END=======================================
}
