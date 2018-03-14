package com.ekfans.base.product.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ekfans.base.product.model.ProductCategory;

/**
 * 商品分类实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductCategoryService {
	
	
	/**
	 * 
	 * @Title: getCategories
	 * @Description: TODO 获取一级分类列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return List<ProductCategory> 商品分类的集合
	 * @throws
	 */
	public List<ProductCategory> getCategories();
	
	/**
	 * 
	 * @Title: getCategories
	 * @Description: TODO 根据商品类型获取分类列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return List<ProductCategory> 商品分类的集合
	 * @throws
	 */
	public List<ProductCategory> getCategoriesByType(String productType);
	

	/**
	 * 
	 * @Title: getChildCategories
	 * @Description: TODO 根据父分类id获取子分类列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param parentId
	 * @param @return 设定文件
	 * @return List<ProductCategory> 商品分类的集合
	 * @throws
	 */
	public List<ProductCategory> getChildCategories(String parentId);

	/**
	 * 
	 * @Title: getChildCategories
	 * @Description: TODO 根据父分类id获取子分类列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param parentId
	 * @param @return 设定文件
	 * @return List<ProductCategory> 商品分类的集合
	 * @throws
	 */
	public List<String> getChildCategorieIds(String parentId);
	
	/**
	 * 
	 * @Title: getChildCategories
	 * @Description: TODO 根据父分类id获取子分类列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param parentId
	 * @param @return 设定文件
	 * @return List<ProductCategory> 商品分类的集合
	 * @throws
	 */
	public List<ProductCategory> getChildCategories(String parentId, boolean canParentNull, int showNumber);

	/**
	 * 
	 * @Title: getStoreCategories
	 * @Description: TODO 获取店铺分类 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param parentId
	 * @param canParentNull
	 * @param showNubmer
	 * @return List<ProductCategory> 返回类型
	 * @throws
	 */
	public List<ProductCategory> getStoreCategories(String storeId, String parentId, boolean canParentNull, int showNumber);

	/**
	 * 
	 * @Title: getAllCategoryIdsById
	 * @Description: TODO(根据分类id查找所有子分类id(包括自己)) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return List<String> 返回类型
	 * @throws
	 */
	public List<String> getAllCategoryIdsById(String id);

	/**
	 * 新增商品分类
	 * 
	 * @param category
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addCategory(ProductCategory category, File uploadFile, String uploadFileContentType);

	/**
	 * 根据id删除商品分类
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteCategory(String id);

	/**
	 * 修改商品分类
	 * 
	 * @param category
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyCategory(ProductCategory category, File uploadFile, String uploadFileContentType);

	/**
	 * 根据id查找商品分类
	 * 
	 * @param id
	 * @return
	 */
	public ProductCategory getCategoryById(String id);

	/**
	 * 
	 * @Title: getCategories
	 * @Description: TODO(根据条件查询) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param name
	 * @return List<ProductCategory> 返回类型
	 * @throws
	 */
	public List<ProductCategory> getCategories(String name,String parentId);

	/**
	 * 
	 * @Title: searchCategoriesByName
	 * @Description: TODO(根据名字搜索分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param name
	 * @return List<ProductCategory> 返回类型
	 * @throws
	 */
	public Map<String, String> searchCategoriesByName(String name,String fullPath);

	/**
	 * 新增内容分类
	 * 
	 * @param category
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addCategory(ProductCategory category, String picturePath);

	/**
	 * 修改内容分类
	 * 
	 * @param category
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyCategory(ProductCategory category, String picturePath);

	/**
	 * 根据name查找分类对象
	 */
	public ProductCategory getCategoryByName(String name);

	/**
	 * 根据id查到productCategory集合
	 */
	public List<ProductCategory> listProductCategory(String id);

	/**
	 * 
	 * @Title: getProductCategoryByStoreId
	 * @Description: TODO(根据店铺id得到商品分类的集合) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param StoreId
	 * @return List<String> 返回类型
	 * @throws
	 */
	public List<ProductCategory> getProductCategoryByStoreId(String StoreId);

	/**
	 * 根据商品分类ID获取分类所关联的商品模板，如果该分类没有关联分类，则往上一层找，直到找到根分类
	 * 
	 * @Title: getTemplateByCategoryId
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param categoryId
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getTemplateByCategoryId(String categoryId);

	/**
	 * 获取分类的全称
	 * 
	 * @Title: getCategoryFullName
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param categoryId 商品分类ID
	 * @param @param splitStr 商品分类名称每级之间的分隔符
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getCategoryFullNameByCategoryId(String categoryId, String splitStr);

	/**
	 * 
	 * @Title: saveStoreCategory
	 * @Description: TODO 店铺分类的保存 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pList
	 * @param cList
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean saveStoreCategory(List<ProductCategory> pList, List<ProductCategory> cList);

	/**
	 * 获取商品所有分类
	 * 
	 * @return List<ProductCategory>
	 */
	public List<ProductCategory> getCompleteByPc();
	/**
	 * 获取商品所有分类名
	 * 
	 * @return List<String>
	 */
	public List<String> getCategoryNames();
	
	
	/**
	 * 获取商城分类
	 * @return
	 */
	public List<ProductCategory> getMallCatg();

	/**
	 *
	 * @Title: getCategories
	 * @Description: TODO 根据商品类型获取分类列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return List<ProductCategory> 商品分类的集合
	 * @throws
	 */
	public List<ProductCategory> getCcwccCategorys(String prouctType);
}