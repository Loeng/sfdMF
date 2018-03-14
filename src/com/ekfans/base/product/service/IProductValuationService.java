package com.ekfans.base.product.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductValuation;
import com.ekfans.pub.util.Pager;

/**
 * 商品计价实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductValuationService {
	
    /**
     * 根据商品id查询商品计价
     */
    public List<ProductValuation> getListByProduct(String productId);
}