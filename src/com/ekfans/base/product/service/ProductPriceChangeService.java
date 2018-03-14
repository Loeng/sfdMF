package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductInfoDao;
import com.ekfans.base.product.dao.IProductPriceChangeDao;
import com.ekfans.base.product.model.ProductInfo;
import com.ekfans.base.product.model.ProductPriceChange;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
/**
 * 商品价格修改业务实现Service
 * @author Administrator
 *
 */
@Service
@SuppressWarnings("unchecked")
public class ProductPriceChangeService implements IProductPriceChangeService {
	// 定义DAO
	@Autowired
	private IProductPriceChangeDao changeDao;

	@Override
	public boolean addChange(ProductPriceChange priceChange) {
		// TODO Auto-generated method stub
		try {
			changeDao.addBean(priceChange);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<ProductPriceChange> list(Pager pager,String productId) {
		// TODO Auto-generated method stub
		StringBuffer hql=new StringBuffer("select p from ProductPriceChange p where 1=1");
		Map<String, Object> params=new HashMap<>();
		if(!StringUtil.isEmpty(productId)){
			hql.append(" and p.productId=:productId");
			params.put("productId", productId);
		}
		hql.append(" order by p.createTime desc");
		try {
			List<ProductPriceChange> list=changeDao.list(pager,hql.toString(), params);
			if(null!=list&&list.size()>0){
				return list;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}