package com.ekfans.base.order.service.StoreOrder.ReturnManagement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IRefundDao;
import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.model.Refund;
import com.ekfans.base.product.dao.IProductDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;


/**
 * 
 * @ClassName: RefundOrderService
 * @Description: TODO(商户中心-退换货管理的service)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-14 上午11:50:08
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class RefundOrderService implements IRefundOrderService {
	
	private Logger log = LoggerFactory.getLogger(RefundOrderService.class);
	@Autowired
	private IRefundDao refundDao;
	@Autowired
	private IProductDao productDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Refund> getRefundOrder(Pager pager, String userId, String orderId, String beginDate, String endDate) {
		// params data
		Map<String,Object> params = new HashMap<String,Object>();
		// hql
	    StringBuffer hql = new StringBuffer("select r,od.totalPrice,oa.name from Refund r,OrderDetail od,OrderAddress oa where r.orderId=od.orderId and r.productId=od.productId and r.orderId=oa.orderId");
	    //setting data
	    if(!StringUtil.isEmpty(userId)){
	    	hql.append(" and r.userId=:userId");
	    	params.put("userId", userId);
	    }
	    if(!StringUtil.isEmpty(orderId)){
	    	hql.append(" and r.orderId=:orderId");
	    	params.put("orderId", orderId);
	    }
	    if(!StringUtil.isEmpty(beginDate)){
	    	hql.append(" and r.createTime>=:beginDate");
	    	params.put("beginDate", beginDate);
	    }
	    if(!StringUtil.isEmpty(endDate)){
	    	hql.append(" and r.createTime<=:endDate");
	    	params.put("endDate", endDate);
	    }
	    hql.append(" order by r.status asc,r.createTime desc");
	    
	    try {
	    	List<Object[]> list = this.refundDao.list(pager, hql.toString(), params);
	    	if(list != null && list.size() > 0){
	    		List<Refund> rlist = new ArrayList<Refund>();
	    		for (Object[] obj : list) {
	    			Refund r = (Refund)obj[0];
	    			r.setTotalPrice((BigDecimal)obj[1]);
	    			r.setReceiptName(obj[2].toString());
	    			
	    			rlist.add(r);
				}
	    		return rlist;
	    	}
	    	return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Refund> getHXQRefundOrder(Pager pager, String storeId, String orderId) {
		// params data
		Map<String,Object> params = new HashMap<String,Object>();
		// hql
		StringBuffer hql = new StringBuffer("select r,od.totalPrice,oa.name from Refund r,OrderDetail od,OrderAddress oa where r.orderId=od.orderId and r.productId=od.productId and r.orderId=oa.orderId");
		hql.append(" and r.productId in (select p.id from Product p,Store s where p.storeId=s.id and s.id=:storeId)");
		// setting data
		params.put("storeId", storeId);
		if(!StringUtil.isEmpty(orderId)){
			hql.append(" and r.orderId=:orderId");
	    	params.put("orderId", orderId);
		}
		 hql.append(" order by r.status asc,r.createTime desc");
		    
	    try {
	    	List<Object[]> list = this.refundDao.list(pager, hql.toString(), params);
	    	if(list != null && list.size() > 0){
	    		List<Refund> rlist = new ArrayList<Refund>();
	    		for (Object[] obj : list) {
	    			Refund r = (Refund)obj[0];
	    			r.setTotalPrice((BigDecimal)obj[1]);
	    			r.setReceiptName(obj[2].toString());
	    			
	    			rlist.add(r);
				}
	    		return rlist;
	    	}
	    	return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	    return null;
	}

	/**
	 * 商户中心-交易管理-退换货处理 退换货处理 店铺管理员根据会员中心过来的退换货处理请求处理退换货 退换货的实现
	 * 卖家确认(status:0)-->退货、换货中(status:1)(-->不同意退货换货(status:2))
	 */
	@Override
	public boolean refundOrExChengeProduct(String refundId, String sellerStatus, String reason) {
		try {
			Refund refund = (Refund) refundDao.get(refundId);
			// 买家确认后 状态为店主是否同意
			if ("1".equals(sellerStatus)) {
				refund.setStatus(sellerStatus);
				// 拒绝退货换货的时候有记录理由
				refund.setNote(reason);
			} else if ("2".equals(sellerStatus)) {
				refund.setStatus(sellerStatus);
			}
			refundDao.updateBean(refund);
			// 操作成功
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 查询出订单详情 不包括订单的商品信息
	 */
	public Object[] getOrderDetailByOrderId(String orderId) {
		try {
			StringBuffer hql = new StringBuffer(" select" + " oa.name,oa.provincial,oa.city,oa.area,oa.address,oa.phoneNum,oa.mobile,oa.email," + // 2
					" o.payment,o.fare,o.logisticsName,o.logisticsNo," + // 3
					" o.invoiceType,o.invoiceTitle,o.invoiceContent," + // 4
					" o.totalPrice,o.paid,otd.createTime,o.id,o.status,o.shipment" + // 5
					" from Order as o,OrderAddress as oa,OrderTreatDetail as otd" + " where 1=1");
			// 添加关联条件
			hql.append(" and o.id=oa.orderId");
			hql.append(" and o.id=otd.orderId");

			// 需要查询出付款时间 所以这里需从OrderTreatDetail取出操作类型为付款的操作时间
			hql.append(" and otd.type='2'");

			// 定义接收参数的Map
			Map<String, Object> params = new HashMap<String, Object>();

			if (StringUtil.isEmpty(orderId)) {
				return null;
			}
			hql.append(" and o.id=:orderId");
			params.put("orderId", orderId);

			// 执行查询
			List list = refundDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回的Object数组
				Object[] returnObj = new Object[3];
				// 接收所查询的订单详情
				Object[] objects = (Object[]) list.get(0);

				// OrderAddress部分
				OrderAddress address = new OrderAddress();
				address.setName((String) objects[0]); // 收货人姓名
				address.setProvincial((String) objects[1]); // 省
				address.setCity((String) objects[2]); // 市
				address.setArea((String) objects[3]); // 区
				address.setAddress((String) objects[4]); // 详细地址
				address.setPhoneNum((String) objects[5]); // 收货人固定电话
				address.setMobile((String) objects[6]); // 收货人手机
				address.setEmail((String) objects[7]); // 收货人邮箱
				returnObj[0] = address;

				// Order部分
				Order order = new Order();
				order.setPayment((String) objects[8]); // 支付方式
				order.setFare((BigDecimal) objects[9]); // 运费
				order.setLogisticsName((String) objects[10]); // 物流公司
				order.setLogisticsNo((String) objects[11]); // 运单号码

				order.setInvoiceType((Boolean) objects[12]); // 发票类型
				order.setInvoiceTitle((String) objects[13]); // 发票抬头
				order.setInvoiceContent((String) objects[14]); // 发票内容

				order.setTotalPrice((BigDecimal) objects[15]); // 应收金额
				order.setPaid((BigDecimal) objects[16]); // 实收金额

				order.setId((String) objects[18]); // 订单id
				order.setStatus((String) objects[19]); // 订单状态
				order.setShipment((String) objects[20]); // 配送方式
				returnObj[1] = order;

				// OrderTreatDetail部分
				OrderTreatDetail treatDetail = new OrderTreatDetail();
				treatDetail.setCreateTime((String) objects[17]); // 付款日期
				returnObj[2] = treatDetail;

				// 将数组返回
				return returnObj;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 退换货商品的的商品详情
	 */
	public Product getProdyctById(String productId) {
		try {
			if (StringUtil.isEmpty(productId)) {
				return null;
			}
			return (Product) productDao.get(productId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据refundId获得refund的详情信息
	 */
	@Override
	public Refund getRefundById(String refundId) {
		try {
			if (!StringUtil.isEmpty(refundId)) {
				return (Refund) refundDao.get(refundId);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 用户中心根据商品的Id进行申请退换货
	 */
	public Refund getRefundByProductId(String productId) {
		// 判定productId
		if (StringUtil.isEmpty(productId)) {
			return null;
		}
		// 定义sql语句
		StringBuffer sql = new StringBuffer("select r from Refund as r,Product as p where 1=1");
		sql.append(" and r.productId = p.id");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(productId)) {
			sql.append(" and r.productId = :productId");
			map.put("productId", productId);
		}
		try {
			List list = refundDao.list(sql.toString(), map);
			if (list.size() > 0) {
				return (Refund) list.get(0);
			}

			return (Refund) list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据orderId得到订单日志列表
	 */
	@Override
	public List<OrderTreatDetail> findOrderTreatDetailByOrderId(String orderId) {
		StringBuffer sql = new StringBuffer("select otd from OrderTreatDetail as otd where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(orderId)) {
			sql.append(" and otd.orderId=:orderId");
			map.put("orderId", orderId);
		}
		try {
			List<OrderTreatDetail> treatDetails = refundDao.list(sql.toString(), map);
			if (treatDetails != null && treatDetails.size() > 0) {
				return treatDetails;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 批量处理退货/换货的同意操作
	 */
	@Override
	public boolean aggreeRefuns(List<String> refundIds) {
		try {
			if (refundIds == null || refundIds.size() == 0) {
				return false;
			}

			for (int i = 0; i < refundIds.size(); i++) {
				Refund Refund = (Refund) refundDao.get(refundIds.get(i));
				// 同意退货、换货
				Refund.setStatus("2");
				refundDao.updateBean(Refund);
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	
}
