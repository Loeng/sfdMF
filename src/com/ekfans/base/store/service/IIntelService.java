package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.Intel;
import com.ekfans.pub.util.Pager;

/**
 * 资质Service接口
 * 
 * @ClassName: IIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IIntelService {
	
	/**
	 * 获取全部资质
	 */
	public List<Intel> getIntelAll();

	/**
	 * 根据企业Id获取（处置,产生,流通）企业选中但未认证的资质
	 * 
	 * @param storeId 企业Id
	 * @param type 0:处置,1:产生,2:流通
	 */
	public List<Intel> getIntelByStoreId(String storeId, String type);
	
	/**
	 * 根据企业Id获取（处置,产生,流通）企业选中但认证通过的资质
	 * 
	 * @param storeId 企业Id
	 * @param type 0:处置,1:产生,2:流通
	 */
	public List<Intel> getIntelByStoreIdTong(String storeId, String type);
	
	/**
	 * 保存资质
	 * 
	 * @return 1:失败，2：资质名称为空，3：成功
	 */
	public int saveIntel(Intel intel);
	
	public List<Intel> getIntelList(Pager page, String name);
	
	public boolean deleteIntel(String ids);
	
	public Intel getIntelById(String id);
	
	/**
	 * 更新资质
	 * 
	 * @return 1:失败，2：资质名称为空，3：成功
	 */
	public int updateIntel(Intel intel);
}