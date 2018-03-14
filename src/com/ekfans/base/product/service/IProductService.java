package com.ekfans.base.product.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.product.model.Product;
import com.ekfans.pub.util.Pager;

/**
 * 商品实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductService {
	/**
	 * 根据id删除商品
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteProduct(String id);

	public Product getProductById(String id, org.hibernate.Session session);

	/**
	 * 添加商品
	 * 
	 * @param product
	 * @return
	 */
	public boolean saveProduct(Product product, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 批量添加商品
	 * 
	 * @param product
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String saveBatchProduct(List<String[]> list, String storeInfo, Map zipMap, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 编辑商品
	 * 
	 * @param product
	 * @return
	 */
	public boolean updateProduct(Product product, String userType, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 根据id查找商品
	 * 
	 * @param id
	 * @return
	 */
	public Product getProductById(String id);

	/**
	 * 修改商品
	 * 
	 * @param product
	 */
	public boolean modifyProduct(Product product);

	/**
	 * 
	 * @Title: listProduct
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param name
	 * @param status
	 * @param brand
	 * @param storeName
	 * @param storeId
	 * @param minUnitPrice
	 * @param maxUnitPrice
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<Product> listProduct(Pager pager, String name, String status, String brand, String storeName, String storeId, String minUnitPrice, String maxUnitPrice);

	/**
	 * 
	 * @Title: getProductsWithoutBrand
	 * @Description: TODO(不关联品牌查询商品列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param name
	 * @param status
	 * @param storeId
	 * @param min
	 * @param max
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<Product> getProductsWithoutBrand(Pager pager, String name, String status, String checkStatus, String storeName, String storeId, String min, String max,
			String type, String productNumber, String categoryId, String fullPath);

	/**
	 * 
	 * @Title: getProductsWithoutBrand
	 * @Description: TODO(不关联品牌查询商品列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param name
	 * @param status
	 * @param storeId
	 * @param min
	 * @param max
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<Product> getProductsWithoutBrand2(Pager pager, String name, String storeName, String storeId, String min, String max, String productNumber, String categoryId,
			String fullPath);

	/**
	 * 查询商品列表
	 * 
	 * @param pager
	 *            分页
	 * @param name
	 *            商品名
	 * @param status
	 *            状态
	 * @param brand
	 *            品牌
	 * @return
	 */
	public List<Product> listUncheckProduct(Pager pager, String productNumber, String name, String status, String brand, String storeId, String minUnitPrice, String maxUnitPrice,
			String fullPath);

	/**
	 * @Title: checkId
	 * @Description: TODO 检查商品ID是否存在 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean checkId(String id);

	/**
	 * 
	 * @Title: listProscenium
	 * @Description: TODO 店铺商品列表页的搜索条件 详细业务流程:
	 *               根据商品分类、商品品牌、商品模板、商品模板扩展字段、价格、购买量搜索
	 * @param brand
	 * @param templateId
	 * @param
	 * @param buceuyCoIt
	 * @param mainCategory
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<Product> listProscenium(String brand, String storeId, String templateId, Double unitPrice, Integer buyCount, String mainCategory, String fieldName,
			String fieldValue);

	/**
	 * 根据店铺Id取得product集合
	 */
	public List<Product> listProduct(String id);

	/**
	 * 
	 * @Title: checkProductId
	 * @Description: TODO( 根据传来的商品编号查询是否商品是否存在) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param productId
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean checkProductId(String productNo, String id);

	public List<Product> getGuessLikeProduct(String orderId);

	/**
	 * 
	 * @Title: getProductOtherByStoreId
	 * @Description: TODO(根据店铺查询出除了这个商品外的本店其他商品) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param storeId
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<Product> getProductOtherByStoreId(String storeId, String productId, String type);

	public List<Product> getProductEssence();

	public List<Product> getProductGreat();

	/**
	 * 根据商品分类主键获取商品集合 -- 正常状态上架商品
	 * 
	 * @Title: getProductsByCategoryId
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param categoryId
	 * @param @return 设定文件
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<Product> getProductsByCategoryId(String categoryId, String type, int showNumber);

	List<Product> getVisitCountMaxProduct();

	/**
	 * 
	 * @Title: queryProductsByCategoryId
	 * @Description: 根据商品分类查询商品分页
	 * @param id
	 *            商品分类id
	 * @return Pager 返回类型
	 * @throws
	 */
	public List<Product> queryProductsByCategoryId(Pager pager, String id);

	/**
	 * @Title: queryProductsByParams
	 * @Description: 根据条件查询商品集合
	 * @param params
	 *            查询条件
	 * @return List<Product> 返回类型
	 */
	public List<Product> queryProductsByParams(Pager pager, Map<String, Object> params);

	/**
	 * 销量排行(销量越高排名越靠前)
	 * 
	 * @param top
	 *            表示取前几条
	 * @return List<Product>
	 */
	public List<Product> getProductSalesRanking(Integer top, String type);

	/**
	 * 前台商品列表
	 * 
	 */
	public List<Product> getList(Pager page, String pName, String type);

	/**
	 * 根据商品分类ID和商品类型获取商品集合
	 * 
	 * @Title: getProductListByCategory
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 * @param type
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<Product> getProductListByCategory(String categoryId, String type, int showNumber);

	/**
	 * 根据商品分类ID和商品类型获取商品ID集合
	 * 
	 * @Title: getProductListByCategory
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 * @param type
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public List<String> getProductIdListByCategory(String categoryId, String type, int showNumber);

	/**
	 * 前台商品列表
	 * 
	 */
	public List<Product> getList(Pager page, String pName, String wName, String type, String categoryId);

	/**
	 * 获取商品的剩余库存
	 * 
	 * @param productId
	 * @return
	 */
	public int getProductListQuantity(String productId);

	/**
	 * 
	 * @Title: getProductByProductNo
	 * @Description: TODO 根据商品编号获取商品 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param no
	 * @return Product 返回类型
	 * @throws
	 */
	public Product getProductByProductNo(String no);

	/**
	 * 
	 * @Title: getProductSum
	 * @Description: TODO(得到对应商品数量) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String getProductSum(String storeId, String status);

	/**
	 * 前台商品列表
	 * 
	 */
	public List<Product> getDaZhongOtherPro(Integer top, String proId, String type);

	public boolean productCheckModify(Product product);

	public List<Product> getProductByCategoryId(Pager pager, String storeId, String categoryId, String areaId);

	/**
	 * 根据分类得到分类下的所有商品
	 */
	public List<Product> getListShowByPcId(Pager pager, String categoryId, String categoryRootId, String name, String minPrice, String maxPrice);

	/**
	 * 获取热门展示商品
	 */
	public List<Product> getHotToShow(Pager pager, String categoryRootId);

	/**
	 * 商品详细页
	 */
	public Product getProDteailById(String id);

	/**
	 * 根据库存量得到商品集合
	 * 
	 * @param quantity
	 * @return
	 */
	public List<Product> getListByQuantity(Pager pager, String productName, int quantity);

	/**
	 * 
	 * @Title: getListByParams
	 * @Description: TODO(销售挂牌获取商品列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param categoryId
	 *            分类名 address 交货地
	 * @return List<Product>
	 * @throws
	 */

	public List<Product> getListByParams(Pager pager, String categoryName, String address, String name, String catgId);

	/**
	 * 查询商品热门供求列表
	 * 
	 * @param categoryId
	 *            类型id
	 * @param type
	 *            供求类型 0 供应 1 求购
	 * @param showNumber
	 *            查询数量
	 * @return
	 */
	public List<Product> getGqProByCategoryId(String categoryId, String type, int showNumber);

	/**
	 * 用于展示首页的现货交易
	 * 
	 * @param pager
	 * @param categoryId
	 * @return
	 */
	public List<List<Product>> getIndexProduct();
	
	/**
	 * 得到成品审核所需的数量
	 * @return
	 * @throws Exception
	 */
	public int getProductNums() throws Exception;
	
}