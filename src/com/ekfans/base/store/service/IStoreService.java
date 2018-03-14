package com.ekfans.base.store.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreFinancialData;
import com.ekfans.base.store.model.StoreInfo;
import com.ekfans.base.store.model.StoreLegalResume;
import com.ekfans.base.user.model.User;
import com.ekfans.controllers.web.vo.ProListTemplateVO;
import com.ekfans.pub.util.Pager;

/**
 * 企业基础信息Service接口
 * 
 * @ClassName: IStoreService
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreService {

	public List<Store> getStoreByGroup(Pager pager, String type);

	/**
	 * @Title: getStoreByType
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据类型查询及区域查询数据
	 *               (详细描述此方法相关的业务处理流程)
	 * @param type
	 * @return List<Store> 返回类型
	 * @throws
	 */
	public List<Store> getStoreByType(Pager pager, String areaId, String pageType);

	/**
	 * 根据id查找企业基础信息
	 */
	public Store getStore(String id);

	/**
	 * （修改或更新）企业基础信息
	 * 
	 * @return 1：成功，2：失败，3：上传企业LOGO，4：上传营业执照，5：企业名称不能为空
	 */
	public int updateStore(Store store);

	/**
	 * 修改企业信息
	 */
	public boolean update(Store store);
	
	/**
	 * 更新企业信息
	 * 
	 * @param store
	 * @return
	 */
	public boolean updateStoreOnly(Store store);

	/**
	 * 修改时验证企业名称是否重复
	 * 
	 * @param oldStoreName
	 * @param newStoreName
	 * @return true：有该用户，false：没有
	 */
	public Boolean checkStoreNameUpdate(String oldStoreName, String newStoreName);

	/**
	 * 根据id删除店铺
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteStore(String id);

	/**
	 * 添加店铺
	 * 
	 * @param store
	 * @return
	 */

	public boolean addStore(Store store, File uploadFile, String uploadFileContentType, boolean checkStatus);

	/**
	 * 修改店铺资料
	 * 
	 * @param store
	 */
	public boolean modifyStore(Store store, File uploadFile, String uploadFileContentType);

	/**
	 * 
	 * @Title: listStore
	 * @Description: TODO(查询店铺列表) 详细业务流程: 根据pager分页，状态，店铺名，所在城市查询店铺列表
	 * @param @param pager
	 * @param @param status
	 * @param @param name
	 * @param @param city
	 * @return List<Store> Store列表
	 * @throws
	 */
	public List<Store> listStore(Pager pager, String userType, Boolean status, String name, String mobile, String email, String userName);

	/**
	 * 
	 * @Title: listUncheckStore
	 * @Description: TODO(查询未审核店铺列表) 详细业务流程: 根据pager分页，状态，店铺名，所在城市查询店铺列表
	 * @param @param pager
	 * @param @param status
	 * @param @param name
	 * @param @param city
	 * @return List<Store> Store列表
	 * @throws
	 */
	public List<Store> listUncheckStore(Pager pager, String status, String name, String mobile, String email, String userName);

	/**
	 * 新增商品时查询店铺列表
	 * 
	 * @return
	 */
	public List<Store> listStore();

	/**
	 * 
	 * @param province省份
	 * @param city城市
	 * @param address地址
	 * @param coordinateX横坐标
	 * @param coordinateY纵坐标
	 * @return
	 */
	public List<Store> listStoreMap(String province, String city, String address, String coordinateX, String coordinateY);

	/**
	 * 向店铺信息中添加坐标
	 */
	public boolean addStoreMap(Store store);

	/**
	 * 根据店铺登录名获取店铺对象
	 * 
	 * @param storeName
	 * @return
	 */
	public Object[] storeLoginByName(String storeName);

	/**
	 * 设置域名
	 * 
	 * @param domain
	 * @return
	 */
	public boolean updateDomain(Store store) throws Exception;

	/**
	 * 验证域名是否已被使用
	 * 
	 * @Title: listDomain
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param querySQL 如果域名被使用，用来查看使用域名的店铺的数量
	 * @param @param map 用来存储域名参数
	 * @param @return 如果域名被使用，返回有该域名的店铺的集合
	 * @return List 返回类型
	 * @throws
	 */
	public boolean existDomain(String domainName, String storeId) throws Exception;

	/**
	 * @throws Exception
	 * 
	 * @Title: updateStoreInfo
	 * @Description: TODO 用来修改查询到的商户的部分注册信息 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param object 存储的查询到的商户的部分user信息和store信息
	 * @param @return 设定文件
	 * @return boolean 修改成功就返回true
	 * @throws
	 */
	public boolean updateStoreInfo(Store store);

	/**
	 * 
	 * @Title: existStoreName
	 * @Description: TODO 查看商户修改的店铺名是否有人已经使用 详细业务流程:
	 * 
	 * @param @param domainId
	 * @param @param store
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean existStoreName(String storeName, String storeId) throws Exception;

	public boolean updateNotes(Store store);

	/**
	 * 
	 * 
	 * @Title: getStoreByStoreName
	 * @Description: TODO(通过storename得到store对象) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param storeName
	 * @return Store 返回类型
	 * @throws
	 */
	public Store getStoreByStoreName(String storeName);

	/**
	 * 
	 * @Title: findProductTemplateFields
	 * @Description: TODO(根据productName获得商品所属模板详细字段名和详细字段值) 详细业务流程:
	 *               (详细描述此方法相关的业务处理流程)
	 * @param @param templateId
	 * @param @return 设定文件
	 * @return List<TemplateFields> 返回类型
	 * @throws
	 */
	public List<ProListTemplateVO> findProductTemplateFieldsByStoreId(String storeId);

	/**
	 * 
	 * @Title: getProductByConditions
	 * @Description: TODO(商品列表里按条件查询与排序<排序使用单一排序>) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param mainCategory
	 * @param @param brand
	 * @param @param salesVolumeType
	 * @param @param popularityType
	 * @param @param newestType
	 * @param @param priceType
	 * @param @param ProductArea
	 * @param @param pager
	 * @param @return 设定文件
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public Object[] getProductByConditionsByStore(String storeId, String mainCategory, String brand, String templateOne, String templateTwo, String sortNameAndType, Pager pager);

	/**
	 * 
	 * @Title: findThisProductBrand
	 * @Description: TODO(查询出匹配商品所属品牌) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productName
	 * @param @return 设定文件
	 * @return List<ProductBrand> 返回类型
	 * @throws
	 */
	public List<ProductBrand> findThisProductBrandByStore(String storeId);

	/**
	 * 
	 * @Title: findThisProductCatagory
	 * @Description: TODO(查询出匹配商品的所属分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param productName
	 * @param @return 设定文件
	 * @return List<ProductCategory> 返回类型
	 * @throws
	 */
	public List<ProductCategory> findThisProductCatagoryByStore(String storeId);

	/**
	 * 
	 * @Title: getProductsById
	 * @Description: TODO(首页商品搜索匹配出满足条件的商品) 详细业务流程: ( 模板名(如裙长和尺码)都来自于模板,
	 *               一个模板对应多个值(模板详细字段) )
	 * @param @param productName
	 * @param @return 设定文件
	 * @return List<Product> 返回类型
	 * @throws
	 */
	public Object[] getProductsById(String storeId, Pager pager);

	/**
	 * 
	 * @Title: findThisProductCatagoryByFullPath
	 * @Description: TODO 根据全路径取得分类 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param fullPath
	 * @param @return 设定文件
	 * @return List<ProductCategory> 返回类型
	 * @throws
	 */
	public List<ProductCategory> findThisProductCatagoryByFullPath(String fullPath);

	/**
	 * 保存企业信息
	 * 
	 * @param user
	 *            用户基础信息
	 * @param store
	 *            企业基础信息
	 * @return true：成功，false：失败
	 */
	public Boolean saveStoreInfo(User user, Store store, HttpServletRequest request);

	/**
	 * 根据条件分页查询企业
	 * 
	 * @param pager
	 *            分页条件
	 * @param status
	 *            用户状态
	 * @param storeName
	 *            企业名称
	 * @param legalMobile
	 *            法人电话
	 * @param mail
	 *            用户常用邮箱
	 * @param name
	 *            用户名
	 * @param type
	 *            会员类型（0：个人会员，1：供应商，2：采购商，3：核心企业）
	 * @param mark
	 *            1：查询全部企业，2：查询待认证企业
	 * @return List<Store>
	 */
	public List<Store> getStoreList(Pager pager, Integer status, String storeName, String legalMobile, String mail, String name, String type, Integer mark);
    
	/**
	 * 根据条件分页获取所有的企业数据
	 * @param pager  分页
	 * @param status 用户状态
	 * @return
	 */
	public List<Store> getStoreList(Pager pager, Integer status);

	
	/**
	 * 根据删除企业
	 * 
	 * @param ids
	 *            企业id集合，如：1,2,3 或者 1
	 * @return
	 */
	public Boolean deleteById(String ids);

	/**
	 * 根据id查询企业相关信息
	 * 
	 * @param id
	 * @return Store
	 */
	public Store getStoreById(String id);

	/**
	 * 更新企业信息
	 * 
	 * @param user
	 *            用户基础信息
	 * @param store
	 *            企业基础信息
	 * @return true：成功，false：失败
	 */
	public Boolean updateStoreInfo(User user, Store store);

	/**
	 * 认证企业信息
	 * 
	 * @param status
	 *            0：未认证，1：认证中，2：认证失败，3：认证成功
	 * @param mark
	 *            1：第一步，2：第二步，3：第三步
	 * @return true：成功，false：失败
	 */
	public Boolean authStoreInfo(String id, String status, String mark, String checknote, String checkMan);

	/**
	 * 验证企业名称是否重复
	 * 
	 * @param storeName
	 * @return true：有该用户，false：没有
	 */
	public Boolean checkStoreName(String storeName);

	/**
	 * @Title: getListStoreByStoreName
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据企业名称查询企业列表
	 *               (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param storeName
	 * @return List<Store> 返回类型
	 * @throws
	 */
	public List<Store> getListStoreByStoreName(Pager pager, String storeName, String type, String htType);

	/**
	 * 企业认证页面，获取认证企业列表
	 * 
	 * @return
	 */
	public List<Store> getAutoCompany();

	public Boolean updateStoreOrStoreInfoOrLgOrCa(Store store, StoreInfo storeInfo, List<StoreLegalResume> lrlist, List<StoreFinancialData> fdlist, int type);

	/**
	 * 获取（未认证和认证中）的企业
	 * 
	 * @param pager
	 *            分页
	 * @param name
	 *            企业名称
	 * @param mark
	 *            1:基础信息认证，2:银行认证
	 */
	public List<Store> getCheckStore(Pager pager, String name, int mark);

	/**
	 * 企业（基础信息，银行）认证
	 * 
	 * @param id
	 *            企业id
	 * @param mark
	 *            1:基础信息认证，2:银行认证
	 * @param status
	 *            2:认证失败，3:认证成功
	 */
	public boolean checkStoreInfo(String id, String mark, String status);

	/**
	 * 
	 * @Title: getWebList
	 * @Description: TODO(前台展现) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @return List<Store> 返回类型
	 * @throws
	 */
	public List<Store> getWebList(Pager pager, String storeTpye);

	/**
	 * @Title: getStoreByHx
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:获取核心企业的基础信息 (详细描述此方法相关的业务处理流程)
	 * @return Store 返回类型
	 * @throws
	 */
	public List<Store> getStoreByHx(String type);

	public List<Store> getBasisStore(Pager pager);

	public boolean jumdmentContractByStore(String mark, String jStoreName, String yStoreName);

	/**
	 * 根据用户类型查询企业
	 * 
	 * @param type
	 *            0:个人会员，1:产生企业，2:采购商，3:处置企业，4:运输企业
	 */
	public List<Store> getStoreByType(String type);

	/**
	 * 查询待认证的会员信息
	 * 
	 * @param rzType
	 * @param storeName
	 * @param email
	 * @param page
	 * @return
	 */
	public List<Store> getStoreUnCheckList(String rzType, String storeName, String email, Pager page);

	/**
	 * 根据虚拟账户ID获取企业信息
	 * 
	 * @param accountNo
	 * @return
	 */
	public Store getStoreByAccountNo(String accountNo);

	/**
	 * 获得所有未被删除的企业
	 * @param pager
	 * @param type
	 * @param storeName
	 * @return
	 */
	public List<Store> getAllNormalStoreList();

}