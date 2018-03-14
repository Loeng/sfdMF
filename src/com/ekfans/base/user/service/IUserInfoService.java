package com.ekfans.base.user.service;

import com.ekfans.base.user.model.UserInfo;

/**
 * 企业详细信息Service接口
 * 
 * @ClassName: IUserInfoService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IUserInfoService {
	/**
	 * 
	 * @Title: updateUser
	 * @Description: TODO更新会员详细信息对象 详细业务流程:通过传过来的UserInfo对象利用dao方法来更新UserInfo对象
	 *               (详细描述此方法相关的业务处理流程)
	 * @param @param userInfo
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean updateUser(UserInfo userInfo) throws Exception;

	/**
	 * 根据用户id获取用户详细信息
	 */
	public UserInfo getUserInfoByUserId(String userId);

	/**
	 * 
	 * @Title: getUserInfoById
	 * @Description: TODO 根据userInfo的主键Id获取UserInfo对象 详细业务流程:
	 *               通过传过来的id参数获取userInfo对象 (详细描述此方法相关的业务处理流程)
	 * @param @param id
	 * @param @return 设定文件
	 * @return UserInfo 返回类型
	 * @throws
	 */
	public UserInfo getUserInfoById(String id) throws Exception;

}