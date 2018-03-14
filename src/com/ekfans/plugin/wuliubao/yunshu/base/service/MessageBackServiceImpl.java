package com.ekfans.plugin.wuliubao.yunshu.base.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.plugin.wuliubao.yunshu.base.dao.MessageBackDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.MessageBack;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;


@Service
public class MessageBackServiceImpl implements IMessageBackService{

	@Autowired
	private MessageBackDao dao;

	@Override
	public boolean addMessageBack(MessageBack m) {
		try {
			dao.addBean(m);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<MessageBack> getMessageBackList(String type,String userName,Pager page) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from MessageBack  where 1=1");
		if(!StringUtil.isEmpty(userName)){
			sql.append(" and userName =:userName");
			map.put("userName", userName);
		}
		if(StringUtil.isEmpty(type)){
			// 根据需求排序
			sql.append(" order by createTime desc");
			@SuppressWarnings("unchecked")
			List<MessageBack> list = dao.list(page,sql.toString(), map);
			return list;
		}else if(type.equals("1")||type.equals("2")){
			sql.append(" and type =:type");
			map.put("type", type);
			// 根据需求排序
			sql.append(" order by createTime desc");
			@SuppressWarnings("unchecked")
			List<MessageBack> list = dao.list(page,sql.toString(), map);
			return list;
		}
		return null;
	}

	@Override
	public MessageBack getMessageBack(String messageBackID) throws Exception {
		MessageBack m  = (MessageBack) dao.get(messageBackID);
		return m;
	}

	@Override
	public void updateMessageBack(MessageBack m) throws Exception {
		dao.updateBean(m);
	}

	
}
