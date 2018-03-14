package com.ekfans.base.product.service;

import java.util.List;

import org.hibernate.Session;

import com.ekfans.base.product.model.ProductInfo;

/**
 * 商品扩展信息实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductInfoService {

	/**
	 * 
	 * @Title: addProductInfo
	 * @Description: TODO 发布商品时对productInfo的保存操作 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productInfo
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean addProductInfo(ProductInfo productInfo);

	/**
	 * 
	 * @Title: getProductInfoByProductId
	 * @Description: TODO 根据productId取得productInfo集合 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productId
	 * @param @return 设定文件
	 * @return List<ProductInfo> 返回类型
	 * @throws
	 */
	public List<ProductInfo> getProductInfoByProductId(String productId);

	/**
	 * 
	 * @Title: getProductInfoByProductId
	 * @Description: TODO 根据productId取得productInfo集合 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productId
	 * @param @return 设定文件
	 * @return List<ProductInfo> 返回类型
	 * @throws
	 */
	public List<ProductInfo> getProductInfoByProductId(String productId, Session session);

	/**
	 * 
	 * @Title: modifyProductInfo
	 * @Description: TODO 修改操作 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param ProductInfo
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean modifyProductInfo(ProductInfo productInfo);

	/**
	 * 
	 * @Title: getProductInfoType
	 * @Description: TODO(通过传来的扩展信息得打productInfo列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<ProductInfo> 返回类型
	 * @throws
	 */
	public List<ProductInfo> getProductInfoType(String productId, String infoType);

	public boolean getProductIdDelete(String productId);
}