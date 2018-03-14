package com.ekfans.base.user.dao;

import java.util.List;

import com.ekfans.base.user.model.User;
import com.ekfans.basic.hibernate.dao.IGenericDao;

/**
 * 会员DAO接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IUserDao extends IGenericDao {

	/**
	 * 更新useData字段
	 * @param ids
	 * @param use
	 * @return
	 */
	boolean updateUseData(Class clz, String ids, boolean use);

	/**
	 * 模糊查询用户，过滤掉采购和供应商
	 */
	public List<User> getUserWithApp(String searchKey);

	/**
	 * 通过环信id查询用户Id
	 * @return
	 */
	public String getUserIdByHxId(String hxId);

	/**
	 * 获取所有未注册环信用户
	 * @return
	 */
	public List<User> getAllUserNoRegHx();
}
