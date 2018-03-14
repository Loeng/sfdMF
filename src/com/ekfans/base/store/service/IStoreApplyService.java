package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.StoreApply;
import com.ekfans.pub.util.Pager;

/**
 * 
* @ClassName: IStoreApplyService
* @Description: TODO(商户申请实现Service接口)
* @author ekfans
* @date 2014-5-16 上午11:34:07
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreApplyService {
    /**
     * 
    * @Title: add
    * @Description: TODO(添加)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return boolean 返回类型
    * @throws
     */
    public boolean addStoreApply(StoreApply storeApply);
    
    /**
     * 
    * @Title: listAll
    * @Description: TODO(后台查询出所有的店铺申请信息及分页)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pager
    * @param operation
    * @param status
    * @param beginDate
    * @param endDate
    * @return List<StoreApply> 返回类型
    * @throws
     */
    public List<StoreApply> listAll(Pager pager,String operation,String status,String beginDate,String endDate);
    
    /**
     * 
    * @Title: deteteStoreApplyByID
    * @Description: TODO(根据id删除申请店铺的信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return boolean 返回类型
    * @throws
     */
    public boolean deteteStoreApplyByID(String id);
    
    /**
     * 
    * @Title: getStoreApplyById
    * @Description: TODO(根据id得到申请的信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return StoreApply 返回类型
    * @throws
     */
    public StoreApply getStoreApplyById(String id);
    
    /**
     * 
    * @Title: updateStatus
    * @Description: TODO(修改申请店铺信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param storeApply
    * @return StoreApply 返回类型
    * @throws
     */
    public boolean updateStatus(StoreApply storeApply);
    
    /**
     * 
    * @Title: judgmentRepeat
    * @Description: TODO 判断企业名是否重复
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param name
    * @return boolean 返回类型
    * @throws
     */
    public boolean judgmentRepeat(String name);
}
