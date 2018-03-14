package com.ekfans.base.order.service.StoreOrder.OrderManage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IAppraiseDao;
import com.ekfans.base.order.dao.IOrderTreatDetailDao;
import com.ekfans.base.order.dao.IStoreOrderDao;
import com.ekfans.base.order.model.Appraise;
import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderAddress;
import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.product.dao.IProductInfoDetailDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: StoreOrderService
 * @Description: TODO(商户中心交易管理(订单)实现方法)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-7 上午11:14:00
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StoreOrderService implements IStoreOrderService {
	// 定义错误处理日志
	public static Logger log = LoggerFactory.getLogger(StoreOrderService.class);

	// 注入DAO
	@Autowired
	private IStoreOrderDao storeOrderDao;

	@Autowired
	private IOrderTreatDetailDao treatDetailDao;

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IAppraiseDao appraiseDao;
	@Autowired
	private IProductInfoDetailDao productInfoDetailDao;

	/**
	 * (商户中心)按照条件查询出满足条件的订单(所有)
	 */
	@Override
	public List<Order> getStoreOrderByConditions(String storeId, String orderId, String userName, String beginDate, String endDate, String status, String type, String actType,Pager pager) {
		try {
			StringBuffer hql = new StringBuffer("select o.id,o.status,o.createTime,o.paid,s.storeName,");
			hql.append(" o.shippingStatus,o.userApp,o.storeApp,o.fare,o.totalPrice,o.type");
			hql.append(" from Order as o,Store as s where 1=1");
			// 添加关联条件
			hql.append(" and o.userId = s.id");
			// 定义存储查询条件的Map
			Map<String, Object> params = new HashMap<String, Object>();

			// 添加当前的店铺id
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}else{
				if(("1").equals(actType)){//查销售订单
					hql.append(" and o.storeId =:storeId");
					params.put("storeId", storeId);
				}
				if(("2").equals(actType)){//查采购订单
					hql.append(" and o.userId =:storeId");
					params.put("storeId", storeId);
				}
			}

			if (!StringUtil.isEmpty(orderId)) {
				hql.append(" and o.id=:orderId");
				params.put("orderId", orderId);
			}
			if (!StringUtil.isEmpty(type)) {
				hql.append(" and o.type=:type");
				params.put("type", Integer.valueOf(type));
			}
			if (!StringUtil.isEmpty(userName)) {
				hql.append(" and s.storeName like:userName");
				params.put("userName", "%" + userName + "%");
			}
			if (!StringUtil.isEmpty(beginDate)) {
				hql.append(" and o.createTime >=:beginDate");
				params.put("beginDate", beginDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				hql.append(" and o.createTime <=:endDate");
				params.put("endDate", endDate);
			}

			if (!StringUtil.isEmpty(status)) {
				hql.append(" and o.status=:status");
				params.put("status", status);
			}
			// 添加排序
			hql.append(" order by o.createTime DESC");

			// 执行查询
			List list = storeOrderDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回的集合
				List<Order> orders = new ArrayList<Order>();
				for (int i = 0; i < list.size(); i++) {
					// 定义接收数据的容器
					Order order = new Order();
					Object[] objects = (Object[]) list.get(i);

					order.setId((String) objects[0]);
					order.setStatus((String) objects[1]);
					order.setCreateTime((String) objects[2]);

					order.setPaid((BigDecimal) objects[3]);
					// 这里将用户名保存在userId字段
					order.setUserId((String) objects[4]);
					order.setShippingStatus((Boolean) objects[5]);
					order.setUserApp((Boolean) objects[6]);
					order.setStoreApp((Boolean) objects[7]);
					order.setFare((BigDecimal) objects[8]);
					order.setTotalPrice((BigDecimal) objects[9]);
					order.setType((Integer) objects[10]);
					orders.add(order);
				}
				return orders;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询出等待付款的订单
	 */
	@Override
	public List<Order> getStoreOrderWaitPayByConditions(String storeId, String orderId, String userName, String beginDate, String endDate, Pager pager) {
		try {
			StringBuffer hql = new StringBuffer("select o.id,o.status,o.createTime,o.paid,u.name,");
			hql.append(" o.shippingStatus,o.userApp,o.storeApp,o.fare");
			hql.append(" from Order as o,User as u where 1=1");
			// 添加关联条件
			hql.append(" and o.userId = u.id");

			// 定义存储查询条件的Map
			Map<String, Object> params = new HashMap<String, Object>();

			// 添加当前的店铺id
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}
			hql.append(" and o.storeId =:storeId");
			params.put("storeId", storeId);

			// 为等待付款的订单添加相应的状态查询 2状态为代付款状态
			hql.append(" and o.status='2'");

			if (!StringUtil.isEmpty(orderId)) {
				hql.append(" and o.id like :orderId");
				params.put("orderId", "%" + orderId + "%");
			}
			if (!StringUtil.isEmpty(userName)) {
				hql.append(" and u.name like:userName");
				params.put("userName", "%" + userName + "%");
			}
			if (!StringUtil.isEmpty(beginDate)) {
				hql.append(" and o.createTime >=:beginDate");
				params.put("beginDate", beginDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				hql.append(" and o.createTime <=:endDate");
				params.put("endDate", endDate);
			}
			// 添加排序
			hql.append(" order by o.createTime DESC");

			// 执行查询
			List list = storeOrderDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回的集合
				List<Order> orders = new ArrayList<Order>();
				for (int i = 0; i < list.size(); i++) {
					// 定义接收数据的容器
					Order order = new Order();
					Object[] objects = (Object[]) list.get(i);

					order.setId((String) objects[0]);
					order.setStatus((String) objects[1]);
					order.setCreateTime((String) objects[2]);

					order.setPaid((BigDecimal) objects[3]);

					// 这里将用户名保存在userId字段
					order.setUserId((String) objects[4]);
					order.setShippingStatus((Boolean) objects[5]);
					order.setUserApp((Boolean) objects[6]);
					order.setStoreApp((Boolean) objects[7]);
					order.setTotalPrice(order.getPaid().add(order.getFare()));

					orders.add(order);
				}
				return orders;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询出等待发货订单
	 */
	@Override
	public List<Order> getStoreOrderWaitShipmentsByConditions(String storeId, String orderId, String userName, String beginDate, String endDate, Pager pager) {
		try {
			StringBuffer hql = new StringBuffer("select o.id,o.status,o.createTime,o.paid,u.name,");
			hql.append(" o.shippingStatus,o.userApp,o.storeApp,o.fare");
			hql.append(" from Order as o,User as u where 1=1");
			// 添加关联条件
			hql.append(" and o.userId = u.id");

			// 定义存储查询条件的Map
			Map<String, Object> params = new HashMap<String, Object>();

			// 添加当前的店铺id
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}
			hql.append(" and o.storeId =:storeId");
			params.put("storeId", storeId);

			// 为其添加待发货状态 待发货状态为 已付款(status=3) 没有发货(shippingStatus=false)
			hql.append(" and o.status='3' and o.shippingStatus=false");

			if (!StringUtil.isEmpty(orderId)) {
				hql.append(" and o.id like:orderId");
				params.put("orderId", "%" + orderId + "%");
			}
			if (!StringUtil.isEmpty(userName)) {
				hql.append(" and u.name like:userName");
				params.put("userName", "%" + userName + "%");
			}
			if (!StringUtil.isEmpty(beginDate)) {
				hql.append(" and o.createTime >=:beginDate");
				params.put("beginDate", beginDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				hql.append(" and o.createTime <=:endDate");
				params.put("endDate", endDate);
			}
			// 添加排序
			hql.append(" order by o.createTime DESC");

			// 执行查询
			List list = storeOrderDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回的集合
				List<Order> orders = new ArrayList<Order>();
				for (int i = 0; i < list.size(); i++) {
					// 定义接收数据的容器
					Order order = new Order();
					Object[] objects = (Object[]) list.get(i);

					order.setId((String) objects[0]);
					order.setStatus((String) objects[1]);
					order.setCreateTime((String) objects[2]);

					order.setPaid((BigDecimal) objects[3]);

					// 这里将用户名保存在userId字段
					order.setUserId((String) objects[4]);
					order.setShippingStatus((Boolean) objects[5]);
					order.setUserApp((Boolean) objects[6]);
					order.setStoreApp((Boolean) objects[7]);
					order.setTotalPrice(order.getPaid().add(order.getFare()));

					orders.add(order);
				}
				return orders;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 退款中的订单
	 */
	@Override
	public List<Order> getStoreOrderRefundByConditions(String storeId, String orderId, String userName, String beginDate, String endDate, Pager pager) {
		try {
			StringBuffer hql = new StringBuffer("select o.id,o.status,o.createTime,o.paid,u.name,");
			hql.append(" o.shippingStatus,o.userApp,o.storeApp");
			hql.append(" from Order as o,User as u where 1=1");
			// 添加关联条件
			hql.append(" and o.userId = u.id");

			// 定义存储查询条件的Map
			Map<String, Object> params = new HashMap<String, Object>();

			// 添加当前的店铺id
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}
			hql.append(" and o.storeId =:storeId");
			params.put("storeId", storeId);

			// 退款中的订单serviceStatus='1'
			hql.append(" and o.serviceStatus='1'");

			if (!StringUtil.isEmpty(orderId)) {
				hql.append(" and o.id=:orderId");
				params.put("orderId", orderId);
			}
			if (!StringUtil.isEmpty(userName)) {
				hql.append(" and u.name like:userName");
				params.put("userName", "%" + userName + "%");
			}
			if (!StringUtil.isEmpty(beginDate)) {
				hql.append(" and o.createTime >=:beginDate");
				params.put("beginDate", beginDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				hql.append(" and o.createTime <=:endDate");
				params.put("endDate", endDate);
			}
			// 添加排序
			hql.append(" order by o.createTime DESC");

			// 执行查询
			List list = storeOrderDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回的集合
				List<Order> orders = new ArrayList<Order>();
				for (int i = 0; i < list.size(); i++) {
					// 定义接收数据的容器
					Order order = new Order();
					Object[] objects = (Object[]) list.get(i);

					order.setId((String) objects[0]);
					order.setStatus((String) objects[1]);
					order.setCreateTime((String) objects[2]);
					order.setPaid((BigDecimal) objects[3]);
					// 这里将用户名保存在userId字段
					order.setUserId((String) objects[4]);
					order.setShippingStatus((Boolean) objects[5]);
					order.setUserApp((Boolean) objects[6]);
					order.setStoreApp((Boolean) objects[7]);

					orders.add(order);
				}
				return orders;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 待评价的订单
	 */
	@Override
	public List<Order> getStoreOrderEvaluateByConditions(String storeId, String orderId, String userName, String beginDate, String endDate, Pager pager) {
		try {
			StringBuffer hql = new StringBuffer("select o.id,o.status,o.createTime,o.paid,u.name,");
			hql.append(" o.shippingStatus,o.userApp,o.storeApp,o.fare");
			hql.append(" from Order as o,User as u where 1=1");
			// 添加关联条件
			hql.append(" and o.userId = u.id");

			// 定义存储查询条件的Map
			Map<String, Object> params = new HashMap<String, Object>();

			// 添加当前的店铺id
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}
			hql.append(" and o.storeId =:storeId");
			params.put("storeId", storeId);

			// 商户中心的评价(serviceStatus=='' 表示用户没有申请退换货)
			hql.append(" and o.shippingStatus='1' and o.userApp='1' and o.status='4' and o.serviceStatus=''");

			if (!StringUtil.isEmpty(orderId)) {
				hql.append(" and o.id like:orderId");
				params.put("orderId", orderId);
			}
			if (!StringUtil.isEmpty(userName)) {
				hql.append(" and u.name like:userName");
				params.put("userName", "%" + userName + "%");
			}
			if (!StringUtil.isEmpty(beginDate)) {
				hql.append(" and o.createTime >=:beginDate");
				params.put("beginDate", beginDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				hql.append(" and o.createTime <=:endDate");
				params.put("endDate", endDate);
			}
			// 添加排序
			hql.append(" order by o.createTime DESC");

			// 执行查询
			List list = storeOrderDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回的集合
				List<Order> orders = new ArrayList<Order>();
				for (int i = 0; i < list.size(); i++) {
					// 定义接收数据的容器
					Order order = new Order();
					Object[] objects = (Object[]) list.get(i);

					order.setId((String) objects[0]);
					order.setStatus((String) objects[1]);
					order.setCreateTime((String) objects[2]);

					order.setPaid((BigDecimal) objects[3]);
					// order.setTempPaid(FormatDigitalUtil.priceFormat(objects[3]));

					// 这里将用户名保存在userId字段
					order.setUserId((String) objects[4]);
					order.setShippingStatus((Boolean) objects[5]);
					order.setUserApp((Boolean) objects[6]);
					order.setStoreApp((Boolean) objects[7]);
					order.setTotalPrice(order.getPaid().add(order.getFare()));

					orders.add(order);
				}
				return orders;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 订单详情包括: step1: 1.商品信息(OrderDetail
	 * Product):商品小图、商品名称、商品价格、商品编号、商品类型(目前所有商品的类型均为普通商品) 注意:一个订单可能存在多个商品
	 * 故这里采用分开查询的处理方式 step2:
	 * 2.收货人信息(OrderAddress):收货人、地址(省市区+详细地址)、固定电话、手机号码、电子邮箱
	 * 3.支付及配送方式(Order):支付方式、运费、物流公司、运单号码 4.发票信息(Order):发票类型、发票抬头、发票内容
	 * 5.付款信息(Order
	 * OrderTreatDetail):付款方式、商品金额(应收金额)、优惠金额(应收金额-实收金额)、支付金额(实收金额)、
	 * 付款时间(订单跟踪表对付款时间的记录)、运费金额
	 */
	/**
	 * step2
	 */
	@Override
	public Object[] getStoreOrderDetailById(String orderId) {
		try {
			StringBuffer hql = new StringBuffer(" select");
			hql.append(" oa.name,oa.provincial,oa.city,oa.area,oa.address,oa.phoneNum,oa.mobile,oa.email,");
			hql.append(" o.payment,o.fare,o.logisticsName,o.logisticsNo,");
			hql.append(" o.invoiceType,o.invoiceTitle,o.invoiceContent,");
			hql.append(" o.totalPrice,o.paid,o.id,o.status,o.shipment,o.userId,o.paymentId,o.invoice,o.productPrice,o.type,o.actualPrice,o.payType ");
			hql.append(" from Order as o,OrderAddress as oa where 1=1");
			// 添加关联条件
			hql.append(" and o.id=oa.orderId");

			// 定义接收参数的Map
			Map<String, Object> params = new HashMap<String, Object>();

			if (StringUtil.isEmpty(orderId)) {
				return null;
			}
			hql.append(" and o.id=:orderId");
			params.put("orderId", orderId);

			// 执行查询
			List list = storeOrderDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回的Object数组
				Object[] returnObj = new Object[2];
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

				order.setInvoice((Boolean) objects[22]);// 是否有发票
				order.setInvoiceType((Boolean) objects[12]); // 发票类型
				order.setInvoiceTitle((String) objects[13]); // 发票抬头
				order.setInvoiceContent((String) objects[14]); // 发票内容

				order.setTotalPrice((BigDecimal) objects[15]); // 应收金额
				order.setPaid((BigDecimal) objects[16]); // 实收金额
				order.setProductPrice((BigDecimal) objects[23]); // 商品总计
				order.setType((Integer) objects[24]); // 订单类型

				order.setId((String) objects[17]); // 订单id
				order.setStatus((String) objects[18]); // 订单状态
				order.setShipment((String) objects[19]); // 配送方式
				order.setUserId((String) objects[20]);
				order.setPaymentId((String) objects[21]);// 配送方式
				order.setActualPrice((BigDecimal) objects[25]);
				order.setPayType((String)objects[26]);
				returnObj[1] = order;

				// 将数组返回
				return returnObj;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// ---------------------------对于已付款订单查询出付款时间START------------------------------
	@Override
	public String getOrderPayTimeOrderId(String orderId) {
		try {
			if (StringUtil.isEmpty(orderId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select otd.createTime from OrderTreatDetail as otd where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and otd.orderId = :orderId");
			params.put("orderId", orderId);
			// 状态为2记录的时间为付款时间
			hql.append(" and otd.type='2'");
			hql.append(" order by otd.createTime DESC");
			List otds = storeOrderDao.list(hql.toString(), params);
			if (otds != null && otds.size() == 1) {
				return (String) otds.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// ----------------------------查询出付款时间END-------------------------------

	/**
	 * step1(非评价用)
	 */
	@Override
	public List<Product> getProductByOrderId(String orderId) {
		try {
			StringBuffer hql = new StringBuffer(" select");
			// hql.append(" p.smallPicture,p.name,p.unitPrice,p.productNumber");
			hql.append(" p.id,p.recommendPicture1,p.name,od.price,p.productNumber,od.quantity,od.totalPrice,od.productInfoDetailId");
			hql.append(" from Product as p,OrderDetail as od where 1=1");
			// 添加关联条件
			hql.append(" and p.id=od.productId");

			// 添加查询条件
			Map<String, Object> params = new HashMap<String, Object>();
			if (!StringUtil.isEmpty(orderId)) {
				hql.append(" and od.orderId=:orderId");
				params.put("orderId", orderId);
			}
			// 执行查询
			List list = storeOrderDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回集合
				List<Product> products = new ArrayList<Product>();

				for (int i = 0; i < list.size(); i++) {
					Object[] objects = (Object[]) list.get(i);
					Product product = new Product();
					product.setId(objects[0].toString());
					product.setSmallPicture((String) objects[1]);// 商品小图
					product.setName((String) objects[2]);// 商品名字
					product.setUnitPrice((BigDecimal) objects[3]);// 商品价格
					product.setProductNumber((String) objects[4]);// 商品编号
					product.setBuyCount((Integer) objects[5]);// 购买数量
					product.setTempProductTotalPrice((BigDecimal) objects[6]);// 小计

					if (null != objects[7] || !"".equals(objects[7]) || !" ".equals(objects[7])) {
						ProductInfoDetail pid = (ProductInfoDetail) this.productInfoDetailDao.get(objects[7].toString());
						List<ProductInfoDetail> pidList = new ArrayList<ProductInfoDetail>();
						pidList.add(pid);
						/*
						 * if(null!=pid.getInfoName1() &&
						 * !"".equals(pid.getInfoName1())){ ProductInfoDetail
						 * pid1 = new ProductInfoDetail();
						 * pid1.setInfoName1(pid.getInfoName1());
						 * pid1.setInfoPic1(pid.getInfoPic1());
						 * pid1.setInfoValue1(pid.getInfoValue1());
						 * pidList.add(pid1); } if(null!=pid.getInfoName2() &&
						 * !"".equals(pid.getInfoName2())){ ProductInfoDetail
						 * pid2 = new ProductInfoDetail();
						 * pid2.setInfoName2(pid.getInfoName2());
						 * pid2.setInfoPic2(pid.getInfoPic2());
						 * pid2.setInfoValue2(pid.getInfoValue2());
						 * pidList.add(pid2); } if(null!=pid.getInfoName3() &&
						 * !"".equals(pid.getInfoName3())){ ProductInfoDetail
						 * pid3 = new ProductInfoDetail();
						 * pid3.setInfoName3(pid.getInfoName3());
						 * pid3.setInfoPic3(pid.getInfoPic3());
						 * pid3.setInfoValue3(pid.getInfoValue3());
						 * pidList.add(pid3); } if(null!=pid.getInfoName4() &&
						 * !"".equals(pid.getInfoName4())){ ProductInfoDetail
						 * pid4 = new ProductInfoDetail();
						 * pid4.setInfoName4(pid.getInfoName4());
						 * pid4.setInfoPic4(pid.getInfoPic4());
						 * pid4.setInfoValue4(pid.getInfoValue4());
						 * pidList.add(pid4); }
						 */

						product.setProductInfoDetails(pidList);
					}

					products.add(product);
				}
				// 返回某条订单所对应的商品
				return products;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 订单跟踪--查询出订餐操作的详情信息
	 */
	@Override
	public List<OrderTreatDetail> findOrderTreatDetailByOrderId(String orderId) {
		try {
			StringBuffer hql = new StringBuffer("select otd from OrderTreatDetail as otd where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtil.isEmpty(orderId)) {
				return null;
			}
			hql.append(" and otd.orderId=:orderId");
			params.put("orderId", orderId);
			hql.append(" order by otd.createTime DESC");

			List<OrderTreatDetail> treatDetails = storeOrderDao.list(hql.toString(), params);
			if (treatDetails != null && treatDetails.size() > 0) {
				return treatDetails;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 商户中心-订单管理-待付款订单修改价格
	 */
	@Override
	public boolean modifyStoreOrderPrice(String orderId, String paid, String createrId) {
		try {
			if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(paid)) {
				return false;
			}
			// 查询出orderId对应的订单信息
			Order order = (Order) storeOrderDao.get(orderId);
			// 为此条订单设置新的价格
			order.setPaid(new BigDecimal(paid));

			// 更新订单
			storeOrderDao.updateBean(order);

			// 跟新订单的时候记录更新的信息到OrderTreatDetail
			OrderTreatDetail treatDetail = new OrderTreatDetail();
			// 获得操作者的名字 由于这里商户中心的店主id与店铺id相同故这样处理
			User user = (User) userDao.get(createrId);
			String creater = user.getName();
			// 获取当前系统时间
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
			String nowTime = sdf.format(date);
			// 设置当前的系统时间到treatDetail
			treatDetail.setCreateTime(nowTime);
			treatDetail.setOrderId(orderId);
			treatDetail.setType(OrderConst.ORDER_TREAT_MODIFY);
			treatDetail.setCreater(creater);
			treatDetail.setNote("修改价格");

			treatDetailDao.addBean(treatDetail);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 待发货订单发货
	 */
	@Override
	public boolean deliveryStoreOrder(String orderId, String storeName, String logisticsName, String logisticsNo, String actualShipping) {
		try {
			if (StringUtil.isEmpty(orderId)) {
				return false;
			}
			// 得到操作的记录
			Order order = (Order) storeOrderDao.get(orderId);
			if (!StringUtil.isEmpty(logisticsName)) {
				// 设置物流公司名字
				order.setLogisticsName(logisticsName);
			}
			// 设置物流单号
			if (!StringUtil.isEmpty(logisticsNo)) {
				order.setLogisticsNo(logisticsNo);
			}
			// 将此订单发货
			order.setShippingStatus(true);
			// 记录实际运费
			if (!StringUtil.isEmpty(actualShipping)) {
				order.setActualPrice(new BigDecimal(actualShipping));
			}
			order.setStatus(OrderConst.ORDER_STATUS_WAIT_REC);
			// 更新订单
			storeOrderDao.updateBean(order);

			// 记录操作日志
			OrderTreatDetail treatDetail = new OrderTreatDetail();
			// 设置当前的系统时间到treatDetail
			treatDetail.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date()));
			treatDetail.setOrderId(orderId);
			treatDetail.setType(OrderConst.ORDER_TREAT_SEND);
			treatDetail.setCreater(storeName);
			treatDetail.setNote("发货操作");
			// 存储操作信息
			treatDetailDao.addBean(treatDetail);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 待评价订单对应的商品
	 */
	@Override
	public List<Product> getProductByOrderId2(String orderId) {
		try {
			StringBuffer hql = new StringBuffer(" select");
			hql.append(" p.smallPicture,p.name,p.unitPrice,p.productNumber,p.id");
			hql.append(" from Product as p,OrderDetail as od where 1=1");
			// 添加关联条件
			hql.append(" and p.id=od.productId");

			// 添加查询条件
			Map<String, Object> params = new HashMap<String, Object>();
			if (!StringUtil.isEmpty(orderId)) {
				hql.append(" and od.orderId=:orderId");
				params.put("orderId", orderId);
			}

			// 执行查询
			List list = storeOrderDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回集合
				List<Product> products = new ArrayList<Product>();

				for (int i = 0; i < list.size(); i++) {
					Object[] objects = (Object[]) list.get(i);
					Product product = new Product();
					product.setSmallPicture((String) objects[0]);// 商品小图
					product.setName((String) objects[1]);// 商品名字
					product.setUnitPrice((BigDecimal) objects[2]);// 商品价格
					product.setProductNumber((String) objects[3]);// 商品编号
					product.setId((String) objects[4]);// 商品Id
					products.add(product);
				}
				// 返回某条订单所对应的商品
				return products;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 商户中心-交易管理-待评价订单评价
	 */
	@Override
	public boolean evaluateStoreOrder(String[] noteArray, String[] typeArray, String[] productIdArray, String orderId, String observer, String target) {
		try {
			if (noteArray != null && typeArray != null && noteArray.length > 0 && noteArray.length == typeArray.length) {
				for (int i = 0; i < noteArray.length; i++) {
					Appraise appraise = new Appraise();
					// 设置评价时间
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
					String nowTime = sdf.format(date);
					appraise.setCreateTime(nowTime);
					// 设置评价人的Id
					appraise.setSource(observer);
					// 设置被评价人的Id
					appraise.setTarget(target);
					// 设置评价类型(好、中、差评)
					appraise.setType(typeArray[i]);
					// 设置评价的具体内容
					appraise.setNote(noteArray[i]);
					// 设置订单id
					appraise.setOrderId(orderId);
					// 设置商品id
					appraise.setProductId(productIdArray[i]);

					appraiseDao.addBean(appraise);
				}
				// 对商品的评价完成后,将订单的状态改为完成状态
				Order order = (Order) storeOrderDao.get(orderId);
				order.setStoreApp(true);
				order.setStatus(OrderConst.ORDER_STATUS_FINISH);
				storeOrderDao.updateBean(order);

				// 记录操作日志
				OrderTreatDetail treatDetail = new OrderTreatDetail();
				// 获得操作者的名字 由于这里商户中心的店主id与店铺id相同故这样处理
				User user = (User) userDao.get(observer);
				String creater = user.getName();
				// 获取当前系统时间
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
				String nowTime = sdf.format(date);
				// 设置当前的系统时间到treatDetail
				treatDetail.setCreateTime(nowTime);
				treatDetail.setOrderId(orderId);
				treatDetail.setType(OrderConst.ORDER_TREAT_STORE_APP);
				treatDetail.setCreater(creater);
				treatDetail.setNote("评价会员");
				// 存储操作信息
				treatDetailDao.addBean(treatDetail);

				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据userId得到User对象
	 */
	@Override
	public User getUserById(String userId) {
		try {
			if (StringUtil.isEmpty(userId)) {
				return null;
			}
			return (User) userDao.get(userId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据Id获取订单信息
	 */
	@Override
	public Order getOrderById(String orderId) {
		try {
			if (StringUtil.isEmpty(orderId)) {
				return null;
			}
			return (Order) storeOrderDao.get(orderId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Order> getCaiStoreOrder(String userId, String orderId, String userName, String beginDate, String endDate, String status, String type, Pager pager) {
		try {
			StringBuffer hql = new StringBuffer("select o.id,o.status,o.createTime,o.paid,s.storeName,");
			hql.append(" o.shippingStatus,o.userApp,o.storeApp,o.fare,o.totalPrice");
			hql.append(" from Order as o,Store as s where 1=1");

			// 定义存储查询条件的Map
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and o.storeId = s.id");
			// 添加当前的店铺id
			if (StringUtil.isEmpty(userId)) {
				return null;
			}
			hql.append(" and o.userId =:userId");
			params.put("userId", userId);

			if (!StringUtil.isEmpty(orderId)) {
				hql.append(" and o.id=:orderId");
				params.put("orderId", orderId);
			}
			if (!StringUtil.isEmpty(type)) {
				hql.append(" and o.type=:type");
				params.put("type", Integer.valueOf(type));
			}
			if (!StringUtil.isEmpty(userName)) {
				hql.append(" and s.storeName like:userName");
				params.put("userName", "%" + userName + "%");
			}
			if (!StringUtil.isEmpty(beginDate)) {
				hql.append(" and o.createTime >=:beginDate");
				params.put("beginDate", beginDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				hql.append(" and o.createTime <=:endDate");
				params.put("endDate", endDate);
			}

			if (!StringUtil.isEmpty(status)) {
				hql.append(" and o.status=:status");
				params.put("status", status);
			}
			// 添加排序
			hql.append(" order by o.createTime DESC");

			// 执行查询
			List list = storeOrderDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 定义返回的集合
				List<Order> orders = new ArrayList<Order>();
				for (int i = 0; i < list.size(); i++) {
					// 定义接收数据的容器
					Order order = new Order();
					Object[] objects = (Object[]) list.get(i);

					order.setId((String) objects[0]);
					order.setStatus((String) objects[1]);
					order.setCreateTime((String) objects[2]);

					order.setPaid((BigDecimal) objects[3]);
					// 这里将用户名保存在userId字段
					order.setUserId((String) objects[4]);
					order.setShippingStatus((Boolean) objects[5]);
					order.setUserApp((Boolean) objects[6]);
					order.setStoreApp((Boolean) objects[7]);
					order.setFare((BigDecimal) objects[8]);
					order.setTotalPrice((BigDecimal) objects[9]);

					orders.add(order);
				}
				return orders;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public String getOrderSum(String storeId, String userId, String type, String status) {
		StringBuffer hql = new StringBuffer("select o.id from Order as o where 1 = 1 ");
		// 定义存储查询条件的Map
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(storeId)) {
			hql.append(" and o.storeId =:storeId");
			params.put("storeId", storeId);
		}
		if (!StringUtil.isEmpty(userId)) {
			hql.append(" and o.userId =:userId");
			params.put("userId", userId);
		}
		if (!StringUtil.isEmpty(status)) {
			hql.append(" and o.status =:status");
			params.put("status", status);
		}
		if (!StringUtil.isEmpty(type)) {
			hql.append(" and o.type =:type");
			params.put("type", Integer.valueOf(type));
		}
		try {
			List list = storeOrderDao.list(hql.toString(), params);
			return list.size() + "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
