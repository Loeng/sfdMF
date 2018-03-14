package com.ekfans.base.order.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderDao;
import com.ekfans.base.order.dao.IOrderDetailDao;
import com.ekfans.base.order.dao.IOrderTreatDetailDao;
import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.pub.util.FormatDigitalUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 订单处理日志业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class OrderTreatDetailService implements IOrderTreatDetailService {
	// 定义DAO
	@Autowired
	private IOrderTreatDetailDao orderTreatDetailDao;
	@Autowired
	private IOrderDao orderDao;
	@Autowired
	private IOrderDetailDao orderDetailDao;

	// 定义一个错误日志文件
	public static Logger log = LoggerFactory.getLogger(OrderTreatDetailService.class);

	/**
	 * 增加订单日志
	 */
	public boolean add(OrderTreatDetail orderTreatDetail) {
		try {
			orderTreatDetailDao.addBean(orderTreatDetail);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * @Title: getOrderTreatDetailByOrderId
	 * @Description: TODO 根据orderId取得OrderTreatDetail对象 详细业务流程:
	 *               (详细描述此方法相关的业务处理流程)
	 * @param @param orderId
	 * @param @return 设定文件
	 * @return OrderDetail 返回类型
	 * @throws
	 */
	public OrderTreatDetail getOrderTreatDetailByOrderId(String orderId) {
		StringBuffer sql = new StringBuffer("select odt from OrderTreatDetail as odt,OrderDetail as od where 1=1");
		sql.append(" and odt.orderId = od.orderId");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(orderId)) {
			sql.append(" and odt.orderId = :orderId");
			map.put("orderId", orderId);
		}
		try {
			List<OrderTreatDetail> list = orderTreatDetailDao.list(sql.toString(), map);
			if (list.size() > 0 && list != null) {
				return list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 修改
	 */
	public boolean update(OrderTreatDetail orderTreatDetail) {
		if (orderTreatDetail == null) {
			return false;
		}
		try {
			orderTreatDetailDao.updateBean(orderTreatDetail);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<OrderTreatDetail> getOrderTreatDetailListByOrderId(String orderId) {
		// 根据订单id查询（订单操作详情）
		String hql = "from OrderTreatDetail otd where otd.orderId=:orderId order by createTime desc";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);

		try {
			List<OrderTreatDetail> list = orderTreatDetailDao.list(hql, map);
			if (null == list || list.size() <= 0) {
				return null;
			}
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Boolean updateOrderTotalPriceGuessPrice(String orderId, String userName, String type, String price) {
		try {
			// 查询订单详情
			Order order = (Order) this.orderDao.get(orderId);

			// 记录修改订单的日志
			OrderTreatDetail otd = new OrderTreatDetail();
			otd.setOrderId(orderId);
			otd.setType(type);
			otd.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			otd.setCreater(userName);
			if (OrderConst.ORDER_TREAT_MODIFY.equals(type)) {
				otd.setNote("修改金额，金额从【" + order.getProductPrice() + "】修改到【" + price + "】");
				otd.setOriginalPrice(order.getTotalPrice());

				order.setProductPrice(new BigDecimal(price));
				order.setPaid(new BigDecimal(price).add(order.getFare()));
				orderDao.updateBean(order);

//				String hql = "from OrderDetail od where od.orderId=:orderId";
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("orderId", orderId);
//				List<OrderDetail> orderDetailList = orderDetailDao.list(hql, map);
//				BigDecimal totalPrice = new BigDecimal("0.00");// 成总:订单中所有订单明细的商品单价的总和
//				for (OrderDetail or : orderDetailList) {
//					totalPrice = totalPrice.add(or.getPrice().multiply(new BigDecimal(or.getQuantity())));
//				}
//				for (OrderDetail or : orderDetailList) {
//					BigDecimal unitPrice = or.getPrice();// 成单:订单明细的商品单价
//					BigDecimal basePrice = or.getPrice();// 商城单价：商品的商城价(没有任何优惠时的价格)
//					// 计算折扣率公式的最终精简公式：(成单/商城单价)*(订单实际成交金额/成总)
//
//					// 修改订单总价后的商品单价
//					BigDecimal newProductPrice = or.getPrice().multiply(((unitPrice.divide(basePrice)).multiply((new BigDecimal(price).divide(totalPrice)))));
//					or.setPrice(newProductPrice);
//					or.setTotalPrice(newProductPrice.multiply(new BigDecimal(or.getQuantity())));
//					orderDetailDao.updateBean(or);
//				}
			} else if (OrderConst.ORDER_TREAT_FREIGHT.equals(type)) {
				otd.setNote("修改运费，金额从【" + order.getFare() + "】修改到【" + FormatDigitalUtil.priceFormat(Double.valueOf(price)) + "】");
				otd.setOriginalPrice(order.getFare());

				order.setFare(new BigDecimal(price));
				order.setPaid(order.getProductPrice().add(new BigDecimal(price)));
				orderDao.updateBean(order);
			}
			otd.setNowPrice(new BigDecimal(price));
			add(otd);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
	}

}