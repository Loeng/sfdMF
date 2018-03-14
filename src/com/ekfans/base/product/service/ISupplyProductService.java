package com.ekfans.base.product.service;

import java.util.List;

import org.hibernate.Session;

import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.product.model.SupplyProduct;
import com.ekfans.pub.util.Pager;

public interface ISupplyProductService {
	
	public Boolean update(SupplyProduct sp);
	
    //保存商品发布
    public boolean saveSupplyProduct(SupplyProduct product);
    //查询发布商品
    public List<SupplyProduct> getList(String supplyName,String storeName,String productNum,Pager pager);
    //根据ID查询商品
    public SupplyProduct getSupplyProductById(String id);
    
  //根据ID查询商品
    public SupplyProduct getSupplyProductById(String id,Session session);
    //根据商品编号查询
    public int getCheckProductNum(String num);
    //供应商查询发布商品列表
    public List<SupplyProduct> getListByGys(String storeName, String productNum, Pager pager);
    /**
     * 查询数量
     */
    public String getProductSum(String storeId);
   
}
