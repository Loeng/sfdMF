package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.Bargain;
import com.ekfans.pub.util.Pager;

/**
 * 议价service接口
 * @ClassName IBargainService
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月16日
 */
public interface IBargainService {

	/**
	 * 根据id获得对象
	 */
	public Bargain getById(String id);

	/**
	 * 根据条件查询议价列表
	 * @param pager
	 * @param productName
	 * @param contactMan
	 * @param contactPhone
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<Bargain> getBargainList(Pager pager, String productName,
			String contactMan, String contactPhone, String userId, String type);

	
	/**
	 * 根据条件查找车源信息
	 * @param pager
	 * @param carName
	 * @param contactMan
	 * @param contactPhone
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<Bargain> getBargainTransList(Pager pager, String carName,
			String contactMan, String contactPhone, String userId, String type, String userType);
	/**
	 * 根据id查找议价详细信息
	 * @param id
	 * @return
	 */
	public Bargain getDetailById(String id);

	/**
	 * 保存核价信息
	 * @param b
	 * @return
	 */
	public boolean saveAuditBargain(Bargain b);

	/**
	 * 根据id删除议价
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);

	/**
	 * 新增议价
	 * @param b
	 * @return
	 */
	public boolean addBargain(Bargain b);

	/**
	 * 更新议价信息
	 * @param b
	 * @return
	 */
	public boolean updateBargain(Bargain b);
	
	/**
	 * @Title getWfDetailById
	 * @Description TODO(根据ID查询车源议价) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return Bargain
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午8:40:47
	 */
	public Bargain getWfDetailById(String id);

	/**
	 * 查询发布人为游客的车源议价
	 * @param id
	 * @return
	 */
	public Bargain getWfDetailByIdFromYouKe(String id);

	/**
	 * 后台查询车源议价
	 * @param pager
	 * @param carName
	 * @param fbType
	 * @return
	 */
	public List<Bargain> getSysBargainTransList(Pager pager, String carName,
			String fbType);

}
