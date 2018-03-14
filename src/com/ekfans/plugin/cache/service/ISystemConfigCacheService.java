package com.ekfans.plugin.cache.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.channel.model.Channel;
import com.ekfans.base.system.model.MessageConfig;
import com.ekfans.base.system.model.MessageConfigDetail;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.user.model.Bank;

public interface ISystemConfigCacheService {

	/**
	 * 
	 * @Title: getContentConfig
	 * @Description: 从缓存中获取系统信息配置 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param key
	 * @return String 返回类型
	 * @throws
	 */
	public String getContentConfig(String key);

	/**
	 * 
	 * @Title: getParamConfig
	 * @Description: 从缓存中获取系统参数配置 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param key
	 * @return String 返回类型
	 * @throws
	 */
	public String getParamConfig(String key);

	/**
	 * 
	 * @Title: refreshContentConfig
	 * @Description: TODO(刷新系统信息配置缓存) 详细业务流程: (详细描述此方法相关的业务处理流程) void 返回类型
	 * @throws
	 */
	public void refreshContentConfig();

	/**
	 * 
	 * @Title: refreshParamConfig
	 * @Description: 刷新系统参数配置缓存 详细业务流程: (详细描述此方法相关的业务处理流程) void 返回类型
	 * @throws
	 */
	public void refreshParamConfig();

	/**
	 * 获取缓存中的消息发送配置
	 * 
	 * @Title: getMessageConfig
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return MessageConfig 返回类型
	 * @throws
	 */
	public MessageConfig getMessageConfig();

	/**
	 * 刷新缓存中的消息发送机制
	 * 
	 * @Title: refreshMessageConfig
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void refreshMessageConfig();
	
	/**
     * 根据ID获取消息详细对象
     * 
     * @param id
     * @return
     */
    public MessageConfigDetail getMessageConfigDetail(String id);

    /**
     * 刷新缓存中的消息对象
     * 
     * @param id
     */
    public void refreshMessageConfigDetail(MessageConfigDetail configDetail);
	/**
	 * 获取所有系统地址
	 * 
	 * @return
	 */
	public Map<String, SystemArea> getAllSystemArea();

	/**
	 * 根据频道类型获取频道集合
	 * 
	 * @param type
	 * @return
	 */
	public List<Channel> getChannels(String type);

	/**
	 * 刷新频道
	 */
	public void refreshChannel(String type);
	
	
	public Channel getChannelById(String id);
	
	public List<SystemArea> allProvinces();
	
	public List<Bank> getBankList();
	
	public SystemArea getSystemAreaById(String areaId);
}
