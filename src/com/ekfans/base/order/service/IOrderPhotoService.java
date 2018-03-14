package com.ekfans.base.order.service;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderPhoto;
import com.ekfans.base.product.model.Product;

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
public interface IOrderPhotoService {

	/**
	 * 添加
	 */
	public boolean add(OrderPhoto op);

	/**
	 * 生成订单快照
	 * 
	 * @param detail
	 * @return
	 */
	public boolean createPhoto(OrderDetail detail, Product product, HttpServletRequest request, Session session);

	/**
	 * 根据orderdteailId得到对应的快照
	 */
	public OrderPhoto getListByOdId(String odId);
}