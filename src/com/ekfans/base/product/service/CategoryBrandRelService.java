package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.ICategoryBrandRelDao;
import com.ekfans.base.product.model.CategoryBrandRel;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品分类与品牌关系业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
public class CategoryBrandRelService implements ICategoryBrandRelService {
	// 定义DAO
	@Autowired
	private ICategoryBrandRelDao categoryBrandRelDao;

    /**
     * 添加
     */
    public boolean addCategoryBrandRel(CategoryBrandRel cbr) {
        try {
            if (cbr == null) {
                return false;
            }
            categoryBrandRelDao.addBean(cbr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据categoryId删除数据
     */
    public boolean delete(String categoryId) {
     // 定义查询SQL
        StringBuffer sql = new StringBuffer(" delete cbr from CategoryBrandRel as cbr where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //如果roleId不为空
        if (!StringUtil.isEmpty(categoryId)) {
            // 添加查询条件
            sql.append(" and cbr.categoryId = :categoryId");
            paramMap.put("categoryId", categoryId);
        }
        try {
            categoryBrandRelDao.delete(sql.toString(), paramMap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}