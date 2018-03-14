package com.ekfans.plugin.wuliubao.yunshu.base.dao;

import java.util.List;

import com.ekfans.base.user.model.User;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
/**
 * 物流宝app运输端用户相关dao接口
 * @author pp
 * @Date 2017年6月30日14:38:57
 */


public interface IWlbAppUserDao extends IGenericDao{

	/**
	 * 根据手机号获取用户
	 * @param mobile
	 * @return  用户实体类
	 * @throws Exception 
	 */
	User getUser(String mobile) throws Exception;
	
	/**
	 * 保存用户信息
	 * @param user
	 */
	void saveUser(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 */
	void updateUser(User user);
	
	/**
	 * 获取所有用户信息
	 * @return
	 * @throws Exception 
	 */
	List<User> getListUser() throws Exception;
	
	/**
	 * 获取用户资质
	 * @param userId  用户ID
	 * @return 用户资质实体类
	 * @throws Exception 
	 */
	Aptitude getUserAptitude(String userId) throws Exception;
}
