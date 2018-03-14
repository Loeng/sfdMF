package com.ekfans.base.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.dao.IMessageConfigDao;
import com.ekfans.base.system.model.MessageConfig;
import com.ekfans.pub.util.StringUtil;

/**
 * 邮箱配置业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Service
public class MessageConfigService implements IMessageConfigService {
	// 定义Log
	public static Logger log = LoggerFactory.getLogger(MessageConfigService.class);
	// 定義DAO
	@Autowired
	private IMessageConfigDao messageConfigDao;

	/**
	 * 从数据库获取
	 * 
	 * @return
	 */
	public MessageConfig getMessageConfig() {
		// 邮箱配置的主键为固定的：mail
		String configId = "message";
		MessageConfig messageConfig = null;
		try {
			// 调用DAO方法从数据库获取数据
			messageConfig = (MessageConfig) messageConfigDao.get(configId);
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}

		return messageConfig;
	}

	/**
	 * 将页面的数据保存到数据库
	 * 
	 * @param request
	 * @return
	 */
	public boolean saveMessageConfig(MessageConfig config) {
		// 如果传进来的配置为空，则返回失败
		if (config == null) {
			return false;
		}

		try {
			// 先从数据库查出原有的消息配置(因为该数据有且只能有一条，所以ID固定为"message")
			MessageConfig messageConfig = (MessageConfig) messageConfigDao.get("message");

			// 如果传进来的对象ID为空，则setId为"message";
			if (StringUtil.isEmpty(config.getId())) {
				config.setId("message");
			}

			// 如果传进来的对象的ID不为"message",则返回失败，不操作数据库
			if (!"message".equals(config.getId())) {
				return false;
			}

			// 如果从数据库查询出来的配置不为空，则更新数据库
			if (messageConfig != null) {
				messageConfigDao.updateBean(config);
			} else {
				// 否则，执行新增操作！
				messageConfigDao.addBean(config);
			}
			return true;

		} catch (Exception e) {
		    // 将报错的信息打印到日志表
            log.error(e.getMessage());

		}

		return false;
	}
}
