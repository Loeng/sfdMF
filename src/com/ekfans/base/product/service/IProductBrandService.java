package com.ekfans.base.product.service;

import java.io.File;
import java.util.List;

import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.pub.util.Pager;

/**
 * 品牌实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductBrandService {
	/**
	 * 查询品牌列表
	 * @param page
	 * @return
	 */
	public List<ProductBrand> getBrands(Pager page, String name, String status);
	
	/**
	 * 新增商品时查询品牌列表
	 * @return
	 */
	public List<ProductBrand> getBrands();
	
	/**
	 * 新增品牌
	 * @param brand
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addBrand(ProductBrand brand,File uploadFile,String uploadFileContentType);
	
	/**
	 * 根据id删除品牌
	 * @param id
	 * @return
	 */
	public boolean deleteBrand(String id);
	
	/**
	 * 修改品牌
	 * @param brand
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyBrand(ProductBrand brand,File uploadFile,String uploadFileContentType);
	
	/**
	 * 根据id查找品牌
	 * @param id
	 * @return
	 */
	public ProductBrand getBrand(String id);
	
	/**
	 * 
	* @Title: getBrandByName
	* @Description: TODO(根据名字得到productBrand对象)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param name
	* @return ProductBrand 返回类型
	* @throws
	 */
	public ProductBrand getBrandByName(String name);
	
	/**
	 * 
	* @Title: getBrandDetailById
	* @Description: TODO(根据id获得品牌的详情信息)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param @param id
	* @param @return    设定文件
	* @return ProductBrand    返回类型
	* @throws
	 */
	public ProductBrand getBrandDetailById(String id);
	/**
	 * 
	* @Title: getBrands
	* @Description: TODO(用于修改的根据categoryId得到集合)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param categoryId
	* @return List<ProductBrand> 返回类型
	* @throws
	 */
	public List<ProductBrand> getBrands(String categoryId);
	
	/**
	 * 根据Id取得productBrand集合
	 */
	public List<ProductBrand> listProductBrand(String id);
}