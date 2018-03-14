package com.ekfans.base.user.service;

import java.util.List;

import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.user.model.UserAddress;

/**
 * 用户地址Service接口
 * 
 * @ClassName: IUserAddressService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IUserAddressService {
	
	/**
	 * 根据用户id获取收货人地址列表
	 */
	public List<UserAddress> listAddress(String userId);

	/**
	 * 添加收货地址
	 */
	public boolean addAdddress(UserAddress ud);
	
	/**
	 * 设置默认收货地址
	 */
	public boolean setDefaultAddress(String id, String trueId);

	/**
	 * 根据id删除收货地址
	 */
	public boolean deleteAdddress(String id);

	/**
	 * 修改收货地址
	 */
	public boolean modifyAddress(UserAddress ud);

	/**
	 * 根据id查询收货地址
	 */
	public UserAddress getUserAddressById(String id);

	/**
	 * 根据登录名获取该登陆用户的收件地址
	 */
	public Object[] storeLoginByName(String address);

	

	/**
	 * 
	 * @Title: getProvinceList
	 * @Description: TODO(查询出所有的省) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return List<SystemArea> 返回类型
	 * @throws
	 */
	public List<SystemArea> getProvinceList();

	/**
	 * 
	 * @Title: getChildAreasByParentId
	 * @Description: TODO(根据父地区ID获取父地区的下面的子地区) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param parentId
	 * @param @return 设定文件
	 * @return List<SystemArea> 返回类型
	 * @throws
	 */
	public List<SystemArea> getChildAreasByParentId(String parentId);

	/**
	 * 
	 * @Title: getDefaultAddess
	 * @Description: TODO(根据用户查询出默认地址) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param userId
	 * @return UserAddress 返回类型
	 * @throws
	 */
	public UserAddress getDefaultAddess(String userId);

	/**
	 * 
	 * @Title: cancelDefaultAddress
	 * @Description: TODO(取消默认收货地址) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param userAddress
	 *            void 返回类型
	 * @throws
	 */
	public void cancelDefaultAddress(UserAddress userAddress);
}