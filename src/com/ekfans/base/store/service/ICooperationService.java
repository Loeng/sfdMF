package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.Cooperation;
import com.ekfans.pub.util.Pager;

/**
 * 合作机构Service接口
 * @ClassName ICooperationService
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月15日
 */
public interface ICooperationService {

	/**
	 * 添加合作机构
	 * @param cp
	 * @return
	 */
	public boolean addCooperation(Cooperation cp);
	
	/**
	 * 修改合作机构信息
	 * @param cp
	 * @return
	 */
	public boolean updateCooperation(Cooperation cp);
	
	/**
	 * 根据id删除合作机构
	 * @param id
	 * @return
	 */
	public boolean delCooperationById(String id);
	
	/**
	 * 根据搜索条件查询合作机构列表
	 * @param pager 分页
	 * @param orgName 合作机构名
	 * @param type 合作机构类型（0：环保 1：金融） 
	 * @param contactMan 联系人
	 * @param contactPhone 联系人电话
	 * @return
	 */
	public List<Cooperation> listCooperation(Pager pager, String orgName, String type, String contactMan, String contactPhone);
	
	
	/**
	 * 根据ids批量删除合作机构
	 * @param ids
	 * @return
	 */
	public boolean delCooperation(String ids);

	/**
	 * 根据id查找合作机构
	 * @param id
	 * @return
	 */
	public Cooperation findById(String id);
}
