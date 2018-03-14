package com.ekfans.base.store.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.store.model.CarInfo;
import com.ekfans.pub.util.Pager;

/**
 * 运输企业车辆信息Service接口
 * 
 * @ClassName: IStoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ICarInfoService {
	
	/**
	 * 添加，返回成功的id
	 * @param carInfo
	 * @return
	 */
	public String addCarInfo(CarInfo carInfo);
	
	/**
	 * 新增或编辑车辆信息
	 * 
	 * @param info
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean saveOrUpdate(CarInfo info, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 批量删除车辆信息
	 * 
	 * @param carIds
	 * @return
	 */
	public boolean delete(String[] carIds, HttpServletRequest request);

	/**
	 * 根据车辆信息ID删除车辆信息
	 * 
	 * @param carId
	 * @return
	 */
	public boolean deleteById(String carId, HttpServletRequest request);

	/**
	 * 直接更新车辆信息表
	 * 
	 * @param info
	 * @return
	 */
	public boolean updateBean(CarInfo info, HttpServletRequest request);

	/**
	 * 根据车辆信息ID获取车辆信息对象
	 * 
	 * @param infoId
	 * @return
	 */
	public CarInfo getCarInfoById(String infoId);

	/**
	 * 根据搜索条件查询车辆信息集合
	 * 
	 * @param userId
	 * @param carNo
	 * @param checkStatus
	 * @return
	 */
	public List<CarInfo> getCarInfosByStoreId(Pager page, String carNo, String checkStatus, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 根据搜索条件查询车辆信息集合
	 * 
	 * @param userId
	 * @param carNo
	 * @param checkStatus
	 * @return
	 */
	public List<CarInfo> getCarInfosByStoreId(Pager page, String storeId, String carNo, String checkStatus);

	public List<CarInfo> getCarInfoSystem(Pager page, String storeName, HttpServletRequest request, HttpServletResponse response);

	public List<CarInfo> getCheckCarsByStoreId(String storeId);

	/**
	 * 查询全部车辆信息
	 * @return
	 */
	public List<CarInfo> getAllCarInfo();

	/**
	 * @Title getCarInfoByInterface
	 * @Description TODO(获取车辆信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<Map<String,Object>>
	 * @Auth：成都易科远见科技有限公司
	 * @date：2015下午2:25:45
	 */
	public List<Map<String, Object>> getCarInfoByInterface(boolean status);

	/**
	 * 得到所有审核通过的CarInfo
	 * @return
	 */
	public List<CarInfo> getAllCheckedList();

}