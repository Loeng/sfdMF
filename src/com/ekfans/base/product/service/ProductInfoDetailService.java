package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductInfoDetailDao;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品扩展信息明细业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class ProductInfoDetailService implements IProductInfoDetailService {
	// 定义DAO
	@Autowired
	private IProductInfoDetailDao productInfoDetailDao;

	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(ProductService.class);

	/**
	 * 根据id得到对象
	 */
	public ProductInfoDetail getProductInfoDetailById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			// 调用DAO方法查找角色
			return (ProductInfoDetail) productInfoDetailDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 保存操作
	 */
	public boolean addProductInfoDetail(ProductInfoDetail productInfoDetail) {
		if (productInfoDetail == null) {
			return false;
		}
		try {
			productInfoDetailDao.addBean(productInfoDetail);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据prodcutId取得ProductInfoDetail集合
	 */
	@Override
	public List<ProductInfoDetail> getProductInfoDetailByProductId(String productId) {
		StringBuffer hql = new StringBuffer("from ProductInfoDetail as pId where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(productId)) {
			hql.append("and pId.productId = :productId");
			map.put("productId", productId);
		}
		try {
			List<ProductInfoDetail> list = productInfoDetailDao.list(hql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改操作
	 */
	@Override
	public boolean modifyProductInfoDerail(ProductInfoDetail productInfoDetail) {
		if (productInfoDetail == null) {
			return false;
		}
		try {
			productInfoDetailDao.updateBean(productInfoDetail);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据商品id删除所有扩展表的数据
	 */
	public boolean getProductIddelete(String productId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" delete pid from ProductInfoDetail as pid where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 如果roleId不为空
		if (!StringUtil.isEmpty(productId)) {
			// 添加查询条件
			sql.append(" and pid.productId = :productId");
			paramMap.put("productId", productId);
		}
		try {
			productInfoDetailDao.delete(sql.toString(), paramMap);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

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
	public ProductInfoDetail getInfoDetailByProductIdAndTemps(String productId, String fieldId1, String tempVal1, String fieldId2, String tempVal2, String fieldId3, String tempVal3, String fieldId4, String tempVal4) {
		// 如果传进来的商品编号为空，则返回
		if (StringUtil.isEmpty(productId)) {
			return null;
		}

		// 定义参数 Map
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 定义SQL
		StringBuffer sql = new StringBuffer("select infoDetail from ProductInfoDetail as infoDetail where 1=1");

		// 关联商品编号
		sql.append(" and infoDetail.productId = :productId");
		paramMap.put("productId", productId);

		// 关联模板值1的主键字段
		if (StringUtil.isEmpty(fieldId1)) {
			sql.append(" and (infoDetail.infoId1 is null or infoDetail.infoId1 = '' or infoDetail.infoId1 = ' ')");
		} else {
			sql.append(" and infoDetail.infoId1 = :infoId1");
			paramMap.put("infoId1", fieldId1);
		}

		// 关联模板值1的值字段
		if (StringUtil.isEmpty(tempVal1)) {
			sql.append(" and (infoDetail.infoValue1 is null or infoDetail.infoValue1 = '' or infoDetail.infoValue1 = ' ')");
		} else {
			sql.append(" and infoDetail.infoValue1 = :tempVal1");
			paramMap.put("tempVal1", tempVal1);
		}

		// 关联模板值2的主键字段
		if (StringUtil.isEmpty(fieldId2)) {
			sql.append(" and (infoDetail.infoId2 is null or infoDetail.infoId2 = '' or infoDetail.infoId2 = ' ')");
		} else {
			sql.append(" and infoDetail.infoId2 = :infoId2");
			paramMap.put("infoId2", fieldId2);
		}

		// 关联模板值2的值字段
		if (StringUtil.isEmpty(tempVal2)) {
			sql.append(" and (infoDetail.infoValue2 is null or infoDetail.infoValue2 = '' or infoDetail.infoValue2 = ' ')");
		} else {
			sql.append(" and infoDetail.infoValue2 = :tempVal2");
			paramMap.put("tempVal2", tempVal2);
		}

		// 关联模板值3的主键字段
		if (StringUtil.isEmpty(fieldId3)) {
			sql.append(" and (infoDetail.infoId3 is null or infoDetail.infoId3 = '' or infoDetail.infoId3 = ' ')");
		} else {
			sql.append(" and infoDetail.infoId3 = :infoId3");
			paramMap.put("infoId3", fieldId3);
		}

		// 关联模板值3的值字段
		if (StringUtil.isEmpty(tempVal3)) {
			sql.append(" and (infoDetail.infoValue3 is null or infoDetail.infoValue3 = '' or infoDetail.infoValue3 = ' ')");
		} else {
			sql.append(" and infoDetail.infoValue3 = :tempVal3");
			paramMap.put("tempVal3", tempVal3);
		}

		// 关联模板值4的主键字段
		if (StringUtil.isEmpty(fieldId4)) {
			sql.append(" and (infoDetail.infoId4 is null or infoDetail.infoId4 = '' or infoDetail.infoId4 = ' ')");
		} else {
			sql.append(" and infoDetail.infoId4 = :infoId4");
			paramMap.put("infoId4", fieldId4);
		}

		// 关联模板值4的值字段
		if (StringUtil.isEmpty(tempVal4)) {
			sql.append(" and (infoDetail.infoValue4 is null or infoDetail.infoValue4 = '' or infoDetail.infoValue4 = ' ')");
		} else {
			sql.append(" and infoDetail.infoValue4 = :tempVal4");
			paramMap.put("tempVal4", tempVal4);
		}

		try {
			// 调用Dao查询SQL
			List<ProductInfoDetail> list = productInfoDetailDao.list(sql.toString(), paramMap);
			if (list != null) {
				return list.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
}