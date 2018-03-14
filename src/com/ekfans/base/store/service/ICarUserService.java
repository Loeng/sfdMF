package com.ekfans.base.store.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.store.model.CarUser;
import com.ekfans.pub.util.Pager;

/**
 * 运输企业车辆人员信息Service接口
 * 
 * @ClassName: IStoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ICarUserService {
	
	/**
	 * 新增车辆人员信息
	 */
	public boolean save(CarUser carUser, HttpServletRequest request, HttpServletResponse response);
	/**
	 * 新增或编辑车辆人员信息
	 * 
	 * @param carUser
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean saveOrUpdate(CarUser carUser, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 批量删除车辆人员信息
	 * 
	 * @param carUserIds
	 * @return
	 */
	public boolean delete(String[] carUserIds);

	/**
	 * 根据车辆人员信息ID删除车辆信息
	 * 
	 * @param carUserId
	 * @return
	 */
	public boolean deleteById(String carUserId);

	/**
	 * 直接更新车辆人员信息表
	 * 
	 * @param info
	 * @return
	 */
	public boolean updateBean(CarUser carUser);

	/**
	 * 根据车辆人员信息ID获取车辆信息对象
	 * 
	 * @param carUserId
	 * @return
	 */
	public CarUser getCarUserById(String carUserId);

	/**
	 * 根据搜索条件查询车辆信息集合
	 * 
	 * @param userId
	 * @param carNo
	 * @param checkStatus
	 * @return
	 */
	public List<CarUser> getCarUsersByStoreId(Pager pager,HttpServletRequest request,
			HttpServletResponse response,String userType, 
			String name, String mobile, String checkStatus);

	
	
	public List<CarUser> getCarUserByPageSystem(String userType,Pager pager,String storeName, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 得到所有审核通过的CarUser
	 * @return
	 */
	public List<CarUser> getAllCheckedList();
}