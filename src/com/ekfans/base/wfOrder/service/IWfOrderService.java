package com.ekfans.base.wfOrder.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.wfOrder.model.ContractCars;
import com.ekfans.base.wfOrder.model.OrderPayLog;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.model.WfOrderLog;
import com.ekfans.base.wfOrder.model.WfOrderTransDriver;
import com.ekfans.base.wfOrder.util.WfOrderTransStore;
import com.ekfans.plugin.wftong.controller.model.FiveSingleModel;
import com.ekfans.pub.util.Pager;

public interface IWfOrderService {

    /**
     * 根据危废申报ID，订单ID获取该申报单剩余数量
     *
     * @param scrapWfpId
     * @return
     */
    public Double getSurplusQuantityByScrapWfpId(String scrapWfpId, String wfOrderId);

    /**
     * 新增或修改危废品订单
     *
     * @param wfOrder
     * @param request
     * @param response
     * @return
     */
    public Boolean saveOrUpdateWfOrder(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response);

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
    public List<WfOrder> getWfOrderList(String scrapId, String contractId, String wfName, String orderStatus, String status, String startTime, String endTime, String orderId,
                                        String saleName, String buyName, Pager pager, String viewType, String storeId);

    /***
     * 根据危废订单id获取危废订单对象
     *
     * @param wfOrderId
     * @return
     */
    public WfOrder getWfOrderById(String wfOrderId);

    /**
     * 根据危废订单ID获取运输企业详情
     *
     * @param wfOrderId
     * @return
     */
    public Map<String, Boolean> getWfOrderTrans(String wfOrderId);

    /**
     * 获取危废订单所关联的运输企业信息
     *
     * @param wfOrderId
     * @return
     */
    public List<WfOrderTransStore> getWfOrderTransList(String wfOrderId);

    /**
     * 获取车辆关联的司机
     *
     * @param wfOrderTransId
     * @return
     */
    public List<WfOrderTransDriver> getTransDriverList(String wfOrderTransId);

    /**
     * 获取订单日志
     *
     * @param wfOrderId
     * @return
     */
    public List<WfOrderLog> getOrderLog(String wfOrderId);

    /**
     * 取消訂單 （修改status為00）
     *
     * @param wfOrder
     * @return
     */
    public boolean wfOrderCancel(WfOrder wfOrder, HttpServletRequest request);

    /**
     * 取消訂單
     */
    public boolean quXiao(WfOrder wfOrder, Store store);

    /**
     * 确认订单 （修改status為00）
     *
     * @param wfOrder
     * @return
     */
    public boolean orderSure(WfOrder wfOrder, String pageType, String[] transStoreIds, HttpServletRequest request);

    /**
     * 获取危废订单所关联的运输企业信息
     *
     * @param wfOrderId
     * @return
     */
    public Boolean getWfOrderTransLists(String wfOrderId, String storeId, HttpServletRequest request);

    /**
     * 根据合同ID、车辆信息表ID获取合同车辆信息对象
     *
     * @param contractId
     * @param carInfoId
     * @return
     */
    public ContractCars getContractCar(String contractId, String carInfoId, Session session);

    /**
     * 危废品订单运输企业选择车辆信息
     *
     * @param wfOrderId
     * @return
     */
    public Boolean updateWfOrderTrans(String wfOrderId, HttpServletRequest request);

    /**
     * 支付预付款
     *
     * @param wfOrderId
     * @param request
     * @return
     */
    public boolean payYFPrice(OrderPayLog payLog, HttpServletRequest request);

    /**
     * 确认发货 1、上传五联单（修改wf_order_trans表） 2、修改wfOrder.status为04
     *
     * @return
     */
    public boolean queRenfh(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response);

    /**
     * 发货 1、上传五联单（修改wf_order_trans表） 2、修改wfOrder.status为04
     *
     * @return
     */
    public boolean faHuo(WfOrder wfOrder, Store store);

    /**
     * 确认收货
     *
     * @param wfOrder
     * @param request
     * @return
     */
    public boolean queRensh(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response);

    /**
     * 校验订单是否存在品位
     *
     * @param wfOrderId
     * @param session
     * @return
     */
    public boolean checkOrderHasContents(String wfOrderId, Session session);

    /**
     * 上传品位化验单 1、添加wfOrderPrice表 2、
     *
     * @param wfOrderPrice
     * @param wfOrder
     * @param request
     * @return
     */
    public boolean uploadPW(WfOrder wfOrder, HttpServletRequest request, HttpServletResponse response);

    /**
     * 确认价格
     *
     * @return
     */
    public boolean queRenPrice(WfOrder wfOrder, HttpServletRequest request);

    /**
     * 结算
     *
     * @return
     */
    public boolean jieSuan(WfOrder wfOrder, HttpServletRequest request);

    /**
     * 选择退款账户
     *
     * @return
     */
    public boolean changeReturnPayId(WfOrder wfOrder, String returnPayId, String type, HttpServletRequest request);

    /**
     * 拒绝退款
     *
     * @return
     */
    public boolean jujueTk(WfOrder wfOrder, HttpServletRequest request);

    /**
     * 订单付款
     *
     * @param wfOrderId
     * @param store
     * @param payType
     * @return
     */
    public boolean wfOrderBCSPay(String wfOrderId, Store store, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request);

    /**
     * 订单付款(线下付款,上传凭证)
     *
     * @param wfOrderId
     * @param store
     * @param payType
     * @return
     */
    public boolean wfOrderULPay(String wfOrderId, String payCert, Store store, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request);

    /**
     * 订单付款(在线支付)
     *
     * @param wfOrderId
     * @param storeId
     * @param payType
     * @return
     */
    public boolean wfOrderOLPay(String wfOrderId, String storeId, String orderType, String payType, String payPattern, String bankId, HttpServletRequest request);

    /**
     * 根据transId，orderId查找司机信息
     *
     * @param transId
     * @param orderId
     * @return
     */
    public List<WfOrderTransDriver> getTransDrivers(String transId, String wfOrderId);

    /**
     * App确认收货
     *
     * @param wfOrder
     * @param storeId
     * @param fiveSingleModels
     * @param request
     * @return
     */
    boolean queRenshApp(WfOrder wfOrder, Store store, List<FiveSingleModel> fiveSingleModels, HttpServletRequest request);

    /**
     * 获取所有在运车辆信息ID
     *
     * @return
     */
    public List<String> getAllInTransitIdList();

    /**
     * 九鼎支付通过
     *
     * @param orderId
     * @return
     */
    boolean wfOrderOLJiudingPay(String orderId, BigDecimal payPrice);

    /**
     * 查询危费订单类型
     *
     * @param orderId
     * @return
     */
    String getPayTypeById(String orderId);

    /**
     * 查询危费订单金额
     * @param orderId
     * @param payType
     * @return
     */
    BigDecimal getPayPrice(String orderId, String payType);

    /**
     * 建设银行支付
     * @param wfOrderId
     * @param payPrice
     * @return
     */
    boolean wfOrderCCBPay(String wfOrderId, BigDecimal payPrice);

}
