package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;

import com.ekfans.plugin.wuliubao.yunshu.base.model.MessageBack;
import com.ekfans.pub.util.Pager;

/**
 * 用户地址Service接口
 * @author Administrator
 *
 */
public interface IMessageBackService {


	
	/**
	 * 添加留言反馈
	 * @param address
	 * @return
	 */
	public boolean addMessageBack(MessageBack m);
	/**
	 * 更新留言反馈
	 * @param m
	 * @throws Exception
	 */
	public void updateMessageBack(MessageBack m) throws Exception;
	/**
	 * 获取留言反馈
	 * @param type
	 * @return
	 * @throws Exception 
	 */
    public List<MessageBack> getMessageBackList(String type,String userName,Pager page) throws Exception;
	
    public MessageBack getMessageBack(String messageBackID) throws Exception;
}

















