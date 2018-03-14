package com.ekfans.base.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderDetailDao;
import com.ekfans.base.order.model.Appraise;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.vo.OderDetailProduct;
import com.ekfans.base.product.dao.IProductDao;
import com.ekfans.base.product.dao.IProductInfoDetailDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 订单明细业务实现Service
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
public class OrderDetailService implements IOrderDetailService {
	// 定义DAO
	@Autowired
	private IOrderDetailDao orderDetailDao;
	@Autowired
	private IProductDao productDao;
	@Autowired
	private IProductInfoDetailDao productInfoDetailDao;

	// 定义一个错误日志文件
	public static Logger log = LoggerFactory.getLogger(OrderDetailService.class);

	/**
	 * 评价商品时取得的订单
	 */
	public List<OrderDetail> getDetail(Pager pager, String userId) {
		// 定义sql语句
		StringBuffer sql = new StringBuffer("select od.userId,od.productName,od.quantity,od.price,od.totalPrice," + "o.createTime ,oa.name ,o.id,a.type from OrderDetail as od,User as u," + "Order as o,OrderAddress as oa,Appraise as a where 1=1");
		// 定义关联表
		sql.append(" and od.userId = u.id");
		sql.append(" and od.orderId = o.id");
		sql.append(" and o.userId = u.id");
		sql.append(" and od.orderId = oa.orderId");

		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(userId)) {
			sql.append(" and od.userId = :userId");
			map.put("userId", userId);
		}
		try {
			List<Object[]> list = orderDetailDao.list(pager, sql.toString(), map);
			List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
			for (Object[] object : list) {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setUserId((String) object[0]);
				orderDetail.setProductName((String) object[1]);
				orderDetail.setQuantity((Integer) object[2]);
				orderDetail.setPrice((BigDecimal) object[3]);
				orderDetail.setTotalPrice((BigDecimal) object[4]);
				// orderDetail.setCreateTime((String)object[5]);
				// orderDetail.setName((String)object[6]);
				orderDetail.setOrderId((String) object[7]);
				orderDetail.setType((String) object[8]);
				orderDetails.add(orderDetail);
			}
			return orderDetails;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 添加
	 */
	public boolean addDetail(OrderDetail orderDetail) {
		try {
			orderDetailDao.addBean(orderDetail);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据传回来的orderId取得OrderDetail集合 wsj 需要的数据：
	 * OrderDetail：orderId，productName，price
	 */
	@Override
	public List<OrderDetail> getOrderDetail(String orderId) {
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

		StringBuffer sql = new StringBuffer("select od.productName,od.productNo,od.price,od.productId,od.totalPrice,od.quantity,od.productImage,od.productInfoDetailId,o.fare,od.infoName1,od.infoName2,od.infoName3,od.infoName4,od.infoValue1,od.infoValue2,od.infoValue3,od.infoValue4,od.id ");
		sql.append(" from OrderDetail as od,Order o where 1=1");
		sql.append("  and o.id=od.orderId");

		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(orderId)) {
			sql.append(" and od.orderId = :orderId");
			map.put("orderId", orderId.trim());
		}
		try {
			List<Object[]> list = orderDetailDao.list(sql.toString(), map);
			for (Object[] objects : list) {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setProductName((String) objects[0]);
				orderDetail.setProductNumber((String) objects[1]);
				orderDetail.setPrice((BigDecimal) objects[2]);
				orderDetail.setProductId((String) objects[3]);
				orderDetail.setTotalPrice((BigDecimal) objects[4]);
				orderDetail.setQuantity((Integer) objects[5]);
				orderDetail.setSmallPicture((String) objects[6]);
				orderDetail.setInfoName1((String) objects[9]);
				orderDetail.setInfoName2((String) objects[10]);
				orderDetail.setInfoName3((String) objects[11]);
				orderDetail.setInfoName4((String) objects[12]);
				orderDetail.setInfoValue1((String) objects[13]);
				orderDetail.setInfoValue2((String) objects[14]);
				orderDetail.setInfoValue3((String) objects[15]);
				orderDetail.setInfoValue4((String) objects[16]);
				orderDetail.setId((String) objects[17]);
				orderDetails.add(orderDetail);
			}
			return orderDetails;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public OrderDetail getOrderDetailByProductId(String productId, String orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "from OrderDetail where productId=:productId and orderId=:orderId";
		map.put("productId", productId);
		map.put("orderId", orderId);
		
		try {
			List<OrderDetail> list = orderDetailDao.list(hql, map);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据店铺id得到List<OrderDetail>对象
	 */
	public List<OrderDetail> getOrderDetailByStoreId(String storeId) {
		StringBuffer sql = new StringBuffer("select od from OrderDetail as od where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and od.storeId = :storeId");
			map.put("storeId", storeId);
		}
		try {
			List<OrderDetail> list = orderDetailDao.list(sql.toString(), map);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<OderDetailProduct> getProductOrderInfo(String orderId) {
		List<OderDetailProduct> odpList = new ArrayList<OderDetailProduct>();

		// 获取订单详情（OrderDetail）
		String hql = "from OrderDetail od where od.orderId=:orderId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);

		try {
			List<OrderDetail> odList = orderDetailDao.list(hql, map);
			for (OrderDetail od : odList) {

				Product product = (Product) productDao.get(od.getProductId());
				ProductInfoDetail pid = (ProductInfoDetail) productInfoDetailDao.get(od.getProductInfoDetailId());

				OderDetailProduct odp = new OderDetailProduct();
				odp.setOrderDetail(od);
				odp.setProduct(product);
				odp.setProductInfoDetail(pid);
				odpList.add(odp);
			}
			return odpList;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}

	}

	@Override
	public List<OderDetailProduct> getOrderDetailAndProductAndApprise(String orderId, String storeId) {
		List<OderDetailProduct> entityList = new ArrayList<OderDetailProduct>();

		Session session = this.orderDetailDao.createSession();
		String sql = "SELECT {od.*},{ap.*},{p.*} FROM order_detail od,appraise ap,product p WHERE od.PRODUCT_ID=p.ID " + "AND od.ID=ap.ORDER_DETAIL_ID AND od.ORDER_ID=? AND ap.target=?";
		Query query = session.createSQLQuery(sql).addEntity("od", OrderDetail.class).addEntity("ap", Appraise.class).addEntity("p", Product.class);
		query.setString(0, orderId);
		query.setString(1, storeId);

		List<Object[]> list = query.list();
		if (null == list || list.size() <= 0) {
			return null;
		}

		for (Object[] obj : list) {
			OderDetailProduct odp = new OderDetailProduct();
			odp.setOrderDetail((OrderDetail) obj[0]);
			odp.setProduct((Product) obj[2]);
			odp.setAppraise((Appraise) obj[1]);

			entityList.add(odp);
		}
		if(null!=session&&session.isOpen()){
			session.close();
		}
		return entityList;
	}

}