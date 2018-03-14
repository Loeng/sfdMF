package com.ekfans.base.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.IUserInfoDao;
import com.ekfans.base.user.model.UserInfo;
import com.ekfans.pub.util.StringUtil;

/**
 * 企业详细信息Service接口实现
 * 
 * @ClassName: UserInfoService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class UserInfoService implements IUserInfoService {
	
	private Logger log = LoggerFactory.getLogger(UserInfoService.class);
	@Autowired
	private IUserInfoDao userInfoDao;

	/**
	 * 更新会员详细信息对象
	 * @throws Exception 
	 */
	@Override
	public boolean updateUser(UserInfo userInfo) throws Exception {
	    if(userInfo == null){
		return false;
	    }
	    userInfoDao.updateBean(userInfo);
	    return true;
	}

	/**
	 * 根据用户id获取用户详细信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UserInfo getUserInfoByUserId(String userId) {
		if(StringUtil.isEmpty(userId)){
			return null;
		}
		
		// param data
		Map<String,Object> map = new HashMap<String,Object>();
		// hql
		String hql = "from UserInfo where userId=:userId";
	    // setting data
	    map.put("userId",userId);
	    
	    try {
	    	List<UserInfo> list = userInfoDao.list(hql,map);
	    	if(list != null && list.size() > 0){
	    		return list.get(0);
	    	}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	    return null;
	}

	/**
	 * 通过ID主键查找获取UserInfo对象
	 * @throws Exception 
	 */
	@Override
	public UserInfo getUserInfoById(String id) throws Exception {
	    UserInfo userInfo =(UserInfo)userInfoDao.get(id);
	    if(userInfo ==null){
		return null;
	    }
	    return userInfo;
	}



}