package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductCategoryRelDao;
import com.ekfans.base.product.model.ProductCategoryRel;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品与分类关系业务实现Service
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
public class ProductCategoryRelService implements IProductCategoryRelService {
	// 定义DAO
	@Autowired
	private IProductCategoryRelDao productCategoryRelDao;

    /**
     * 添加分类与商品关联信息
     */
    public boolean add(ProductCategoryRel pcRl) {
        try {
            if (pcRl == null) {
                return false;
            }
            productCategoryRelDao.addBean(pcRl);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        
    }

    /**
     * 根据商品id得到关系信息
     */
    public ProductCategoryRel getPcRlBy(String productId) {
     // 如果传进来的id为空，则返回null
        if (StringUtil.isEmpty(productId)) {
            return null;
        }
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pcr from ProductCategoryRel as pcr where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        sql.append(" and pcr.productId = :productId");
        paramMap.put("productId", productId);
        try {
            List<ProductCategoryRel> list = productCategoryRelDao.list(sql.toString(), paramMap);
            if(list.size()>0){
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改
     */
    public boolean modify(ProductCategoryRel pcRl) {
        try {
            // 调用DAO方法修改商品
            productCategoryRelDao.updateBean(pcRl);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	/* 
	* (非 Javadoc)
	* <p>Title: remove</p>
	* <p>Description: </p>
	* @param pcRl
	* @see com.ekfans.base.product.service.IProductCategoryRelService#remove(com.ekfans.base.product.model.ProductCategoryRel)
	*/
	@Override
	public boolean remove(ProductCategoryRel pcRl) {
		try {
			String hql = "delete from ProductCategoryRel p where p.productId = :pId and p.categoryId = :cId";
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("pId", pcRl.getProductId());
			map.put("cId", pcRl.getCategoryId());
			productCategoryRelDao.delete(hql, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}