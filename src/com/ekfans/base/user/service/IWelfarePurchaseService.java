package com.ekfans.base.user.service;

import java.util.List;

import com.ekfans.base.user.model.WelfarePurchase;
import com.ekfans.pub.util.Pager;


/**
 * 
* @ClassName: IWelfarePurchaseService
* @Description: TODO 福利采购service接口
* @author ekfans
* @date 2014-11-21 上午9:34:04
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IWelfarePurchaseService {
    /**
     * 
    * @Title: save
    * @Description: TODO 保存福利采购意向
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param purchase
    * @return boolean 返回类型
    * @throws
     */
    public boolean save(WelfarePurchase purchase);
    
    /**
     * 
    * @Title: list
    * @Description: TODO 福利采购列表
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pager
    * @param storeId
    * @param productNo
    * @param companyName
    * @param beginTime
    * @param endTime
    * @return List<WelfarePurchase> 返回类型
    * @throws
     */
    public List<WelfarePurchase> list(Pager pager,String storeId,String productNo,String companyName,String beginTime,String endTime);

    /**
     * 
    * @Title: getPurchaseById
    * @Description: TODO 根据id获取福利采购详情
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return WelfarePurchase 返回类型
    * @throws
     */
    public WelfarePurchase getPurchaseById(String id);
    
    /**
     * 
    * @Title: systemGetPurchaseById
    * @Description: TODO 系统后台查看福利采购
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return WelfarePurchase 返回类型
    * @throws
     */
    public WelfarePurchase systemGetPurchaseById(String id);
    
    /**
     * 
    * @Title: update
    * @Description: TODO 更新福利采购
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param purchase
    * @return boolean 返回类型
    * @throws
     */
    public boolean update(WelfarePurchase purchase);
}
