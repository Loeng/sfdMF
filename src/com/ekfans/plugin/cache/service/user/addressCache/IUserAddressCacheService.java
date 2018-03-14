package com.ekfans.plugin.cache.service.user.addressCache;

import java.util.List;

import com.ekfans.base.user.model.UserAddress;

/**
 * 
* @ClassName: IUserAddressCacheService
* @Description: TODO(会员中心-地址列表的查询缓存)
* @author 成都易科远见科技有限公司
* @date 2014-5-26 下午03:25:51
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IUserAddressCacheService {
    /**
     * 
    * @Title: findAllUserAddresses
    * @Description: TODO(地址簿-地址列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param userId
    * @param @return    设定文件
    * @return List<UserAddress>    返回类型
    * @throws
     */
    public List<UserAddress> findAllUserAddresses(String userId);
    
    /**
     * 
    * @Title: getUserAddressById
    * @Description: TODO(查询出当前用户的某一条地址信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param userId
    * @param @param addressId
    * @param @return    设定文件
    * @return UserAddress    返回类型
    * @throws
     */
    public UserAddress getUserAddressById(String userId,String addressId);
    
    /**
     * 
    * @Title: refreshUserAddressCache
    * @Description: TODO(刷新涉及修改操作<增、删、改>,更新当前用户的缓存)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param     设定文件
    * @return void    返回类型
    * @throws
     */
    public void refreshUserAddressCache(String userId);
    
}
