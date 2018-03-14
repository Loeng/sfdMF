package com.ekfans.base.system.service;

import com.ekfans.base.system.model.MessageConfig;

/**
 * 邮箱配置实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IMessageConfigService {
	/**
	 * 从数据库获取
	 * 
	 * @return
	 */
	public MessageConfig getMessageConfig();

	/**
	 * 将页面的数据保存到数据库
	 * 
	 * @param config
	 *            基础参数设置类
	 * @return
	 */
	public boolean saveMessageConfig(MessageConfig config);
}
