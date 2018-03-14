package com.ekfans.plugin.cache.utils.user.addressCache;
/**
 * 
* @ClassName: UserAddressCacheUtil
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 成都易科远见科技有限公司
* @date 2014-5-26 下午03:39:28
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class UserAddressCacheUtil {
    // 地址薄-查询当前用户收货地址KEY  全部
    public static final String USER_ADDRESSES_KEY = "/user/addresses/";
    
    // 当前用户的全部收货地址
    public static String getUserAddressesKey(String userId){
	return USER_ADDRESSES_KEY + userId;
    }
}
