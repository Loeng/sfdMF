package com.ekfans.base.system.service;

import java.util.List;

import com.ekfans.base.system.model.Message;

/**
 * 待发送消息Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IMessageService {
	/**
	 * 从数据库获取
	 * 
	 * @return
	 */
	public List<Message> getMessages();

	/**
	 * 将数据保存到数据库
	 * 
	 * @param config
	 *            基础参数设置类
	 * @return
	 */
	public boolean saveMessage(Message message);

}
