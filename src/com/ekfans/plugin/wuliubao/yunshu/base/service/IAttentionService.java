package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.plugin.wuliubao.yunshu.base.model.Address;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Attention;
import com.ekfans.pub.util.Pager;


/**
 * 用户关注Service接口
 * @author Administrator
 *
 */
public interface IAttentionService {

	/**
	 * 得到用户所有关注对象信息
	 * @return
	 */
	public List<Attention> getAllAttention(Pager page,HttpServletRequest request);
	
	/**
	 * 添加一个关注对象
	 * @param address
	 * @return
	 */
	public boolean addAttention(String friendId,HttpServletRequest request) ;
	
	/**
	 * 取消关注
	 * @param id
	 * @return
	 */
	public boolean deleteAttention(String friendId,HttpServletRequest request);
	
	/**
	 * 得到那些关注过我的用户id
	 * @param userId
	 * @return
	 */
	public List<String> getUserId(String friendId);
	
	/**
	 * 判断用户是否被关注
	 * @param friendId
	 * @return
	 */
	public boolean isAttention(String friendId);

}

















