package com.ekfans.base.product.service;

import java.math.BigDecimal;
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

import com.ekfans.base.order.service.InquiryService;
import com.ekfans.base.product.dao.IProductCategoryRelDao;
import com.ekfans.base.product.dao.IProductDao;
import com.ekfans.base.product.dao.IProductInfoDao;
import com.ekfans.base.product.dao.IProductInfoDetailDao;
import com.ekfans.base.product.dao.IProductPictureDao;
import com.ekfans.base.product.dao.IProductPriceDao;
import com.ekfans.base.product.dao.IProductValuationDao;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategoryRel;
import com.ekfans.base.product.model.ProductInfo;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.base.product.model.ProductPrice;
import com.ekfans.base.product.model.ProductValuation;
import com.ekfans.base.product.util.ProductHelper;
import com.ekfans.base.store.dao.IWarehouseLogDao;
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
 * 商品计价业务实现Service
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
public class ProductValuationService implements IProductValuationService {

	private Logger log = LoggerFactory.getLogger(ProductValuationService.class);

	@Autowired
    private IProductValuationDao productValuationDao;
	
    @Override
    public List<ProductValuation> getListByProduct(String productId) {
     // 定义查询SQL
        StringBuffer sql = new StringBuffer("from ProductValuation as pv where 1 = 1 ");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(productId)) {
            sql.append(" and pv.productId = :productId");
            paramMap.put("productId", productId);
        }
        try {
            // 调用DAO方法查找商品
            List<ProductValuation> list = productValuationDao.list(sql.toString(), paramMap);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

	
}