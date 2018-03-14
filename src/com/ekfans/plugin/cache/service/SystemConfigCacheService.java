package com.ekfans.plugin.cache.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.channel.model.Channel;
import com.ekfans.base.channel.service.IChannelService;
import com.ekfans.base.system.model.MessageConfig;
import com.ekfans.base.system.model.MessageConfigDetail;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.model.SystemContentConfig;
import com.ekfans.base.system.model.SystemParamConfig;
import com.ekfans.base.system.service.IMessageConfigDetailService;
import com.ekfans.base.system.service.IMessageConfigService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.service.ISystemContentConfigService;
import com.ekfans.base.system.service.ISystemParamConfigService;
import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.service.IBankService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.utils.SystemConfigCacheKeyUtil;
import com.ekfans.pub.util.StringUtil;

public class SystemConfigCacheService implements ISystemConfigCacheService {

	@Override
	public String getContentConfig(String key) {

		// 调用方法从缓存中获取
		String config = (String) Cache.engine.get(key);
		// 如果从缓存中获取的数据为空，则从数据库中查询，并放入缓存
		if (config == null) {
			// 调用Service方法获取集合
			ISystemContentConfigService systemContentConfigService = SpringContextHolder.getBean(ISystemContentConfigService.class);
			List<SystemContentConfig> configList = systemContentConfigService.getConfigList();
			// 将查询出来的集合放入缓存中
			for (int i = 0; i < configList.size(); i++) {
				Cache.engine.add(configList.get(i).getKey(), configList.get(i).getValue());
			}
			// 　重新从缓存取到列表
			config = (String) Cache.engine.get(key);
		}

		return config;
	}

	@Override
	public void refreshContentConfig() {
		// 调用Service方法获取系统信息配置集合
		ISystemContentConfigService systemContentConfigService = SpringContextHolder.getBean(ISystemContentConfigService.class);
		List<SystemContentConfig> configList = systemContentConfigService.getConfigList();
		// 将查询出来的集合先从缓存中清除再放入缓存中
		for (int i = 0; i < configList.size(); i++) {
			Cache.engine.remove(configList.get(i).getKey());
			Cache.engine.add(configList.get(i).getKey(), configList.get(i).getValue());
		}
	}

	@Override
	public MessageConfig getMessageConfig() {
		// 获取缓存中的Key
		String cacheKey = SystemConfigCacheKeyUtil.getMessageConfigKey();
		// 从缓存中获取配置
		MessageConfig messageConfig = (MessageConfig) Cache.engine.get(cacheKey);
		if (messageConfig == null) {
			// 使用驻入形式定义Service
			IMessageConfigService messageConfigService = SpringContextHolder.getBean(IMessageConfigService.class);
			// 调用Service获取消息配置对象
			messageConfig = messageConfigService.getMessageConfig();
			// 如果获取的消息对象不为空，则放入缓存
			if (messageConfig != null) {
				Cache.engine.add(cacheKey, messageConfig);
			}
		}
		return messageConfig;
	}

	@Override
	public void refreshMessageConfig() {
		// TODO Auto-generated method stub
		// 获取缓存中的Key
		String cacheKey = SystemConfigCacheKeyUtil.getMessageConfigKey();
		Cache.engine.remove(cacheKey);
		this.getMessageConfig();
	}

	@Override
	public String getParamConfig(String key) {
		// 调用方法从缓存中获取
		String config = (String) Cache.engine.get(key);
		// 如果从缓存中获取的数据为空，则从数据库中查询，并放入缓存
		if (config == null) {
			// 调用Service方法获取集合
			ISystemParamConfigService systemParamConfigService = SpringContextHolder.getBean(ISystemParamConfigService.class);
			List<SystemParamConfig> configList = systemParamConfigService.getConfigList(null);
			// 将查询出来的集合放入缓存中
			for (int i = 0; i < configList.size(); i++) {
				Cache.engine.add(configList.get(i).getKey(), configList.get(i).getValue());
			}
			// 　重新从缓存取到列表
			config = (String) Cache.engine.get(key);
		}

		return config;
	}

	@Override
	public void refreshParamConfig() {
		// 调用Service方法获取系统参数配置集合
		ISystemParamConfigService systemParamConfigService = SpringContextHolder.getBean(ISystemParamConfigService.class);
		List<SystemParamConfig> configList = systemParamConfigService.getConfigList(null);
		// 将查询出来的集合先从缓存中清除再放入缓存中
		for (int i = 0; i < configList.size(); i++) {
			Cache.engine.remove(configList.get(i).getKey());
			Cache.engine.add(configList.get(i).getKey(), configList.get(i).getValue());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, SystemArea> getAllSystemArea() {
		// 获取缓存中的Key
		String cacheKey = SystemConfigCacheKeyUtil.getSystemAreaCacheKey();
		// 从缓存中获取配置
		Map<String, SystemArea> areas = (Map<String, SystemArea>) Cache.engine.get(cacheKey);
		if (areas == null || areas.size() <= 0) {
			// 使用驻入形式定义Service
			ISystemAreaService areaService = SpringContextHolder.getBean(ISystemAreaService.class);
			// 调用Service获取消息配置对象
			areas = areaService.getAllSystemAreas();
			// 如果获取的消息对象不为空，则放入缓存
			if (areas != null) {
				Cache.engine.add(cacheKey, areas);
			}
		}
		return areas;
	}

	/**
	 * 根据频道类型获取频道集合
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Channel> getChannels(String type) {
		// 获取缓存中的Key
		String cacheKey = SystemConfigCacheKeyUtil.getChannelCacheKey(type);

		List<Channel> channels = (List<Channel>) Cache.engine.get(cacheKey);
		if (channels == null || channels.size() <= 0) {
			// 使用驻入形式定义Service
			IChannelService channelService = SpringContextHolder.getBean(IChannelService.class);
			channels = channelService.getAllChannel(null, type);
			if (channels != null && channels.size() > 0) {
				Cache.engine.add(cacheKey, channels);
			}
		}

		return channels;
	}

	/**
	 * 刷新频道
	 */
	public void refreshChannel(String type) {
		// 获取缓存中的Key
		String cacheKey = SystemConfigCacheKeyUtil.getChannelCacheKey(type);
		Cache.engine.remove(cacheKey);
		this.getChannels(type);
	}

	/**
	 * 根据ID获取消息详细对象
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public MessageConfigDetail getMessageConfigDetail(String id) {
		String cacheKey = SystemConfigCacheKeyUtil.getMessageConfigDetailKey(id);
		MessageConfigDetail configDetail = (MessageConfigDetail) Cache.engine.get(cacheKey);
		if (configDetail == null) {
			// 使用驻入的方法定义Service
			IMessageConfigDetailService messageConfigDetailService = SpringContextHolder.getBean(IMessageConfigDetailService.class);
			configDetail = messageConfigDetailService.getDetailById(id);
			if (configDetail != null) {
				Cache.engine.add(cacheKey, configDetail);
			}
		}
		return configDetail;
	}

	/**
	 * 刷新缓存中的消息对象
	 * 
	 * @param id
	 */
	@Override
	public void refreshMessageConfigDetail(MessageConfigDetail configDetail) {
		if (configDetail == null) {
			return;
		}
		String cacheKey = SystemConfigCacheKeyUtil.getMessageConfigDetailKey(configDetail.getId());
		Cache.engine.remove(cacheKey);
		Cache.engine.add(cacheKey, configDetail);
	}

	@Override
	public Channel getChannelById(String id) {

		IChannelService channelService = SpringContextHolder.getBean(IChannelService.class);
		return channelService.getChannelById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemArea> allProvinces() {
		// 获取缓存中的Key
		String cacheKey = SystemConfigCacheKeyUtil.getSystemAreaProvinceCacheKey();
		// 从缓存中获取配置
		// List<SystemArea> areas = (List<SystemArea>)
		// Cache.engine.get(cacheKey);
		List<SystemArea> areas = null;
		if (areas == null || areas.size() <= 0) {
			// 使用驻入形式定义Service
			ISystemAreaService areaService = SpringContextHolder.getBean(ISystemAreaService.class);
			// 调用Service获取消息配置对象
			areas = areaService.getProvinceList();
			// 如果获取的消息对象不为空，则放入缓存
			if (areas != null) {
				for (SystemArea area : areas) {
					area.setAreaName(area.getAreaName().contains("省") ? area.getAreaName().replace("省", "") : area.getAreaName());
					area.setAreaName(area.getAreaName().contains("特别行政区") ? area.getAreaName().replace("特别行政区", "") : area.getAreaName());
					area.setAreaName(area.getAreaName().contains("自治区") ? area.getAreaName().replace("自治区", "") : area.getAreaName());
					area.setAreaName(area.getAreaName().contains("回族") ? area.getAreaName().replace("回族", "") : area.getAreaName());
					area.setAreaName(area.getAreaName().contains("壮族") ? area.getAreaName().replace("壮族", "") : area.getAreaName());
					area.setAreaName(area.getAreaName().contains("维吾尔") ? area.getAreaName().replace("维吾尔", "") : area.getAreaName());
					area.setAreaName(area.getAreaName().contains("市") ? area.getAreaName().replace("市", "") : area.getAreaName());
				}
				Cache.engine.add(cacheKey, areas);
			}
		}
		return areas;
	}

	@Override
	public List<Bank> getBankList() {
		// TODO Auto-generated method stub
		IBankService bankService = SpringContextHolder.getBean(IBankService.class);
		List<Bank> bank = bankService.getBank();
		return bank;
	}

	public SystemArea getSystemAreaById(String areaId) {
		if (StringUtil.isEmpty(areaId)) {
			return null;
		}
		String cacheKey = SystemConfigCacheKeyUtil.getSystemAreaByIdKey(areaId);
		SystemArea area = (SystemArea) Cache.engine.get(cacheKey);
		if (area == null) {
			ISystemAreaService systemAreaService = SpringContextHolder.getBean(ISystemAreaService.class);
			area = systemAreaService.getSystemAreaById(areaId);
			if (area != null) {
				Cache.engine.add(cacheKey, area);
			}
		}
		return area;
	}
}
