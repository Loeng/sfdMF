package com.ekfans.base.wfOrder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.plugin.ccb.CcbPayUtil;
import com.ekfans.plugin.number.NoManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.ICarInfoDao;
import com.ekfans.base.store.dao.ICarUserDao;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.CarUser;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccountLogService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.wfOrder.dao.IContractDao;
import com.ekfans.base.wfOrder.dao.IOrderPayLogDao;
import com.ekfans.base.wfOrder.dao.IScrapPricesDao;
import com.ekfans.base.wfOrder.dao.IScrapWfpDao;
import com.ekfans.base.wfOrder.dao.IWfOrderDao;
import com.ekfans.base.wfOrder.dao.IWfOrderLogDao;
import com.ekfans.base.wfOrder.dao.IWfOrderPriceDao;
import com.ekfans.base.wfOrder.dao.IWfOrderTransDao;
import com.ekfans.base.wfOrder.dao.IWfOrderTransDriverDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ContractCars;
import com.ekfans.base.wfOrder.model.OrderPayLog;
import com.ekfans.base.wfOrder.model.ScrapPrices;
import com.ekfans.base.wfOrder.model.ScrapWfp;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderLog;
import com.ekfans.base.wfOrder.model.WfOrderTrans;
import com.ekfans.base.wfOrder.model.WfOrderTransDriver;
import com.ekfans.base.wfOrder.util.WfOrderHelper;
import com.ekfans.base.wfOrder.util.WfOrderTransStore;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.plugin.wftong.controller.model.FiveSingleModel;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class WfOrderService implements IWfOrderService {
    Logger log = LoggerFactory.getLogger(WfOrderService.class);

    @Autowired
    private IWfOrderDao wfOrderDao;
    @Autowired
    private IContractDao contractDao;
    @Autowired
    private IWfOrderLogDao orderLogDao;
    @Autowired
    private IWfOrderPriceDao priceDao;
    @Autowired
    private IWfOrderTransDao transDao;
    @Autowired
    private IWfOrderTransDriverDao transDriverDao;
    @Autowired
    private IWfOrderTransDriverDao driverDao;
    @Autowired
    private ICarInfoDao carInfoDao;
    @Autowired
    private IScrapWfpDao wfpDao;
    @Autowired
    private IOrderPayLogDao payLogDao;

    /**
     * 根据危废申报ID，订单ID获取该申报单剩余数量
     *
     * @param scrapWfpId
     * @return
     */
    @Override
    public Double getSurplusQuantityByScrapWfpId(String scrapWfpId, String wfOrderId) {
        if (StringUtil.isEmpty(scrapWfpId)) {
            return 0.00;
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select sw.quantity,sum(wo.wfpTotal) from WfOrder as wo,ScrapWfp as sw where 1=1");
        sql.append(" and wo.scrapId = :scrapId");
        paramMap.put("scrapId", scrapWfpId);
        sql.append(" and wo.scrapId = sw.id");

        if (!StringUtil.isEmpty(wfOrderId)) {
            sql.append(" and wo.id != :wfOrderId");
            paramMap.put("wfOrderId", wfOrderId);
        }

        sql.append(" and wo.status != :status");
        paramMap.put("status", WfOrderHelper.WFORDER_STATUS_FAILD);

        try {
            List list = wfOrderDao.list(sql.toString(), paramMap);
            if (list != null && list.size() > 0) {
                Object[] obj = (Object[]) list.get(0);
                if (obj[0] == null || obj[1] == null) {
                    ScrapWfp wfp = (ScrapWfp) wfpDao.get(scrapWfpId);
                    if (wfp != null) {
                        return wfp.getQuantity();
                    } else {
                        return 0.00;
                    }
                } else {
                    Double quantity = (Double) obj[0];
                    return quantity - Double.parseDouble(obj[1].toString());
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    /**
     * 新增或修改危废品订单
     *
     * @param wfOrder
     * @param request
     * @param response
     * @return
     */
    public Boolean saveOrUpdateWfOrder(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response) {
        // 获取Session中的Store对象
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        // 如果传过来的WfOrder对象为空或者Store对象为空，则返回失败
        if (wfOrder == null || store == null) {
            return false;
        }

        Contract contract = null;
        try {
            contract = (Contract) contractDao.get(wfOrder.getContractId());
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (contract == null) {
            return false;
        }
        Session session = null;
        Transaction transaction = null;
        boolean isUpdate = false;
        if (!StringUtil.isEmpty(wfOrder.getId())) {
            isUpdate = true;
        }

        try {
            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            wfOrder.setCountType("1".equals(contract.getPayType()) ? true : false);

            // 如果是一口价计价
            if (wfOrder.isCountType()) {
                wfOrder.setContractOriginal(new BigDecimal(contract.getYkjPrice()));
            }
            if (!isUpdate) {
                wfOrder.setCreateTime(DateUtil.getSysDateTimeString());
            }
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());

            if (!isUpdate) {
                NoManager noManager = new NoManager();
                String orderId = noManager.createWfOrderId();
                wfOrder.setBankOrderId(orderId);
                wfOrder.setId(orderId);// 直接使用短的id
                wfOrderDao.addBean(wfOrder, session);
            }

            String[] trans = request.getParameterValues("transportStoreId");
            WfOrderHelper.saveOrUpdateOrderTrans(isUpdate, trans, wfOrder, session, null, request);

            if ("1".equals(contract.getFreightType())) {
                wfOrder.setFreightBuyer(true);
            }
            // 截取字符串
            // String [] lc = wfOrder.getMileage();
            // lc.subSequence(lc.length() - 2, lc.length());
            // wfOrder.setMileage(mileage);
            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote(isUpdate ? "修改危废品订单" : "新建危废品订单");
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
     * 获取危废品订单列表
     *
     * @param scrapId
     * @param wfName
     * @param status
     * @param startTime
     * @param endTime
     * @param orderId
     * @param pager
     * @param pageType
     * @param store
     * @return
     */
    public List<WfOrder> getWfOrderList(String scrapId, String contractId, String wfName, String orderStatus, String status, String startTime, String endTime, String orderId, String saleName,
                                        String buyName, Pager pager, String viewType, String storeId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select distinct wfOrder from WfOrder as wfOrder");

        if ("2".equals(viewType)) {
            sql.append(" , WfOrderTrans as wt ");
        }

        sql.append(" where 1=1");

        if (!StringUtil.isEmpty(scrapId)) {
            sql.append(" and wfOrder.scrapId = :scrapId");
            paramMap.put("scrapId", scrapId);
        }

        if (!StringUtil.isEmpty(contractId)) {
            sql.append(" and wfOrder.contractId = :contractId");
            paramMap.put("contractId", contractId);
        }

        if (!StringUtil.isEmpty(wfName)) {
            sql.append(" and wfOrder.wfpName like :wfpName");
            paramMap.put("wfpName", "%" + wfName + "%");
        }

        if (!StringUtil.isEmpty(status)) {
            sql.append(" and wfOrder.status = :status");
            paramMap.put("status", status);
        }

        if (!StringUtil.isEmpty(startTime)) {
            sql.append(" and wfOrder.createTime >= :startTime");
            paramMap.put("startTime", startTime + " 00:00:00");
        }

        if (!StringUtil.isEmpty(endTime)) {
            sql.append(" and wfOrder.createTime <= :endTime");
            paramMap.put("endTime", endTime + " 23:59:59");
        }

        if (!StringUtil.isEmpty(orderId)) {
            sql.append(" and wfOrder.id like :orderId");
            paramMap.put("orderId", "%" + orderId + "%");
        }

        if (!StringUtil.isEmpty(saleName)) {
            sql.append(" and wfOrder.saleName like :saleName");
            paramMap.put("saleName", "%" + saleName + "%");
        }

        if (!StringUtil.isEmpty(buyName)) {
            sql.append(" and wfOrder.buyName like :buyName");
            paramMap.put("buyName", "%" + buyName + "%");
        }

        if ("0".equals(viewType)) {
            sql.append(" and wfOrder.buyId = :storeId");
            paramMap.put("storeId", storeId);

            if ("待处理".equals(orderStatus)) {
                sql.append(" and (wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SURE).append("'");
                sql.append(" or (wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF).append("' and wfOrder.yfPrice > 0)");
                sql.append(" or wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SH).append("'");
                sql.append(" or wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PW).append("'");
                sql.append(" or (wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SURE_PRICE).append("' and wfOrder.buySureStatus = :buySureStatus)");
                sql.append(" or (wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PAY).append("' and wfOrder.realTotalPrice > wfOrder.payPrice)");
                paramMap.put("buySureStatus", false);
                sql.append(")");
            } else if ("已处理".equals(orderStatus)) {
                sql.append(" and wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SURE).append("'");
                sql.append(" and (wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF).append("' or wfOrder.yfPrice < 0)");
                sql.append(" and wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SH).append("'");
                sql.append(" and wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PW).append("'");
                sql.append(" and (wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SURE_PRICE).append("' or wfOrder.buySureStatus = :buySureStatus)");
                sql.append(" and (wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PAY).append("' or wfOrder.realTotalPrice < wfOrder.payPrice)");
                paramMap.put("buySureStatus", true);
            }
        } else if ("1".equals(viewType)) {
            sql.append(" and wfOrder.saleId = :storeId");
            paramMap.put("storeId", storeId);

            if ("待处理".equals(orderStatus)) {
                sql.append(" and (wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_FH).append("'");
                sql.append(" or (wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF).append("' and wfOrder.yfPrice < 0)");
                sql.append(" or (wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SURE_PRICE).append("' and wfOrder.buySureStatus = :saleSureStatus)");
                sql.append(" or (wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PAY).append("' and wfOrder.realTotalPrice < wfOrder.payPrice)");
                paramMap.put("saleSureStatus", false);
                sql.append(")");
            } else if ("已处理".equals(orderStatus)) {
                sql.append(" and wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_FH).append("'");
                sql.append(" and (wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF).append("' or wfOrder.yfPrice > 0)");
                sql.append(" and (wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SURE_PRICE).append("' or wfOrder.buySureStatus = :saleSureStatus)");
                sql.append(" and (wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_PAY).append("' or wfOrder.realTotalPrice > wfOrder.payPrice)");
                paramMap.put("saleSureStatus", true);
            }

        } else if ("2".equals(viewType)) {
            sql.append(" and wt.wfOrderId = wfOrder.id");
            sql.append(" and wt.transStoreId = :storeId");
            paramMap.put("storeId", storeId);

            if ("待处理".equals(orderStatus)) {
                sql.append(" and wfOrder.status = '").append(WfOrderHelper.WFORDER_STATUS_WAIT_TRANS_SURE).append("'");
            } else if ("已处理".equals(orderStatus)) {
                sql.append(" and wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_SURE).append("'");
                sql.append(" and wfOrder.status != '").append(WfOrderHelper.WFORDER_STATUS_WAIT_TRANS_SURE).append("'");
            }
        }

        sql.append(" order by wfOrder.createTime desc");

        try {
            List<WfOrder> orderList = null;
            if (pager != null) {
                orderList = wfOrderDao.list(pager, sql.toString(), paramMap);
            } else {
                orderList = wfOrderDao.list(sql.toString(), paramMap);
            }
            return orderList;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    /***
     * 根据危废订单id获取危废订单对象
     *
     * @param wfOrderId
     * @return
     */
    public WfOrder getWfOrderById(String wfOrderId) {
        if (StringUtil.isEmpty(wfOrderId)) {
            return null;
        }
        try {
            // 开始测试 同步数据到监控平台,之后删除或者注释
            // WfOrderTrans orderTrans = (WfOrderTrans)
            // transDao.get("402880fc4f63f176014f6407ec0f0015");
            // MonitorSyncService syncService = new MonitorSyncService(null,
            // null, "E:/workspace/.metadata/.me_tcat7/webapps/sfd", null, null,
            // null, null, null, orderTrans, "401", null,
            // "synchroOrderTransdata");
            // Thread thread = new Thread(syncService);
            // thread.start();
            // 结束

            return (WfOrder) wfOrderDao.get(wfOrderId);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    /**
     * 根据危废订单ID获取运输企业详情
     *
     * @param wfOrderId
     * @return
     */
    public Map<String, Boolean> getWfOrderTrans(String wfOrderId) {
        if (StringUtil.isEmpty(wfOrderId)) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select distinct(trans.transStoreId) from WfOrderTrans as trans where 1=1");
        sql.append(" and trans.wfOrderId = :wfOrderId");
        map.put("wfOrderId", wfOrderId);

        Map<String, Boolean> returnMap = new HashMap<String, Boolean>();
        try {
            List<String> transes = transDao.list(sql.toString(), map);
            if (transes != null && transes.size() > 0) {
                for (String storeId : transes) {
                    if (StringUtil.isEmpty(storeId)) {
                        continue;
                    }
                    returnMap.put(storeId, true);
                }
            }
            return returnMap;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 获取危废订单所关联的运输企业信息
     *
     * @param wfOrderId
     * @return
     */
    public List<WfOrderTransStore> getWfOrderTransList(String wfOrderId) {
        if (StringUtil.isEmpty(wfOrderId)) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select distinct trans from WfOrderTrans as trans where 1=1");
        sql.append(" and trans.wfOrderId = :wfOrderId");
        map.put("wfOrderId", wfOrderId);
        sql.append(" order by trans.createTime desc");
        try {
            List<WfOrderTrans> transes = transDao.list(sql.toString(), map);
            List<WfOrderTransStore> storeList = new ArrayList<WfOrderTransStore>();

            if (transes != null && transes.size() > 0) {
                Map<String, WfOrderTransStore> sMap = new HashMap<String, WfOrderTransStore>();
                for (WfOrderTrans trans : transes) {
                    if (trans == null || StringUtil.isEmpty(trans.getTransStoreId())) {
                        continue;
                    }
                    if (!StringUtil.isEmpty(trans.getCarInfoId())) {
                        List<WfOrderTransDriver> driverList = getTransDriverList(trans.getId());
                        trans.setDrivers(driverList);
                    }
                    WfOrderTransStore transStore = null;
                    if (sMap.containsKey(trans.getTransStoreId())) {
                        transStore = sMap.get(trans.getTransStoreId());
                    } else {
                        transStore = new WfOrderTransStore();
                    }
                    transStore.setStoreId(trans.getTransStoreId());
                    transStore.setStoreName(trans.getTransStoreName());
                    List<WfOrderTrans> transLists = transStore.getTransList();
                    if (transLists == null) {
                        transLists = new ArrayList<WfOrderTrans>();
                    }
                    if (!StringUtil.isEmpty(trans.getCarInfoId())) {
                        transLists.add(trans);
                    }
                    transStore.setTransList(transLists);
                    sMap.put(trans.getTransStoreId(), transStore);
                }

                if (sMap != null && sMap.size() > 0) {
                    Object[] objs = sMap.values().toArray();
                    for (Object obj : objs) {
                        storeList.add((WfOrderTransStore) obj);
                    }
                }
            }

            return storeList;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public List<WfOrderTransDriver> getTransDriverList(String wfOrderTransId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select driver from WfOrderTransDriver as driver where 1=1");
        sql.append(" and driver.wfOrderTransId = :wfOrderTransId");
        map.put("wfOrderTransId", wfOrderTransId);
        sql.append(" order by driver.createTime desc");

        try {
            List<WfOrderTransDriver> driverList = driverDao.list(sql.toString(), map);
            return driverList;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    /**
     * 获取订单日志
     *
     * @param wfOrderId
     * @return
     */
    public List<WfOrderLog> getOrderLog(String wfOrderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select log from WfOrderLog as log where 1=1");
        sql.append(" and log.wfOrderId = :wfOrderId");
        map.put("wfOrderId", wfOrderId);
        sql.append(" order by log.createTime desc");

        try {
            List<WfOrderLog> driverList = driverDao.list(sql.toString(), map);
            return driverList;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    @Override
    public boolean wfOrderCancel(WfOrder wfOrder, HttpServletRequest request) {
        // 获取Session中的Store对象
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        return quXiao(wfOrder, store);
    }

    @Override
    public boolean quXiao(WfOrder wfOrder, Store store) {
        if (wfOrder != null) {
            Session session = null;
            Transaction transaction = null;
            try {
                session = wfOrderDao.createSession();
                transaction = session.beginTransaction();

                wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_FAILD);
                wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());

                wfOrderDao.updateBean(wfOrder, session);

                WfOrderLog orderLog = new WfOrderLog();
                orderLog.setCreateTime(DateUtil.getSysDateTimeString());
                orderLog.setCreator(store.getId());
                orderLog.setCreatorName(store.getStoreName());
                orderLog.setWfOrderId(wfOrder.getId());
                orderLog.setNote("取消订单");
                orderLogDao.addBean(orderLog, session);

                session.flush();
                transaction.commit();
                session.close();
                return true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        }
        return false;
    }

    /**
     * 获取危废订单所关联的运输企业信息
     *
     * @param wfOrderId
     * @return
     */
    public Boolean getWfOrderTransLists(String wfOrderId, String storeId, HttpServletRequest request) {
        if (StringUtil.isEmpty(wfOrderId) || StringUtil.isEmpty(storeId)) {
            return false;
        }

        String carInfoIds = request.getParameter("carInfoIds");

        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select distinct trans from WfOrderTrans as trans where 1=1");
        sql.append(" and trans.wfOrderId = :wfOrderId");
        map.put("wfOrderId", wfOrderId);
        sql.append(" and trans.transStoreId = :storeId");
        map.put("storeId", storeId);
        sql.append(" order by trans.createTime desc");
        try {
            List<WfOrderTrans> transes = transDao.list(sql.toString(), map);
            List<WfOrderTrans> returnList = new ArrayList<WfOrderTrans>();

            if (transes != null && transes.size() > 0) {
                for (WfOrderTrans trans : transes) {
                    if (trans == null || StringUtil.isEmpty(trans.getTransStoreId())) {
                        continue;
                    }
                    if (StringUtil.isEmpty(trans.getCarInfoId())) {
                        continue;
                    }
                    if (!StringUtil.isEmpty(carInfoIds) && carInfoIds.indexOf(trans.getCarInfoId()) != -1) {
                        carInfoIds = carInfoIds.replaceAll(trans.getCarInfoId() + ";", "");
                    }
                    List<WfOrderTransDriver> driverList = getTransDriverList(trans.getId());
                    trans.setDrivers(driverList);
                    CarInfo carInfo = (CarInfo) carInfoDao.get(trans.getCarInfoId());
                    trans.setCarInfo(carInfo);
                    returnList.add(trans);
                }
            }
            request.setAttribute("transList", returnList);

            if (!StringUtil.isEmpty(carInfoIds)) {
                String[] ids = carInfoIds.split(";");
                String idStr = "";
                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        if (StringUtil.isEmpty(id)) {
                            continue;
                        }

                        if (!StringUtil.isEmpty(idStr)) {
                            idStr = idStr + ",";
                        }
                        idStr = idStr + "'" + id + "'";
                    }
                }

                if (!StringUtil.isEmpty(idStr)) {
                    StringBuffer sql2 = new StringBuffer(" from CarInfo as ci where 1=1");
                    sql2.append(" and ci.id in (").append(idStr).append(")");
                    sql2.append(" order by ci.createTime desc");
                    List carInfoList = carInfoDao.list(sql2.toString(), null);
                    request.setAttribute("carInfoList", carInfoList);
                }

            }
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return false;
    }

    /**
     * 根据合同ID、车辆信息表ID获取合同车辆信息对象
     *
     * @param contractId
     * @param carInfoId
     * @return
     */
    public ContractCars getContractCar(String contractId, String carInfoId, Session session) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer(" from ContractCars as cc where 1=1");
        sql.append(" and cc.contractId = :contractId");
        map.put("contractId", contractId);
        sql.append(" and cc.carInfoId = :carInfoId ");
        map.put("carInfoId", carInfoId);
        try {
            List<ContractCars> cars = transDao.list(sql.toString(), map, session);
            if (cars != null && cars.size() > 0) {
                return cars.get(0);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    /**
     * 危废品订单运输企业选择车辆信息
     *
     * @param wfOrderId
     * @return
     */
    public Boolean updateWfOrderTrans(String wfOrderId, HttpServletRequest request) {
        if (StringUtil.isEmpty(wfOrderId)) {
            return false;
        }

        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        if (store == null) {
            return false;
        }

        WfOrder wfOrder = getWfOrderById(wfOrderId);
        if (wfOrder == null) {
            return false;
        }

        String carInfoIds = request.getParameter("carInfoIds");
        if (StringUtil.isEmpty(carInfoIds)) {
            return false;
        }
        String[] carInfos = carInfoIds.split(";");
        if (carInfos == null || carInfos.length <= 0) {
            return false;
        }

        String mileageStr = request.getParameter("mileage");
        mileageStr = StringUtil.isEmpty(mileageStr) ? "0" : mileageStr;
        BigDecimal mileage = new BigDecimal(mileageStr);

        Session session = null;
        Transaction transaction = null;
        try {
            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            Map<String, Object> delMap = new HashMap<String, Object>();
            StringBuffer delSql = new StringBuffer(" from WfOrderTrans as wt where 1=1");
            delSql.append(" and wt.wfOrderId = :wfOrderId");
            delMap.put("wfOrderId", wfOrder.getId());
            delSql.append(" and wt.transStoreId = :transStoreId ");
            delMap.put("transStoreId", store.getId());
            transDao.delete(delSql.toString(), delMap, session);

            StringBuffer delSql2 = new StringBuffer(" from WfOrderTransDriver as wt where 1=1");
            delSql2.append(" and wt.wfOrderId = :wfOrderId");
            delSql2.append(" and wt.transStoreId = :transStoreId ");
            transDao.delete(delSql2.toString(), delMap, session);

            ICarInfoDao infoDao = SpringContextHolder.getBean(ICarInfoDao.class);
            ICarUserDao carUserDao = SpringContextHolder.getBean(ICarUserDao.class);
            for (String carInfoId : carInfos) {
                // String numStr = request.getParameter(carInfoId + "Num");
                String supercargo = request.getParameter(carInfoId + "supercargoId");
                CarUser carUser = (CarUser) carUserDao.get(supercargo, session);
                CarInfo carInfo = (CarInfo) infoDao.get(carInfoId, session);
                ContractCars cCar = getContractCar(wfOrder.getContractId(), carInfoId, session);
                WfOrderTrans trans = new WfOrderTrans();
                trans.setCarInfoId(carInfoId);
                trans.setCarNum(carInfo.getCarNo());
                trans.setCreateTime(DateUtil.getSysDateTimeString());
                trans.setCreator(store.getId());
                trans.setWfOrderId(wfOrderId);
                trans.setTransStoreId(store.getId());
                trans.setTransStoreName(store.getStoreName());

                // try {
                // trans.setNum(Double.parseDouble(numStr));
                // } catch (Exception e) {
                // // TODO: handle exception
                // }
                trans.setSupercargo(supercargo);
                trans.setSupercargoCarNo(carUser.getCardNo());
                trans.setSupercargoLicenseNum(carUser.getLicenseNum());
                trans.setSupercargoName(carUser.getName());
                trans.setSupercargoPhone(carUser.getMobile());
                trans.setSupercargoLicenseNum(carUser.getLicenseNum());
                trans.setFreight(new BigDecimal(cCar != null ? (cCar.getPrice().doubleValue() * trans.getNum() * mileage.doubleValue()) : 0.00));
                transDao.addBean(trans, session);

                String driverIdStr = request.getParameter(carInfoId + "driverId");
                if (!StringUtil.isEmpty(driverIdStr)) {
                    String[] driverIds = driverIdStr.split(";");
                    if (driverIds != null && driverIds.length > 0) {
                        for (int i = 0; i < driverIds.length; i++) {
                            String driverId = driverIds[i];
                            CarUser droverInfo = (CarUser) carUserDao.get(driverId, session);
                            WfOrderTransDriver driver = new WfOrderTransDriver();
                            driver.setCreateTime(DateUtil.getSysDateTimeString());
                            driver.setWfOrderId(wfOrderId);
                            driver.setWfOrderTransId(trans.getId());
                            driver.setTransStoreId(store.getId());
                            driver.setDriver(droverInfo.getId());
                            driver.setDriverName(droverInfo.getName());
                            driver.setDriverCarNo(droverInfo.getCardNo());
                            driver.setDriverLicenseNum(droverInfo.getLicenseNum());
                            driver.setDriverPhone(droverInfo.getMobile());
                            transDriverDao.addBean(driver, session);
                        }
                    }
                }
            }

            Map<String, Object> listMap = new HashMap<String, Object>();
            StringBuffer sql = new StringBuffer("select wt.id from WfOrderTrans as wt where 1=1");
            sql.append(" and (wt.carInfoId is null or wt.carInfoId = 'null' or wt.carInfoId ='')");
            sql.append(" and wt.wfOrderId = :wfOrderId");
            listMap.put("wfOrderId", wfOrderId);
            List list = transDao.list(sql.toString(), listMap, session);
            if (list == null || list.size() <= 0) {

                Map<String, Object> map2 = new HashMap<String, Object>();
                StringBuffer sql2 = new StringBuffer("select sum(wt.freight) from WfOrderTrans as wt where 1=1");
                sql2.append(" and wt.wfOrderId = :wfOrderId");
                map2.put("wfOrderId", wfOrderId);
                List list2 = transDao.list(sql2.toString(), map2, session);
                if (list2 != null && list2.size() > 0) {
                    wfOrder.setFreight((BigDecimal) list2.get(0));
                }
                if (wfOrder.getYfPrice().doubleValue() != 0) {
                    wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF);
                } else {
                    wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_FH);
                    wfOrder.setPayStatus("1");
                }
                wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
                wfOrderDao.updateBean(wfOrder, session);

            }

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote("确认危废运输车辆信息");
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
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

    @Override
    public boolean queRenfh(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response) {
        // 获取Session中的Store对象
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        return faHuo(wfOrder, store);
    }

    @Override
    public boolean faHuo(WfOrder wfOrder, Store store) {
        if (wfOrder == null || store == null) {
            return false;
        }
        Session session = null;
        Transaction transaction = null;
        try {
            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_SH);
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote("上传五联单并发货");
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
     * 确认收货
     */
    @Override
    public boolean queRensh(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response) {
        if (wfOrder == null) {
            return false;
        }
        Contract contract = null;
        // 获取Session中的Store对象
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        Session session = null;
        Transaction transaction = null;

        String transIdStr = request.getParameter("transIds");
        if (StringUtil.isEmpty(transIdStr)) {
            return false;
        }
        try {
            contract = (Contract) contractDao.get(wfOrder.getContractId());
            if (contract == null) {
                return false;
            }
            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            String[] transIds = transIdStr.split(";");
            Double total = 0.00;
            for (String transId : transIds) {
                if (StringUtil.isEmpty(transId)) {
                    continue;
                }
                WfOrderTrans trans = (WfOrderTrans) transDao.get(transId, session);
                if (trans == null) {
                    continue;
                }

                String transNum = request.getParameter(trans.getId() + "Num");
                if (!StringUtil.isEmpty(transNum)) {
                    try {
                        trans.setNum(Double.parseDouble(transNum));
                        total = total + trans.getNum();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

                // 设置检测报告
                String currentPath = "/customerfiles/store/" + store.getId() + "/wfp/" + DateUtil.getNoSpSysDateString();
                // 获取PDF文件路径转换为服务端路径
                String fiveSingle = FileUploadHelper.uploadFile(trans.getId() + "fiveSingle", currentPath, request, response);
                trans.setFiveSingle(fiveSingle);
                transDao.updateBean(trans, session);
            }

            wfOrder.setWfpTotal(total);
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
            wfOrder.setYkjType(contract.getYkjType());
            if (wfOrder.isCountType()) {
                wfOrder.setContractOriginal(new BigDecimal(contract.getYkjPrice()));
                wfOrder.setActualPrice(wfOrder.getContractOriginal());
                wfOrder.setTotalPrice(wfOrder.getActualPrice().multiply(new BigDecimal(wfOrder.getWfpTotal())));
                wfOrder.setRealPrice(wfOrder.getContractOriginal());
                if (!contract.getYkjType()) {
                    wfOrder.setRealTotalPrice(wfOrder.getTotalPrice());
                    wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PAY);
                } else {
                    wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PW);
                }
            } else {
                wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PW);
            }
            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote("确认收货");
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
     * App确认收货,因内部实现与pc端收货差异过大，故分两个方法实现
     */
    @Override
    public boolean queRenshApp(WfOrder wfOrder, Store store, List<FiveSingleModel> fiveSingleModels, HttpServletRequest request) {
        // 数据验证
        if (wfOrder == null || fiveSingleModels == null || fiveSingleModels.size() <= 0) {
            return false;
        }
        for (FiveSingleModel fiveSingleModel : fiveSingleModels) {
            if (StringUtil.isEmpty(fiveSingleModel.getTransId()) || StringUtil.isEmpty(fiveSingleModel.getPic()) || StringUtil.isEmpty(fiveSingleModel.getCarWeight() + "")) {
                return false;
            }
        }

        Contract contract = null;
        // 获取Session中的Store对象
        Session session = null;
        Transaction transaction = null;

        try {
            contract = (Contract) contractDao.get(wfOrder.getContractId());
            if (contract == null) {
                return false;
            }
            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            Double total = 0.00;
            for (FiveSingleModel fiveSingleModel : fiveSingleModels) {
                String tranId = fiveSingleModel.getTransId();
                WfOrderTrans trans = (WfOrderTrans) transDao.get(tranId, session);
                if (trans == null) {
                    continue;
                }

                double transNum = fiveSingleModel.getCarWeight();
                trans.setNum(transNum);
                total = total + trans.getNum();

                // 设置检测报告
                String picStream = fiveSingleModel.getPic();
                String currentPath = "/customerfiles/store/" + store.getId() + "/wfp/" + DateUtil.getNoSpSysDateString();
                // 获取PDF文件路径转换为服务端路径
                String fiveSingle = FileUtil.baseStringToImage(request, picStream, currentPath);
                trans.setFiveSingle(fiveSingle);
                transDao.updateBean(trans, session);
            }

            wfOrder.setWfpTotal(total);
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
            wfOrder.setYkjType(contract.getYkjType());
            if (wfOrder.isCountType()) {
                wfOrder.setContractOriginal(new BigDecimal(contract.getYkjPrice()));
                wfOrder.setActualPrice(wfOrder.getContractOriginal());
                wfOrder.setTotalPrice(wfOrder.getActualPrice().multiply(new BigDecimal(wfOrder.getWfpTotal())));
                wfOrder.setRealPrice(wfOrder.getContractOriginal());
                if (!contract.getYkjType()) {
                    wfOrder.setRealTotalPrice(wfOrder.getTotalPrice());
                    wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PAY);
                } else {
                    wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PW);
                }
            } else {
                wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PW);
            }
            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote("确认收货");
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
     * 支付预付款
     */
    @Override
    public boolean payYFPrice(OrderPayLog payLog, HttpServletRequest request) {
        if (payLog == null) {
            return false;
        }

        // 获取Session中的Store对象
        Store store = null;
        if (request != null) {
            store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        }
        Session session = null;
        Transaction transaction = null;
        try {
            WfOrder wfOrder = (WfOrder) wfOrderDao.get(payLog.getOrderId());

            if (wfOrder == null) {
                return false;
            }

            if (wfOrder.getStatus().equals(WfOrderHelper.WFORDER_STATUS_WAIT_FH) && wfOrder.getPayStatus().equals("1")) {
                return true;
            }

            String note = "支付预付款";
            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            if (payLog.getPrice().doubleValue() > 0) {
                wfOrder.setPayPrice(payLog.getPrice());
            } else {
                wfOrder.setPayPrice(new BigDecimal(0).subtract(payLog.getPrice()));
            }
            // if (wfOrder.getBuyId().equals(payLog.getUserId())) {
            // wfOrder.setPayPrice(payLog.getPrice());
            // } else {
            // wfOrder.setPayPrice(new
            // BigDecimal(0).subtract(payLog.getPrice()));
            // }
//			if(OrderPayLog.PAYTYPE_UL.equals(payLog.getPayType())){
//				wfOrder.setPayStatus("0");
//				wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PAYYF_SURE);
//				wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
//			}else {
            wfOrder.setPayStatus("1");
            wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_FH);
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
//			}
            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store != null ? store.getId() : wfOrder.getBuyId());
            orderLog.setCreatorName(store != null ? store.getStoreName() : wfOrder.getBuyName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote(note);
            orderLogDao.addBean(orderLog, session);

            payLogDao.updateBean(payLog, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
     * 确认价格
     */
    public boolean queRenPrice(WfOrder wfOrder, HttpServletRequest request) {
        if (wfOrder == null) {
            return false;
        }
        // 获取Session中的Store对象
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        Session session = null;
        Transaction transaction = null;

        try {
            String note = "确认订单结算信息";
            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            wfOrder.setSaleSureStatus(true);
            wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_PAY);
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());

            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote(note);
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();

            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
     * 取消訂單 （修改status為00）
     *
     * @param wfOrder
     * @return
     */
    public boolean orderSure(WfOrder wfOrder, String pageType, String[] transStoreIds, HttpServletRequest request) {
        if (wfOrder == null) {
            return false;
        }
        // 获取Session中的Store对象
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        if (wfOrder != null) {
            Session session = null;
            Transaction transaction = null;
            try {
                Contract contract = (Contract) contractDao.get(wfOrder.getContractId());
                String note = "";
                session = wfOrderDao.createSession();
                transaction = session.beginTransaction();
                if ("1".equals(pageType) && "0".equals(contract.getPartyAORpartyB())) {
                    note = "选择运输企业并";
                    WfOrderHelper.saveOrUpdateOrderTrans(true, transStoreIds, wfOrder, session, transDao, request);
                }
                wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_TRANS_SURE);
                wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
                String payBankId = request.getParameter("payBankId");
                if (!StringUtil.isEmpty(payBankId)) {
                    wfOrder.setPayBankId(payBankId);
                }
                wfOrderDao.updateBean(wfOrder, session);

                WfOrderLog orderLog = new WfOrderLog();
                orderLog.setCreateTime(DateUtil.getSysDateTimeString());
                orderLog.setCreator(store.getId());
                orderLog.setCreatorName(store.getStoreName());
                orderLog.setWfOrderId(wfOrder.getId());
                note = note + "确认订单信息";
                orderLog.setNote(note);
                orderLogDao.addBean(orderLog, session);

                session.flush();
                transaction.commit();
                session.close();
                return true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        }
        return false;

    }

    /**
     * 校验订单是否存在品位
     *
     * @param wfOrderId
     * @param session
     * @return
     */
    public boolean checkOrderHasContents(String wfOrderId, Session session) {
        if (StringUtil.isEmpty(wfOrderId)) {
            return false;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select wp.id from WfOrderPrice as wp where 1=1");
        sql.append(" and wp.wfOrderId = :wfOrderId");
        map.put("wfOrderId", wfOrderId);

        try {
            List<String> prices = null;
            if (session != null) {
                prices = priceDao.list(sql.toString(), map, session);
            } else {
                prices = priceDao.list(sql.toString(), map);
            }
            if (prices != null && prices.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    /**
     * 上传品位化验单 1、添加wfOrderPrice表 2、
     *
     * @param
     * @param wfOrder
     * @param request
     * @return
     */
    public boolean uploadPW(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response) {
        if (wfOrder == null) {
            return false;
        }
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        Session session = null;
        Transaction transaction = null;
        try {

            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            WfOrderHelper.saveOrUpdateOrderContracts(true, wfOrder, session, priceDao, null, request, response);
            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            if (WfOrderHelper.WFORDER_STATUS_WAIT_SURE_PW.equals(wfOrder.getStatus())) {
                orderLog.setNote("修改最终化验品位信息并确认最终价格信息");
            } else {
                orderLog.setNote("提交最终化验品位信息并确认最终价格信息");
            }

            wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_SURE_PRICE);
            wfOrder.setBuySureStatus(true);
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
            orderLogDao.addBean(orderLog, session);

            wfOrderDao.updateBean(wfOrder, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
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

    public boolean jieSuan(WfOrder wfOrder, HttpServletRequest request) {
        if (wfOrder == null) {
            return false;
        }
        // 获取Session中的Store对象
        Store store = null;
        if (request != null) {
            store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        }
        Session session = null;
        Transaction transaction = null;

        try {
            String note = "结算";
            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            wfOrder.setPayStatus("2");
            wfOrder.setPayPrice(wfOrder.getTotalPrice());
            wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_FINISH);
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());

            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store != null ? store.getId() : wfOrder.getBuyId());
            orderLog.setCreatorName(store != null ? store.getStoreName() : wfOrder.getBuyName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote(note);
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();

            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
     * 选择退款账户
     *
     * @return
     */
    public boolean changeReturnPayId(WfOrder wfOrder, String returnPayId, String type, HttpServletRequest request) {

        if (wfOrder == null) {
            return false;
        }
        // 获取Session中的Store对象
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        Session session = null;
        Transaction transaction = null;

        try {
            String note = "";

            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();

            wfOrder.setReturnPayId(returnPayId);
            if ("0".equals(type)) {
                note = "选择退还多余预付金收款账户";
                wfOrder.setListStatus(WfOrderHelper.WFORDER_LIST_PRICE_STATUS_SHENQ_WAIT);
            } else if ("1".equals(type)) {
                note = "申请退还多余预付金";
                wfOrder.setListStatus(WfOrderHelper.WFORDER_LIST_PRICE_STATUS_SHENQ);
            } else {
                note = "将多余预付金计入下次订单";
                wfOrder.setStatus(WfOrderHelper.WFORDER_STATUS_WAIT_FINISH);
                wfOrder.setListStatus("0");
                IScrapPricesDao pricesDao = SpringContextHolder.getBean(IScrapPricesDao.class);
                ScrapPrices prices = new ScrapPrices();
                prices.setListPrice(wfOrder.getPayPrice().subtract(wfOrder.getTotalPrice()));
                prices.setOrderId(wfOrder.getId());
                prices.setPrice(prices.getListPrice());
                prices.setSalStoreId(wfOrder.isYfType() ? wfOrder.getBuyId() : wfOrder.getSaleId());
                prices.setPayStoreId(wfOrder.isYfType() ? wfOrder.getSaleId() : wfOrder.getBuyId());
                prices.setScrapId(wfOrder.getScrapId());
                prices.setStatus("1");
                prices.setCreateTime(DateUtil.getSysDateTimeString());
                pricesDao.addBean(prices, session);
            }

            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());

            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote(note);
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();

            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
     * 拒绝退款
     *
     * @return
     */
    public boolean jujueTk(WfOrder wfOrder, HttpServletRequest request) {

        if (wfOrder == null) {
            return false;
        }
        // 获取Session中的Store对象
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        Session session = null;
        Transaction transaction = null;

        try {
            String note = "驳回退款申请";

            session = wfOrderDao.createSession();
            transaction = session.beginTransaction();
            wfOrder.setListStatus(WfOrderHelper.WFORDER_LIST_PRICE_STATUS_SHENQ_JJ);
            wfOrder.setUpdateTime(DateUtil.getSysDateTimeString());
            wfOrderDao.updateBean(wfOrder, session);

            WfOrderLog orderLog = new WfOrderLog();
            orderLog.setCreateTime(DateUtil.getSysDateTimeString());
            orderLog.setCreator(store.getId());
            orderLog.setCreatorName(store.getStoreName());
            orderLog.setWfOrderId(wfOrder.getId());
            orderLog.setNote(note);
            orderLogDao.addBean(orderLog, session);

            session.flush();
            transaction.commit();
            session.close();

            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;

    }

    @Override
    public boolean wfOrderBCSPay(String wfOrderId, Store store, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request) {
        // TODO Auto-generated method stub

        WfOrder order = getWfOrderById(wfOrderId);
        String type = "1";
        BigDecimal payPrice = null;
        if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
            payPrice = order.getYfPrice();
            type = "1";
        } else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
            payPrice = order.getTotalPrice().subtract(order.getPayPrice());
            if (payPrice.doubleValue() <= 0) {
                payPrice = new BigDecimal(0.00).subtract(payPrice);
            }
            type = "2";
        } else if (OrderPayLog.PAY_TYPE_JSTK.equals(payType)) {
            payPrice = order.getPayPrice().subtract(order.getTotalPrice());
            if (payPrice.doubleValue() <= 0) {
                payPrice = new BigDecimal(0.00).subtract(payPrice);
            }
            type = "2";
        }

        String receiveId = null;
        if (store.getId().equals(order.getBuyId())) {
            receiveId = order.getSaleId();
        } else {
            receiveId = order.getBuyId();
        }

        IAccountLogService accountLogService = SpringContextHolder.getBean(IAccountLogService.class);

        Boolean payStatus = accountLogService.payForOrder(store.getId(), receiveId, order.getId(), type, payPrice);
        if (!payStatus) {
            return false;
        }

        IOrderPayLogService payLogService = SpringContextHolder.getBean(IOrderPayLogService.class);
        OrderPayLog payLog = new OrderPayLog();
        payLog.setOrderId(order.getId());
        payLog.setPrice(payPrice);
        payLog.setUserId(store.getId());
        payLog.setStoreId(store.getId().equals(order.getSaleId()) ? order.getBuyId() : order.getSaleId());
        payLog.setOrderType(OrderPayLog.ORDER_TYPE_WF);
        payLog.setCreateTime(DateUtil.getSysDateTimeString());
        payLog.setPayType(payType);
        payLog.setBankId(bankId);
        payLog.setPayPattern(payPattern);
        payLog.setStatus("1");
        payLogService.saveOrderPayLog(payLog);

        if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
            return payYFPrice(payLog, request);
        } else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
            return jieSuan(order, request);
        } else {
            return jieSuan(order, request);
        }
    }


    @Override
    public boolean wfOrderCCBPay(String wfOrderId, BigDecimal payPrice) {
        return restfulPayConfirm(wfOrderId,payPrice);
    }

    /**
     * 支付确认，用于第三方支付成功后修改订单状态
     * 注：改方法满足幂等性，即多次执行返回一致结果
     * @return
     */
    private boolean restfulPayConfirm(String wfOrderId, BigDecimal payPrice) {
        WfOrder order = getWfOrderById(wfOrderId);

        // 根据id来判断当前支付类型
        String payType = this.getPayTypeById(wfOrderId);

        /*
         * 如果订单已进入完成状态，直接返回已完成
         * 以满足文档中："商户订单系统对于某笔订单已经收到成功应答，有可能后续再次收到 该笔订单的成功应答，则商户只需要回应九派支付平台收到成功即可，商户的账 务数据不应被修改。"的幂等性需求
         */
        if (WfOrderHelper.WFORDER_STATUS_WAIT_FH.equals(order.getStatus()) || WfOrderHelper.WFORDER_STATUS_WAIT_FINISH.equals(order.getStatus())) {
            return true;
        } else {
            IOrderPayLogService payLogService = SpringContextHolder.getBean(IOrderPayLogService.class);
            OrderPayLog payLog = new OrderPayLog();
            payLog.setOrderId(order.getId());
            payLog.setPrice(payPrice);
            payLog.setUserId(order.getBuyId());
            payLog.setStoreId(order.getSaleId());
            payLog.setOrderType(OrderPayLog.ORDER_TYPE_WF);
            payLog.setCreateTime(DateUtil.getSysDateTimeString());
            payLog.setPayType(payType);
            payLog.setBankId(null);
            payLog.setPayPattern(null);
            payLog.setPayCert(null);
            payLog.setStatus("1");
            payLogService.saveOrderPayLog(payLog);

            if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
                return payYFPrice(payLog, null);
            } else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
                return jieSuan(order, null);
            } else {
                return jieSuan(order, null);
            }
        }
    }

    /**
     * 订单付款(线下付款,上传凭证)
     *
     * @param wfOrderId
     * @param store
     * @param payType
     * @return
     */
    public boolean wfOrderULPay(String wfOrderId, String payCert, Store store, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request) {
        WfOrder order = getWfOrderById(wfOrderId);
        BigDecimal payPrice = null;
        if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
            payPrice = order.getYfPrice();
        } else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
            payPrice = order.getTotalPrice().subtract(order.getPayPrice());
            if (payPrice.doubleValue() <= 0) {
                payPrice = new BigDecimal(0.00).subtract(payPrice);
            }
        } else if (OrderPayLog.PAY_TYPE_JSTK.equals(payType)) {
            payPrice = order.getPayPrice().subtract(order.getTotalPrice());
            if (payPrice.doubleValue() <= 0) {
                payPrice = new BigDecimal(0.00).subtract(payPrice);
            }
        }

        IOrderPayLogService payLogService = SpringContextHolder.getBean(IOrderPayLogService.class);
        OrderPayLog payLog = new OrderPayLog();
        payLog.setOrderId(order.getId());
        payLog.setPrice(payPrice);
        payLog.setUserId(store.getId());
        payLog.setStoreId(store.getId().equals(order.getSaleId()) ? order.getBuyId() : order.getSaleId());
        payLog.setOrderType(OrderPayLog.ORDER_TYPE_WF);
        payLog.setCreateTime(DateUtil.getSysDateTimeString());
        payLog.setPayType(payType);
        payLog.setBankId(bankId);
        payLog.setPayPattern(payPattern);
        payLog.setPayCert(payCert);
        payLog.setStatus("1");
        payLogService.saveOrderPayLog(payLog);

        if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
            return payYFPrice(payLog, request);
        } else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
            return jieSuan(order, request);
        } else {
            return jieSuan(order, request);
        }
    }

    /**
     * 订单付款(在线支付)
     *
     * @param wfOrderId
     * @param store
     * @param payType
     * @return
     */
    public boolean wfOrderOLPay(String wfOrderId, String storeId, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request) {
        WfOrder order = getWfOrderById(wfOrderId);
        BigDecimal payPrice = null;
        if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
            payPrice = order.getYfPrice();
        } else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
            payPrice = order.getTotalPrice().subtract(order.getPayPrice());
            if (payPrice.doubleValue() <= 0) {
                payPrice = new BigDecimal(0.00).subtract(payPrice);
            }
        } else if (OrderPayLog.PAY_TYPE_JSTK.equals(payType)) {
            payPrice = order.getPayPrice().subtract(order.getTotalPrice());
            if (payPrice.doubleValue() <= 0) {
                payPrice = new BigDecimal(0.00).subtract(payPrice);
            }
        }

        IOrderPayLogService payLogService = SpringContextHolder.getBean(IOrderPayLogService.class);
        OrderPayLog payLog = new OrderPayLog();
        payLog.setOrderId(order.getId());
        payLog.setPrice(payPrice);
        payLog.setUserId(storeId);
        payLog.setStoreId(storeId.equals(order.getSaleId()) ? order.getBuyId() : order.getSaleId());
        payLog.setOrderType(OrderPayLog.ORDER_TYPE_WF);
        payLog.setCreateTime(DateUtil.getSysDateTimeString());
        payLog.setPayType(payType);
        payLog.setBankId(bankId);
        payLog.setPayPattern(payPattern);
//		payLog.setPayCert(payCert);
        payLog.setStatus("1");
        payLogService.saveOrderPayLog(payLog);

        if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
            return payYFPrice(payLog, request);
        } else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
            return jieSuan(order, request);
        } else {
            return jieSuan(order, request);
        }
    }

    @Override
    public List<WfOrderTransDriver> getTransDrivers(String transId, String wfOrderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select driver from WfOrderTransDriver as driver where 1=1");
        sql.append(" and driver.wfOrderTransId = :wfOrderTransId");
        map.put("wfOrderTransId", transId);
        sql.append(" and driver.wfOrderId = :wfOrderId");
        map.put("wfOrderId", wfOrderId);

        try {
            List<WfOrderTransDriver> transDrivers = driverDao.list(sql.toString(), map);
            return transDrivers;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getAllInTransitIdList() {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT ts.carInfoId FROM WfOrderTrans ts,WfOrder od WHERE 1=1");
        sql.append(" AND od.id = ts.wfOrderId");
        // 待收货：在运
        sql.append(" AND od.status = '" + WfOrderHelper.WFORDER_STATUS_WAIT_SH + "'");
        // 去重
        sql.append(" GROUP BY ts.carInfoId");

        try {
            List<String> wfOrderTrans = driverDao.list(sql.toString(), map);
            return wfOrderTrans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean wfOrderOLJiudingPay(String wfOrderId, BigDecimal payPrice) {
        return restfulPayConfirm(wfOrderId,payPrice);
    }

    @Override
    public String getPayTypeById(String orderId) {
        String payType = null;
        try {
            WfOrder order = (WfOrder) wfOrderDao.get(orderId);
            if (WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF.equals(order.getStatus())) {
                payType = OrderPayLog.PAY_TYPE_YF;
            } else if (WfOrderHelper.WFORDER_LIST_PRICE_STATUS_SHENQ.equals(order.getListStatus()) || WfOrderHelper.WFORDER_LIST_PRICE_STATUS_SHENQ_WAIT.equals(order.getListStatus())) {
                payType = OrderPayLog.PAY_TYPE_JS;
            } else {
                payType = OrderPayLog.PAY_TYPE_JSTK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payType;
    }

    @Override
    public BigDecimal getPayPrice(String orderId, String payType) {
        BigDecimal payPrice = new BigDecimal("0");
        try {
            WfOrder order = (WfOrder) wfOrderDao.get(orderId);

            if (OrderPayLog.PAY_TYPE_YF.equals(payType)) {
                payPrice = order.getYfPrice();
            } else if (OrderPayLog.PAY_TYPE_JS.equals(payType)) {
                payPrice = order.getTotalPrice().subtract(order.getPayPrice()).abs();
            } else if (OrderPayLog.PAY_TYPE_JSTK.equals(payType)) {
                payPrice = order.getPayPrice().subtract(order.getTotalPrice()).abs();
            }
            System.out.println("orderId:"+orderId);
            System.out.println("payType:"+payType);
            System.out.println("payPrice:"+payPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payPrice;
    }
}
