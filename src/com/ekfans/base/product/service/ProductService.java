package com.ekfans.base.product.service;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.InquiryService;
import com.ekfans.base.product.dao.IProductCategoryRelDao;
import com.ekfans.base.product.dao.IProductDao;
import com.ekfans.base.product.dao.IProductInfoDao;
import com.ekfans.base.product.dao.IProductInfoDetailDao;
import com.ekfans.base.product.dao.IProductPictureDao;
import com.ekfans.base.product.dao.IProductPriceDao;
import com.ekfans.base.product.dao.IProductValuationDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.ProductCategoryRel;
import com.ekfans.base.product.model.ProductInfo;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.base.product.model.ProductValuation;
import com.ekfans.base.product.util.ProductHelper;
import com.ekfans.base.store.dao.IWarehouseLogDao;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.service.SystemAreaService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.service.IProductCacheService;
import com.ekfans.plugin.cache.service.ProductCacheServiceImpl;
import com.ekfans.plugin.number.NoManager;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProductService implements IProductService {

	private Logger log = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private IProductDao productDao = null;

	@Autowired
	private IProductValuationDao productValuationDao;

	@Autowired
	private IProductCategoryRelDao productCategoryRelDao = null;

	@Autowired
	private IProductPictureDao productPictureDao = null;

	@Autowired
	private IProductInfoDetailDao infoDetailDao = null;

	@Autowired
	private IProductInfoDao infoDao = null;

	@Autowired
	private IProductPriceDao priceDao = null;

	@Autowired
	private SystemAreaService systemAreaService;

	@Autowired
	private IProductPictureService productPictureService;

	@Autowired
	private InquiryService inquiryService;

	@Autowired
	private IProductCategoryService categoryService;

	/**
	 * 根据id删除商品
	 * 
	 * TODO:删除山商品需要删除ProductCategoryRel、Product、ProductPic、ProductInfo、
	 * ProductInfoDetail这几个表的数据
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteProduct(String id) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		IProductCacheService cacheService = new ProductCacheServiceImpl();

		Product product = cacheService.getProductById(id);
		if (product == null) {
			return false;
		}
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("select pcr.categoryId from ProductCategoryRel as pcr where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" and pcr.productId = :productId");
		paramMap.put("productId", id);

		// 定义查询SQL
		StringBuffer delSql = new StringBuffer("from ProductCategoryRel as pcr where 1=1");
		// 定义参数MAP
		Map<String, Object> delMap = new HashMap<String, Object>();

		delSql.append(" and pcr.productId = :productId");
		delMap.put("productId", id);

		// 定义查询SQL
		StringBuffer piSql = new StringBuffer("from ProductInfo as pi where 1=1");
		// 定义参数MAP
		Map<String, Object> piMap = new HashMap<String, Object>();

		piSql.append(" and pi.productId = :productId");
		piMap.put("productId", id);

		// 定义查询SQL
		StringBuffer priceSql = new StringBuffer("from ProductValuation as pv where 1=1");
		// 定义参数MAP
		Map<String, Object> priceMap = new HashMap<String, Object>();

		priceSql.append(" and pv.productId = :productId");
		priceMap.put("productId", id);

		// 定义查询SQL
		StringBuffer pidSql = new StringBuffer("from ProductInfoDetail as pid where 1=1");
		// 定义参数MAP
		Map<String, Object> pidMap = new HashMap<String, Object>();

		pidSql.append(" and pid.productId = :productId");
		pidMap.put("productId", id);

		// 定义查询SQL
		StringBuffer picSql = new StringBuffer("from ProductPicture as pic where 1=1");
		// 定义参数MAP
		Map<String, Object> picMap = new HashMap<String, Object>();

		picSql.append(" and pic.productId = :productId");
		picMap.put("productId", id);
		try {
			// 调用DAO方法删除商品
			productDao.deleteById(id);
			// 删除商品扩展信息
			productDao.delete(piSql.toString(), piMap);
			// 删除商品扩展信息详情
			productDao.delete(pidSql.toString(), pidMap);
			// 删除商品多角度视图
			productDao.delete(picSql.toString(), picMap);
			// 删除所对应的关系
			productDao.delete(delSql.toString(), delMap);
			// 调用DAO方法删除商品与分类关系
			List categoryList = productCategoryRelDao.list(sql.toString(), paramMap);
			if (categoryList != null && categoryList.size() > 0) {
				for (int i = 0; i < categoryList.size(); i++) {
					String obj = (String) categoryList.get(i);
					cacheService.refrefshProductsByCategory(obj, product.getType());
				}
			}
			cacheService.refreshProduct(id);

			// 删除商品价格
			productDao.delete(priceSql.toString(), priceMap);

			// 删除时更新缓存
			Cache.refrefshIndexProductXsgp();
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 添加商品
	 * 
	 * @param product
	 * @return
	 */
	public boolean saveProduct(Product product, HttpServletRequest request, HttpServletResponse response) {
		if (product == null) {
			return false;
		}

		// 定义商品多角度视图的properties数组
		String[] popPicProperties = null;

		// 获取模板中非规格型号类的字段值 取出来是 tempId;name;category;position;value;
		String[] tempFields = request.getParameterValues("templateFields");

		// 获取商品多角度视图图片控件的property值
		String popPic = request.getParameter("popPicProperties");
		if (!StringUtil.isEmpty(popPic)) {
			popPicProperties = popPic.split(";");
		}

		// 获取商品扩展字段规格型号类的选中
		String[] modelConfigs = request.getParameterValues("modelDetailCheck");

		// 定义Session
		Session session = null;
		// 定义事物处理
		Transaction transaction = null;
		try {
			// 创建Session
			session = productDao.createSession();
			// 开启事物处理
			transaction = session.beginTransaction();

			// 通过共用方法获取商品编号
			NoManager no = new NoManager();
			String productId = no.createProductId();
			// 将获取的商品编号放入商品对象
			product.setId(productId);

			// 设置图标保存路径
			String currentPath = "/customerfiles/store/" + product.getStoreId() + "/product/" + DateUtil.getNoSpSysDateString();
			// 调用方法获取分类图标，返回图标路径
			String picturePath = FileUploadHelper.uploadFile("picture", currentPath, request, response);
			// 获取PDF文件路径转换为服务端路径
			String pdf = FileUploadHelper.uploadFile("jcFile", currentPath, request, response);
			product.setJcFile(pdf);

			product.setCreateTime(DateUtil.getSysDateTimeString());
			product.setUpdateTime(DateUtil.getSysDateTimeString());
			// 商品图 - 大中小+推荐图
			if (!StringUtil.isEmpty(picturePath)) {
				// 保存原图
				product.setPicture(picturePath);
				// 调用Helper方法处理图片
				ProductHelper.img(ProductHelper.PIC_TYPE_PIC, product, request);
			}

			// 商品多角度视图
			if (popPicProperties != null && popPicProperties.length > 0) {
				for (int i = 0; i < popPicProperties.length; i++) {
					String picProperty = popPicProperties[i];
					if (!StringUtil.isEmpty(picProperty)) {
						// 调用方法获取分类图标，返回图标路径
						String popPicPath = FileUploadHelper.uploadFile(picProperty, currentPath, request, response);
						// 如果获取的图片路径不为空，则定义一个多角多视图对象，将数据放入进去
						if (!StringUtil.isEmpty(popPicPath)) {
							ProductPicture pPicture = new ProductPicture();
							pPicture.setPicture(popPicPath);
							pPicture.setPosition((i + 1) + "");
							pPicture.setProductId(productId);
							// 调用方法处理多角多视图
							ProductHelper.img(ProductHelper.PIC_TYPE_POP, pPicture, request);
							// 调用DAO采用事物处理的方式保存数据
							productPictureDao.addBean(pPicture, session);
						}
					}
				}
			}

			// 创建商品与商品分类的关系表
			if (!StringUtil.isEmpty(product.getMainCategory())) {
				// 定义新的对象
				ProductCategoryRel pcRel = new ProductCategoryRel();
				// 将数据放入对象
				pcRel.setCategoryId(product.getMainCategory());
				pcRel.setMain(true);
				pcRel.setPosition(0);
				pcRel.setProductId(productId);
				// 调用DAO方法采用事物处理的方式保存数据
				productCategoryRelDao.addBean(pcRel, session);
			}

			// 获取商品价格集合
			String[] valuationId = request.getParameterValues("valuationId");
			String[] valuationNumber = request.getParameterValues("valuationNumber");
			String[] valuationPrice = request.getParameterValues("valuationPrice");
			String[] valuationUnit = request.getParameterValues("valuationUnit");
			// 保存赠品
			if (valuationId != null && valuationId.length > 0) {
				for (int i = 0; i < valuationId.length; i++) {
					if (StringUtil.isEmpty(valuationId[i])) {
						String hql = "delete from ProductValuation where productId='" + product.getId() + "'";
						// 根据商品ID查询阶梯价表
						productPictureDao.delete(hql, null, session);
						continue;
					}
					ProductValuation pv = new ProductValuation();
					pv.setProductId(productId);
					String[] vaNameAndId = valuationId[i].split("_");
					pv.setValuationId(vaNameAndId[0]);
					pv.setSpecName(vaNameAndId[1]);
					pv.setValuationNumber(new BigDecimal(valuationNumber[i]));
					pv.setValuationPrice(new BigDecimal(valuationPrice[i]));
					pv.setValuationUnit(valuationUnit[i]);
					pv.setCreateTime(DateUtil.getSysDateTimeString());
					productValuationDao.addBean(pv, session);
				}
			}
			// 图片MAP
			Map<String, String> picMap = new HashMap<String, String>();
			// 规格参数的动态Map
			Map<String, ProductInfo> infoMap = new HashMap<String, ProductInfo>();
			// 商品规格型号
			if (modelConfigs != null && modelConfigs.length > 0) {
				for (int i = 0; i < modelConfigs.length; i++) {
					String configNames = modelConfigs[i];
					if (!StringUtil.isEmpty(configNames)) {
						ProductInfoDetail infoDetail = new ProductInfoDetail();
						String[] tempIds = configNames.split("--");
						if (tempIds != null && tempIds.length > 0) {
							infoDetail.setProductId(productId);
							infoDetail.setQuantityWarning(Integer.parseInt(request.getParameter(configNames + "Warn")));
							infoDetail.setQuantity(Integer.parseInt(request.getParameter(configNames + "Stock")));
							infoDetail.setPrice(new BigDecimal(request.getParameter(configNames + "Price")));

							if (tempIds.length >= 1) {
								String title = request.getParameter(tempIds[0] + "Title");
								String value = request.getParameter("tempValue" + tempIds[0]);
								String categoryId = request.getParameter("category" + tempIds[0]);
								String position = request.getParameter("position" + tempIds[0]);
								String tempId = tempIds[0].substring(tempIds[0].indexOf("-") + 1, tempIds[0].length());

								infoDetail.setInfoId1(tempId);
								infoDetail.setInfoName1(title);
								infoDetail.setInfoValue1(value);

								if (picMap.containsKey(tempIds[0])) {
									infoDetail.setInfoPic1(picMap.get(tempIds[0]));
								} else {
									String modelPicPath = FileUploadHelper.uploadFile("pic" + tempIds[0], currentPath, request, response);
									infoDetail.setInfoPic1(modelPicPath);
									picMap.put(tempIds[0], modelPicPath);
								}

								if (infoMap.containsKey(tempId)) {
									ProductInfo pInfo = infoMap.get(tempId);
									if (!pInfo.getInfoValue().contains(value)) {
										pInfo.setInfoValue(pInfo.getInfoValue() + ";" + value);
										infoMap.remove(tempId);
										infoMap.put(tempId, pInfo);
									}

								} else {
									ProductInfo pInfo = new ProductInfo();
									pInfo.setCategoryId(categoryId);
									pInfo.setInfoName(title);
									pInfo.setInfoValue(value);
									pInfo.setProductId(productId);
									pInfo.setInfoType("1");
									pInfo.setPosition(Integer.parseInt(position));
									pInfo.setTemplateFieldId(tempId);
									infoMap.put(tempId, pInfo);
								}

							}

							if (tempIds.length >= 2) {
								String title = request.getParameter(tempIds[1] + "Title");
								String value = request.getParameter("tempValue" + tempIds[1]);
								String categoryId = request.getParameter("category" + tempIds[1]);
								String position = request.getParameter("position" + tempIds[1]);
								String tempId = tempIds[1].substring(tempIds[1].indexOf("-") + 1, tempIds[1].length());

								infoDetail.setInfoId2(tempId);
								infoDetail.setInfoName2(title);
								infoDetail.setInfoValue2(value);
								if (picMap.containsKey(tempIds[1])) {
									infoDetail.setInfoPic2(picMap.get(tempIds[1]));
								} else {
									String modelPicPath = FileUploadHelper.uploadFile("pic" + tempIds[1], currentPath, request, response);
									infoDetail.setInfoPic2(modelPicPath);
									picMap.put(tempIds[1], modelPicPath);
								}

								if (infoMap.containsKey(tempId)) {
									ProductInfo pInfo = infoMap.get(tempId);
									if (!pInfo.getInfoValue().contains(value)) {
										pInfo.setInfoValue(pInfo.getInfoValue() + ";" + value);
										infoMap.remove(tempId);
										infoMap.put(tempId, pInfo);
									}

								} else {
									ProductInfo pInfo = new ProductInfo();
									pInfo.setCategoryId(categoryId);
									pInfo.setInfoName(title);
									pInfo.setInfoType("1");
									pInfo.setPosition(Integer.parseInt(position));
									pInfo.setInfoValue(value);
									pInfo.setProductId(productId);
									pInfo.setTemplateFieldId(tempId);
									infoMap.put(tempId, pInfo);
								}
							}

							if (tempIds.length >= 3) {

								String title = request.getParameter(tempIds[2] + "Title");
								String value = request.getParameter("tempValue" + tempIds[2]);
								String categoryId = request.getParameter("category" + tempIds[2]);
								String position = request.getParameter("position" + tempIds[2]);
								String tempId = tempIds[2].substring(tempIds[2].indexOf("-") + 1, tempIds[2].length());

								infoDetail.setInfoId3(tempId);
								infoDetail.setInfoName3(title);
								infoDetail.setInfoValue3(value);
								if (picMap.containsKey(tempIds[2])) {
									infoDetail.setInfoPic3(picMap.get(tempIds[2]));
								} else {
									String modelPicPath = FileUploadHelper.uploadFile("pic" + tempIds[2], currentPath, request, response);
									infoDetail.setInfoPic3(modelPicPath);
									picMap.put(tempIds[2], modelPicPath);
								}

								if (infoMap.containsKey(tempId)) {
									ProductInfo pInfo = infoMap.get(tempId);
									if (!pInfo.getInfoValue().contains(value)) {
										pInfo.setInfoValue(pInfo.getInfoValue() + ";" + value);
										infoMap.remove(tempId);
										infoMap.put(tempId, pInfo);
									}

								} else {
									ProductInfo pInfo = new ProductInfo();
									pInfo.setCategoryId(categoryId);
									pInfo.setInfoName(title);
									pInfo.setInfoValue(value);
									pInfo.setProductId(productId);
									pInfo.setInfoType("1");
									pInfo.setPosition(Integer.parseInt(position));
									pInfo.setTemplateFieldId(tempId);
									infoMap.put(tempId, pInfo);
								}
							}

							if (tempIds.length >= 4) {
								String title = request.getParameter(tempIds[3] + "Title");
								String value = request.getParameter("tempValue" + tempIds[3]);
								String categoryId = request.getParameter("category" + tempIds[3]);
								String position = request.getParameter("position" + tempIds[3]);
								String tempId = tempIds[3].substring(tempIds[3].indexOf("-") + 1, tempIds[3].length());

								infoDetail.setInfoId4(tempId);
								infoDetail.setInfoName4(title);
								infoDetail.setInfoValue4(value);
								if (picMap.containsKey(tempIds[3])) {
									infoDetail.setInfoPic4(picMap.get(tempIds[3]));
								} else {
									String modelPicPath = FileUploadHelper.uploadFile("pic" + tempIds[3], currentPath, request, response);
									infoDetail.setInfoPic4(modelPicPath);
									picMap.put(tempIds[3], modelPicPath);
								}

								if (infoMap.containsKey(tempId)) {
									ProductInfo pInfo = infoMap.get(tempId);
									if (!pInfo.getInfoValue().contains(value)) {
										pInfo.setInfoValue(pInfo.getInfoValue() + ";" + value);
										infoMap.remove(tempId);
										infoMap.put(tempId, pInfo);
									}

								} else {
									ProductInfo pInfo = new ProductInfo();
									pInfo.setCategoryId(categoryId);
									pInfo.setInfoName(title);
									pInfo.setInfoValue(value);
									pInfo.setProductId(productId);
									pInfo.setInfoType("1");
									pInfo.setPosition(Integer.parseInt(position));
									pInfo.setTemplateFieldId(tempId);
									infoMap.put(tempId, pInfo);
								}
							}

						}

						infoDetailDao.addBean(infoDetail, session);
					}
				}
			}

			if (tempFields != null && tempFields.length > 0) {
				for (int i = 0; i < tempFields.length; i++) {
					String field = tempFields[i];
					if (!StringUtil.isEmpty(field)) {
						String[] fieldStrs = field.split("\\*\\*");
						if (fieldStrs != null && fieldStrs.length > 0) {
							ProductInfo pInfo = new ProductInfo();
							pInfo.setCategoryId(fieldStrs[2]);
							pInfo.setInfoName(fieldStrs[1]);
							pInfo.setInfoValue(fieldStrs.length == 5 ? fieldStrs[4] : "");
							pInfo.setInfoType("0");
							pInfo.setPosition(Integer.parseInt(fieldStrs[3]));
							pInfo.setTemplateFieldId(fieldStrs[0]);
							pInfo.setProductId(productId);
							infoDao.addBean(pInfo, session);
						}
					}
				}
			}

			Collection<ProductInfo> c = infoMap.values();
			Iterator<ProductInfo> it = c.iterator();
			for (; it.hasNext();) {
				ProductInfo info = (ProductInfo) it.next();
				info.setInfoType("1");
				infoDao.addBean(info, session);
			}
			if(product.getUnitPrice()==null){
				product.setRiseDrop(0.00);	
			}else{
			product.setRiseDrop(product.getUnitPrice().doubleValue());}
			// 调用DAO方法添加商品
			productDao.addBean(product, session);
			// 事物处理提交以及关闭事物
			session.flush();
			transaction.commit();
			session.close();

			IProductCacheService cacheService = new ProductCacheServiceImpl();
			cacheService.refrefshProductsByCategory(product.getMainCategory(), product.getType());
			cacheService.refreshProduct(product.getId());
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}

			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 编辑商品保存
	 * 
	 * @param product
	 * @return
	 */
	public boolean updateProduct(Product product, String userType, HttpServletRequest request, HttpServletResponse response) {
		if (product == null) {
			return false;
		}
		boolean checkStatus = true;
		if (StringUtil.isEmpty(userType)) {
			userType = "user";
		}

		// 从数据库查询出原商品数据
		Product oldProduct = null;
		
		try {
			oldProduct = (Product) productDao.get(product.getId());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 如果仓库中不存在该商品，则转到新增保存方法
		if (oldProduct == null) {
			this.saveProduct(product, request, response);
		}

		String systemCheckstatus = Cache.getSystemParamConfig("是否需要审核");
		if (!"0".equals(systemCheckstatus)) {

			// 判断商品是否需要重新审核 -- 管理员修改则不需要重新审核
			if (ProductHelper.checkDeffrens(oldProduct.getProductNumber(), product.getProductNumber())) {
				checkStatus = false;
			}

			// 判断商品是否需要重新审核 -- 管理员修改则不需要重新审核
			if (ProductHelper.checkDeffrens(oldProduct.getName(), product.getName())) {
				checkStatus = false;
			}

			// 判断商品是否需要重新审核 -- 管理员修改则不需要重新审核
			if (ProductHelper.checkDeffrens(oldProduct.getSortName(), product.getSortName())) {
				checkStatus = false;
			}

			// 判断商品是否需要重新审核 -- 管理员修改则不需要重新审核
			if (ProductHelper.checkDeffrens(oldProduct.getSearchKey(), product.getSearchKey())) {
				checkStatus = false;
			}

			// 判断商品是否需要重新审核 -- 管理员修改则不需要重新审核
			if (ProductHelper.checkDeffrens(oldProduct.getNote(), product.getNote())) {
				checkStatus = false;
			}

			// 判断商品是否需要重新审核 -- 管理员修改则不需要重新审核
			if (ProductHelper.checkDeffrens(oldProduct.getDescription(), product.getDescription())) {
				checkStatus = false;
			}
		} else {
			checkStatus = true;
		}

		if (product.getUnitPrice() != null && oldProduct.getUnitPrice().compareTo(product.getUnitPrice()) != 0) {
			product.setRiseDrop((product.getUnitPrice().subtract(oldProduct.getUnitPrice())).doubleValue());
			oldProduct.setRiseDrop(product.getRiseDrop());
		}
		// 将商品简称称放入原商品中
		oldProduct.setSortName(product.getSortName());
		// 将商品的编号放入原商品中
		oldProduct.setProductNumber(product.getProductNumber());
		// 将商品名称放入原商品中
		oldProduct.setName(product.getName());
		// 将商品库存称放入原商品中
		oldProduct.setQuantity(product.getQuantity());
		// 将商品库存预警放入原商品中
		oldProduct.setQuantityWarning(product.getQuantityWarning());
		// 将商品商城价称放入原商品中
		oldProduct.setUnitPrice(product.getUnitPrice());
		// 将商品市场价放入原商品中
		oldProduct.setListPrice(product.getListPrice());
		// 将商品运费称放入原商品中
		oldProduct.setFare(product.getFare());
		// 将商品所属品牌放入原商品中
		oldProduct.setBrand(product.getBrand());
		// 将状态放入原商品中
		oldProduct.setStatus(product.isStatus());
		// 将模板id放入原商品中
		oldProduct.setTemplateId(product.getTemplateId());
		// 将分类id放入原商品中
		oldProduct.setMainCategory(product.getMainCategory());
		// 将地区ID放入原商品中
		oldProduct.setHabitat(product.getHabitat());
		// 将商品所在详细地放入原商品中
		oldProduct.setHabitatAddress(product.getHabitatAddress());
		oldProduct.setType(product.getType());
		oldProduct.setUnit(product.getUnit());
		oldProduct.setPfPrice(product.getPfPrice());

		// 将商品简称称放入原商品中
		oldProduct.setSearchKey(product.getSearchKey());
		// 将商品简介称放入原商品中
		oldProduct.setNote(product.getNote());

		// 将商品介绍称放入原商品中
		oldProduct.setDescription(product.getDescription());

		oldProduct.setJcjg(product.getJcjg());
		oldProduct.setProductModel(product.getProductModel());
		oldProduct.setDeliceAddress(product.getDeliceAddress());
		oldProduct.setDeliceAddressId(product.getDeliceAddressId());
		oldProduct.setDeliceType(product.getDeliceType());
		oldProduct.setDeliceData(product.getDeliceData());
		oldProduct.setIsAdvance(product.getIsAdvance());
		oldProduct.setAdvancePrice(product.getAdvancePrice());
		oldProduct.setAdvanceNum(product.getAdvanceNum());
		oldProduct.setPayType(product.getPayType());
		oldProduct.setProductAreaId(product.getProductAreaId());
		oldProduct.setProductJjdStatus(product.getProductJjdStatus());
	
		// 定义商品多角度视图的properties数组
		String[] popPicProperties = null;

		// 获取模板中非规格型号类的字段值 取出来是 tempId;name;category;position;value;
		String[] tempFields = request.getParameterValues("templateFields");

		// 获取商品多角度视图图片控件的property值
		String popPic = request.getParameter("popPicProperties");
		if (!StringUtil.isEmpty(popPic)) {
			popPicProperties = popPic.split(";");
		}

		// 获取商品扩展字段规格型号类的选中
		String[] modelConfigs = request.getParameterValues("modelDetailCheck");

		// 定义Session
		Session session = null;
		// 定义事物处理
		Transaction transaction = null;
		try {
			// 创建Session
			session = productDao.createSession();
			// 开启事物处理
			transaction = session.beginTransaction();

			// 设置图标保存路径
			String currentPath = "/customerfiles/store/" + product.getStoreId() + "/product/" + DateUtil.getNoSpSysDateString();
			// 调用方法获取分类图标，返回图标路径
			String picturePath = FileUploadHelper.uploadFile("picture", currentPath, request, response);
			// 获取PDF文件路径转换为服务端路径
			String pdf = FileUploadHelper.uploadFile("jcFile", currentPath, request, response);
			product.setJcFile(pdf);
			// 商品图 - 大中小+推荐图
			if (!StringUtil.isEmpty(picturePath)) {
				if (StringUtil.isEmpty(oldProduct.getPicture()) || !picturePath.equals(oldProduct.getPicture())) {
					// 保存原图
					oldProduct.setPicture(picturePath);
					// 调用Helper方法处理图片
					ProductHelper.img(ProductHelper.PIC_TYPE_PIC, oldProduct, request);
					if (!"0".equals(systemCheckstatus)) {
						checkStatus = false;
					}
				}
			}

			List<ProductPicture> priceList = null;
			// 删除原商品多角度视图 -- 使用SQL删除
			Map<String, Object> delMap = new HashMap<String, Object>();
			StringBuffer delPops = new StringBuffer(" from ProductPicture as pic where 1=1");
			delPops.append(" and pic.productId = :productId");
			delMap.put("productId", product.getId());

			// 先查出原商品多角度视图
			priceList = productPictureDao.list(delPops.toString(), delMap, session);
			// 执行SQL删除多角度视图数据
			productPictureDao.delete(delPops.toString(), delMap, session);

			// 将查询出来的List转化成Map
			Map<String, Object> oldPicMap = new HashMap<String, Object>();
			if (priceList != null) {
				for (int i = 0; i < priceList.size(); i++) {
					ProductPicture pic = priceList.get(i);
					if (pic != null && !StringUtil.isEmpty(pic.getPicture())) {
						oldPicMap.put(pic.getPicture(), true);
					}
				}
			}

			// 商品多角度视图
			if (popPicProperties != null && popPicProperties.length > 0) {
				for (int i = 0; i < popPicProperties.length; i++) {
					String picProperty = popPicProperties[i];
					if (!StringUtil.isEmpty(picProperty)) {
						// 调用方法获取分类图标，返回图标路径
						String popPicPath = FileUploadHelper.uploadFile(picProperty, currentPath, request, response);
						if (!"0".equals(systemCheckstatus) && !oldPicMap.containsKey(popPicPath)) {
							checkStatus = false;
						}
						// 如果获取的图片路径不为空，则定义一个多角多视图对象，将数据放入进去
						if (!StringUtil.isEmpty(popPicPath)) {
							ProductPicture pPicture = new ProductPicture();
							pPicture.setPicture(popPicPath);
							pPicture.setPosition((i + 1) + "");
							pPicture.setProductId(product.getId());
							// 调用方法处理多角多视图
							ProductHelper.img(ProductHelper.PIC_TYPE_POP, pPicture, request);
							// 调用DAO采用事物处理的方式保存数据
							productPictureDao.addBean(pPicture, session);
						}
					}
				}
			}

			// 删除原商品与分类关联关系-- 使用SQL删除
			Map<String, Object> categoryMap = new HashMap<String, Object>();
			StringBuffer categoryPops = new StringBuffer(" from ProductCategoryRel as catrgoryRel where 1=1");
			categoryPops.append(" and catrgoryRel.productId = :productId");
			categoryMap.put("productId", product.getId());
			productPictureDao.delete(categoryPops.toString(), categoryMap, session);
			// 创建商品与商品分类的关系表
			if (!StringUtil.isEmpty(product.getMainCategory())) {
				// 定义新的对象
				ProductCategoryRel pcRel = new ProductCategoryRel();
				// 将数据放入对象
				pcRel.setCategoryId(product.getMainCategory());
				pcRel.setMain(true);
				pcRel.setPosition(0);
				pcRel.setProductId(product.getId());
				// 调用DAO方法采用事物处理的方式保存数据
				productCategoryRelDao.addBean(pcRel, session);
			}

			// 删除原商品扩展属性
			Map<String, Object> tempsMap = new HashMap<String, Object>();
			StringBuffer tempSql = new StringBuffer(" from ProductInfo as info where 1=1");
			tempSql.append(" and info.productId = :productId");
			tempsMap.put("productId", product.getId());
			infoDao.delete(tempSql.toString(), tempsMap, session);

			List<ProductInfoDetail> detailList = new ArrayList<ProductInfoDetail>();
			// 图片MAP
			Map<String, String> picMap = new HashMap<String, String>();
			// 规格参数的动态Map
			Map<String, ProductInfo> infoMap = new HashMap<String, ProductInfo>();
			// 商品规格型号
			if (modelConfigs != null && modelConfigs.length > 0) {
				for (int i = 0; i < modelConfigs.length; i++) {
					String configNames = modelConfigs[i];
					if (!StringUtil.isEmpty(configNames)) {
						ProductInfoDetail infoDetail = null;

						String infoDetailId = "";

						if (configNames.indexOf("||") != -1) {
							infoDetailId = configNames.substring(configNames.lastIndexOf("||") + 2);
							configNames = configNames.substring(0, configNames.lastIndexOf("||"));
						}
						String[] tempIds = configNames.split("--");

						if (!StringUtil.isEmpty(infoDetailId)) {
							infoDetail = (ProductInfoDetail) infoDetailDao.get(infoDetailId);
						}

						if (infoDetail == null) {
							infoDetail = new ProductInfoDetail();
						}

						if (tempIds != null && tempIds.length > 0) {
							infoDetail.setProductId(product.getId());
							infoDetail.setQuantityWarning(Integer.parseInt(request.getParameter(configNames + "Warn")));
							infoDetail.setQuantity(Integer.parseInt(request.getParameter(configNames + "Stock")));
							infoDetail.setPrice(new BigDecimal(request.getParameter(configNames + "Price")));

							if (tempIds.length >= 1) {
								String title = request.getParameter(tempIds[0] + "Title");
								String value = request.getParameter("tempValue" + tempIds[0]);
								String categoryId = request.getParameter("category" + tempIds[0]);
								String position = request.getParameter("position" + tempIds[0]);
								String tempId = tempIds[0].substring(tempIds[0].indexOf("-") + 1, tempIds[0].length());

								infoDetail.setInfoId1(tempId);
								infoDetail.setInfoName1(title);
								infoDetail.setInfoValue1(value);

								if (picMap.containsKey(tempIds[0])) {
									infoDetail.setInfoPic1(picMap.get(tempIds[0]));
								} else {
									String modelPicPath = FileUploadHelper.uploadFile("pic" + tempIds[0], currentPath, request, response);
									infoDetail.setInfoPic1(modelPicPath);
									picMap.put(tempIds[0], modelPicPath);
								}

								if (infoMap.containsKey(tempId)) {
									ProductInfo pInfo = infoMap.get(tempId);
									if (!pInfo.getInfoValue().contains(value)) {
										pInfo.setInfoValue(pInfo.getInfoValue() + ";" + value);
										infoMap.remove(tempId);
										infoMap.put(tempId, pInfo);
									}

								} else {
									ProductInfo pInfo = new ProductInfo();
									pInfo.setCategoryId(categoryId);
									pInfo.setInfoName(title);
									pInfo.setInfoValue(value);
									pInfo.setProductId(product.getId());
									pInfo.setInfoType("1");
									pInfo.setPosition(Integer.parseInt(position));
									pInfo.setTemplateFieldId(tempId);
									infoMap.put(tempId, pInfo);
								}

							}

							if (tempIds.length >= 2) {
								String title = request.getParameter(tempIds[1] + "Title");
								String value = request.getParameter("tempValue" + tempIds[1]);
								String categoryId = request.getParameter("category" + tempIds[1]);
								String position = request.getParameter("position" + tempIds[1]);
								String tempId = tempIds[1].substring(tempIds[1].indexOf("-") + 1, tempIds[1].length());

								infoDetail.setInfoId2(tempId);
								infoDetail.setInfoName2(title);
								infoDetail.setInfoValue2(value);
								if (picMap.containsKey(tempIds[1])) {
									infoDetail.setInfoPic2(picMap.get(tempIds[1]));
								} else {
									String modelPicPath = FileUploadHelper.uploadFile("pic" + tempIds[1], currentPath, request, response);
									infoDetail.setInfoPic2(modelPicPath);
									picMap.put(tempIds[1], modelPicPath);
								}

								if (infoMap.containsKey(tempId)) {
									ProductInfo pInfo = infoMap.get(tempId);
									if (!pInfo.getInfoValue().contains(value)) {
										pInfo.setInfoValue(pInfo.getInfoValue() + ";" + value);
										infoMap.remove(tempId);
										infoMap.put(tempId, pInfo);
									}

								} else {
									ProductInfo pInfo = new ProductInfo();
									pInfo.setCategoryId(categoryId);
									pInfo.setInfoName(title);
									pInfo.setInfoType("1");
									pInfo.setPosition(Integer.parseInt(position));
									pInfo.setInfoValue(value);
									pInfo.setProductId(product.getId());
									pInfo.setTemplateFieldId(tempId);
									infoMap.put(tempId, pInfo);
								}
							}

							if (tempIds.length >= 3) {

								String title = request.getParameter(tempIds[2] + "Title");
								String value = request.getParameter("tempValue" + tempIds[2]);
								String categoryId = request.getParameter("category" + tempIds[2]);
								String position = request.getParameter("position" + tempIds[2]);
								String tempId = tempIds[2].substring(tempIds[2].indexOf("-") + 1, tempIds[2].length());

								infoDetail.setInfoId3(tempId);
								infoDetail.setInfoName3(title);
								infoDetail.setInfoValue3(value);
								if (picMap.containsKey(tempIds[2])) {
									infoDetail.setInfoPic3(picMap.get(tempIds[2]));
								} else {
									String modelPicPath = FileUploadHelper.uploadFile("pic" + tempIds[2], currentPath, request, response);
									infoDetail.setInfoPic3(modelPicPath);
									picMap.put(tempIds[2], modelPicPath);
								}

								if (infoMap.containsKey(tempId)) {
									ProductInfo pInfo = infoMap.get(tempId);
									if (!pInfo.getInfoValue().contains(value)) {
										pInfo.setInfoValue(pInfo.getInfoValue() + ";" + value);
										infoMap.remove(tempId);
										infoMap.put(tempId, pInfo);
									}

								} else {
									ProductInfo pInfo = new ProductInfo();
									pInfo.setCategoryId(categoryId);
									pInfo.setInfoName(title);
									pInfo.setInfoValue(value);
									pInfo.setProductId(product.getId());
									pInfo.setInfoType("1");
									pInfo.setPosition(Integer.parseInt(position));
									pInfo.setTemplateFieldId(tempId);
									infoMap.put(tempId, pInfo);
								}
							}

							if (tempIds.length >= 4) {
								String title = request.getParameter(tempIds[3] + "Title");
								String value = request.getParameter("tempValue" + tempIds[3]);
								String categoryId = request.getParameter("category" + tempIds[3]);
								String position = request.getParameter("position" + tempIds[3]);
								String tempId = tempIds[3].substring(tempIds[3].indexOf("-") + 1, tempIds[3].length());

								infoDetail.setInfoId4(tempId);
								infoDetail.setInfoName4(title);
								infoDetail.setInfoValue4(value);
								if (picMap.containsKey(tempIds[3])) {
									infoDetail.setInfoPic4(picMap.get(tempIds[3]));
								} else {
									String modelPicPath = FileUploadHelper.uploadFile("pic" + tempIds[3], currentPath, request, response);
									infoDetail.setInfoPic4(modelPicPath);
									picMap.put(tempIds[3], modelPicPath);
								}

								if (infoMap.containsKey(tempId)) {
									ProductInfo pInfo = infoMap.get(tempId);
									if (!pInfo.getInfoValue().contains(value)) {
										pInfo.setInfoValue(pInfo.getInfoValue() + ";" + value);
										infoMap.remove(tempId);
										infoMap.put(tempId, pInfo);
									}

								} else {
									ProductInfo pInfo = new ProductInfo();
									pInfo.setCategoryId(categoryId);
									pInfo.setInfoName(title);
									pInfo.setInfoValue(value);
									pInfo.setProductId(product.getId());
									pInfo.setInfoType("1");
									pInfo.setPosition(Integer.parseInt(position));
									pInfo.setTemplateFieldId(tempId);
									infoMap.put(tempId, pInfo);
								}
							}

						}
						detailList.add(infoDetail);
					}
				}
			}

			// 删除原商品规格参数
			Map<String, Object> tempInfoMap = new HashMap<String, Object>();
			StringBuffer infoSql = new StringBuffer(" from ProductInfoDetail as infoDetail where 1=1");
			infoSql.append(" and infoDetail.productId = :productId");

			if (detailList != null && detailList.size() > 0) {
				infoSql.append(" and infoDetail.id not in (");
				for (int i = 0; i < detailList.size(); i++) {
					ProductInfoDetail infoDetail = detailList.get(i);
					if (infoDetail != null && !StringUtil.isEmpty(infoDetail.getId())) {
						infoDetailDao.updateBean(infoDetail, session);
					} else {
						infoDetailDao.addBean(infoDetail, session);
					}

					if (i <= 0) {
						infoSql.append(":rmvId").append(i);
					} else {
						infoSql.append(",:rmvId").append(i);
					}
					tempInfoMap.put("rmvId" + i, infoDetail.getId());
				}
				infoSql.append(")");
			}

			tempInfoMap.put("productId", product.getId());
			infoDetailDao.delete(infoSql.toString(), tempInfoMap, session);

			if (tempFields != null && tempFields.length > 0) {
				for (int i = 0; i < tempFields.length; i++) {
					String field = tempFields[i];
					if (!StringUtil.isEmpty(field)) {
						String[] fieldStrs = field.split("\\*\\*");
						if (fieldStrs != null && fieldStrs.length > 0) {
							ProductInfo pInfo = new ProductInfo();
							pInfo.setCategoryId(fieldStrs[2]);
							pInfo.setInfoName(fieldStrs[1]);
							pInfo.setInfoValue(fieldStrs.length == 5 ? fieldStrs[4] : "");
							pInfo.setInfoType("0");
							pInfo.setPosition(Integer.parseInt(fieldStrs[3]));
							pInfo.setTemplateFieldId(fieldStrs[0]);
							pInfo.setProductId(product.getId());
							infoDao.addBean(pInfo, session);
						}
					}
				}
			}

			// 更新商品批发价

			// 删除原商品批发价
			Map<String, Object> priceParamMap = new HashMap<String, Object>();
			StringBuffer priceSql = new StringBuffer(" from ProductValuation as pv where 1=1");
			priceSql.append(" and pv.productId = :productId");
			priceParamMap.put("productId", product.getId());
			infoDao.delete(priceSql.toString(), priceParamMap, session);
			// 获取商品价格集合
			String[] valuationId = request.getParameterValues("valuationId");
			String[] valuationNumber = request.getParameterValues("valuationNumber");
			String[] valuationPrice = request.getParameterValues("valuationPrice");
			String[] valuationUnit = request.getParameterValues("valuationUnit");
			// 保存赠品
			if (valuationId != null && valuationId.length > 0) {
				for (int i = 0; i < valuationId.length; i++) {
					if (StringUtil.isEmpty(valuationId[i])) {
						String hql = "delete from ProductValuation where productId='" + product.getId() + "'";
						// 根据商品ID查询阶梯价表
						productPictureDao.delete(hql, null, session);
						continue;
					}
					ProductValuation pv = new ProductValuation();
					pv.setProductId(product.getId());
					String[] vaNameAndId = valuationId[i].split("_");
					pv.setValuationId(vaNameAndId[0]);
					pv.setSpecName(vaNameAndId[1]);
					pv.setValuationNumber(new BigDecimal(valuationNumber[i]));
					pv.setValuationPrice(new BigDecimal(valuationPrice[i]));
					pv.setValuationUnit(valuationUnit[i]);
					pv.setCreateTime(DateUtil.getSysDateTimeString());
					productValuationDao.addBean(pv, session);
				}
			}

			Collection<ProductInfo> c = infoMap.values();
			Iterator<ProductInfo> it = c.iterator();
			for (; it.hasNext();) {
				ProductInfo info = (ProductInfo) it.next();
				info.setInfoType("1");
				infoDao.addBean(info, session);
			}

			if (!checkStatus && !"system".equals(userType)) {
				oldProduct.setCheckStatus(0);
			} else {
				oldProduct.setCheckStatus(1);
			}
			oldProduct.setUpdateTime(DateUtil.getSysDateTimeString());
			oldProduct.setCreateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法添加商品
			productDao.updateBean(oldProduct, session);
			// 事物处理提交以及关闭事物
			session.flush();
			transaction.commit();
			session.close();

			IProductCacheService cacheService = new ProductCacheServiceImpl();
			cacheService.refrefshProductsByCategory(oldProduct.getMainCategory(), oldProduct.getType());
			cacheService.refreshProduct(oldProduct.getId());
			// if (!checkStatus) {
			StringBuffer sql = new StringBuffer("select rel.categoryId from ProductCategoryRel as rel where 1=1");
			sql.append(" and rel.productId = :productId");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("productId", product.getId());
			try {
				List list = productCategoryRelDao.list(sql.toString(), paramMap);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						cacheService.refrefshProductsByCategory((String) list.get(i), product.getType());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// }
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 根据id查找商品
	 * 
	 * @param id
	 * @return
	 */
	public Product getProductById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			// 调用DAO方法查找角色
			return (Product) productDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据id查找商品
	 * 
	 * @param id
	 * @return
	 */
	public Product getProductById(String id, org.hibernate.Session session) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			// 调用DAO方法查找角色
			return (Product) productDao.get(id, session);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 修改商品
	 * 
	 * @param product
	 */
	public boolean modifyProduct(Product product) {
		if (product == null) {
			return false;
		}
		try {
			product.setUpdateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法修改商品
			productDao.updateBean(product);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

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
	public List<Product> listProduct(Pager pager, String name, String status, String brand, String storeName, String storeId, String min, String max) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select p.id,p.name,p.unitPrice,p.listPrice,p.quantity,pb.name,p.status,p.checkStatus,p.visitCount,p.buyCount,s.storeName,p.productNumber from Product as p ");
		sql.append(",ProductBrand as pb");
		sql.append(", Store as s ");
		sql.append(" where 1=1");
		sql.append(" and p.brand = pb.id");
		sql.append(" and p.storeId = s.id");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and p.name like :name");
			paramMap.put("name", "%" + name + "%");
		}
		// 如果状态不为空，添加查询条件
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and p.status = :status");
			paramMap.put("status", Boolean.parseBoolean(status));
		}
		// 如果查询条件输入了brand，添加查询条件
		if (!StringUtil.isEmpty(brand)) {
			sql.append(" and pb.name like :brand");
			paramMap.put("brand", "%" + brand + "%");
		}
		// 关联店铺名
		if (!StringUtil.isEmpty(storeName)) {
			sql.append(" and s.storeName like :storeName");
			paramMap.put("storeName", "%" + storeName + "%");
		}
		// 关联店铺id
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and s.id = :storeId");
			paramMap.put("storeId", storeId);
		}

		// 价格区间的查找
		if (!StringUtil.isEmpty(min) && !StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice  BETWEEN :minUnitPrice AND :maxUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(min));
			paramMap.put("maxUnitPrice", new BigDecimal(max));
		} else if (!StringUtil.isEmpty(min) && StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice > :minUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(min));
		} else if (StringUtil.isEmpty(min) && !StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice < :maxUnitPrice");
			paramMap.put("maxUnitPrice", new BigDecimal(max));
		}

		sql.append(" order by p.checkTime desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = productDao.list(pager, sql.toString(), paramMap);
			List<Product> products = new ArrayList<Product>();
			for (Object[] object : list) {

				Product product = new Product();
				product.setId((String) object[0]);
				product.setName((String) object[1]);
				product.setUnitPrice((BigDecimal) object[2]);
				product.setListPrice((BigDecimal) object[3]);
				product.setQuantity((Integer) object[4]);
				product.setBrand((String) object[5]);
				product.setStatus((Boolean) object[6]);
				product.setCheckStatus((Integer) object[7]);
				product.setVisitCount((Integer) object[8]);
				product.setBuyCount((Integer) object[9]);
				product.setStoreId((String) object[10]);
				product.setProductNumber((String) object[11]);
				products.add(product);
			}
			return products;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Product> getProductsWithoutBrand(Pager pager, String name, String status, String checkStatus, String storeName, String storeId, String min, String max,
			String type, String productNumber, String categoryId, String fullPath) {
		// 定义查询SQL
		// rootCatgId
		StringBuffer sql = new StringBuffer(
				" select p.id,p.name,p.unitPrice,p.pfPrice,p.quantity,p.status,p.checkStatus,p.visitCount,p.buyCount,s.storeName ,p.productNumber,p.smallPicture,pc.fullPath from Product as p , Store as s,ProductCategoryRel as pcr,ProductCategory as pc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.storeId = s.id");
		sql.append(" and pcr.productId = p.id");
		sql.append(" and pcr.categoryId = pc.id");
		if (!StringUtil.isEmpty(fullPath)) {
			// 关联商品分类
			sql.append(" and pc.fullPath like :fullPath");
			paramMap.put("fullPath", "%" + fullPath + "%");
		}
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(categoryId)) {
			sql.append(" and pcr.categoryId = :categoryId");
			paramMap.put("categoryId", categoryId);
		}
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and p.name like :name");
			paramMap.put("name", "%" + name + "%");
		}

		// 如果状态不为空，添加查询条件
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and p.status = :status");
			paramMap.put("status", Boolean.parseBoolean(status));
		}
		// 关联店铺名
		if (!StringUtil.isEmpty(storeName)) {
			sql.append(" and s.storeName like :storeName");
			paramMap.put("storeName", "%" + storeName + "%");
		}

		// 关联店铺id
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and s.id = :storeId");
			paramMap.put("storeId", storeId);
		}
		if (!StringUtil.isEmpty(checkStatus)) {
			sql.append(" and p.checkStatus = :checkStatus");
			paramMap.put("checkStatus", Integer.parseInt(checkStatus));
		}

		if (!StringUtil.isEmpty(productNumber)) {
			sql.append(" and p.productNumber=:productNumber");
			paramMap.put("productNumber", productNumber);
		}
		// 价格区间的查找
		if (!StringUtil.isEmpty(min) && !StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice  BETWEEN :minUnitPrice AND :maxUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(min));
			paramMap.put("maxUnitPrice", new BigDecimal(max));
		} else if (!StringUtil.isEmpty(min) && StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice >= :minUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(min));
		} else if (StringUtil.isEmpty(min) && !StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice <= :maxUnitPrice");
			paramMap.put("maxUnitPrice", new BigDecimal(max));
		}
		sql.append(" order by p.id desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = productDao.list(pager, sql.toString(), paramMap);
			List<Product> products = new ArrayList<Product>();
			for (Object[] object : list) {

				Product product = new Product();
				product.setId((String) object[0]);
				product.setName((String) object[1]);
				product.setUnitPrice((BigDecimal) object[2]);
				product.setListPrice((BigDecimal) object[3]);
				product.setQuantity((Integer) object[4]);
				product.setStatus((Boolean) object[5]);
				product.setCheckStatus((Integer) object[6]);
				product.setVisitCount((Integer) object[7]);
				product.setBuyCount((Integer) object[8]);
				product.setStoreId((String) object[9]);
				product.setProductNumber((String) object[10]);
				product.setSmallPicture((String) object[11]);
				product.setRootCatgId(formatCatgRootId((String) object[12]));
				products.add(product);
			}
			return products;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String formatCatgRootId(String source) {
		if (StringUtil.isEmpty(source)) {
			return "";
		}
		String formated = "";
		try {
			int indexBegin = source.indexOf("_", source.indexOf("_") + 1);
			int indexEnd = source.indexOf("_", indexBegin + 1);
			formated = source.substring(indexBegin + 1, indexEnd == -1 ? source.length() : indexEnd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formated;
	}

	@Override
	public List<Product> getProductsWithoutBrand2(Pager pager, String name, String storeName, String storeId, String min, String max, String productNumber, String categoryId,
			String fullPath) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select p.id,p.name,p.unitPrice,p.pfPrice,p.quantity,p.status,p.checkStatus,p.visitCount,p.buyCount,s.storeName ,p.productNumber,p.smallPicture,pc.fullPath from Product as p , Store as s,ProductCategoryRel as pcr,ProductCategory as pc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.storeId = s.id");
		sql.append(" and pcr.productId = p.id");
		sql.append(" and pcr.categoryId = pc.id");
		if (!StringUtil.isEmpty(fullPath)) {
			// 关联商品分类
			sql.append(" and pc.fullPath like :fullPath");
			paramMap.put("fullPath", "%" + fullPath + "%");
		}
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(categoryId)) {
			sql.append(" and pcr.categoryId = :categoryId");
			paramMap.put("categoryId", categoryId);
		}
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and p.name like :name");
			paramMap.put("name", "%" + name + "%");
		}

		// 关联店铺名
		if (!StringUtil.isEmpty(storeName)) {
			sql.append(" and s.storeName like :storeName");
			paramMap.put("storeName", "%" + storeName + "%");
		}

		// 关联店铺id
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and s.id = :storeId");
			paramMap.put("storeId", storeId);
		}
		if (!StringUtil.isEmpty(productNumber)) {
			sql.append(" and p.productNumber = :productNumber");
			paramMap.put("productNumber", productNumber);
		}
		sql.append(" and (p.checkStatus = :checkStatus or p.status=:status)");
		paramMap.put("status", false);
		paramMap.put("checkStatus", 0);

		// 价格区间的查找
		if (!StringUtil.isEmpty(min) && !StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice  BETWEEN :minUnitPrice AND :maxUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(min));
			paramMap.put("maxUnitPrice", new BigDecimal(max));
		} else if (!StringUtil.isEmpty(min) && StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice >= :minUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(min));
		} else if (StringUtil.isEmpty(min) && !StringUtil.isEmpty(max)) {
			sql.append(" and p.unitPrice <= :maxUnitPrice");
			paramMap.put("maxUnitPrice", new BigDecimal(max));
		}
		sql.append(" order by p.id desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = productDao.list(pager, sql.toString(), paramMap);
			List<Product> products = new ArrayList<Product>();
			for (Object[] object : list) {
				Product product = new Product();
				product.setId((String) object[0]);
				product.setName((String) object[1]);
				product.setUnitPrice((BigDecimal) object[2]);
				product.setListPrice((BigDecimal) object[3]);
				product.setQuantity((Integer) object[4]);
				product.setStatus((Boolean) object[5]);
				product.setCheckStatus((Integer) object[6]);
				product.setVisitCount((Integer) object[7]);
				product.setBuyCount((Integer) object[8]);
				product.setStoreId((String) object[9]);
				product.setProductNumber((String) object[10]);
				product.setSmallPicture((String) object[11]);
				product.setRootCatgId(formatCatgRootId((String) object[12]));
				products.add(product);
			}
			return products;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询为审核商品
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
			String fullPath) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select p.id,p.name,p.unitPrice,p.pfPrice,p.quantity,p.status,p.checkStatus,p.visitCount,p.buyCount,s.storeName ,p.productNumber from Product as p, Store as s ,ProductCategoryRel as pcr,ProductCategory as pc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.storeId = s.id");
		sql.append(" and pcr.productId = p.id");
		sql.append(" and pcr.categoryId = pc.id");
		if (!StringUtil.isEmpty(fullPath)) {
			// 关联商品分类
			sql.append(" and pc.fullPath like :fullPath");
			paramMap.put("fullPath", "%" + fullPath + "%");
		}
		// sql.append(" and p.brand = pb.id");
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and p.name like :name");
			paramMap.put("name", "%" + name + "%");
		}

		// 如果状态不为空，添加查询条件
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and p.status = :status");
			paramMap.put("status", Boolean.parseBoolean(status));
		}

		// 如果查询条件输入了brand，添加查询条件
		if (!StringUtil.isEmpty(brand)) {
			sql.append(" and pb.name like :brand");
			paramMap.put("brand", "%" + brand + "%");
		}
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and s.storeName like :storeName");
			paramMap.put("storeName", "%" + storeId + "%");
		}
		if (!StringUtil.isEmpty(productNumber)) {
			sql.append(" and p.productNumber = :productNumber");
			paramMap.put("productNumber", productNumber);
		}
		// 价格区间的查找
		if (!StringUtil.isEmpty(minUnitPrice) && !StringUtil.isEmpty(maxUnitPrice)) {
			sql.append(" and p.unitPrice  BETWEEN :minUnitPrice AND :maxUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(minUnitPrice));
			paramMap.put("maxUnitPrice", new BigDecimal(maxUnitPrice));
		} else if (!StringUtil.isEmpty(minUnitPrice) && StringUtil.isEmpty(maxUnitPrice)) {
			sql.append(" and p.unitPrice >= :minUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(minUnitPrice));
		} else if (StringUtil.isEmpty(minUnitPrice) && !StringUtil.isEmpty(maxUnitPrice)) {
			sql.append(" and p.unitPrice <= :maxUnitPrice");
			paramMap.put("maxUnitPrice", new BigDecimal(maxUnitPrice));
		}
		sql.append(" and (p.checkStatus = 0 or p.checkStatus = -1)");
		sql.append(" order by p.id desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = productDao.list(pager, sql.toString(), paramMap);
			List<Product> products = new ArrayList<Product>();
			for (Object[] object : list) {
				Product product = new Product();
				product.setId((String) object[0]);
				product.setName((String) object[1]);
				product.setUnitPrice((BigDecimal) object[2]);
				product.setListPrice((BigDecimal) object[3]);
				product.setQuantity((Integer) object[4]);
				// product.setBrand((String) object[10]);
				product.setStatus((Boolean) object[5]);
				product.setCheckStatus((Integer) object[6]);
				product.setVisitCount((Integer) object[7]);
				product.setBuyCount((Integer) object[8]);
				product.setStoreId((String) object[9]);
				product.setProductNumber((String) object[10]);
				products.add(product);

			}
			return products;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 检查商品id是否存在
	 */
	public boolean checkId(String id) {
		try {
			if (productDao.get(id) != null) {
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 商品列表的查询包括
	 * 
	 * 1、商品列表中的分类ID，品牌ID，模板ID，模板扩展字段值，价格，销量 2、分类名 3、模板名 4、模板扩展字段的值 5、品牌名 6、页面显示：
	 * 
	 * 
	 */
	public List<Product> listProscenium(String brand, String storeId, String templateId, Double unitPrice, Integer buyCount, String mainCategory, String fieldName,
			String fieldValue) {
		// StringBuffer sql = new
		// StringBuffer("select p.id,p.name,p.unitPrice,p.buyCount,p.mainCategory, p.templateId,p.storeId,p.brand,pb.name,pt.name ,"
		// +
		// "s.name ,tf.tempId,pc.name,from Product as p,ProductBrand as pb,ProductTemplate as pt,Store as s ,ProductCategory as pc where 1=1");

		StringBuffer sql = new StringBuffer(
				"select p.id,p.unitPrice,p.buyCount,p.mainCategory, p.templateId,p.brand,pb.name,pt.name,tf.fieldValue,"
						+ "pc.name, tf.fieldName,p.storeId ,s.province p.picture from Product as p,ProductBrand as pb,ProductTemplate as pt,TemplateFields as tf ,ProductCategory as pc where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		// 建立各表的对应关系
		sql.append(" and p.storeId = s.id");
		sql.append(" and p.brand = pb.id");
		sql.append(" and p.templateId = pt.id");
		sql.append(" and p.mainCategory = pc.id");
		sql.append(" and tf.tempId = pt.id");

		if (!StringUtil.isEmpty(brand)) {
			sql.append(" and pb.name = :brand");
			map.put("brand", brand);
		}
		if (!StringUtil.isEmpty(templateId)) {
			sql.append(" and pt.name = :templateId");
			map.put("templateId", templateId);
		}
		if (!StringUtil.isEmpty(mainCategory)) {
			sql.append(" and pc.name = :mainCategory");
			map.put("mainCategory", mainCategory);
		}
		if (!StringUtil.isEmpty(fieldValue)) {
			sql.append(" and tf.fieldValue = :fieldValue");
			map.put("fieldValue", fieldValue);
		}
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and p.storeId = :storeId");
			map.put("storeId", storeId);
		}
		sql.append(" order by p.buyCount desc");
		// sql.append(" order by p.unitPrice desc");

		try {

			List<Object[]> list = productDao.list(sql.toString(), map);
			List<Product> products = new ArrayList<Product>();
			for (Object[] object : list) {
				Product product = new Product();
				product.setId((String) object[0]);
				product.setUnitPrice((BigDecimal) object[1]);
				product.setBuyCount((Integer) object[2]);
				product.setMainCategory((String) object[3]);
				product.setTemplateId((String) object[4]);
				product.setBrand((String) object[5]);
				product.setBrandName((String) object[6]);
				product.setTemplateName((String) object[7]);
				product.setFieldValue((String) object[8]);
				product.setCategoryName((String) object[9]);
				product.setFieldName((String) object[10]);
				product.setStoreId((String) object[11]);
				product.setProvince((String) object[12]);
				product.setPicture((String) object[13]);
				products.add(product);
			}
			return products;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据店铺Id取得product集合
	 */
	public List<Product> listProduct(String id) {
		StringBuffer sql = new StringBuffer("select p from Product as p,Store as s where 1=1");
		sql.append(" and s.id = p.storeId");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(id)) {
			sql.append(" and p.storeId = :storeId");
			map.put("storeId", id);
		}
		try {
			List<Product> list = productDao.list(sql.toString(), map);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Product> getGuessLikeProduct(String orderId) {
		List<Product> productList = new ArrayList<Product>();
		String hql = "SELECT DISTINCT pr.id,pr.name,pr.picture,pr.listPrice,pr.unitPrice,pr.buyCount FROM OrderDetail od,ProductCategoryRel pcr,Product pr"
				+ " WHERE od.productId=pcr.productId AND pr.id=pcr.productId AND od.orderId=:orderId" + " ORDER BY pr.buyCount DESC,pr.visitCount DESC LIMIT 0,8";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);

		try {
			List<Object[]> list = productDao.list(hql, map);
			if (null == list || list.size() <= 0) {
				return null;
			}
			for (Object[] obj : list) {
				Product pr = new Product();
				pr.setId(obj[0].toString());
				pr.setName(obj[1].toString());
				pr.setPicture(obj[2] == null ? "" : obj[2].toString());
				pr.setListPrice((BigDecimal) obj[3]);
				pr.setUnitPrice((BigDecimal) obj[4]);
				pr.setBuyCount((Integer) obj[5]);
				productList.add(pr);
			}

			return productList;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 根据商品编号查询商品是否存在
	 */
	public boolean checkProductId(String productNo, String id) {
		StringBuffer sql = new StringBuffer("select p from Product as p where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(productNo)) {
			sql.append(" and p.productNumber = :productNo");
			map.put("productNo", productNo);
		}
		if (!StringUtil.isEmpty(id)) {
			sql.append(" and p.id != :id");
			map.put("id", id);
		}
		try {
			List<Product> list = productDao.list(sql.toString(), map);
			if (list.size() <= 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Product> getProductOtherByStoreId(String storeId, String productId, String type) {
		StringBuffer sql = new StringBuffer("from Product as p,Store as s where 1=1");

		Map<String, Object> map = new HashMap<String, Object>();
		sql.append(" and p.storeId = s.id");
		sql.append(" and p.status = true");
		sql.append(" and p.checkStatus = 1");
		if (!StringUtil.isEmpty(type)) {
			sql.append(" and p.type = :type");
			map.put("type", type);
		}
		if (!StringUtil.isEmpty(productId)) {
			sql.append(" and p.id != :productId");
			map.put("productId", productId);
		}
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and p.storeId = :storeId");
			map.put("storeId", storeId);
		}
		sql.append(" order by p.buyCount desc");
		sql.append(" limit 0,4");
		try {
			List<Product> list = productDao.list(sql.toString(), map);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ekfans.base.product.service.IProductService#getProductGreat()
	 */
	@Override
	public List<Product> getProductGreat() {
		List<Product> productList = new ArrayList<Product>();
		StringBuffer sql = new StringBuffer("select p.id,p.name,p.picture,p.unitPrice from Product as p");
		try {
			Pager page = new Pager();
			page.setFromRow(0);
			page.setRowsPerPage(5);
			List<Object[]> list = productDao.list(page, sql.toString(), null);
			if (null == list || list.size() <= 0) {
				return null;
			}
			for (Object[] obj : list) {
				Product pr = new Product();
				pr.setId(obj[0].toString());
				pr.setName(obj[1].toString());
				pr.setPicture(obj[2] == null ? "" : obj[2].toString());
				pr.setUnitPrice((BigDecimal) obj[3]);
				productList.add(pr);
			}
			sql.append(" order by p.visitCount DESC");
			sql.append(" limit 0,5");
			return productList;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

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
	public List<Product> getProductsByCategoryId(String categoryId, String type, int showNumber) {
		// 定义SQL参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL，需要关联商品表、商品分类表、商品与分类关系关联表、店铺表
		StringBuffer sql = new StringBuffer("select product from Product product ,ProductCategory category,ProductCategoryRel pcr,User user where 1=1");

		// 关联商品分类
		sql.append(" and category.fullPath like :categoryId");
		paramMap.put("categoryId", "%" + categoryId + "%");

		// 商品分类状态必须为正常
		sql.append(" and category.status = :categoryStatus");
		paramMap.put("categoryStatus", true);

		// if (!StringUtil.isEmpty(type)) {
		// sql.append(" and product.type = :productType");
		// paramMap.put("productType", type);
		// }

		// 关联商品分类表 与 商品与分类关系表
		sql.append(" and category.id = pcr.categoryId");

		// 关联商品与分类关系表 与 商品表
		sql.append(" and pcr.productId = product.id");

		// 关联商品表与会员表
		sql.append(" and product.storeId = user.id");

		// 会员状态为正常
		sql.append(" and user.status = :userStatus");
		paramMap.put("userStatus", 1);

		// 会员激活状态为正常
		sql.append(" and user.verificationStatus = :userVerifyStatus");
		paramMap.put("userVerifyStatus", true);

		// 商品状态为上架
		sql.append(" and product.status = :productStatus");
		paramMap.put("productStatus", true);

		// 商品审核状态为已审核
		sql.append(" and product.checkStatus = :productCheckStatus");
		paramMap.put("productCheckStatus", 1);

		// 添加排序
		sql.append(" order by product.id desc");

		try {
			// 定义返回集合
			List<Product> productList = null;
			if (showNumber > 0) {
				// 定义分页
				Pager page = new Pager(1);
				page.setRowsPerPage(showNumber); // 调用DAO执行SQL
				productList = productDao.list(page, sql.toString(), paramMap);
			} else {
				// 调用DAO执行SQL
				productList = productDao.list(sql.toString(), paramMap);
			}

			return productList;
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * 查询访问量最高的商品
	 */
	@Override
	public List<Product> getVisitCountMaxProduct() {
		StringBuffer hql = new StringBuffer("from Product where 1=1 order by visitCount DESC");
		Session session = this.productDao.createSession();
		// 定义查询SQL

		Query query = session.createQuery(hql.toString());
		query.setFirstResult(0);
		query.setMaxResults(5);
		try {
			List<Product> list = query.list();
			if (list != null && list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return null;
	}

	/*
	 * (非 Javadoc) <p>Title: queryProductsByCategoryId</p> <p>Description: </p>
	 *
	 * @param id
	 *
	 * @return
	 *
	 * @see
	 * com.ekfans.base.product.service.IProductService#queryProductsByCategoryId
	 * (java.lang.String)
	 */
	@Override
	public List<Product> queryProductsByCategoryId(Pager pager, String id) {
		// 定义查询hql
		String hql = "select p from Product as p,ProductCategoryRel rel where p.id = rel.productId and rel.categoryId = :id and rel.main = 0";

		// 定义查询条件map
		Map<String, Object> map = new HashMap<String, Object>();
		// 分类ID
		map.put("id", id);
		try {
			// 调用dao查询
			List<Product> list = productDao.list(pager, hql, map);

			// 通过商品表中分类ID查询分类名称，再将分类名称设置到每一商品实体中
			setCategroyName(list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Product>();
	}

	/*
	 * (非 Javadoc) <p>Title: queryProductsByParams</p> <p>Description: </p>
	 *
	 * @param params
	 *
	 * @return
	 *
	 * @see
	 * com.ekfans.base.product.service.IProductService#queryProductsByParams
	 * (java.util.Map)
	 */
	@Override
	public List<Product> queryProductsByParams(Pager pager, Map<String, Object> params) {
		// 定义查询hql
		StringBuffer hql = new StringBuffer();

		hql.append("select p from Product p,ProductCategoryRel pcr where p.id = pcr.productId");
		// 如果查询条件map不为空
		if (null != params) {
			// 获取商品名
			String pName = (String) params.get("pName");
			// 分类id
			List<String> ids = (List<String>) params.get("ids");
			if (StringUtils.isNotEmpty(pName)) {
				// 拼装hql
				hql.append(" and p.name like :pName");
			}
			if (ids != null && ids.size() > 0) {
				// 拼装hql
				hql.append(" and pcr.categoryId in (");
				for (int i = 0; i < ids.size(); i++) {
					hql.append("'").append(ids.get(i)).append("'");
					if (i < ids.size() - 1) {
						hql.append(",");
					}
				}
				hql.append(")");
			}
			// 拼装hql
			hql.append(" and pcr.productId not in (select cr.productId from ProductCategoryRel cr where 1=1");
			hql.append(" and cr.categoryId = :categoryId");
			hql.append(")");

			// 拼装hql
			hql.append(" and pcr.main = true");
		}

		try {
			// 调用dao查询
			List<Product> list = productDao.list(pager, hql.toString(), params);
			// 通过商品表中分类ID查询分类名称，再将分类名称设置到每一商品实体中
			// setCategroyName(list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Product> getProductEssence() {
		List<Product> productList = new ArrayList<Product>();
		StringBuffer sql = new StringBuffer("select p.id,p.name,p.picture,p.unitPrice from Product as p");
		sql.append(" order by p.buyCount DESC");

		// hibernate 不支持hql limit分页
		Pager page = new Pager();
		page.setCurrentPage(0);
		page.setRowsPerPage(5);
		try {
			List<Object[]> list = productDao.list(page, sql.toString(), null);
			if (null == list || list.size() <= 0) {
				return null;
			}
			for (Object[] obj : list) {
				Product pr = new Product();
				pr.setId(obj[0].toString());
				pr.setName(obj[1].toString());
				pr.setPicture(obj[2] == null ? "" : obj[2].toString());
				pr.setUnitPrice((BigDecimal) obj[3]);
				productList.add(pr);
			}
			return productList;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @Title: setCategroyName
	 * @Description: 通过商品表中分类ID查询分类名称，再将分类名称设置到每一商品实体中
	 * @param list
	 * @throws Exception
	 *             void 返回类型
	 */
	private void setCategroyName(List<Product> list) throws Exception {
		if (null != list) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (Product product : list) {
				String hql = "select c.name from ProductCategory c where id = :id";
				map.put("id", product.getMainCategory());
				List<String> cnames = productDao.list(hql, map);
				if (null != cnames) {
					for (String cname : cnames) {
						product.setMainCategory(cname);
					}
				}
			}
		}
	}

	@Override
	public List<Product> getProductSalesRanking(Integer top, String type) {
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setRowsPerPage(top);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// hql
		StringBuffer sql = new StringBuffer("select p from Product as p where 1=1");
		sql.append(" and p.status=:status");
		paramMap.put("status", true);
		sql.append(" and p.checkStatus=:checkStatus");
		paramMap.put("checkStatus", 1);

		if (!StringUtil.isEmpty(type)) {
			sql.append(" and p.type=:type");
			paramMap.put("type", type);
		}
		sql.append(" order by p.buyCount desc");
		try {
			List<Product> list = this.productDao.list(pager, sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return list;
			}
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Product> getList(Pager page, String pName, String type) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select p from Product as p where 1 = 1 ");
		sql.append(" and p.status = true");
		sql.append(" and p.checkStatus = 1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(pName)) {
			sql.append(" and p.name = :name");
			paramMap.put("name", pName);
		}
		if (!StringUtil.isEmpty(type)) {
			sql.append(" and p.type = :type");
			paramMap.put("type", type);
		}
		try {
			List<Product> list = this.productDao.list(page, sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return list;
			}
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Product> getList(Pager page, String pName, String wName, String type, String categoryId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select p.id,wh.name,p.pfPrice,p.unit,p.name,p.sortName from Product as p,Warehouse wh where 1 = 1 ");
		sql.append(" and p.wareHouseId = wh.id");
		sql.append(" and p.status = true");
		sql.append(" and p.checkStatus = 1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(pName)) {
			sql.append(" and p.name = :name");
			paramMap.put("name", pName);
		}
		if (!StringUtil.isEmpty(wName)) {
			sql.append(" and wh.name = :wName");
			paramMap.put("wName", wName);
		}
		if (!StringUtil.isEmpty(type)) {
			sql.append(" and p.type = :type");
			paramMap.put("type", type);
		}
		if (!StringUtil.isEmpty(categoryId)) {
			sql.append(" and p.mainCategory = :categoryId");
			paramMap.put("categoryId", categoryId);
		}
		try {
			List<Object[]> list = productDao.list(page, sql.toString(), paramMap);
			List<Product> products = new ArrayList<Product>();
			for (Object[] object : list) {
				Product product = new Product();
				product.setId((String) object[0]);
				product.setWareHouseId((String) object[1]);
				product.setPfPrice((BigDecimal) object[2]);
				product.setUnit((String) object[3]);
				product.setName((String) object[4]);
				product.setSortName((String) object[5]);
				products.add(product);
			}
			return products;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

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
	public List<Product> getProductListByCategory(String categoryId, String type, int showNumber) {
		// 定义SQL参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL，需要关联商品表、商品分类表、商品与分类关系关联表、店铺表
		StringBuffer sql = new StringBuffer("select product from Product as product ,ProductCategory category,ProductCategoryRel pcr,User user where 1=1");

		// 关联商品分类
		sql.append(" and category.fullPath like :categoryId");
		paramMap.put("categoryId", "%" + categoryId + "%");

		// 商品分类状态必须为正常
		sql.append(" and category.status = :categoryStatus");
		paramMap.put("categoryStatus", true);

		// 关联商品分类表 与 商品与分类关系表
		sql.append(" and category.id = pcr.categoryId");

		// 关联商品与分类关系表 与 商品表
		sql.append(" and pcr.productId = product.id");

		// 判断商品类型
		if (!StringUtil.isEmpty(type)) {
			sql.append(" and product.type = :productType");
			paramMap.put("productType", type);
		}
		// 关联商品表与会员表
		sql.append(" and product.storeId = user.id");

		// 会员状态为正常
		sql.append(" and user.status = :userStatus");
		paramMap.put("userStatus", 1);

		// 会员激活状态为正常
		sql.append(" and user.verificationStatus = :userVerifyStatus");
		paramMap.put("userVerifyStatus", true);

		// 商品状态为上架
		sql.append(" and product.status = :productStatus");
		paramMap.put("productStatus", true);

		// 商品审核状态为已审核
		sql.append(" and product.checkStatus = :productCheckStatus");
		paramMap.put("productCheckStatus", 1);

		// 添加排序
		sql.append(" order by product.id desc");

		try {
			// 定义返回集合
			List<Product> productList = null;
			if (showNumber > 0) {
				// 定义分页
				Pager page = new Pager(1);
				page.setRowsPerPage(showNumber); // 调用DAO执行SQL
				productList = productDao.list(page, sql.toString(), paramMap);
			} else {
				// 调用DAO执行SQL
				productList = productDao.list(sql.toString(), paramMap);
			}

			return productList;
		} catch (Exception e) {
		}
		return null;
	}

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
	public List<String> getProductIdListByCategory(String categoryId, String type, int showNumber) {
		// 定义SQL参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL，需要关联商品表、商品分类表、商品与分类关系关联表、店铺表
		StringBuffer sql = new StringBuffer("select distinct(product.id) from Product as product ,ProductCategory category,ProductCategoryRel pcr,User user where 1=1");

		// 关联商品分类
		sql.append(" and category.fullPath like :categoryId");
		paramMap.put("categoryId", "%" + categoryId + "%");

		// 商品分类状态必须为正常
		sql.append(" and category.status = :categoryStatus");
		paramMap.put("categoryStatus", true);

		// 关联商品分类表 与 商品与分类关系表
		sql.append(" and category.id = pcr.categoryId");

		// 关联商品与分类关系表 与 商品表
		sql.append(" and pcr.productId = product.id");

		// 判断商品类型
		if (!StringUtil.isEmpty(type)) {
			sql.append(" and product.type = :productType");
			paramMap.put("productType", type);
		}
		// 关联商品表与会员表
		sql.append(" and product.storeId = user.id");

		// 会员状态为正常
		sql.append(" and user.status = :userStatus");
		paramMap.put("userStatus", 1);

		// 会员激活状态为正常
		sql.append(" and user.verificationStatus = :userVerifyStatus");
		paramMap.put("userVerifyStatus", true);

		// 商品状态为上架
		sql.append(" and product.status = :productStatus");
		paramMap.put("productStatus", true);

		// 商品审核状态为已审核
		sql.append(" and product.checkStatus = :productCheckStatus");
		paramMap.put("productCheckStatus", 1);

		// 添加排序
		sql.append(" order by product.id desc");

		try {
			// 定义返回集合
			List<String> productList = null;
			if (showNumber > 0) {
				// 定义分页
				Pager page = new Pager(1);
				page.setRowsPerPage(showNumber); // 调用DAO执行SQL
				productList = productDao.list(page, sql.toString(), paramMap);
			} else {
				// 调用DAO执行SQL
				productList = productDao.list(sql.toString(), paramMap);
			}

			return productList;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取商品的剩余库存
	 */
	public int getProductListQuantity(String productId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select sum(wl.number) from WarehouseLog as wl");
		sql.append(" where wl.productId = :productId");
		paramMap.put("productId", productId);
		sql.append(" and wl.type = :type");
		paramMap.put("type", true);
		IWarehouseLogDao wareHouseDao = SpringContextHolder.getBean(IWarehouseLogDao.class);
		int rkNum = 0;
		try {
			List l = wareHouseDao.list(sql.toString(), paramMap);
			if (l != null && l.size() > 0) {
				Long num1 = (Long) l.get(0);
				rkNum = num1.intValue();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		StringBuffer sql2 = new StringBuffer("select sum(wl.number) from WarehouseLog as wl");
		sql2.append(" where wl.productId = :productId");
		paramMap2.put("productId", productId);
		sql2.append(" and wl.type = :type");
		paramMap2.put("type", false);
		int cuNum = 0;
		try {
			List l = wareHouseDao.list(sql2.toString(), paramMap2);
			if (l != null && l.size() > 0) {
				Long num2 = (Long) l.get(0);
				cuNum = num2.intValue();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rkNum > cuNum) {
			return rkNum - cuNum;
		} else {
			return 0;
		}
	}

	// **************************************************核心企业中心业务**********************************************************************//

	@Override
	public Product getProductByProductNo(String no) {
		// 商品编号为空返回null
		if (StringUtil.isEmpty(no)) {
			return null;
		}
		StringBuffer sql = new StringBuffer("select p from Product as p where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		// 添加查询条件
		sql.append(" and p.productNumber = :productNo");
		map.put("productNo", no);
		try {
			List<Product> list = productDao.list(sql.toString(), map);
			if (list != null && list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public String getProductSum(String storeId, String status) {
		StringBuffer hql = new StringBuffer("select p.id from Product as p where 1 = 1 ");
		// 定义存储查询条件的Map
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(storeId)) {
			hql.append(" and p.storeId =:storeId");
			params.put("storeId", storeId);
		}
		if (!StringUtil.isEmpty(status)) {
			if (status.equals("true")) {
				hql.append(" and p.checkStatus = 1 ");
				hql.append(" and p.status =:status");
				params.put("status", Boolean.valueOf(status));
			} else {
				hql.append(" and (p.checkStatus != 1 or p.status =:status)");
				params.put("status", Boolean.valueOf(status));
			}

		}

		try {
			List list = productDao.list(hql.toString(), params);
			return list.size() + "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Product> getDaZhongOtherPro(Integer top, String proId, String type) {
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setRowsPerPage(top);
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select p from Product as p,Warehouse wh where 1 = 1 ");
		sql.append(" and p.wareHouseId = wh.id");
		sql.append(" and p.status = true");
		sql.append(" and p.checkStatus=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(proId)) {
			sql.append(" and p.id != :proId");
			paramMap.put("proId", proId);
		}
		if (!StringUtil.isEmpty(type)) {
			sql.append(" and p.type = :type");
			paramMap.put("type", type);
		}
		try {
			List<Product> list = productDao.list(pager, sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存批量商品 list 商品信息集合 picPath 上传的图片
	 */
	@Override
	public String saveBatchProduct(List<String[]> list, String storeInfo, Map zipMap, HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		// 图片,excel都存在
		if (list != null && zipMap != null) {
			result += this.saveProductBatch(list, storeInfo);
			result += this.savePriture(zipMap, request, response);
			// 只传了excel
		} else if (list != null) {
			result += this.saveProductBatch(list, storeInfo);
			// 只传了zip包
		} else if (zipMap != null) {
			result += this.savePriture(zipMap, request, response);
		} else {
			return "保存出错";
		}
		return result;
	}

	// 只保存商品信息
	public String saveProductBatch(List<String[]> list, String storeInfo) {
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			// 获取上传的商品分类ID,模板ID
			String[] titleByID = list.get(0);
			// 获取商品的字段
			// String[] titleByProduct = list.get(1);
			// 从下标2开始获得商品的数据
			if (i > 1) {
				try {
					// 通过共用方法获取商品编号
					NoManager no = new NoManager();
					String productId = no.createProductId();
					String[] productInfo = list.get(i);
					Product product = new Product();
					product.setId(productId);
					// 设置商品分类ID
					if (!StringUtil.isEmpty(titleByID[1])) {
						product.setMainCategory(titleByID[1]);
					}
					// 商品编号
					if (!StringUtil.isEmpty(productInfo[1])) {
						product.setProductNumber(StringSubmit(productInfo[1]));
					} else {
						result += "第" + (i + 1) + "行商品编号为空";
						continue;
					}
					// 设置模板ID
					if (!StringUtil.isEmpty(titleByID[3])) {
						product.setTemplateId(titleByID[3]);
					}
					if (!StringUtil.isEmpty(storeInfo)) {
						product.setStoreId(storeInfo);
					}
					// 商品名称
					if (!StringUtil.isEmpty(productInfo[0])) {
						product.setName(productInfo[0]);
					} else {
						result += "第" + (i + 1) + "行商品名称为空,";
						continue;
					}

					// 商品类型(1:优选商城，2：大宗采购)
					if (!StringUtil.isEmpty(productInfo[2])) {
						String type = StringSubmit(productInfo[2]);
						product.setType(type);
					} else {
						result += "第" + (i + 1) + "行(商品类型不能为空,1：优选商城，2：大宗采购)!";
						continue;
					}
					// 库存量
					if (!StringUtil.isEmpty(productInfo[3])) {
						product.setQuantity((int) Double.parseDouble(productInfo[3]));
					} else {
						result += "第" + (i + 1) + "行库存不能为空";
						continue;
					}
					// 数量单位
					if (!StringUtil.isEmpty(productInfo[4])) {
						product.setUnit(productInfo[4]);
					} else {
						result += "第" + (i + 1) + "行数量单位不能为空,";
						continue;
					}
					// 优选商城必须有 商城价/市场价
					if (!StringUtil.isEmpty(productInfo[5]) && !StringUtil.isEmpty(productInfo[6]) && !StringUtil.isEmpty(productInfo[1])
							&& "1".equals(StringSubmit(productInfo[2]))) {
						product.setUnitPrice(new BigDecimal(StringSubmit(productInfo[5])));
						product.setListPrice(new BigDecimal(StringSubmit(productInfo[5])));
					} else {
						// 如果商城价或市场
						if (!StringUtil.isEmpty(productInfo[7]) && !StringUtil.isEmpty(productInfo[2]) && "2".equals(StringSubmit(productInfo[2]))) {
							product.setPfPrice(new BigDecimal(StringSubmit(productInfo[7])));
						} else {
							result += "第" + (i + 1) + "行 若(商品类型：1优选商城必须填写商城价,市场价),(商品类型：2大宗采购必须填写批发价)!";
							continue;
						}
					}
					// 商品产地
					product.setHabitatAddress(productInfo[8]);
					// 设置审核状态
					product.setCheckStatus(1);
					// 设置上架
					product.setStatus(true);
					product.setRiseDrop(product.getUnitPrice().doubleValue());
					// 根据商品编号查询是否存在该条商品
					Product pro = this.getProductByProductNo(product.getProductNumber());
					if (pro != null) {
						productDao.updateBean(pro);
					} else {
						// 调用DAO方法添加商品
						productDao.addBean(product);
					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
		}
		return result;
	}

	// 处理图片
	public String savePriture(Map<String, List<String>> map, HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		try {
			// 循环map
			Set<String> keySet = map.keySet();
			for (String obj : keySet) {// 遍历key
				String productNumber = obj;
				List<String> list = map.get(obj);
				Product product = this.getProductByProductNo(productNumber);
				// 商品多角度视图
				if (product != null && list != null && list.size() > 0) {
					for (int j = 0; j < list.size(); j++) {
						String popPicPath = list.get(j);
						String path = popPicPath.substring(popPicPath.lastIndexOf("/") + 1);
						if (!StringUtil.isEmpty(path) && path.indexOf("-") != -1) {
							// 如果获取的图片路径不为空，则定义一个多角多视图对象，将数据放入进去
							if (!StringUtil.isEmpty(popPicPath)) {
								ProductPicture pPicture = new ProductPicture();
								pPicture.setPicture(popPicPath);
								pPicture.setPosition((j + 1) + "");
								pPicture.setProductId(product.getId());
								// 调用方法处理多角多视图
								ProductHelper.img(ProductHelper.PIC_TYPE_POP, pPicture, request);
								// 调用DAO采用事物处理的方式保存数据
								productPictureDao.addBean(pPicture);
							}
						} else if (!StringUtil.isEmpty(popPicPath)) {
							product.setPicture(popPicPath);
							ProductHelper.img(ProductHelper.PIC_TYPE_PIC, product, request);
							// 更新商品主图
							productDao.updateBean(product);
						}
					}
				} else {
					result += "处理图片(商品编号为:" + productNumber + "未找到相对应的商品数据)!";
					continue;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return result;
	}

	public static String StringSubmit(String num) {
		if (num.indexOf(".") > -1) {
			num = num.substring(0, num.indexOf("."));
		}
		return num;
	}

	public boolean productCheckModify(Product product) {
		IProductCacheService cacheService = new ProductCacheServiceImpl();

		StringBuffer sql = new StringBuffer("select rel.categoryId from ProductCategoryRel as rel where 1=1");
		sql.append(" and rel.productId = :productId");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("productId", product.getId());

		try {
			this.modifyProduct(product);
			List list = productCategoryRelDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					cacheService.refrefshProductsByCategory((String) list.get(i), product.getType());
				}
			}

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Product> getProductByCategoryId(Pager pager, String storeId, String categoryId, String areaId) {
		String hql = "select p,s from Product p,Store s,ProductCategory pc where p.storeId=s.id and p.checkStatus=1 and p.mainCategory=pc.id and pc.fullPath like '%" + categoryId
				+ "%' and p.quantity!=0";

		if (!StringUtil.isEmpty(areaId)) {
			hql += " and s.areaId like '%" + areaId + "%'";
		}
		if (!StringUtil.isEmpty(storeId)) {
			hql += " and s.id='" + storeId.trim() + "'";
		}
		hql += " order by p.updateTime desc";
		try {
			List<Object[]> list = productDao.list(pager, hql, null);
			if (list != null && list.size() > 0) {
				List<Product> plist = new ArrayList<Product>();
				for (Object[] obj : list) {
					Product pr = (Product) obj[0];
					pr.setJcjg(pr.getJcjg().trim());
					pr.setStore((Store) obj[1]);
					if (pr != null) {
						String hql1 = "from ProductValuation where productId='" + pr.getId() + "'";
						pr.setProductValuation(productDao.list(hql1, null));
					}

					plist.add(pr);
				}

				return plist;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Product> getListShowByPcId(Pager pager, String categoryId, String categoryRootId, String name, String minPrice, String maxPrice) {
		return productDao.getListWebShow(pager, categoryId, categoryRootId, name, minPrice, maxPrice, false);
	}

	@Override
	public List<Product> getHotToShow(Pager pager, String categoryRootId) {
		return productDao.getListWebShow(pager, null, categoryRootId, null, null, null, true);
	}

	@Override
	public Product getProDteailById(String id) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(
				" select p.id,p.name,p.unitPrice,p.quantity,s.storeName,p.productNumber,p.centerPicture,p.description,p.unit,p.productModel,p.habitatAddress,p.deliceData,p.cjl,p.deliceType,p.deliceAddress,p.payType,p.jcjg,p.jcFile,p.note,p.recommendPicture4,p.isAdvance,p.advancePrice,s.storeRefer,p.storeId,p.createTime from Product as p,Store as s where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		sql.append(" and p.storeId = s.id");

		// 如果查询条件输入了id，添加查询条件
		if (!StringUtil.isEmpty(id)) {
			sql.append(" and p.id = :id");
			paramMap.put("id", id);
		} else {
			return null;
		}
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = productDao.list(sql.toString(), paramMap);
			List<Product> products = new ArrayList<Product>();
			for (Object[] object : list) {
				Product product = new Product();
				product.setId((String) object[0]);
				product.setName((String) object[1]);
				product.setUnitPrice((BigDecimal) object[2]);
				product.setQuantity((Integer) object[3]);
				product.setStoreName((String) object[4]);
				product.setProductNumber((String) object[5]);
				product.setCenterPicture((String) object[6]);
				product.setDescription((String) object[7]);
				product.setUnit((String) object[8]);
				product.setProductModel((String) object[9]);
				product.setHabitatAddress((String) object[10]);
				product.setDeliceData((String) object[11]);
				product.setCjl((Integer) object[12]);
				product.setDeliceType((String) object[13]);
				product.setDeliceAddress((String) object[14]);
				product.setPayType((String) object[15]);
				product.setJcjg((String) object[16]);
				product.setJcFile((String) object[17]);
				product.setNote((String) object[18]);
				product.setRecommendPicture4((String) object[19]);
				product.setIsAdvance((String) object[20]);
				product.setAdvancePrice((BigDecimal) object[21]);
				product.setStoreRefer((String) object[22]);
				product.setStoreId((String) object[23]);
				product.setCreateTime((String) object[24]);
				products.add(product);
			}
			if (products != null && products.size() > 0) {
				return products.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Product> getListByQuantity(Pager pager, String productName, int quantity) {
		// 获取商品集合的hql
		StringBuffer hql = new StringBuffer(" from Product c where 1=1 ");
		// 获取商品含量集合的hql
		StringBuffer hql2 = new StringBuffer(" from ProductValuation pv where pv.productId=:productId");
		if (quantity == 0) {
			hql.append(" and c.quantity=0");
		} else {
			hql.append(" and c.quantity!=0");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(productName)) {
			hql.append(" and c.name like :productName");
			map.put("productName", "%" + productName + "%");
		}
		hql.append(" order by c.updateTime desc");
		try {
			// 获取商品集合
			List<Product> list = productDao.list(pager, hql.toString(), map);

			List<ProductValuation> pvList = new ArrayList<ProductValuation>();
			Product pro = null;
			// 遍历商品集合
			for (int i = 0; i < list.size(); i++) {
				pro = list.get(i);
				map.put("productId", pro.getId());
				// 根据商品id得到商品含量集合
				pvList = productValuationDao.list(hql2.toString(), map);
				// 设置商品里面的商品集合
				pro.setProductValuation(pvList);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Product> getListByParams(Pager pager, String categoryName, String address, String name, String catgId) {
		StringBuffer hql = new StringBuffer("select p,s from Product as p,Store as s,ProductCategoryRel as pcr,ProductCategory as pc where 1=1");
		hql.append(" and p.id = pcr.productId and pc.id = pcr.categoryId and s.id = p.storeId");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(categoryName) && !("全部".equals(categoryName))) {
			ProductCategory category = categoryService.getCategoryByName(categoryName);
			if (category != null) {
				hql.append(" and pc.fullPath like :category");
				params.put("category", "%" + category.getId() + "%");
			} else {
				return new ArrayList<>();
			}
			// hql.append(" and pc.name =:categoryName");
			// params.put("categoryName", categoryName);
		}
//		if (!StringUtil.isEmpty(address) && !("全部".equals(address))) {
//			hql.append(" and p.deliceAddress like :address");
//			params.put("address", "%" + address + "%");
//		}
		
		//按交货地查找
		if (!StringUtil.isEmpty(address) && !("全部".equals(address))) {
			hql.append(" and p.deliceAddress like :address");
			params.put("address", "%" + address + "%");
		}
		
		if (!StringUtil.isEmpty(name)) {
			hql.append(" and p.name like:name");
			params.put("name", "%" + name + "%");
		}
		if (!StringUtil.isEmpty(catgId)) {
			hql.append(" and pc.fullPath like :catgId");
			params.put("catgId", "%" + catgId + "%");
		}
		// 商品状态为上架
		hql.append(" and p.status = :productStatus");
		params.put("productStatus", true);

		// 商品审核状态为已审核
		hql.append(" and p.checkStatus = :productCheckStatus");
		params.put("productCheckStatus", 1);

		// 按商品创建时间倒序
		hql.append(" order by p.createTime desc");

		List<Product> products = new ArrayList<Product>();
		try {
			List<Object[]> objects = this.productDao.list(pager, hql.toString(), params);
			if (objects != null && objects.size() > 0) {
				for (int i = 0; i < objects.size(); i++) {
					Product product = (Product) objects.get(i)[0];
					Store store = (Store) objects.get(i)[1];
					product.setStore(store);
					products.add(product);
				}
				//通过 areaId 获取地址，以逗号分隔
				ISystemAreaService systemAreaService = SpringContextHolder.getBean(ISystemAreaService.class);
				for(Product pd : products){
					String pdAreaId = pd.getDeliceAddressId();
					String pdDestination = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(pdAreaId,",");
					pd.setDeliceAddress(pdDestination);
				}
				return products;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Product> getGqProByCategoryId(String categoryId, String type, int showNumber) {
		// 定义SQL参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL，需要关联商品表、商品分类表、商品与分类关系关联表、店铺表
		StringBuffer sql = new StringBuffer("select product,s.storeName from Product product ,ProductCategory category,ProductCategoryRel pcr,User user,Store s where 1=1");

		// 关联商品分类
		sql.append(" and category.fullPath like :categoryId");
		paramMap.put("categoryId", "%" + categoryId + "%");

		// 商品分类状态必须为正常
		sql.append(" and category.status = :categoryStatus");
		paramMap.put("categoryStatus", true);
		// 关联商品分类表 与 商品与分类关系表
		sql.append(" and category.id = pcr.categoryId");

		// 关联商品与分类关系表 与 商品表
		sql.append(" and pcr.productId = product.id");

		// 关联商品表与会员表
		sql.append(" and product.storeId = user.id");

		// 关联商品表与店铺表
		sql.append(" and product.storeId = s.id");

		// 会员状态为正常
		sql.append(" and user.status = :userStatus");
		paramMap.put("userStatus", 1);

		// 会员激活状态为正常
		sql.append(" and user.verificationStatus = :userVerifyStatus");
		paramMap.put("userVerifyStatus", true);

		// 商品状态为上架
		sql.append(" and product.status = :productStatus");
		paramMap.put("productStatus", true);

		// 商品审核状态为已审核
		sql.append(" and product.checkStatus = :productCheckStatus");
		paramMap.put("productCheckStatus", 1);
		// 定义排序规则 0 供应 1 求购
		if (!StringUtil.isEmpty(type)) {
			if (("0").equals(type)) {// 供应根据成交量排序
				sql.append(" order by product.cjl desc");
			} else {// 求购根据查看次数排序
				sql.append(" order by product.visitCount desc");
			}
		}

		try {
			List<Object[]> objs = null;
			// 定义返回集合
			List<Product> productList = null;
			Product p = null;
			if (showNumber > 0) {
				// 定义分页
				Pager page = new Pager(1);
				page.setRowsPerPage(showNumber); // 调用DAO执行SQL
				objs = productDao.list(page, sql.toString(), paramMap);
			} else {
				// 调用DAO执行SQL
				objs = productDao.list(sql.toString(), paramMap);
			}
			if (null != objs && objs.size() > 0) {
				productList = new ArrayList<>();
				for (Object[] obj : objs) {
					p = (Product) obj[0];
					p.setStoreName((String) obj[1]);
					productList.add(p);
				}
			}
			return productList;
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * 用于展示首页的现货交易
	 * 
	 * @return
	 */
	public List<List<Product>> getIndexProduct() {
        // 获得现货分类id
        String categoryId = Cache.getResource("cengpinProduct");

        // 定义分页
        Pager pager = new Pager();
        pager.setRowsPerPage(36); // 首页显示最新的36条数据

		StringBuffer hql = new StringBuffer(
				"select p.name,p.productModel,p.habitat,p.unitPrice,p.riseDrop,p.unit from Product as p,ProductCategoryRel as pcr,ProductCategory as pc where 1=1");
		hql.append(" and p.id = pcr.productId and pc.id = pcr.categoryId");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(categoryId)) {
			hql.append(" and pc.fullPath like :catgId");
			params.put("catgId", "%" + categoryId + "%");
		}
		// 商品状态为上架
		hql.append(" and p.status = :productStatus");
		params.put("productStatus", true);

		// 商品审核状态为已审核
		hql.append(" and p.checkStatus = :productCheckStatus");
		params.put("productCheckStatus", 1);

		// 按商品创建时间倒序
		hql.append(" order by p.updateTime desc");

		List<List<Product>> rList = new ArrayList<List<Product>>();
		try {
			List<Object[]> objects = this.productDao.list(pager, hql.toString(), params);
			if (objects != null && objects.size() > 0) {
				ISystemAreaService systemAreaService = SpringContextHolder.getBean(ISystemAreaService.class);
				List<Product> products = null;
				for (int i = 0; i < objects.size(); i++) {
					if (i % 3 == 0) {
						products = new ArrayList<Product>();
					}
					Object[] obj = objects.get(i);
					Product product = new Product();
					product.setName(obj[0].toString());
					product.setProductModel(obj[1].toString());
					String area = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(obj[2].toString(), ".");
					if (area != null && area.indexOf(".", area.indexOf(".") + 1) != -1) {
						area = area.substring(0, area.indexOf(".", area.indexOf(".") + 1));
					}
					product.setHabitat(area);
					product.setUnitPrice(obj[3] != null ? new BigDecimal(obj[3].toString()) :  new BigDecimal("0.00"));
					product.setRiseDrop((double) obj[4]);
					product.setUnit(obj[5].toString());
					products.add(product);
					if (i != 0 && i % 3 == 2) {
						rList.add(products);
					}
				}
			}
			return rList;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getProductNums() throws Exception {          
		String sql= "select count(*) from Product where check_Status =0 ";
		List<BigInteger> num = productDao.createSession().createSQLQuery(sql).list();
		return num.get(0).intValue();
	}

}