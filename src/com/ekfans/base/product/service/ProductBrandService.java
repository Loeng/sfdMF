package com.ekfans.base.product.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductBrandDao;
import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 品牌业务实现Service
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
public class ProductBrandService implements IProductBrandService {
	// 定义一个错误日志文件
	public static Logger log = LoggerFactory.getLogger(ProductBrandService.class);
	// 定义DAO
	@Autowired
	private IProductBrandDao productBrandDao;

	/**
	 * 查询品牌列表
	 * 
	 * @param page
	 * @return
	 */
	public List<ProductBrand> getBrands(Pager page, String name, String status) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select productBrand from ProductBrand as productBrand where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果品牌名不为空，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and productBrand.name like :name");
			paramMap.put("name", "%" + name + "%");
		}

		// 如果状态不为空，添加查询条件
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and productBrand.status = :status");
			paramMap.put("status", Boolean.parseBoolean(status));
		}
		try {
			List<ProductBrand> list = productBrandDao.list(page, sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 新增品牌
	 * 
	 * @param brand
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean addBrand(ProductBrand brand, File uploadFile, String uploadFileContentType) {
		// 如果传进来的品牌对象为空，则返回false
		if (brand == null) {
			return false;
		}
		try {

			// 设置创建时间为当前系统时间
			brand.setCreateTime(DateUtil.getSysDateTimeString());
			// 调用SERVICE的方法添加店铺等级
			productBrandDao.addBean(brand);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据id删除品牌
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteBrand(String id) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			// 删除旧图标
			ProductBrand brand = (ProductBrand) productBrandDao.get(id);
			if (!StringUtil.isEmpty(brand.getIcon())) {
				File file = new File(brand.getIcon());
				if (file.exists()) {
					file.delete();
				}
			}
			// 调用SERVICE的方法删除品牌
			productBrandDao.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改品牌
	 * 
	 * @param brand
	 * @param uploadFile
	 * @param uploadFileContentType
	 * @param uploadFileName
	 * @return
	 */
	public boolean modifyBrand(ProductBrand brand, File uploadFile, String uploadFileContentType) {
		// 如果传进来的品牌等级对象为空，则返回false
		if (brand == null) {
			return false;
		}
		try {
			// 删除旧图标
			if (!StringUtil.isEmpty(brand.getIcon())) {
				File file = new File(brand.getIcon());
				if (file.exists()) {
					file.delete();
				}
			}

			// 调用SERVICE的方法修改品牌
			productBrandDao.updateBean(brand);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据id查找品牌
	 * 
	 * @param id
	 * @return
	 */
	public ProductBrand getBrand(String id) {
		// 如果传进来的id为空，则返回null
		if (StringUtil.isEmpty(id)) {
			return null;
		}

		try {
			// 调用SERVICE的方法查找品牌
			return (ProductBrand) productBrandDao.get(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 新增商品时查询品牌列表
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public List<ProductBrand> getBrands() {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select productBrand from ProductBrand as productBrand where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		try {
			List<ProductBrand> list = productBrandDao.list(sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据name查询
	 */
	public ProductBrand getBrandByName(String name) {
		StringBuffer sql = new StringBuffer("select s from ProductBrand as s where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(name)) {
			sql.append("and s.name like :name");
			map.put("name", "%" + name + "%");
		}

		try {
			List<ProductBrand> list = productBrandDao.list(sql.toString(), map);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据id获得品牌的详情信息
	 */
	@Override
	public ProductBrand getBrandDetailById(String id) {
		try {
			if (StringUtil.isEmpty(id)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select pb from ProductBrand as pb where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and pb.id = :id");
			params.put("id", id);
			List<ProductBrand> productBrands = productBrandDao.list(hql.toString(), params);
			if (productBrands.size() > 0) {
				return productBrands.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 用于分类管理修改页面的根据categoryId得到集合
	 */
	public List<ProductBrand> getBrands(String categoryId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select productBrand from ProductBrand as productBrand where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql2 = new StringBuffer(" select productBrand from ProductBrand as productBrand,CategoryBrandRel as cbr where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		// 添加关联条件
		// 将两个表关联起来
		sql2.append(" and productBrand.id = cbr.brandId");
		if (!StringUtil.isEmpty(categoryId)) {
			sql2.append(" and cbr.categoryId = :categoryId");
			paramMap2.put("categoryId", categoryId);
		}
		try {
			List<ProductBrand> list = productBrandDao.list(sql.toString(), paramMap);
			// 调用方法得到根据roleID得到的权限集合
			List<ProductBrand> haveList = productBrandDao.list(sql2.toString(), paramMap2);
			// 循环比较用户的id是否选中，如果选中给check属性赋值true
			if (haveList != null) {
				for (ProductBrand pb : list) {
					for (ProductBrand havepb : haveList) {
						if (havepb.getId().equals(pb.getId())) {
							pb.setChecked(true);
						}
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据Id取得productBrand集合
	 */
	public List<ProductBrand> listProductBrand(String id) {
		StringBuffer sql = new StringBuffer("select distinct pb from ProductBrand as pb,Product as p where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		sql.append(" and pb.id = p.brand");
		try {
			List<ProductBrand> list = productBrandDao.list(sql.toString(), map);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

}