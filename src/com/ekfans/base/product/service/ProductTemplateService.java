package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductTemplateDao;
import com.ekfans.base.product.dao.ITemplateFieldsDao;
import com.ekfans.base.product.model.ProductTemplate;
import com.ekfans.base.product.model.TemplateFields;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品模版业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author wsj
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class ProductTemplateService implements IProductTemplateService {
	// 定义DAO
	@Autowired
	private IProductTemplateDao productTemplateDao = null;

	@Autowired
	private ITemplateFieldsDao templateFieldsDao = null;

	private Logger log = LoggerFactory.getLogger(ProductTemplateService.class);

	// 模板名称条件查询

	public List<ProductTemplate> listTemplate(Pager pager, String name) {

		StringBuffer sql = new StringBuffer("select pt from ProductTemplate as pt where 1=1 ");

		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (!StringUtil.isEmpty(name)) {
			sql.append(" and pt.name like :name");
			paramMap.put("name", "%" + name + "%");
		}
		sql.append(" order by pt.createTime desc");
		try {
			if (pager == null) {
				List<ProductTemplate> list = productTemplateDao.list(sql.toString(), paramMap);
				return list;
			} else {
				List<ProductTemplate> list = productTemplateDao.list(pager, sql.toString(), paramMap);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 根据Id删除对象
	public boolean deleteTemplate(String id) {
		// 如果id为空，返回false;
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		// 定义sql语句
		StringBuffer sql = new StringBuffer(" select tf from TemplateFields as tf where 1=1 ");
		// 定义Map函数
		Map<String, Object> hashMap = new HashMap<String, Object>();

		sql.append(" and tf.tempId = :tempId");
		hashMap.put("tempId", id);
		try {
			productTemplateDao.deleteById(id);
			List<TemplateFields> list = templateFieldsDao.list(sql.toString(), hashMap);
			if (list.size() > 0) {
				TemplateFields tfs = list.get(0);
				templateFieldsDao.deleteById(tfs.getId());
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 根据Id查找商品模板
	public ProductTemplate getProductTemplateById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			return (ProductTemplate) productTemplateDao.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据templateId取得productTempLate对象----------------wsj--------------------
	 */

	public ProductTemplate getProductTemplateByTempLateId(String templateId) {
		StringBuffer sql = new StringBuffer("select pt from ProductTemplate as pt,ProductCategory as pc where 1=1");
		// sql.append(" and pc.templateId = pt.id");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(templateId)) {
			sql.append(" and pt.id = :templateId");
			map.put("templateId", templateId);
		}
		try {
			List<ProductTemplate> list = productTemplateDao.list(sql.toString(), map);
			return list.get(0);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// 修改模板
	public boolean modifyTemplate(ProductTemplate productTemplate) {
		if (productTemplate == null) {
			return false;
		}
		try {
			productTemplateDao.updateBean(productTemplate);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 保存模板
	public boolean saveTempalte(ProductTemplate productTemplate) {
		if (productTemplate == null) {
			return false;
		}
		try {
			productTemplateDao.addBean(productTemplate);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 新增商品时查询店铺列表
	 */
	public List<ProductTemplate> listTemplate() {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select p from ProductTemplate as p where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		try {
			List<ProductTemplate> list = productTemplateDao.list(sql.toString(), paramMap);

			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ProductTemplate getTemplateByName(String name) {
		StringBuffer sql = new StringBuffer("select pt from ProductTemplate as pt where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(name)) {
			sql.append("and pt.name = :name");
			map.put("name", name);
		}
		try {
			List<ProductTemplate> list = productTemplateDao.list(sql.toString(), map);
			return list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

    @Override
    public List<ProductTemplate> getProductTemplate() {
        StringBuffer sql = new StringBuffer("select pt from ProductTemplate as pt");
        try {
            List<ProductTemplate> list = productTemplateDao.list(sql.toString(), null);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}