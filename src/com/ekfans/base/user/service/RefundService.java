package com.ekfans.base.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderDao;
import com.ekfans.base.order.dao.IRefundDao;
import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.model.Refund;
import com.ekfans.base.product.dao.IProductDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.user.dto.RefundDto;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: RefundService
 * @Description: TODO(退货/换货的service实现)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-12 下午4:52:21
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("rawtypes")
public class RefundService implements IRefundService {

	@Resource
	private IRefundDao refundDao;
	@Resource
	private IProductDao productDao;
	@Resource
	private IOrderDao orderDao;

	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(UserService.class);

	/**
	 * 
	 * @Title: getRefund
	 * @Description: TODO(获取会员 的退货 /换货的列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param orderID
	 * @param freightSn
	 * @param user
	 * @return List<Refund> 返回类型
	 * @throws
	 */
	public List<RefundDto> getRefund(String orderID, String freightSn, User user, Pager pager) {

		List<RefundDto> lrfd = new ArrayList<RefundDto>();
		try {

			StringBuffer hql = new StringBuffer();
			hql.append(" select r.type,r.orderId,o.logisticsNo,p.sortName,r.status,r.createTime,r.id,r.productId " + " from Refund as r , Product as p ,Order as o where 1=1 ");

			// 关联产品
			hql.append(" and r.productId=p.id ");
			// 关联订单
			hql.append(" and r.orderId=o.id ");
			// 用户的的id
			hql.append(" and r.userId='").append(user.getId()).append("' ");

			Map<String, Object> map = new HashMap<String, Object>();

			// 订单的编号
			if (!StringUtil.isEmpty(orderID)) {
				hql.append(" o.id=:orderID");
				map.put("orderID", orderID);
			}
			// 订单的货运单号
			if (!StringUtil.isEmpty(freightSn)) {
				hql.append(" and  o.logisticsNo=:logisticsNo ");
				map.put("logisticsNo", freightSn);
			}

			List<?> list = refundDao.list(hql.toString(), map);

			for (int i = 0; i < list.size(); i++) {
				Object[] object = (Object[]) list.get(i);
				RefundDto rd = new RefundDto();

				String type = object[0].toString();
				String orderId = object[1].toString();
				String logisticsNo = object[2].toString();
				String sortName = object[3].toString();
				String status = object[4].toString();
				String createTime = object[5].toString();
				String id = object[6].toString();
				String productId = object[7].toString();

				// 0:换货；1：退货
				rd.setType(type);
				// 订单号
				rd.setOrderId(orderId);
				// 0：等待卖家确认；1：退货/换货中；:2：退货/换货完成
				rd.setStatus(status);
				// 发起申请的时间
				rd.setCreateTime(createTime);
				// 订单编号
				rd.setLogisticsNo(logisticsNo);
				// 产品名称
				rd.setProductSortName(sortName);

				rd.setId(id);
				rd.setProductId(productId);

				lrfd.add(rd);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return lrfd;
	}

	/**
	 * 
	 * @Title: getRefundInfo
	 * @Description: TODO(获取退换货的信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param refundId
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	public Map<String, Object> getRefundInfo(String refundId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 退换货
			Refund refund = (Refund) refundDao.get(refundId);
			// 产品
			Product product = (Product) productDao.get(refund.getProductId());
			// 订单
			Order order = (Order) orderDao.get(refund.getOrderId());
			// 收货地址
			OrderAddress orderAddress = new OrderAddress();
			List<?> ltadd = refundDao.list(" from OrderAddress where  orderId='" + refund.getOrderId() + "' ", null);
			if (ltadd.size() > 0) {
				orderAddress = (OrderAddress) ltadd.get(0);
			}
			// 订单日志
			List<?> lotd = new ArrayList<OrderTreatDetail>();
			List<?> lorderLog = refundDao.list(" from OrderTreatDetail where  orderId='" + refund.getOrderId() + "' ", null);
			if (lorderLog.size() > 0) {
				lotd = lorderLog;
			}
			map.put("refund", refund);
			map.put("product", product);
			map.put("order", order);
			map.put("orderAddress", orderAddress);
			map.put("lotd", lotd);

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return map;

	}

	/**
	 * 保存操作
	 */
	public boolean addRefund(Refund refund) {
		if (refund == null) {
			return false;
		}
		try {
			refundDao.addBean(refund);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据productId取得Refund对象 wsj 需要的数据： orderDetail: orderAddress:
	 */
	@Override
	public Refund getRefundByProductId(String productId) {
		// 定义sql语句
		StringBuffer sql = new StringBuffer("select r from Refund as r,OrderDetail as od,Product as p,OrderAddress as oa");
		sql.append(",Order as o,OrderTreatDetail as odt where 1=1");
		// 定义关联表
		sql.append(" r.productId = p.id");// 退换表的商品ID与product表的ID对应
		sql.append(" r.orderId = o.id");// 退换表的订单ID与order表的ID对应
		sql.append(" r.orderId = od.orderId");// 退换表的订单ID与orderdetail表的orderID对应
		sql.append(" r.orderId = odt.orderId");// 退换表的订单ID与orderTreatDetail表的orderID对应
		sql.append(" r.orderId = oa.orderId");// 退换表的订单ID与orderAddress表的orderID对应
		Map<String, Object> map = new HashMap<String, Object>();

		if (!StringUtil.isEmpty(productId)) {
			sql.append(" and r.productId = :productId");
			map.put("productId", productId);
		}
		try {
			List list = refundDao.list(sql.toString(), map);
			return (Refund) list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

}
