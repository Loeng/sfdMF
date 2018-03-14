package com.ekfans.plugin.cache.service.user.addressCache;

import java.util.List;

import com.ekfans.base.user.model.UserAddress;
import com.ekfans.base.user.service.IUserAddressService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.utils.user.addressCache.UserAddressCacheUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: UserAddressCacheService
* @Description: TODO(会员中心收货地址列表的查询缓存)
* @author 成都易科远见科技有限公司
* @date 2014-5-26 下午03:31:45
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("unchecked")
public class UserAddressCacheService implements IUserAddressCacheService {

    /**
     * 地址簿-地址列表
     */
    @Override
    public List<UserAddress> findAllUserAddresses(String userId) {
	// 如果userId为null,则返回null
	if(StringUtil.isEmpty(userId)){
	    return null;
	}
	// 获取查询当前会员的全部地址的key
	String key = 
	    UserAddressCacheUtil.getUserAddressesKey(userId);
	// 调用方法、先尝试从缓存中取
	List<UserAddress> userAddresses = 
	    (List<UserAddress>)Cache.engine.get(key);
	
	// 判断是否存缓存中取到值  如果没取到就调用service从数据库查询出来、并将其放入缓存
	if(userAddresses==null || userAddresses.size()==0){
	    IUserAddressService userAddressService = 
		SpringContextHolder.getBean(IUserAddressService.class);
	    userAddresses = userAddressService.listAddress(userId);
	    Cache.engine.add(key,userAddresses);
	}
	return userAddresses;
    }

    /**
     * 查询出当前用户的某一地址信息
     */
    @Override
    public UserAddress getUserAddressById(String userId,String addressId) {
	if(StringUtil.isEmpty(userId) || StringUtil.isEmpty(addressId)){
	    return null;
	}
	// 得到全部的userAddress
	List<UserAddress> userAddresses = 
	                  findAllUserAddresses(userId);
	if(userAddresses != null && userAddresses.size()>0){
	    for(UserAddress userAddress:userAddresses){
		if(addressId.equals(userAddress.getId())){
		    return userAddress;
		}
	    }   
	}
	return null;
    }

    /**
     * 涉及增删改时需要刷新缓存
     */
    @Override
    public void refreshUserAddressCache(String userId) {
	String key = 
	    UserAddressCacheUtil.getUserAddressesKey(userId);
	// 删除原有缓存
	Cache.engine.remove(key);
	IUserAddressService userAddressService = 
	    SpringContextHolder.getBean(IUserAddressService.class);
	Cache.engine.add(key,userAddressService.listAddress(userId));
    }
    
}
