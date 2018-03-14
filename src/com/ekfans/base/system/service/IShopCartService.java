package com.ekfans.base.system.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.ekfans.base.system.model.ShopCart;

/**
 * 购物车Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IShopCartService {

	/**
	 * 根据userId得到集合
	 * 
	 * @param id
	 * @return
	 */
	public List<ShopCart> getShopCartByUserId(String userId);

	/**
	 * 
	 * @Title: deleteShopCartById
	 * @Description: TODO(根据id删除) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean deleteShopCartById(String id);

	public boolean deleteShopCartById(String id, Session session);

	/**
	 * 
	 * @Title: getByShopCartById
	 * @Description: TODO(根据id得到对象) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return ShopCart 返回类型
	 * @throws
	 */
	public ShopCart getByShopCartById(String id);

	/**
	 * 
	 * @Title: update
	 * @Description: TODO(修改) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param shopCart
	 * @return Boolean 返回类型
	 * @throws
	 */
	public Boolean update(ShopCart shopCart);

	/**
	 * 
	 * @Title: add
	 * @Description: TODO(添加商品到购物车) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param shopCart
	 * @return Boolean 返回类型
	 * @throws
	 */
	public Boolean add(ShopCart shopCart);

	/**
	 * 
	 * @Title: getByShopCartByProductId
	 * @Description: TODO(根据productId返回有或者无) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param shopCart
	 * @return Boolean 返回类型
	 * @throws
	 */
	public Boolean getByShopCartByProductId(String pId, String UserId, String pInfoId);

	/**
	 * 
	 * @Title: getShopCartByProductId
	 * @Description: TODO(根据商品id得到购物车数据) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pId
	 * @return ShopCart 返回类型
	 * @throws
	 */
	public ShopCart getShopCartByProductId(String pId, String UserId, String pInfoId);

	/**
	 * 
	 * @Title: getShopCartByUserIdAndStoreId
	 * @Description: TODO(根据用户id，和店铺id得到集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param userId
	 * @param storeId
	 * @return List<ShopCart> 返回类型
	 * @throws
	 */
	public List<ShopCart> getShopCartByUserIdAndStoreId(String userId, String storeId);

	/**
	 * 
	 * @Title: getShopCartByUserIdAndStoreIdCheck
	 * @Description: TODO(选择后跳转到确认页面的集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param userId
	 * @param storeId
	 * @return List<ShopCart> 返回类型
	 * @throws
	 */
	public List<ShopCart> getShopCartByUserIdCheck(String userId);

	/**
	 * 
	 * @Title: getShopCartByUserIdAndStoreIdCheck
	 * @Description: TODO(选择后跳转到确认页面的集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param userId
	 * @param storeId
	 * @return List<ShopCart> 返回类型
	 * @throws
	 */
	public List<ShopCart> getShopCartByUserIdCheckNew(String userId, String cartId);

	/**
	 * 
	 * @Title: getListByUserId
	 * @Description: TODO(根据userId得到集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param userId
	 * @return List<ShopCart> 返回类型
	 * @throws
	 */
	public List<ShopCart> getListByUserId(String userId);

	/**
	 * 
	 * @Title: getProductNumber
	 * @Description: TODO(在购物车中查询单件商品的库存) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param cartId
	 * @param userId
	 * @return Integer 返回类型
	 * @throws
	 */
	public Integer getProductNumber(String cartId, String userId);

	/**
	 * 
	 * @Title: getShopCartSum
	 * @Description: TODO(根据用户id得到购物车总的商品数量) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param userId
	 * @return String 返回类型
	 * @throws
	 */
	public String getShopCartSum(String userId);

	/**
	 * 优选商城交易生成订单
	 * 
	 * @param request
	 * @return
	 */
	public boolean commitShopCartToOrder(HttpServletRequest request);

	/**
	 * 下单
	 * 
	 * @param request
	 * @return
	 */
	public boolean commitPurchaseCartToOrder(HttpServletRequest request);
	
	/**
	 * 订购
	 */
	public boolean commitDingGouCartToOrder(HttpServletRequest request);

	/**
	 * 议价后下单
	 * @param request
	 * @return
	 */
	public boolean commitBargainCartToOrder(HttpServletRequest request);
}
