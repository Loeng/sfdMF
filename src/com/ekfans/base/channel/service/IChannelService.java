package com.ekfans.base.channel.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.channel.model.Channel;
import com.ekfans.pub.util.Pager;

/**
 * 频道实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IChannelService {
	/**
	 * 添加频道
	 * 
	 * @param channel
	 * @return
	 */
	public boolean addChannel(Channel channel);

	/**
	 * 根据id查找频道
	 * 
	 * @param id
	 * @return
	 */
	public Channel getChannelById(String id);

	/**
	 * 根据id删除频道
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteChannel(String id);

	/**
	 * 修改频道
	 * 
	 * @param channel
	 */
	public boolean modifyChannel(Channel channel);

	/**
	 * 查找频道列表
	 * 
	 * @param pager
	 *            分页
	 * @param name
	 *            频道名
	 * @return
	 */
	public List<Channel> listChannel(Pager pager, String name);

	/**
	 * 检查频道id是否存在
	 * 
	 * @param id
	 * @return
	 */
	public boolean checkId(String id);

	/**
	 * 获取所有频道
	 * 
	 * @param @return 设定文件
	 * @return List<Channel> 返回类型
	 * @throws
	 */
	public List<Channel> getAllChannel(HttpServletRequest request,String type);

	/**
	 * 获取所有频道
	 * 
	 * @param @return 设定文件
	 * @return List<Channel> 返回类型
	 * @throws
	 */
	public List<Channel> getTopChannels(HttpServletRequest request,String type);
	/**
	 * 处理编辑保存
	 * 
	 * @Title: processHtml
	 * @Description: 处理HTML语言，将之解析后写入XML，并生成HTML 详细业务流程:
	 *               处理HTML语言，将之解析后写入XML，并生成HTML
	 * @param @param channelId
	 * @param @param htmlStr
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean processHtml(String channelId, HttpServletRequest request);

}
