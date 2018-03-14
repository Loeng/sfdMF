package com.ekfans.base.store.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.store.model.StoreChannelConfig;

/**
 * 频道配置实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IStoreChannelConfigService {

	/**
	 * 根据频道ID获取频道配置集合，以Key-value形式返回
	 * 
	 * @Title: getChannelConfigsByChannelId
	 * @param @param channelId
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	public Map<String, StoreChannelConfig> getChannelConfigsByChannelId(HttpServletRequest request, String channelId, String storeId);

	/**
	 * 根据频道ID、配置类型以及配置位置获取配置对象
	 * 
	 * @Title: getChannelConfigsByChannelId
	 * @param @param channelId
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	public StoreChannelConfig getChannelConfig(String channelId,String storeId, String configType, String position);

	/**
	 * 根据频道ID、配置类型以及配置位置删除配置对象
	 * 
	 * @Title: getChannelConfigsByChannelId
	 * @param @param channelId
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	public boolean removeChannelConfig(HttpServletRequest request,String channelId,String storeId, String configType, String position);

	/**
	 * 更新或保存频道配置
	 * 
	 * @Title: saveOrUpdate
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param config
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean saveOrUpdate(StoreChannelConfig config);

}
