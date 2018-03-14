package com.ekfans.base.product.service.web.productList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.controllers.web.vo.ProListTemplateVO;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: ProductListService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-19 上午09:48:19
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProductListService implements IProductListService {
	private Logger log = LoggerFactory.getLogger(ProductListService.class);

	@Autowired
	private IProductDao productDao;

	/**
	 * 那条件查询出满足条件的商品
	 */
	@Override
	public Object[] getProductByConditions( // 先决条件
			String productName,
			// 搜索条件
			String categoryId, String brand,
			// productInfoDetail
			String infoNameAndValue1, String infoNameAndValue2, String infoNameAndValue3, String infoNameAndValue4,
			// 排序条件
			String sortNameAndType,
			// 所在地区
			String productArea,
			// 分页条件
			Pager pager) {
		try {
			StringBuffer hql = new StringBuffer("select distinct p.id,p.name,p.recommendPicture5,p.unitPrice,p.listPrice,p.buyCount,s.city,p.picture,p.sortName");
			hql.append(" from Product as p,ProductInfoDetail as pid,Store as s,ProductCategoryRel as pcr,ProductCategory as pc where 1=1");

			Map<String, Object> params = new HashMap<String, Object>();
			// 添加关联条件

			hql.append(" and p.storeId = s.id");
			hql.append(" and p.id = pcr.productId");

			// 先决条件
			if (!StringUtil.isEmpty(productName)) {
				hql.append(" and p.name like :productName");
				params.put("productName", "%" + productName + "%");
			}
			// 搜索普通条件
			if (!StringUtil.isEmpty(categoryId)) {
				hql.append(" and pc.fullPath like :categoryId");
				params.put("categoryId", "COMMON_%" + categoryId + "%");
				hql.append(" and pc.id = pcr.categoryId");
			}
			if (!StringUtil.isEmpty(brand)) {
				hql.append(" and p.brand = :brand");
				params.put("brand", brand);
			}
			// ProductInfoDetail
			if (!StringUtil.isEmpty(infoNameAndValue1)) {
				String[] infoArry1 = infoNameAndValue1.split("/");
				String infoName1 = infoArry1[0];
				String infoValue1 = infoArry1[1];
				hql.append(" and p.id = pid.productId");
				hql.append(" and (pid.infoName1 =:infoName1 and pid.infoValue1 =:infoValue1)");
				params.put("infoName1", infoName1);
				params.put("infoValue1", infoValue1);
			}
			if (!StringUtil.isEmpty(infoNameAndValue2)) {
				String[] infoArry2 = infoNameAndValue2.split("/");
				String infoName2 = infoArry2[0];
				String infoValue2 = infoArry2[1];
				hql.append(" and p.id = pid.productId");
				hql.append(" and (pid.infoName2=:infoName2 and pid.infoValue2=:infoValue2)");
				params.put("infoName2", infoName2);
				params.put("infoValue2", infoValue2);
			}
			if (!StringUtil.isEmpty(infoNameAndValue3)) {
				String[] infoArry3 = infoNameAndValue3.split("/");
				String infoName3 = infoArry3[0];
				String infoValue3 = infoArry3[1];
				hql.append(" and p.id = pid.productId");
				hql.append(" and (pid.infoName3=:infoName3 and pid.infoValue3=:infoValue3)");
				params.put("infoName3", infoName3);
				params.put("infoValue3", infoValue3);
			}
			if (!StringUtil.isEmpty(infoNameAndValue4)) {
				String[] infoArry4 = infoNameAndValue4.split("/");
				String infoName4 = infoArry4[0];
				String infoValue4 = infoArry4[1];
				hql.append(" and p.id = pid.productId");
				hql.append(" and (pid.infoName4=:infoName4 and pid.infoValue4=:infoValue4)");
				params.put("infoName4", infoName4);
				params.put("infoValue4", infoValue4);
			}

			// 添加排序条件
			if (!StringUtil.isEmpty(sortNameAndType)) {
				String[] sortArray = sortNameAndType.split("/");
				String sortName = sortArray[0];
				String sortType = sortArray[1];
				hql.append(ProductListService.addSortCondition(sortName, sortType));
			}

			// 添加地区
			if (!StringUtil.isEmpty(productArea)) {
				hql.append(" and s.area=:productArea");
				params.put("productArea", productArea);
			}

			List list = productDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 返回的Object数组
				Object[] objArray = new Object[2];

				List<Product> products = new ArrayList<Product>();
				for (int i = 0; i < list.size(); i++) {
					Product p = new Product();
					Object[] objects = (Object[]) list.get(i);
					p.setId((String) objects[0]);
					p.setName((String) objects[1]);
					p.setRecommendPicture5((String) objects[2]);
					p.setUnitPrice((BigDecimal) objects[3]);
					p.setListPrice((BigDecimal) objects[4]);
					p.setBuyCount((Integer) objects[5]);
					// 地区处理城市之快
					p.setArea((String) objects[6]);
					p.setPicture((String) objects[7]);
					p.setSortName((String) objects[8]);
					products.add(p);
				}
				// 为Object数组赋值
				objArray[0] = products;
				objArray[1] = list.size();
				return objArray;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: addSortCondition
	 * @Description: TODO(添加排序) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param sortName
	 * @param @param sortType
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	private static String addSortCondition(String sortName, String sortType) {
		if (!StringUtil.isEmpty(sortName) && !StringUtil.isEmpty(sortType)) {
			// 按时间
			if ("newest".equals(sortName)) {
				if ("desc".equals(sortType)) {
					return " order by p.checkTime DESC";
				} else if ("asc".equals(sortType)) {
					return " order by p.checkTime ASC";
				}
				// 按人气
			} else if ("popularity".equals(sortName)) {
				if ("desc".equals(sortType)) {
					return " order by p.visitCount DESC";
				} else if ("asc".equals(sortType)) {
					return " order by p.visitCount ASC";
				}
				// 按销量
			} else if ("salesVolume".equals(sortName)) {
				if ("desc".equals(sortType)) {
					return " order by p.buyCount DESC";
				} else if ("asc".equals(sortType)) {
					return " order by p.buyCount ASC";
				}
				// 按价格
			} else if ("price".equals(sortName)) {
				if ("desc".equals(sortType)) {
					return " order by p.unitPrice DESC";
				} else if ("asc".equals(sortType)) {
					return " order by p.unitPrice ASC";
				}
			}
		}
		return "";
	}

	// ==================================================商品分类START============================================
	/**
	 * 查询出符合条件商品的所属分类
	 */
	@Override
	public List<ProductCategory> findThisProductCatagory(String categoryId, String productName) {
		// 按名字搜索商品进入商品列表,查询出满足条件商品的主分类
		if (StringUtil.isEmpty(categoryId) && !StringUtil.isEmpty(productName)) {
			return ProductListService.getProMainCategory(productName, productDao, log);
		} else if (!StringUtil.isEmpty(categoryId) && StringUtil.isEmpty(productName)) {
			return ProductListService.getChildOrBrotherCategory(categoryId, productDao, log);
		}
		return null;
	}

	/**
	 * 
	 * @Title: getProMainCategory
	 * @Description: TODO(根据商品名字,查询出商品的主分类) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param proName
	 * @param @return 设定文件
	 * @return List<ProductCategory> 返回类型
	 * @throws
	 */
	private static List<ProductCategory> getProMainCategory(String proName, IProductDao iProductDao, Logger ilog) {
		try {
			if (StringUtil.isEmpty(proName)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select distinct(pc.id),pc.name" + " from Product as p,ProductCategory as pc" + " where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			// 添加关联条件
			hql.append(" and p.mainCategory = pc.id");

			// 添加查询条件
			hql.append(" and p.name like:productName");
			params.put("productName", "%" + proName + "%");

			List list = iProductDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<ProductCategory> pcs = new ArrayList<ProductCategory>();
				for (int i = 0; i < list.size(); i++) {
					ProductCategory pc = new ProductCategory();
					Object[] objects = (Object[]) list.get(i);
					pc.setId((String) objects[0]);
					pc.setName((String) objects[1]);
					pcs.add(pc);
				}
				return pcs;
			}
		} catch (Exception e) {
			ilog.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: getChildOrBrotherCategory
	 * @Description: TODO(根据categoryId查询出该分类下的子分类, 如果该分类下不存在子分类就查询出其同级分类)
	 *               详细业务流程:这 (详细描述此方法相关的业务处理流程)
	 * @param @param categoryId
	 * @param @return 设定文件
	 * @return List<ProductCategory> 返回类型
	 * @throws
	 */
	private static List<ProductCategory> getChildOrBrotherCategory(String categoryId, IProductDao iProductDao, Logger ilog) {
		try {
			if (StringUtil.isEmpty(categoryId)) {
				return null;
			}
			// 定义查询SQL
			StringBuffer hql = new StringBuffer(" select pc from ProductCategory as pc where 1=1");
			// 定义参数MAP
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 如果传进来的父ID不为空，则加上父ID条件;若为空，则返回null
			hql.append(" and pc.parentId = :parentId");
			paramMap.put("parentId", categoryId);
			// 权限的状态是放开
			hql.append(" and pc.status = '1'");
			// 根据位置进行排序
			hql.append(" order by pc.position asc");
			// 查询出其子分类
			List<ProductCategory> pcs = iProductDao.list(hql.toString(), paramMap);
			if (pcs != null && pcs.size() > 0) {
				return pcs;
			}
			// ===============================================================
			// 方法执行到这里的话,证明该分类下没有子分类了,这时查询出该分类的同级分类
			// 获得该分类的父级分类id
			StringBuffer mhql = new StringBuffer("select pc.parentId from ProductCategory as pc where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			mhql.append(" and pc.id = :categoryId");
			params.put("categoryId", categoryId);
			List mlList = iProductDao.list(mhql.toString(), params);
			if (mlList != null && mlList.size() == 1) {
				Object object = mlList.get(0);
				// 得到当前分类的父分类id
				String thisParentId = object.toString();
				if (StringUtil.isEmpty(thisParentId)) {
					return null;
				}
				// 查询出当前分类的同级分类
				StringBuffer mchql = new StringBuffer("select pc from ProductCategory as pc where 1=1");
				Map<String, Object> mcparams = new HashMap<String, Object>();
				mchql.append(" and pc.parentId = :parentId");
				mcparams.put("parentId", thisParentId);
				// 权限的状态是放开
				hql.append(" and pc.status = '1'");
				// 根据位置进行排序
				hql.append(" order by pc.position asc");
				List<ProductCategory> mcpcs = iProductDao.list(mchql.toString(), mcparams);
				return mcpcs;
			}
		} catch (Exception e) {
			ilog.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询出商品的默认分类信息
	 */
	@Override
	public ProductCategory getDefaultProCategory(String categoryId) {
		try {
			if (StringUtil.isEmpty(categoryId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select pc from ProductCategory as pc where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and pc.id = :categoryId");
			params.put("categoryId", categoryId);

			List list = productDao.list(hql.toString(), params);
			if (list != null && list.size() == 1) {
				return (ProductCategory) list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// ==================================================商品分类END=============================================

	// =============================================商品品牌START================================================
	/**
	 * 查询出匹配商品所属品牌
	 */
	@Override
	public List<ProductBrand> findThisProductBrand(List<String> categoryIds, String productName) {
		try {
			StringBuffer hql = new StringBuffer("select distinct(pb.id),pb.name");
			hql.append(" from Product as p,ProductBrand as pb,ProductCategoryRel as pcr");
			hql.append(" where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			// 添加关联条件
			hql.append(" and p.brand = pb.id");
			hql.append(" and p.id = pcr.productId");

			// 添加查询条件
			if (!StringUtil.isEmpty(productName)) {
				hql.append(" and p.name like:productName");
				params.put("productName", "%" + productName + "%");
			}

			// 添加商品分类条件
			if (categoryIds != null && categoryIds.size() > 0) {
				hql.append(" and pcr.categoryId in (");
				for (int i = 0; i < categoryIds.size(); i++) {
					hql.append("'").append(categoryIds.get(i)).append("'");
					if (i < categoryIds.size() - 1) {
						hql.append(",");
					}
				}
				hql.append(")");
			}

			List list = productDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<ProductBrand> pbs = new ArrayList<ProductBrand>();
				for (int i = 0; i < list.size(); i++) {
					ProductBrand pb = new ProductBrand();
					Object[] objects = (Object[]) list.get(i);
					pb.setId((String) objects[0]);
					pb.setName((String) objects[1]);
					pbs.add(pb);
				}
				return pbs;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// ===========================================商品品牌END=====================================================

	// =====================================商品所属模板详细字段START==============================================
	/**
	 * 获得商品的所属模板的详细字段名和详细字段值
	 */
	@Override
	public List<ProListTemplateVO> findProductTemplateFields(List<String> categoryIds, String productName) {
		try {
			StringBuffer hql = new StringBuffer("select distinct tf.fieldName,tf.fieldValue,tf.position");
			hql.append(" from TemplateFields as tf,Product as p,ProductCategoryRel as pcr where 1=1");
			// 添加关联条件
			hql.append(" and p.templateId = tf.tempId");
			hql.append(" and p.id = pcr.productId");
			hql.append(" and tf.commons = 1");

			Map<String, Object> params = new HashMap<String, Object>();
			if (!StringUtil.isEmpty(productName)) {
				hql.append(" and p.name like :productName");
				params.put("productName", "%" + productName + "%");
			}
			// 添加商品分类条件
			if (categoryIds != null && categoryIds.size() > 0) {
				hql.append(" and pcr.categoryId in (");
				for (int i = 0; i < categoryIds.size(); i++) {
					hql.append("'").append(categoryIds.get(i)).append("'");
					if (i < categoryIds.size() - 1) {
						hql.append(",");
					}
				}
				hql.append(")");
			}
			List list = productDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<ProListTemplateVO> templateVOs = new ArrayList<ProListTemplateVO>();
				for (int i = 0; i < list.size(); i++) {
					Object[] objects = (Object[]) list.get(i);
					ProListTemplateVO vo = new ProListTemplateVO();
					vo.setFieldName((String) objects[0]);

					String fieldValue = (String) objects[1];
					// 将模板详细字段值拆分成一个字符串数组 并存入集合
					String[] fieldValueArray = fieldValue.split(";");
					List<String> fieldValueList = new ArrayList<String>();
					for (int h = 0; h < fieldValueArray.length; h++) {
						fieldValueList.add(fieldValueArray[h]);
					}
					vo.setFieldValueList(fieldValueList);
					// 设置显示位置
					vo.setPosition((String) (objects[2] + ""));
					templateVOs.add(vo);
				}
				return templateVOs;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// ==========================================商品所属模板详细字段END==================================================

}