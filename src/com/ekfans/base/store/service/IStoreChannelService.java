package com.ekfans.base.store.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.store.model.StoreChannel;
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
public interface IStoreChannelService {
	/**
	 * 添加频道
	 * 
	 * @param channel
	 * @return
	 */
	public boolean addChannel(StoreChannel channel);

	/**
	 * 根据id查找频道
	 * 
	 * @param id
	 * @return
	 */
	public StoreChannel getChannelById(String id,String storeId);

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
	public boolean modifyChannel(StoreChannel channel);

	/**
	 * 查找频道列表
	 * 
	 * @param pager
	 *            分页
	 * @param name
	 *            频道名
	 * @return
	 */
	public List<StoreChannel> listChannel(Pager pager, String name);

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
	 * @return List<StoreChannel> 返回类型
	 * @throws
	 */
	public List<StoreChannel> getAllChannel(HttpServletRequest request);

	/**
	 * 处理编辑保存
	 * 
	 * @Title: processHtml
	 * @Description: 处理HTML语言，将之解析后写入XML，并生成HTML 详细业务流程:
	 *               处理HTML语言，将之解析后写入XML，并生成HTML
	 * @param @param channelId
	 * @param @param storeId
	 * @param @param request
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean processHtml(String channelId,String storeId,String storeName, HttpServletRequest request);

}
