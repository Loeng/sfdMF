package com.ekfans.base.product.service;

import java.util.List;

import com.ekfans.base.product.model.ProductInfoDetail;

/**
 * 商品扩展信息明细实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductInfoDetailService {
	/**
	 * 
	 * @Title: getProductInfoDetailById
	 * @Description: TODO(根据id得到完整的对象) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return ProductInfoDetail 返回类型
	 * @throws
	 */
	public ProductInfoDetail getProductInfoDetailById(String id);

	/**
	 * 
	 * @Title: addProductInfoDetail
	 * @Description: TODO 保存操作 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param ProductInfoDetail
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean addProductInfoDetail(ProductInfoDetail productInfoDetail);

	/**
	 * 
	 * @Title: getProductInfoDetailByProductId
	 * @Description: TODO 根据prodcutId取得ProductInfoDetail集合 详细业务流程:
	 *               (详细描述此方法相关的业务处理流程)
	 * @param @param productId
	 * @param @return 设定文件
	 * @return List<ProductInfoDetail> 返回类型
	 * @throws
	 */
	public List<ProductInfoDetail> getProductInfoDetailByProductId(String productId);

	/**
	 * 
	 * @Title: modifyProductInfoDerail
	 * @Description: TODO 修改操作 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productInfoDetail
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean modifyProductInfoDerail(ProductInfoDetail productInfoDetail);

	/**
	 *根据商品id删除对应模板扩展字段表中的数据
	 */
	public boolean getProductIddelete(String productId);

	/**
	 * 获取商品模板明细
	 * 
	 * @Title: getInfoDetailByProductIdAndTemps
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productId 商品主键
	 * @param @param fieldId1 模板明细ID1
	 * @param @param tempVal1 模板明细值
	 * @param @param fieldId2 模板明细ID2
	 * @param @param tempVal2 模板明细值2
	 * @param @param fieldId3 模板明细ID3
	 * @param @param tempVal3 模板明细值3
	 * @param @param fieldId4 模板明细ID4
	 * @param @param tempVal14 模板明细值4
	 * @param @return 设定文件
	 * @return ProductInfoDetail 返回类型
	 * @throws
	 */
	public ProductInfoDetail getInfoDetailByProductIdAndTemps(String productId, String fieldId1, String tempVal1, String fieldId2, String tempVal2, String fieldId3, String tempVal3, String fieldId4, String tempVal4);
}