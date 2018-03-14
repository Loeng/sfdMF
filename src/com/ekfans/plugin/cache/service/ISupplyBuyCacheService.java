package com.ekfans.plugin.cache.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.order.model.SupplyBuy;

public interface ISupplyBuyCacheService {
	/**
	 * 根据商品分类ID和商品类型获取商品集合
	 * 
	 * @Title: getSupplyBuysByCategory
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 * @param SupplyBuyType
	 * @return List<SupplyBuy> 返回类型
	 * @throws
	 */
	public List<String> getSupplyBuysByCategory(String categoryId, String SupplyBuyType);

	/**
	 * 根据商品主键获取商品对象
	 * 
	 * @param SupplyBuyId
	 * @return
	 */
	public SupplyBuy getSupplyBuyById(String SupplyBuyId);

	/**
	 * 刷新缓存中的商品对象
	 * 
	 * @param SupplyBuyId
	 */
	public void refreshSupplyBuy(String SupplyBuyId);

	/**
	 * 刷新缓存中的商品分类所对应的商品id集合
	 * 
	 * @param categoryId
	 * @param SupplyBuyType
	 */
	public void refrefshSupplyBuysByCategory(String categoryId, String SupplyBuyType);

	/**
	 * 得到首页供求最新数据缓存
	 * @return 首页产品展示数据
	 */
	public List<SupplyBuy> getWebIndexSupplyBuyInfo(String type, String productType);


	/**
	 * 刷新首页供求缓存
	 */
	public void refrefshWebIndexSupplyBuyInfo(String type, String productType);

}
