package com.ekfans.base.order.service;

import com.ekfans.base.order.dao.*;
import com.ekfans.base.order.model.*;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccountLogService;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.wfOrder.model.OrderPayLog;
import com.ekfans.base.wfOrder.service.IOrderPayLogService;
import com.ekfans.plugin.number.NoManager;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ekfans.basic.spring.SpringContextHolder.getBean;

/**
 * 订单Service接口实现
 *
 * @author lgy
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * Company:成都易科远见科技有限公司 www.ekfans.com
 * @ClassName: OrderService
 * @Description:
 * @date 2014-9-20 上午11:55:14
 */
@Service
public class OrderService implements IOrderService {

    private Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderAddressDao orderAddressDao;
    @Autowired
    private IOrderDetailDao orderDetailDao;
    @Autowired
    private IOrderTreatDetailDao treatDetailDao;
    @Autowired
    private IStoreDao storeDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IAppraiseDao appraiseDao;
    @Autowired
    private IOrderWfpDao orderWfpDao;
    @Autowired
    private IOrderLogDao orderLogDao;

    @SuppressWarnings("unchecked")
    @Override
    public List<Order> listOrder(Pager pager, String orderNum, String beginDate, String endDate, String beginPrice, String endPrice, String shippingStatus, int type) {
        // 定义参数Map
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 定义hql
        StringBuffer hql = new StringBuffer("select o,s.storeName,u.name from Order o,Store s,User u where o.storeId=s.id and o.userId=u.id and o.type=:type");
        // 设置参数
        paramMap.put("type", type);
        if (!StringUtil.isEmpty(shippingStatus)) {
            hql.append(" and o.shippingStatus=:shippingStatus");
            paramMap.put("shippingStatus", Boolean.parseBoolean(shippingStatus));
        }
        if (!StringUtil.isEmpty(orderNum)) {
            hql.append(" and o.id like :orderNum");
            paramMap.put("orderNum", "%" + orderNum + "%");
        }
        if (!StringUtil.isEmpty(beginDate)) {
            hql.append(" and o.createTime>=:beginDate");
            paramMap.put("beginDate", beginDate);
        }
        if (!StringUtil.isEmpty(endDate)) {
            hql.append(" and o.createTime<=:endDate");
            paramMap.put("endDate", endDate);
        }
        if (!StringUtil.isEmpty(beginPrice)) {
            hql.append(" and o.totalPrice>=:beginPrice");
            paramMap.put("beginPrice", new BigDecimal(beginPrice));
        }
        if (!StringUtil.isEmpty(endPrice)) {
            hql.append(" and o.totalPrice<=:endPrice");
            paramMap.put("endPrice", new BigDecimal(endPrice));
        }
        hql.append(" order by o.createTime desc");

        try {
            List<Object[]> list = this.orderDao.list(pager, hql.toString(), paramMap);
            if (list != null && list.size() > 0) {
                List<Order> olist = new ArrayList<Order>();
                for (Object[] obj : list) {
                    Order o = (Order) obj[0];
                    // 卖家名称放入storeId
                    o.setStoreId(obj[1].toString());
                    // 买家名称放入userId
                    o.setUserId(obj[2].toString());

                    olist.add(o);
                }

                return olist;
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Title: getPayOrderList @Description: TODO(这里用一句话描述这个方法的作用)
     * 详细业务流程:后台查询wfp订单 (详细描述此方法相关的业务处理流程) @param pager @param
     * orderNumber @param startTime @param endTime @return List<OrderWfp>
     * 返回类型 @throws
     */
    public List<Order> getPayOrderList(Pager pager, int orderType, String userId, String storeId, String buyName, String salName, String orderNumber, String startTime, String endTime) {

        // 定义参数Map
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 定义hql
        StringBuffer hql = new StringBuffer("select o,s.storeName,s2.storeName from Order o,Store s,Store s2 where o.userId=s.id and o.storeId=s2.id and o.type=:type");
        // 设置参数
        paramMap.put("type", orderType);

        if (!StringUtil.isEmpty(orderNumber)) {
            hql.append(" and o.id like :orderNum");
            paramMap.put("orderNum", "%" + orderNumber + "%");
        }

        if (!StringUtil.isEmpty(buyName)) {
            hql.append(" and s.storeName like :buyName");
            paramMap.put("buyName", "%" + buyName + "%");
        }

        if (!StringUtil.isEmpty(salName)) {
            hql.append(" and s2.storeName like :salName");
            paramMap.put("salName", "%" + salName + "%");
        }

        if (!StringUtil.isEmpty(userId)) {
            hql.append(" and o.userId = :userId");
            paramMap.put("userId", userId);
        }

        if (!StringUtil.isEmpty(storeId)) {
            hql.append(" and o.storeId = :storeId");
            paramMap.put("storeId", storeId);
        }

        if (!StringUtil.isEmpty(startTime)) {
            hql.append(" and o.createTime>=:beginDate");
            paramMap.put("beginDate", startTime);
        }
        if (!StringUtil.isEmpty(endTime)) {
            hql.append(" and o.createTime<=:endDate");
            paramMap.put("endDate", endTime);
        }
        hql.append(" order by o.createTime desc");

        try {
            List<Object[]> list = this.orderDao.list(pager, hql.toString(), paramMap);
            if (list != null && list.size() > 0) {
                List<Order> olist = new ArrayList<Order>();
                for (Object[] obj : list) {
                    Order o = (Order) obj[0];
                    // 卖家名称放入storeId
                    o.setBuyName(obj[1].toString());
                    // 买家名称放入userId
                    o.setSalName(obj[2].toString());
                    olist.add(o);
                }

                return olist;
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 客服下单 新增订单(User中心新增订单、后台客服下单) 第一部分: Order相应的信息 第二部分: OrderDetail相应的信息
     * 第三部分: OrderAddress部分信息 第四部分: OrderTreatDetail部分信息
     */
    @Override
    public boolean add(Order order, OrderAddress orderAddress, List<OrderDetail> detailList, OrderTreatDetail orderTreatDetail) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = orderDao.createSession();
            transaction = session.beginTransaction();

            String systemTime = DateUtil.getSysDateTimeString();
            // 得到orderId
            NoManager no = new NoManager();
            String orderId = no.createOrderId();
            String bankOrderId = orderId.substring(2, orderId.length());
            bankOrderId = bankOrderId.substring(0, 5) + bankOrderId.substring(bankOrderId.length() - 4, bankOrderId.length());
            // Order部分的处理
            order.setCreateTime(systemTime);
            order.setBankOrderId(bankOrderId);
            order.setId(orderId);
            orderDao.addBean(order, session);

            if (detailList != null && detailList.size() > 0) {
                // OrderDetail部分的处理
                for (OrderDetail detail : detailList) {
                    if (detail != null) {
                        detail.setOrderId(orderId);
                        orderDetailDao.addBean(detail, session);
                    }
                }
            }

            if (orderAddress != null) {
                // OrderAddress部分的处理
                orderAddress.setOrderId(orderId);
                orderAddressDao.addBean(orderAddress);
            }

            if (orderTreatDetail != null) {
                // OrderTreatDetail部分的处理
                orderTreatDetail.setCreateTime(systemTime);
                orderTreatDetail.setOrderId(orderId);
                treatDetailDao.addBean(orderTreatDetail);
            }

            OrderLog orderLog = new OrderLog();
            orderLog.setCreateTime(systemTime);
            orderLog.setCreatorName(order.getNickName());
            orderLog.setCreator(order.getUserId());
            orderLog.setNote("创建订单");
            orderLog.setOrderId(order.getId());
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }

    /**
     * @Title: changeOrderStatus @Description: TODO(User中心新增订单、后台客服下单) 详细业务流程:
     * (作为后台客服下单的一个功能模块涉及的操作有 第一部分: Order相应的信息 第二部分:
     * OrderDetail相应的信息(orderDetail部分由关联商品得到 ,通过关联商品的productId集合,完成订单) 第三部分:
     * OrderAddress部分信息 第四部分: OrderTreatDetail部分信息 ) @param @param
     * order @param @return 设定文件 @return boolean 返回类型 @throws
     */
    public boolean changePayOrderStatus(Order order, String status) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = orderDao.createSession();
            transaction = session.beginTransaction();

            String systemTime = DateUtil.getSysDateTimeString();
            order.setStatus(status);
            orderDao.updateBean(order, session);

            OrderLog orderLog = new OrderLog();
            orderLog.setCreateTime(systemTime);
            orderLog.setCreatorName(order.getNickName());
            orderLog.setCreator(order.getUserId());

            String treatTtype = "";
            String note = "";
            if (OrderConst.ORDER_STATUS_WAIT_PAY.equals(status)) {
                treatTtype = OrderConst.ORDER_TREAT_ORDER_SURE;
                note = "确认订单信息";
            } else if (OrderConst.ORDER_STATUS_WAIT_PAY_SURE.equals(status)) {
                note = "支付订单，等待收款方确认收款信息";
                treatTtype = OrderConst.ORDER_TREAT_PAY_SURE;
            } else if (OrderConst.ORDER_STATUS_CLOSE.equals(status)) {
                note = "取消订单";
                treatTtype = OrderConst.ORDER_TREAT_CLOSE_ORDER;
            } else if (OrderConst.ORDER_STATUS_WAIT_SEND.equals(status)) {
                note = "买家已付款，待卖家发货";
                treatTtype = OrderConst.ORDER_TREAT_PAY;
            }

            if (OrderConst.ORDER_STATUS_FINISH.equals(status)) {
                if (order.getPayType().equals(OrderConst.ORDER_PAY_TYPE_UNLINE)) {
                    note = "确认收款信息并结束订单";
                    treatTtype = OrderConst.ORDER_TREAT_OK;
                } else {
                    note = "完成订单支付并结束订单";
                    treatTtype = OrderConst.ORDER_TREAT_OK;
                }
            }
            orderLog.setNote(note);
            orderLog.setOrderId(order.getId());
            orderLogDao.addBean(orderLog, session);

            OrderTreatDetail treat = new OrderTreatDetail();
            treat.setCreater(order.getNickName());
            treat.setCreateTime(DateUtil.getSysDateTimeString());
            treat.setNote(note);
            treat.setNowPrice(order.getTotalPrice());
            treat.setOrderId(order.getId());
            treat.setType(treatTtype);
            treat.setOriginalPrice(order.getProductPrice());
            treatDetailDao.addBean(treat, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }

    /**
     * @Title: update @Description: TODO(User中心新增订单、后台客服下单) 详细业务流程:
     * (作为后台客服下单的一个功能模块涉及的操作有 第一部分: Order相应的信息 第二部分:
     * OrderDetail相应的信息(orderDetail部分由关联商品得到 ,通过关联商品的productId集合,完成订单) 第三部分:
     * OrderAddress部分信息 第四部分: OrderTreatDetail部分信息 ) @param @param
     * order @param @return 设定文件 @return boolean 返回类型 @throws
     */
    public boolean update(Order order, OrderAddress orderAddress, List<OrderDetail> detailList, OrderTreatDetail orderTreatDetail) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = orderDao.createSession();
            transaction = session.beginTransaction();

            String systemTime = DateUtil.getSysDateTimeString();
            if (order == null || StringUtil.isEmpty(order.getId())) {
                return false;
            }
            // Order部分的处理
            order.setCreateTime(systemTime);
            orderDao.updateBean(order, session);

            if (detailList != null && detailList.size() > 0) {
                // OrderDetail部分的处理
                for (OrderDetail detail : detailList) {
                    if (detail != null) {
                        detail.setOrderId(order.getId());
                        orderDetailDao.updateBean(detail, session);
                    }
                }
            }

            if (orderAddress != null) {
                // OrderAddress部分的处理
                orderAddress.setOrderId(order.getId());
                orderAddressDao.updateBean(orderAddress);
            }

            if (orderTreatDetail != null) {
                // OrderTreatDetail部分的处理
                orderTreatDetail.setCreateTime(systemTime);
                orderTreatDetail.setOrderId(order.getId());
                treatDetailDao.updateBean(orderTreatDetail);
            }

            OrderLog orderLog = new OrderLog();
            orderLog.setCreateTime(systemTime);
            orderLog.setCreatorName(order.getNickName());
            orderLog.setCreator(order.getUserId());
            orderLog.setNote("修改订单");
            orderLog.setOrderId(order.getId());
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }

    /**
     * 查询出订单表展示的总页数
     */
    @Override
    public int findOrderTotalPage(Pager pager, String storeId, String userId, String status) {
        // 获得每页显示的记录数
        int rowsPerPage = pager.getRowsPerPage();
        // 定义SQL
        StringBuffer sql = new StringBuffer(" select o from Order as o where 1=1");

        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果查询条件输入了status，添加查询条件
        if (!StringUtil.isEmpty(status)) {
            sql.append(" and o.status = :status");
            paramMap.put("status", Boolean.parseBoolean(status));
        }

        // 如果查询条件输入了userId，添加查询条件
        if (!StringUtil.isEmpty(userId)) {
            sql.append(" and o.userId = :userId");
            paramMap.put("userId", userId);
        }

        // 如果查询条件输入了storeId,添加查询条件
        if (!StringUtil.isEmpty(storeId)) {
            sql.append(" and o.storeId = :storeId");
            paramMap.put("storeId", storeId);
        }

        try {
            List<Order> orders = orderDao.list(sql.toString(), paramMap);
            if (orders.size() % rowsPerPage == 0) {
                return orders.size() / rowsPerPage;
            } else {
                return orders.size() / rowsPerPage + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 订单id效验
     */
    public boolean checkId(String id) {
        try {
            if (orderDao.get(id) != null) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据商店名 查询出商店id
     */
    @Override
    public String getStoreIdByName(String storeName) {
        StringBuffer hql = new StringBuffer("select s.id from Store as s where 1=1");
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(storeName)) {
            hql.append(" and s.storeName = :storeName");
            params.put("storeName", storeName);
        }
        try {
            List list = storeDao.list(hql.toString(), params);
            if (list.size() > 0) {
                return (String) list.get(0);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据用户名查询出用户id
     */
    @Override
    public String getUserIdByName(String userName) {
        StringBuffer hql = new StringBuffer("select u.id from User as u where 1=1");
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(userName)) {
            hql.append(" and u.name=:userName");
            params.put("userName", userName);
        }
        try {
            List list = userDao.list(hql.toString(), params);
            if (list.size() > 0) {
                return (String) list.get(0);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 查询出某个订单的详情信息
     */
    @Override
    public Object[] getOrderDetailById(String id) {
        try {
            StringBuffer hql = new StringBuffer("select o.id,o.totalPrice,o.paid,oa.provincial,oa.city,oa.area,oa.address,oa.phoneNum,oa.mobile," + "oa.email,oa.zipCode,s.storeName,u.name,od.productName,od.quantity,od.price,o.createTime,o.shippingStatus,oa.name "
                    + "from Order as o,OrderDetail as od,OrderAddress as oa,User as u,Store as s " + "where o.id = od.orderId and o.id=oa.orderId and od.storeId = s.id and od.userId=u.id");
            Map<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(id)) {
                hql.append(" and o.id =:id");
                params.put("id", id);
            }
            List list = orderDao.list(hql.toString(), params);
            if (list.size() > 0) {
                // 定义返回的对象数组
                Object[] returnObjs = new Object[5];
                Object[] objects = (Object[]) list.get(0);
                Order order = new Order();
                order.setId((String) objects[0]);
                order.setTotalPrice((BigDecimal) objects[1]);
                order.setPaid((BigDecimal) objects[2]);
                order.setCreateTime((String) objects[16]);
                order.setShippingStatus((Boolean) objects[17]);
                returnObjs[0] = order;

                OrderAddress orderAddress = new OrderAddress();
                orderAddress.setProvincial((String) objects[3]);
                orderAddress.setCity((String) objects[4]);
                orderAddress.setArea((String) objects[5]);
                orderAddress.setAddress((String) objects[6]);
                orderAddress.setPhoneNum((String) objects[7]);
                orderAddress.setMobile((String) objects[8]);
                orderAddress.setEmail((String) objects[9]);
                orderAddress.setZipCode((String) objects[10]);
                orderAddress.setName((String) objects[18]);
                returnObjs[1] = orderAddress;

                returnObjs[2] = (String) objects[11];
                returnObjs[3] = (String) objects[12];

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProductName((String) objects[13]);
                orderDetail.setQuantity((Integer) objects[14]);
                orderDetail.setPrice((BigDecimal) objects[15]);
                returnObjs[4] = orderDetail;

                return returnObjs;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 查询订单列表
     */
    public List<Order> getMyOrders(Pager pager, String userId, String status, String shippingStatus, String userApp) {
        try {
            StringBuffer sql = new StringBuffer("select o.id,o.createTime,o.totalPrice,u.name,");
            sql.append(" o.status,o.shippingStatus,o.userApp,o.storeApp,o.serviceStatus,o.fare,o.paid");
            sql.append(" from Order as o,User as u where 1=1");
            sql.append(" and o.userId = u.id");

            Map<String, Object> map = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(userId)) {
                sql.append(" and o.userId = :userId");
                map.put("userId", userId);
            }
            if (!StringUtil.isEmpty(status)) {
                sql.append(" and o.status = :status");
                map.put("status", status);
            }
            if (!StringUtil.isEmpty(shippingStatus)) {
                sql.append(" and o.shippingStatus = :shippingStatus");
                map.put("shippingStatus", Boolean.parseBoolean(shippingStatus));
            }
            if (!StringUtil.isEmpty(userApp)) {
                sql.append(" and o.userApp =:userApp ");
                map.put("userApp", Boolean.parseBoolean(userApp));
            }
            sql.append(" order by o.createTime DESC");

            List<Object[]> orders = orderDao.list(pager, sql.toString(), map);
            if (orders != null && orders.size() > 0) {
                // 定义返回集合
                List<Order> os = new ArrayList<Order>();
                for (int i = 0; i < orders.size(); i++) {
                    Order o = new Order();
                    Object[] objs = (Object[]) orders.get(i);
                    o.setId((String) objs[0]);
                    o.setCreateTime((String) objs[1]);
                    o.setTotalPrice((BigDecimal) objs[2]);
                    o.setName((String) objs[3]);
                    o.setStatus((String) objs[4]);
                    o.setShippingStatus((Boolean) objs[5]);
                    o.setUserApp((Boolean) objs[6]);
                    o.setStoreApp((Boolean) objs[7]);
                    o.setServiceStatus((String) objs[8]);
                    o.setFare((BigDecimal) objs[9]);
                    o.setPaid((BigDecimal) objs[10]);
                    List<OrderDetail> ods = this.getOrderDetailsByOrderId((String) objs[0]);
                    o.setDetails(ods);

                    os.add(o);
                }
                return os;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * @Title: getOrderDetailByOrderId @Description: TODO(获取订单详情) 详细业务流程:
     * (详细描述此方法相关的业务处理流程) @param @param orderId @param @param
     * myDaom @param @param mylog @param @return 设定文件 @return List<OrderDetail>
     * 返回类型 @throws
     */
    private List<OrderDetail> getOrderDetailsByOrderId(String orderId) {
        try {
            if (StringUtil.isEmpty(orderId)) {
                return null;
            }
            StringBuffer hql = new StringBuffer("select od.orderId,od.storeId,od.userId,od.quantity,od.price,od.totalPrice,");
            hql.append(" od.productId,od.productName,od.productImage,od.productImage,od.productInfoDetailId,od.infoName1,od.infoName2,od.infoName3,od.infoName4,od.infoValue1,od.infoValue2,od.infoValue3,od.infoValue4 ");
            hql.append(" from OrderDetail as od where 1=1");
            hql.append(" and od.orderId = :orderId");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", orderId);
            List<Object[]> ods = this.orderDetailDao.list(hql.toString(), params);
            if (ods != null && ods.size() > 0) {
                // 定义返回集合
                List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
                for (int i = 0; i < ods.size(); i++) {
                    OrderDetail od = new OrderDetail();
                    Object[] objs = (Object[]) ods.get(i);
                    od.setOrderId((String) objs[0]);
                    od.setStoreId((String) objs[1]);
                    od.setUserId((String) objs[2]);
                    od.setQuantity((Integer) objs[3]);
                    od.setPrice((BigDecimal) objs[4]);
                    od.setTotalPrice((BigDecimal) objs[5]);
                    od.setProductId((String) objs[6]);
                    od.setProductName((String) objs[7]);
                    od.setPicture((String) objs[8]);
                    od.setSmallPicture((String) objs[9]);
                    od.setProductInfoDetailId((String) objs[10]);
                    od.setInfoName1((String) objs[11]);
                    od.setInfoName2((String) objs[12]);
                    od.setInfoName3((String) objs[13]);
                    od.setInfoName4((String) objs[14]);
                    od.setInfoValue1((String) objs[15]);
                    od.setInfoValue2((String) objs[16]);
                    od.setInfoValue3((String) objs[17]);
                    od.setInfoValue4((String) objs[18]);
                    orderDetails.add(od);
                }
                return orderDetails;
            }
        } catch (Exception e) {
            this.log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 新增订单
     */
    @Override
    public boolean addOrder(Order order) {
        try {
            if (order != null) {
                orderDao.addBean(order);
                String bankOrderId = order.getId().substring(2, order.getId().length());
                bankOrderId = bankOrderId.substring(0, 6) + bankOrderId.substring(bankOrderId.length() - 4, bankOrderId.length());
                order.setBankOrderId(bankOrderId);
                orderDao.updateBean(order);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据orderId取得order对象
     */

    public Order getOrderByOrderId(String id) {
        if (id == null) {
            return null;
        }
        try {
            Order order = (Order) orderDao.get(id);
            return order;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 修改订单
     */
    @Override
    public boolean modifyOrder(Order order) {
        if (order == null) {
            return false;
        }
        try {
            orderDao.updateBean(order);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据orderId取得Order对象
     */
    @Override
    public Order getOrderId(String orderId) {
        // 定义sql语句
        StringBuffer sql = new StringBuffer("select o from Order as o,OrderDetail as od where 1=1");
        sql.append(" and o.id = od.orderId");
        Map<String, Object> map = new HashMap<String, Object>();

        if (!StringUtil.isEmpty(orderId)) {
            sql.append(" and o.id = :orderId");
            map.put("orderId", orderId);
        }
        try {
            List<Order> list = orderDao.list(sql.toString(), map);
            if (list.size() > 0 && list != null) {
                return list.get(0);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 用户中心-已买到的宝贝评价
     */
    @Override
    public boolean evaluateOrder(String[] noteArray, String[] typeArray, String[] productIdArray, String orderId, String observer, String storeId) {
        try {
            if (noteArray != null && typeArray != null && noteArray.length > 0 && noteArray.length == typeArray.length) {
                for (int i = 0; i < noteArray.length; i++) {
                    Appraise appraise = new Appraise();
                    // 设置评价时间
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
                    String nowTime = sdf.format(date);
                    appraise.setCreateTime(nowTime);
                    // 设置评价人
                    appraise.setSource(observer);
                    appraise.setTarget(storeId);
                    // 设置评价类型(好、中、差评)
                    appraise.setType(typeArray[i]);
                    // 设置评价的具体内容
                    appraise.setNote(noteArray[i]);
                    // 设置订单id
                    appraise.setOrderId(orderId);
                    appraise.setProductId(productIdArray[i]);
                    appraiseDao.addBean(appraise);
                }
                // // 对商品的评价完成后,将订单的状态改为完成状态
                // Order order = (Order)storeOrderDao.get(orderId);
                // order.setStatus("5");
                // storeOrderDao.updateBean(order);
                //
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据传回来的Id取得order集合 需要的数据： orderAddress：name order：createtime， productPrice
     * orderDetail：productName product：smallPicture
     */
    @Override
    public List<Order> getOrderById(String id) {
        StringBuffer sql = new StringBuffer("select oa.name,o.createTime,o.id,o.storeId");
        sql.append(" from OrderAddress as oa,Order as o where 1=1");
        sql.append(" and o.id = oa.orderId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(id)) {
            sql.append(" and o.id = :id");
            map.put("id", id);
        }
        try {
            List<Object[]> list = orderDao.list(sql.toString(), map);
            List<Order> orders = new ArrayList<Order>();
            for (Object[] objects : list) {
                Order order = new Order();
                order.setName((String) objects[0]);
                order.setCreateTime((String) objects[1]);
                order.setId((String) objects[2]);
                order.setStoreId((String) objects[3]);
                orders.add(order);
            }
            return orders;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 订单详情包括: wsj(用户中心) step1: 1.商品信息(OrderDetail
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
    public Object[] getOrderDetailByOrderId(String orderId) {
        try {
            StringBuffer hql = new StringBuffer(" select" + " oa.name,oa.provincial,oa.city,oa.area,oa.address,oa.phoneNum,oa.mobile,oa.email," + // 2
                    " o.payment,o.fare,o.logisticsName,o.logisticsNo," + // 3
                    " o.invoiceType,o.invoiceTitle,o.invoiceContent," + // 4
                    " o.totalPrice,o.paid,otd.createTime,o.id,o.status,o.shipment,o.productPrice,o.paymentId,o.invoice,o.type,o.payType" + // 5
                    " from Order as o,OrderAddress as oa,OrderTreatDetail as otd" + " where 1=1");
            // 添加关联条件
            hql.append(" and o.id=oa.orderId");
            hql.append(" and o.id=otd.orderId");

            // 需要查询出付款时间 所以这里需从OrderTreatDetail取出操作类型为付款的操作时间
            hql.append(" and otd.type='0'");

            // 定义接收参数的Map
            Map<String, Object> params = new HashMap<String, Object>();

            if (StringUtil.isEmpty(orderId)) {
                return null;
            }
            hql.append(" and o.id=:orderId");
            params.put("orderId", orderId);

            // 执行查询
            List list = orderDao.list(hql.toString(), params);
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
                order.setPaymentId((String) objects[22]);
                order.setFare((BigDecimal) objects[9]); // 运费
                order.setLogisticsName((String) objects[10]); // 物流公司
                order.setLogisticsNo((String) objects[11]); // 运单号码

                order.setInvoice((Boolean) objects[23]);// 是否有发票
                order.setInvoiceType((Boolean) objects[12]); // 发票类型
                order.setInvoiceTitle((String) objects[13]); // 发票抬头
                order.setInvoiceContent((String) objects[14]); // 发票内容
                order.setType((Integer) objects[24]);

                order.setTotalPrice((BigDecimal) objects[15]); // 应收金额
                order.setPaid((BigDecimal) objects[16]); // 实收金额
                order.setId((String) objects[18]); // 订单id
                order.setStatus((String) objects[19]); // 订单状态
                order.setShipment((String) objects[20]); // 配送方式
                order.setProductPrice((BigDecimal) objects[21]);
                order.setPayType((String) objects[25]);
                returnObj[1] = order;

                // OrderTreatDetail部分
                OrderTreatDetail treatDetail = new OrderTreatDetail();
                // if("3".equals(objects[19].toString())){
                treatDetail.setCreateTime((String) objects[17]); // 付款日期
                // }
                returnObj[2] = treatDetail;

                // 将数组返回
                return returnObj;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Order> orderListByPayId(String payId, String userId) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select o from Order as o where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 如果为空直接返回空
        if (!StringUtil.isEmpty(userId)) {
            sql.append(" and o.userId = :userId");
            paramMap.put("userId", userId);
        }
        if (!StringUtil.isEmpty(payId)) {
            sql.append(" and o.payId = :payId");
            paramMap.put("payId", payId);
        }
        try {
            List<Order> list = orderDao.list(sql.toString(), paramMap);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 查询出所有的宝贝
     */
    @Override
    public List<OrderDetail> getAllOrderDetailByUserId(String userId) {
        try {
            if (StringUtil.isEmpty(userId)) {
                return null;
            }
            StringBuffer hql = new StringBuffer("select od.orderId,od.storeId,od.userId,od.quantity,od.price,od.totalPrice,");
            hql.append(" p.id,p.name,p.picture,p.smallPicture");
            hql.append(" from Order as o,OrderDetail as od,Product as p where 1=1");
            hql.append(" and od.productId = p.id");
            hql.append(" and o.id = od.orderId");
            Map<String, Object> params = new HashMap<String, Object>();
            hql.append(" and o.userId = :userId");
            params.put("userId", userId);
            hql.append(" order by o.createTime DESC");

            List ods = orderDao.list(hql.toString(), params);
            if (ods != null && ods.size() > 0) {
                // 定义返回集合
                List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
                for (int i = 0; i < ods.size(); i++) {
                    OrderDetail od = new OrderDetail();
                    Object[] objs = (Object[]) ods.get(i);
                    od.setOrderId((String) objs[0]);
                    od.setStoreId((String) objs[1]);
                    od.setUserId((String) objs[2]);
                    od.setQuantity((Integer) objs[3]);
                    od.setPrice((BigDecimal) objs[4]);
                    od.setTotalPrice((BigDecimal) objs[5]);
                    od.setProductId((String) objs[6]);
                    od.setProductName((String) objs[7]);
                    od.setPicture((String) objs[8]);
                    od.setSmallPicture((String) objs[9]);

                    orderDetails.add(od);
                }
                return orderDetails;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Order> getAlreadyProductOrder(String id, Pager pager) {
        List<Order> tempList = new ArrayList<Order>();

        try {
            String hql = "select o.id,o.createTime,oa.name,o.storeId from Order o,OrderAddress oa where o.id=oa.orderId and o.id=:id";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            List<Object[]> oList = this.orderDao.list(hql, map);
            if (null != oList) {
                for (Object[] obj : oList) {
                    Order or = new Order();
                    or.setId((String) obj[0]);
                    or.setCreateTime((String) obj[1]);
                    or.setName((String) obj[2]);
                    or.setStoreId((String) obj[3]);

                    tempList.add(or);
                }

                hql = "select od.productId,od.productName,p.recommendPicture1,od.totalPrice,od.id from OrderDetail od,Product p where od.productId=p.id and od.orderId=:orderId";
                for (Order tempO : tempList) {
                    List<OrderDetail> tempODList = new ArrayList<OrderDetail>();

                    Map<String, Object> mapNew = new HashMap<String, Object>();
                    mapNew.put("orderId", tempO.getId());
                    List<Object[]> odList = this.orderDetailDao.list(pager, hql, mapNew);
                    for (Object[] objects : odList) {
                        OrderDetail od = new OrderDetail();
                        od.setProductId((String) objects[0]);
                        od.setProductName((String) objects[1]);
                        od.setSmallPicture((String) objects[2]);
                        od.setTotalPrice((BigDecimal) objects[3]);
                        od.setId((String) objects[4]);

                        tempODList.add(od);
                    }

                    tempO.setDetails(tempODList);
                }
            }

            return tempList;
        } catch (Exception e) {
            // e.printStackTrace();
            log.error(e.getMessage());
        }

        return null;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean appraiseProductInfo(String orderId, String storeId, String[] productId, String type, String[] note, User user, String[] orderDetailId, int judgment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        String[] newType = type.split(",");

        // 打开一个新的
        Session session = this.orderDao.createSession();
        // 在session中获取一个事务
        Transaction tran = session.beginTransaction();

        if (null != productId) {
            for (int i = 0; i < productId.length; i++) {
                Appraise appraise = new Appraise();

                // 订单id
                appraise.setOrderId(orderId);
                // 商品id
                appraise.setProductId(productId[i]);
                // 评价人
                appraise.setSource(user.getId());
                // 被评价
                appraise.setTarget(storeId);
                // 类型
                appraise.setType(newType[i]);
                // 评价时间
                appraise.setCreateTime(sdf.format(new Date()));
                // 订单详情id
                appraise.setOrderDetailId(orderDetailId[i]);

                if (null != note) {
                    if (productId.length == note.length) {
                        if (StringUtil.isEmpty(note[i])) {
                            setAppraiseContent(newType, i, appraise);
                        } else {
                            appraise.setNote(note[i]);
                        }
                    } else {
                        // int temp = productId.length - note.length;
                        int judg = 0;
                        for (int j = i; j < note.length; j++) {
                            appraise.setNote(note[i]);
                            judg = 1;
                            break;
                        }
                        if (judg == 0) {
                            setAppraiseContent(newType, i, appraise);
                        }
                    }
                } else {
                    setAppraiseContent(newType, i, appraise);
                }
                session.save(appraise);
            }

            OrderTreatDetail otd = new OrderTreatDetail();
            otd.setOrderId(orderId);
            otd.setCreateTime(DateUtil.getSysDateTimeString());
            if (StringUtil.isEmpty(user.getNickName())) {
                otd.setCreater(user.getName());
            } else {
                otd.setCreater(user.getNickName());
            }
            Order order = (Order) session.get(Order.class, orderId);
            if (judgment == 1) {
                otd.setType(OrderConst.ORDER_TREAT_USER_APP);
                otd.setNote("评价买到的宝贝");

                order.setUserApp(true);
                session.update(order);
            } else if (judgment == 2) {
                otd.setType(OrderConst.ORDER_TREAT_STORE_APP);
                otd.setNote("对买家评价");

                // 对商品的评价完成后,将订单的状态改为完成状态
                order.setStoreApp(true);
//				order.setStatus("5");
                session.update(order);
            }
            session.save(otd);

            try {
                tran.commit();
                session.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                if (null != tran) {
                    tran.rollback();
                }
                return false;
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        } else {
            return false;
        }
    }

    private void setAppraiseContent(String[] newType, int i, Appraise appraise) {
        switch (Integer.valueOf(newType[i])) {
            case 1:
                appraise.setNote("好评");
                break;
            case 2:
                appraise.setNote("中评");
                break;
            case 3:
                appraise.setNote("差评");
                break;
        }
    }

    @Override
    public Order getOrderInfoAndOrderAddressInfo(String orderId) {
        Session session = this.orderDao.createSession();
        String sql = "select {o.*},{oa.*} from `order` o,order_address oa where o.id=oa.order_id and o.id=?";

        Query query = session.createSQLQuery(sql).addEntity("o", Order.class).addEntity("oa", OrderAddress.class);
        query.setString(0, orderId);
        List<Object[]> list = query.list();

        if (null == list || list.size() <= 0) {
            return null;
        }

        Object[] obj = list.get(0);
        Order order = (Order) obj[0];
        order.setOrderAddress((OrderAddress) obj[1]);

        if (null != session && session.isOpen()) {
            session.close();
        }

        return order;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Order> getDaZongOrderList(Pager pager) {
        // param data
        Map<String, Object> params = new HashMap<String, Object>();
        // hql
        StringBuffer hql = new StringBuffer("select o.createTime,p.name,od.quantity,p.unit,u.nickName,o.paid from Order o,Product p,OrderDetail od,User u " + "where o.id=od.orderId and od.productId=p.id and o.userId=u.id order by o.createTime desc");
        // setting data
        // params.put("type", 1);

        try {
            List<Object[]> list = this.orderDao.list(pager, hql.toString(), params);
            if (list == null || list.size() <= 0) {
                return null;
            }

            List<Order> olist = new ArrayList<Order>();
            for (Object[] obj : list) {
                Order o = new Order();
                o.setCreateTime(obj[0] == null ? "" : obj[0].toString());
                o.setProductName(obj[1] == null ? "" : obj[1].toString());
                o.setQuantity(obj[2] == null ? 0 : Integer.valueOf(obj[2].toString()));
                o.setUnit(obj[3] == null ? "" : obj[3].toString());
                o.setNickName(obj[4] == null ? "" : obj[4].toString());
                o.setPaid(new BigDecimal(obj[5] == null ? "0.00" : obj[5].toString()));

                olist.add(o);
            }

            return olist;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /******************************************* 危废品后台 ********************************************************/
    @Override
    public List<OrderWfp> listOrderWfp(Pager pager, String orderNumber, String startTime, String endTime) {
        List<OrderWfp> wfpLsit = new ArrayList<OrderWfp>();
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer buffer = new StringBuffer("from OrderWfp wfp where 1=1 ");
        if (!StringUtil.isEmpty(orderNumber)) {
            buffer.append(" and wfp.orderNumber =:orderNumber");
            params.put("orderNumber", orderNumber);
        }
        if (!StringUtil.isEmpty(startTime)) {
            buffer.append(" and wfp.createTime >=:startTime");
            params.put("startTime", startTime);
        }
        if (!StringUtil.isEmpty(endTime)) {
            buffer.append(" and wfp.createTime <=:endTime");
            params.put("endTime", endTime);
        }
        buffer.append(" order by wfp.createTime desc");

        try {
            wfpLsit = orderWfpDao.list(pager, buffer.toString(), params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wfpLsit;
    }

    @Override
    public boolean orderBCSPay(String orderId, Store store, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request) {
        // TODO Auto-generated method stub

        Order order = getOrderByOrderId(orderId);
        String type = "1";
        BigDecimal payPrice = order.getPaid();

        IAccountLogService accountLogService = getBean(IAccountLogService.class);

        Boolean payStatus = accountLogService.payForOrder(order.getUserId(), order.getStoreId(), order.getId(), type, payPrice);
        if (!payStatus) {
            return false;
        }
        order.setPayType(OrderConst.ORDER_PAY_TYPE_YUE);
        IOrderPayLogService payLogService = getBean(IOrderPayLogService.class);
        OrderPayLog payLog = new OrderPayLog();
        payLog.setOrderId(order.getId());
        payLog.setPrice(payPrice);
        payLog.setUserId(store.getId());
        payLog.setStoreId(order.getStoreId());
        payLog.setOrderType(orderType);
        payLog.setPayPattern(payPattern);
        payLog.setBankId(bankId);
        payLog.setCreateTime(DateUtil.getSysDateTimeString());
        payLog.setPayType(payType);
        payLog.setStatus("1");
        payLogService.saveOrderPayLog(payLog);
        order.setPayType(payType);
        order.setNickName(store.getStoreName());
        if (order.getType().equals(OrderConst.ORDER_TYPE_ZF)) {
            return changePayOrderStatus(order, OrderConst.ORDER_STATUS_FINISH);
        } else {
            return changePayOrderStatus(order, OrderConst.ORDER_STATUS_WAIT_SEND);
        }
    }

    /**
     * 线下支付订单
     *
     * @param order
     * @param store
     * @param orderType
     * @param payType
     * @param payPattern
     * @param bankId
     * @param request
     * @return
     */
    public boolean orderULPay(Order order, Store store, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request) {
        BigDecimal payPrice = order.getTotalPrice();
//		order.setPayType(OrderConst.ORDER_PAY_TYPE_YUE);
        IOrderPayLogService payLogService = getBean(IOrderPayLogService.class);
        OrderPayLog payLog = new OrderPayLog();
        payLog.setOrderId(order.getId());
        payLog.setPrice(payPrice);
        payLog.setUserId(store.getId());
        payLog.setStoreId(order.getStoreId());
        payLog.setOrderType(orderType);
        payLog.setPayPattern(payPattern);
        payLog.setBankId(bankId);
        payLog.setCreateTime(DateUtil.getSysDateTimeString());
        payLog.setPayType(payType);
        payLog.setPayCert(order.getPayCert());
        payLog.setStatus("1");
        payLogService.saveOrderPayLog(payLog);
        order.setPayType(payType);
        order.setNickName(store.getStoreName());
        if (order.getType().equals(OrderConst.ORDER_TYPE_ZF)) {
            return changePayOrderStatus(order, OrderConst.ORDER_STATUS_WAIT_PAY_SURE);
        } else {
            return changePayOrderStatus(order, OrderConst.ORDER_STATUS_WAIT_SEND);
        }
    }

    /**
     * 在线支付订单
     *
     * @param orderId
     * @param userId
     * @param orderType
     * @param payType
     * @param payPattern
     * @param bankId
     * @param request
     * @return
     */
    public boolean orderOLPay(String orderId, String userId, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request) {
        Order order = getOrderByOrderId(orderId);
        BigDecimal payPrice = order.getTotalPrice();

        order.setPayType(OrderConst.ORDER_PAY_TYPE_ONLINE);
        IOrderPayLogService payLogService = getBean(IOrderPayLogService.class);
        OrderPayLog payLog = new OrderPayLog();
        payLog.setOrderId(order.getId());
        payLog.setPrice(payPrice);
        payLog.setUserId(userId);
        payLog.setStoreId(order.getStoreId());
        payLog.setOrderType(orderType);
        payLog.setPayPattern(payPattern);
        payLog.setBankId(bankId);
        payLog.setCreateTime(DateUtil.getSysDateTimeString());
        payLog.setPayType(payType);
        payLog.setPayCert(order.getPayCert());
        payLog.setStatus("1");
        payLogService.saveOrderPayLog(payLog);
        order.setPayType(payType);
        if (order.getType().equals(OrderConst.ORDER_TYPE_ZF)) {
            return changePayOrderStatus(order, OrderConst.ORDER_STATUS_FINISH);
        } else {
            return changePayOrderStatus(order, OrderConst.ORDER_STATUS_WAIT_SEND);
        }
    }

    /**
     * 在线支付订单---建设银行善融支付
     *
     * @param orderId
     * @param queryId
     * @return
     */
    public boolean orderOLCCBPay(String orderId, String queryId) {
        boolean status;
        Order order = getOrderByOrderId(orderId);
        /*
         * 如果订单已进入完成状态，直接返回已完成
         * 以满足文档中："商户订单系统对于某笔订单已经收到成功应答，有可能后续再次收到 该笔订单的成功应答，则商户只需要回应九派支付平台收到成功即可，商户的账 务数据不应被修改。"的幂等性需求
         */
        if (OrderConst.ORDER_STATUS_FINISH.equals(order.getStatus()) || OrderConst.ORDER_STATUS_WAIT_SEND.equals(order.getStatus())) {
            status = true;
        } else {
            order.setPayType(OrderConst.ORDER_PAY_TYPE_ONLINE);

            if (order.getType().equals(OrderConst.ORDER_TYPE_ZF)) {
                status = changePayOrderStatus(order, OrderConst.ORDER_STATUS_FINISH);
            } else {
                status = changePayOrderStatus(order, OrderConst.ORDER_STATUS_WAIT_SEND);
            }

            BigDecimal payPrice = order.getTotalPrice();
            IOrderPayLogService payLogService = getBean(IOrderPayLogService.class);
            OrderPayLog payLog = new OrderPayLog();
            payLog.setOrderId(order.getId());
            payLog.setPrice(payPrice);
            payLog.setUserId(order.getUserId());
            payLog.setStoreId(order.getStoreId());
            payLog.setOrderType(order.getType() + "");
            payLog.setPayPattern(OrderPayLog.PAYTYPE_OT);
            payLog.setCreateTime(DateUtil.getSysDateTimeString());
            payLog.setPayCert(order.getPayCert());
            payLog.setStatus("1");
            payLog.setQueryId(queryId);
            payLogService.saveOrderPayLog(payLog);
        }
        return status;
    }

    /**
     * 九鼎支付
     * @param orderId
     * @param queryId
     * @param request
     * @return
     */
    public boolean orderOLJiudingPay(String orderId, String queryId, HttpServletRequest request){
        boolean status = false;
        Order order = getOrderByOrderId(orderId);
        /*
         * 如果订单已进入完成状态，直接返回已完成
         * 以满足文档中："商户订单系统对于某笔订单已经收到成功应答，有可能后续再次收到 该笔订单的成功应答，则商户只需要回应九派支付平台收到成功即可，商户的账 务数据不应被修改。"的幂等性需求
         */
        if (OrderConst.ORDER_STATUS_FINISH.equals(order.getStatus()) || OrderConst.ORDER_STATUS_WAIT_SEND.equals(order.getStatus())) {
            status = true;
        } else {
            order.setPayType(OrderConst.ORDER_PAY_TYPE_ONLINE);

            if (order.getType().equals(OrderConst.ORDER_TYPE_ZF)) {
                status = changePayOrderStatus(order, OrderConst.ORDER_STATUS_FINISH);
            } else {
                status = changePayOrderStatus(order, OrderConst.ORDER_STATUS_WAIT_SEND);
            }

            BigDecimal payPrice = order.getTotalPrice();
            IOrderPayLogService payLogService = getBean(IOrderPayLogService.class);
            OrderPayLog payLog = new OrderPayLog();
            payLog.setOrderId(order.getId());
            payLog.setPrice(payPrice);
            payLog.setUserId(order.getUserId());
            payLog.setStoreId(order.getStoreId());
            payLog.setOrderType(order.getType() + "");
            payLog.setPayPattern(OrderPayLog.PAYTYPE_OT);
            payLog.setBankId("JIUDING_PAY");
            payLog.setCreateTime(DateUtil.getSysDateTimeString());
            payLog.setPayType(OrderPayLog.PAY_TYPE_COMMON);
            payLog.setPayCert(order.getPayCert());
            payLog.setStatus("1");
            payLog.setQueryId(queryId);
            payLogService.saveOrderPayLog(payLog);
        }
        return status;
    }
}
