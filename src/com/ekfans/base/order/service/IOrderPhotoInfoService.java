package com.ekfans.base.order.service;

import org.hibernate.Session;

import com.ekfans.base.order.model.OrderPhotoInfo;

/**
 * 订单快照实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2014-1-6
 * @version 1.0
 */
public interface IOrderPhotoInfoService {

	/**
	 * 添加
	 */
	public boolean add(OrderPhotoInfo info);

	/**
	 * 添加
	 */
	public boolean add(OrderPhotoInfo info, Session session);

}