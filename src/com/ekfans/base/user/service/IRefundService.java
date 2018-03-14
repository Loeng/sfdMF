package com.ekfans.base.user.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.order.model.Refund;
import com.ekfans.base.user.dto.RefundDto;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.Pager;

/**
 * 
* @ClassName: IRefundService
* @Description: TODO (退货/换货的 service接口)
* @author Eugene
* @date 2014-5-12 下午4:51:04
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IRefundService {
    
    /**
     * 
    * @Title: getRefund
    * @Description: TODO(获取会员 的退货 /换货的列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param orderID
    * @param freightSn
    * @param user
    * @return List<Refund> 返回类型
    * @throws
     */
    public List<RefundDto> getRefund(String orderID,String freightSn,User user,Pager pager);
    
    
    /**
     * 
    * @Title: getRefundInfo
    * @Description: TODO(获取退换货的信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param refundId
    * @return Map<String,Object> 返回类型
    * @throws
     */
    public Map<String,Object> getRefundInfo(String refundId);
    
    
    /**
     * 
    * @Title: addRefund  退/换货的理由保存
    * @Description: TODO 
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param refund
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean addRefund(Refund refund);
    /**
     * 
    * @Title: getRefundByProductId
    * @Description: TODO 根据productId取得Refund对象
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param productId
    * @param @return    设定文件
    * @return Refund    返回类型
    * @throws
     */
    public Refund getRefundByProductId(String productId);
}
