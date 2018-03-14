package com.ekfans.base.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ekfans.base.system.dao.IMessageDao;
import com.ekfans.base.system.model.Message;

/**
 * 待发送消息Service实现类
 * 
 * @ClassName: MessageService
 * @author 成都易科远见科技有限公司
 * @date 2014-6-1 下午03:27:39
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class MessageService implements IMessageService {
	@Autowired
	IMessageDao messageDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getMessages() {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer(" from Message");
		try {
			return messageDao.list(sql.toString(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean saveMessage(Message message) {
		// TODO Auto-generated method stub
		if (message == null) {
			return false;
		}

		try {
			messageDao.addBean(message);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	
}
