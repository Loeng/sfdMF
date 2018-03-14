package com.ekfans.base.order.service.StoreOrder.IndexAccount;

import java.util.List;
import com.ekfans.base.order.model.Order;
/**
 * 
* @ClassName: StoreIndexAccount
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 成都易科远见科技有限公司
* @date 2014-6-3 下午02:57:08
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreIndexAccountService {

    /**
     * 
    * @Title: getOrderSellAccount
    * @Description: TODO(商户中心-查询出 商店首页的销售结算状况信息-qxh)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param yd
    * @param @param storeID
    * @param @return    设定文件
    * @return List<Order>    返回类型
    * @throws
     */
    public List<Order> getOrderSellAccount(String yd,String storeID);
    
    /**
     * 
    * @Title: getOrderCount
    * @Description: TODO(各种类型的订单统计,暂时返回订单该种订单的数量)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param OrderStatus
    * @param @param shippingStatus
    * @param @param serviceStatus
    * @param @param refundStatus
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    public String getOrderCount(String orderStatus,boolean shippingStatus,
                                String userApp,String storeApp,
                                String refundStatus,String storeId);
}
