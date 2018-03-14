package com.ekfans.base.product.service;

import java.util.List;

import org.hibernate.Session;

import com.ekfans.base.product.model.ProductPrice;

/**
 * 商品价格区间Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductPriceService {
	/**
	 * 根据商品价格区间ID获取对象
	 * 
	 * @param priceId
	 * @return
	 */
	public ProductPrice getProductPrice(String priceId);

	/**
	 * 根据商品ID获取该商品的价格区间集合
	 * 
	 * @param ProductId
	 * @return
	 */
	public List<ProductPrice> getProductPriceByProductId(String productId);

	/**
	 * 根据商品ID删除该商品所有价格区间
	 * 
	 * @param ProductId
	 * @return
	 */
	public boolean removeAllPriceByProductId(String productId);

	/**
	 * 根据商品ID删除该商品所有价格区间(带事物处理)
	 * 
	 * @param ProductId
	 * @return
	 */
	public boolean removeAllPriceByProductId(String productId, Session session);
}