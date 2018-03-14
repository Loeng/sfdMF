package com.ekfans.base.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderValuationDao;
import com.ekfans.base.order.dao.IOrderWfpDao;
import com.ekfans.base.order.model.OrderValuation;
import com.ekfans.base.order.model.OrderWfp;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.wfOrder.dao.IContractDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OrderWfpService implements IOrderWfpService {
	@Autowired
	private IOrderWfpDao orderWfpDao;

	@Autowired
	private IOrderValuationDao orderValuationDao;

	@Autowired
	private IContractDao contractDao;

	@Autowired
	private IStoreService storeService;
	@Autowired
	private IUserDao userDao;

	/**
	 * 添加订单
	 */
	@Override
	public boolean addOrderWfp(OrderWfp wfp) {
		// 定义Session
		Session session = null;
		// 定义事物处理
		Transaction transaction = null;
		try {
			session = orderWfpDao.createSession();
			transaction = session.beginTransaction();
			orderWfpDao.addBean(wfp, session);
			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 根据条件查询危废品订单 storeId
	 */
	@Override
	public List<OrderWfp> getOrderWfpByParams(Pager pager, Store store,
			String orgId, String orderNumber, String orderStatus,
			String startTime, String endTime, String wfpName, String isType,
			String type) {
		List<OrderWfp> orderList = new ArrayList<OrderWfp>();
		String isYs = "0";
		// 获取登录账户的User基础信息
		User u = new User();
		try {
			u = (User) userDao.get(store.getId());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 定义存储查询条件的Map
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer(
				"select o.id,o.orderNumber,o.wfpName,o.wfpNumber,o.totalPrice,o.status,o.createTime,o.unit,o.price,o.storeId,c.firstId,c.secondId,c.partyAORpartyB,o.shoppingStatus,o.threeCouplet,o.ysStoreId,o.buyStoreId from OrderWfp o,Contract c where 1=1 ");

		if (u != null) {
			// 判断是否是运输企业
			if (u.getType().equals("4")) {
				buffer.append(" and  c.id = o.ysStoreId");
				isYs = "1";
			} else {
				if (!StringUtil.isEmpty(type)) {
					if (type.equals("1")) {
						buffer.append(" and o.storeId='" + store.getId() + "' ");
					} else {
						buffer.append(" and o.buyStoreId ='" + store.getId()
								+ "' ");
					}
				}
				buffer.append(" and c.id = o.contractId");
			}
		}
		// 判断是否是子企业
		if (!StringUtil.isEmpty(orgId)) {
			buffer.append(" and (o.orgId =:orgId or c.firstId=:storeId  or c.secondId =:storeId)");
			params.put("orgId", orgId);
		} else {
			buffer.append(" and( o.storeId =:storeId or c.firstId=:storeId  or c.secondId =:storeId)");
			params.put("storeId", store.getId());
		}
		// 判断订单ID
		if (!StringUtil.isEmpty(orderNumber)) {
			buffer.append(" and o.orderNumber =:orderNumber");
			params.put("orderNumber", orderNumber);
		}
		// 判断订单状态
		if (!StringUtil.isEmpty(orderStatus)) {
			buffer.append(" and o.status =:orderStatus");
			params.put("orderStatus", orderStatus);
		}
		// 判断危废品名称是否为空
		if (!StringUtil.isEmpty(wfpName)) {
			buffer.append(" and o.wfpName like :wfpName");
			params.put("wfpName", "%" + wfpName + "%");
		}
		// 判断订单时间
		if (!StringUtil.isEmpty(startTime)) {
			buffer.append(" and o.createTime >=:startTime");
			params.put("startTime", startTime);
		}
		if (!StringUtil.isEmpty(endTime)) {
			buffer.append(" and o.createTime <=:endTime");
			params.put("endTime", endTime);
		}
		// 根据创建时间倒序
		buffer.append(" order by o.createTime desc  ");
		try {
			List<Object[]> list = orderWfpDao.list(pager, buffer.toString(),
					params);
			if (list != null && list.size() > 0) {
				for (Object[] obj : list) {
					String first = (String) obj[10];
					// String second = (String) obj[11];
					String aOrb = (String) obj[12];
					// 记录乙方ID
					String sec = "";
					// 如果是运输企业
					if (isYs == "1") {
						sec = "ys";
					}
					// 判断是否是处置企业查询列表
					if (!StringUtil.isEmpty(isType)) {
						if ((first.equals(store.getId()) && aOrb.equals("0"))
								|| (sec.equals(store.getId()) && aOrb
										.equals("1"))) {
							OrderWfp wfp = new OrderWfp();
							wfp.setId((String) obj[0]);
							wfp.setOrderNumber((String) obj[1]);
							wfp.setWfpName((String) obj[2]);
							wfp.setWfpNumber((String) obj[3]);
							wfp.setTotalPrice((String) obj[4]);
							wfp.setStatus((String) obj[5]);
							wfp.setCreateTime((String) obj[6]);
							wfp.setUnit((String) obj[7]);
							wfp.setPrice((String) obj[8]);
							wfp.setStoreId((String) obj[9]);
							wfp.setShoppingStatus((String) obj[13]);
							wfp.setThreeCouplet((String) obj[14]);
							wfp.setYsStoreId((String) obj[15]);
							wfp.setBuyStoreId((String) obj[16]);
							wfp.setSecond(sec);
							orderList.add(wfp);
						}
					} else {
						OrderWfp wfp = new OrderWfp();
						wfp.setId((String) obj[0]);
						wfp.setOrderNumber((String) obj[1]);
						wfp.setWfpName((String) obj[2]);
						wfp.setWfpNumber((String) obj[3]);
						wfp.setTotalPrice((String) obj[4]);
						wfp.setStatus((String) obj[5]);
						wfp.setCreateTime((String) obj[6]);
						wfp.setUnit((String) obj[7]);
						wfp.setPrice((String) obj[8]);
						wfp.setStoreId((String) obj[9]);
						wfp.setShoppingStatus((String) obj[13]);
						wfp.setThreeCouplet((String) obj[14]);
						wfp.setYsStoreId((String) obj[15]);
						wfp.setBuyStoreId((String) obj[16]);
						wfp.setSecond(sec);
						orderList.add(wfp);
					}
				}
			}
			return orderList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据ID查询危废品详情
	 */
	@Override
	public OrderWfp getOrderDtailById(String orderId, Store s) {
		try {
			if (!StringUtil.isEmpty(orderId)) {
				OrderWfp wfp = (OrderWfp) orderWfpDao.get(orderId);
				// 根据订单的ID查询订单含量
				StringBuffer buf = new StringBuffer(
						" from OrderValuation ov where ov.orderId='"
								+ wfp.getScrapId() + "'");
				List<OrderValuation> li = orderValuationDao.list(
						buf.toString(), null);
				if (li == null || li.size() == 0)
					return wfp;
				wfp.setValationList(li);
				// 根据订单合同ID查询合同路径
				Contract con = (Contract) contractDao.get(wfp.getContractId());
				wfp.setContractPath(con.getFile());
				// 如果运输企业ID不为空
				if (!StringUtil.isEmpty(wfp.getYsStoreId())) {
					// 获取运输合同
					Contract yscon = (Contract) contractDao.get(wfp
							.getYsStoreId());
					// 查询运输企业的基础信息
					User u = (User) userDao.get(yscon.getFirstId());
					String id = "";
					// 判断是否是运输企业
					if (u.getType().equals("4")) {
						id = u.getId();
					} else {
						User uu = (User) userDao.get(yscon.getSecondId());
						id = uu.getId();
					}
					// 查询运输企业的资料
					Store ss = storeService.getStore(id);
					if (ss != null) {
						wfp.setYsName(ss.getStoreName());
					}
				}
				return wfp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新危废品信息
	 */
	@Override
	public boolean updateOrder(OrderWfp wfp) {
		try {
			orderWfpDao.updateBean(wfp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<OrderWfp> getAllOrderList(Pager pager, String orderNumber,
			String orderStatus, String wfpName, String startTime, String endTime) {
		List<OrderWfp> orderList = new ArrayList<OrderWfp>();
		// 定义存储查询条件的Map
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer(
				"select o.id,o.orderNumber,o.wfpName,o.wfpNumber,o.totalPrice,o.status,o.createTime,o.unit,o.price,o.storeId,c.firstId,c.secondId,c.partyAORpartyB,o.shoppingStatus,o.threeCouplet,o.ysStoreId from OrderWfp o,Contract c where  c.id = o.contractId ");
		// 判断订单ID
		if (!StringUtil.isEmpty(orderNumber)) {
			buffer.append(" and o.orderNumber =:orderNumber");
			params.put("orderNumber", orderNumber);
		}
		// 判断订单状态
		if (!StringUtil.isEmpty(orderStatus)) {
			buffer.append(" and o.status =:orderStatus");
			params.put("orderStatus", orderStatus);
		}
		// 判断危废品名称是否为空
		if (!StringUtil.isEmpty(wfpName)) {
			buffer.append(" and o.wfpName like :wfpName");
			params.put("wfpName", "%" + wfpName + "%");
		}
		// 判断订单时间
		if (!StringUtil.isEmpty(startTime)) {
			buffer.append(" and o.createTime >=:startTime");
			params.put("startTime", startTime);
		}
		if (!StringUtil.isEmpty(endTime)) {
			buffer.append(" and o.createTime <=:endTime");
			params.put("endTime", endTime);
		}
		// 根据创建时间倒序
		buffer.append(" order by o.createTime desc");
		try {
			List<Object[]> list = orderWfpDao.list(pager, buffer.toString(),
					params);
			if (list != null && list.size() > 0) {
				for (Object[] obj : list) {
					OrderWfp wfp = new OrderWfp();
					wfp.setId((String) obj[0]);
					wfp.setOrderNumber((String) obj[1]);
					wfp.setWfpName((String) obj[2]);
					wfp.setWfpNumber((String) obj[3]);
					wfp.setTotalPrice((String) obj[4]);
					wfp.setStatus((String) obj[5]);
					wfp.setCreateTime((String) obj[6]);
					wfp.setUnit((String) obj[7]);
					wfp.setPrice((String) obj[8]);
					wfp.setStoreId((String) obj[9]);
					wfp.setShoppingStatus((String) obj[13]);
					wfp.setThreeCouplet((String) obj[14]);
					wfp.setYsStoreId((String) obj[15]);
					orderList.add(wfp);

				}
			}
			return orderList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getOrderSum(String storeId, String status) {
		StringBuffer hql = new StringBuffer(
				"select o.id from OrderWfp as o where 1 = 1 ");
		// 定义存储查询条件的Map
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(storeId)) {
			hql.append(" and o.ysStoreId =:storeId");
			params.put("storeId", storeId);
		}

		if (!StringUtil.isEmpty(status)) {
			hql.append(" and o.status =:status");
			params.put("status", status);
		}

		try {
			List list = orderWfpDao.list(hql.toString(), params);
			return list.size() + "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public OrderWfp getOrderDtailByOrderId(String orderId, Store s) {
		if (StringUtil.isEmpty(orderId)) {
			return null;
		}

		try {
			String hql = "from OrderWfp where orderNumber='" + orderId + "'";
			List<OrderWfp> list = orderWfpDao.list(hql, null);
			if (list == null || list.size() <= 0) {
				return null;
			}

			OrderWfp wfp = list.get(0);

			// 根据订单的ID查询订单含量
			String hql1 = "from OrderValuation ov where ov.orderId='"
					+ wfp.getId() + "'";
			List<OrderValuation> li = orderValuationDao.list(hql1, null);

			if (li == null || li.size() <= 0) {
				return wfp;
			}
			wfp.setValationList(li);

			// 根据订单合同ID查询合同路径
			Contract con = (Contract) contractDao.get(wfp.getContractId());
			wfp.setContractPath(con.getFile());

			// 如果运输企业ID不为空
			if (!StringUtil.isEmpty(wfp.getYsStoreId())) {
				// 获取运输合同
				Contract yscon = (Contract) contractDao.get(wfp.getYsStoreId());
				// 查询运输企业的基础信息
				User u = (User) userDao.get(yscon.getFirstId());

				String id = "";
				// 判断是否是运输企业
				if (u.getType().equals("4")) {
					id = u.getId();
				} else {
					User uu = (User) userDao.get(yscon.getSecondId());
					id = uu.getId();
				}
				// 查询运输企业的资料
				Store ss = storeService.getStore(id);
				if (ss != null) {
					wfp.setYsName(ss.getStoreName());
				}
			}

			return wfp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 测试报表方法
	 */
	@Override
	public List<String> getAll() {
		List<String> list = new ArrayList<String>();
		String hql = "select w.deliveData,count(w.deliveData) as total from OrderWfp w group by w.deliveData";
		try {
			List<Object[]> li = orderWfpDao.list(hql, null);
			for (Object[] obj : li) {
				list.add((String) obj[0] + "," + (String) obj[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
