package com.ekfans.base.product.service;

import java.io.File;
import java.util.List;

import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductPriceChange;
import com.ekfans.pub.util.Pager;

/**
 * 商品价格修改Service接口
 * @author Administrator
 *
 */
public interface IProductPriceChangeService {
	
	public boolean addChange(ProductPriceChange priceChange);
	
	public List<ProductPriceChange> list(Pager pager,String productId); 
}