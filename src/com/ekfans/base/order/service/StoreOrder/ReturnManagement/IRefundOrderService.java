package com.ekfans.base.order.service.StoreOrder.ReturnManagement;

import java.util.List;

import com.ekfans.base.order.model.OrderTreatDetail;
import com.ekfans.base.order.model.Refund;
import com.ekfans.base.product.model.Product;
import com.ekfans.pub.util.Pager;

public interface IRefundOrderService {

    /**
     * 根据用户id查询该用户的所有的退换货信息
     */
    public List<Refund> getRefundOrder(Pager pager, String userId, String orderId, String beginDate, String endDate);
    
    /**
     * 根据企业id获取退换货信息
     * 
     * @param pager 分页
     * @param storeId 企业id
     * @param orderId 订单号
     * @return  List<Refund>
     */
    public List<Refund> getHXQRefundOrder(Pager pager, String storeId, String orderId);
    
    /**
     * 
    * @Title: refundOrExChengeProduct
    * @Description: TODO(退换货处理)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param refundType
    * @param @param refundId
    * @param @param sellerStatus
    * @param @param reason
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean refundOrExChengeProduct(String refundId, String sellerStatus, String reason);
     
    /**
     * 
    * @Title: getOrderDetailByOrderId
    * @Description: TODO(退换货的商品所在订单的详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param orderId
    * @param @return    设定文件
    * @return Object[]    返回类型
    * @throws
     */
    public Object[] getOrderDetailByOrderId(String orderId);
    
    /**
     * 
    * @Title: getProdyctById
    * @Description: TODO(退换货商品的商品详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param productId
    * @param @return    设定文件
    * @return Product    返回类型
    * @throws
     */
    public Product getProdyctById(String productId);
    
    /**
     * 
    * @Title: getRefundById
    * @Description: TODO(根据refundId获得refund的详情信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param refundId
    * @param @return    设定文件
    * @return Refund    返回类型
    * @throws
     */
    public Refund getRefundById(String refundId);
    /**
     * 
    * @Title: getRefundByProductId
    * @Description: TODO 用户中心根据商品的Id进行申请退换货
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param productId
    * @param @return    设定文件
    * @return Refund    返回类型
    * @throws
     */
    public Refund getRefundByProductId(String productId);
    
    /**
     * 
    * @Title: findOrderTreatDetailByOrderId
    * @Description: TODO 根据orderId得到订单日志列表
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param orderId
    * @param @return    设定文件
    * @return List<OrderTreatDetail>    返回类型
    * @throws
     */
    public List<OrderTreatDetail> findOrderTreatDetailByOrderId(String orderId);
    
    /**
     * 
    * @Title: aggreeRefuns
    * @Description: TODO(批量处理退货/换货;注意批量处理退货/换货,只能是同意退货/换货;如果不同意需要单个
    *                    说明,不能进行批量操作)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param refundIds
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean aggreeRefuns(List<String> refundIds);
}
